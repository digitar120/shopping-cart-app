package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.globalhandler.BadRequestException;
import com.digitar120.shoppingcartapp.exception.globalhandler.NotFoundException;
import com.digitar120.shoppingcartapp.exception.globalhandler.ServiceUnavailableException;
import com.digitar120.shoppingcartapp.feignclient.UserClient;
import com.digitar120.shoppingcartapp.mapper.CartDTOtoCart;
import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.CartRepository;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
import com.digitar120.shoppingcartapp.util.LocalUtilityMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.digitar120.shoppingcartapp.util.LocalUtilityMethods.*;

/**
 * Business logic for the Cart endpoint.
 * @author Gabriel Pérez (digitar120)
 * @see Cart
 * @see CartRepository
 * @see UserClient
 */
@Service
public class CartService {

    private final CartRepository repository;
    private final CartDTOtoCart mapper;
    private final UserClient userServiceConnection;

    public CartService(CartRepository repository, CartDTOtoCart mapper, UserClient userServiceConnection) {
        this.repository = repository;
        this.mapper = mapper;
        this.userServiceConnection = userServiceConnection;
    }

    // Read methods

    /**
     * Returns all the Carts in the database.
     * @return A List of Carts containing all elements.
     */
    public List<Cart> findAll() {
        return repository.findAll();
    }

    /**
     * Returns a single Cart that matches and ID.
     * @param id ID to execute the search with.
     * @return Cart object with a matching ID.
     * @throws BadRequestException If no element matches.
     */
    public Cart findById(Long id){
        return verifyElementExistsAndReturn(repository, id, "No se encontró el carrito de ID " + id, HttpStatus.NOT_FOUND);
    }

    /**
     * Calls the User service to verify that an user exists, then searches and returns a Cart object matching by its {@code userId}.
     * @param userId {@code userId} userId to execute the search with.
     * @return A Cart with a matching {@code userId}.
     * @throws ServiceUnavailableException If the {@code FeignClient} call fails.
     * @throws NotFoundException If the {@code FeignClient} call succeeds but the User doesn't exist.
     */
    public Cart findByUserId(Integer userId){
        Optional<Cart> optionalCart = repository.findByUserId(userId);

        if (userServiceConnection
                .getUserByUserId(userId)
                .getId() == -1){
            throw new ServiceUnavailableException("Error interno. No se pudo verificar que el usuario existe. Intente más tarde.");

        } else if (optionalCart.isEmpty()) {
            throw new NotFoundException("No existe un carrito asignado a ése usuario.");
        }

        return optionalCart.get();
    }

    /**
     * Retrieves the Item Set of a Cart, matching by ID.
     * <p>It reuses the {@link CartService#findById(Long)} method, so it's subject to its behavior.</p>
     * @param id ID to execute the search with.
     * @return A matching Cart's Item Set.
     */
    public Set<Item> getContent(Long id){
        return findById(id).getItems();
    }

    // Create methods

    /**
     * Creates a new Cart with a given {@link NewCartDTO}.
     * <p>Uses a {@link CartDTOtoCart} mapper.</p>
     * @param cartDTO A {@code NewCartDTO} object to provide basic information.
     * @return A newly created Cart object.
     */
    public Cart newCart(NewCartDTO cartDTO){
        return repository.save(mapper.map(cartDTO));
    }

    // Update methods

    /**
     * Adds an Item with a referenced Product and a quantity to a Cart's Item Set
     * <p>This method is a bit complex in behavior. It will check for the following:</p>
     * <p><ul>
     *     <li>If the quantity value is valid</li>
     *     <li>If {@code cartId} matches any Cart (reusing {@link CartService#findById(Long)} and thus inheriting its
     *     behavior)</li>
     *     <li>If {@code productId} matches any referenced Products already listed in the Cart's Item Set. If positive, updates
     *     the quantity instead. If negative, creates a new one</li>
     * </ul></p>
     * @param cartId The ID to match a Cart with
     * @param productId The ID of the Product to work with
     * @param quantity Desired quantity for the referenced Product
     * @return An updated Cart object, containing the newly created or edited Item in its Item Set.
     * @throws BadRequestException If the {@code quantity} equals or is below 0.
     * @throws NotFoundException If the {@code productId} matches no Products.
     */
    @Transactional
    public Cart addItemToCart(Long cartId, Long productId, Integer quantity){

        // Verificar que el valor de cantidad sea válido
        if(quantity <= 0){
            throw new BadRequestException("La cantidad ingresada es inválida.");
        }

        // Adquirir el carrito, verificando que existe mediante findById()
        Cart cart = this.findById(cartId);

        Item matchedItem = new Item();
        Product matchedProduct = new Product();

        // Existe un ítem con el mismo referencedProduct?
        for (Item element : cart.getItems()) {
            if (element.getReferencedProduct().getId().equals(productId)) {
                matchedItem = element;
                matchedProduct = element.getReferencedProduct();
            }
        }

        // Si existe, igualar su cantidad a la ingresada
        if (matchedProduct.getId() != null) {
            // Eliminar el ítem encontrado
            cart.getItems().remove(matchedItem);

            // Editar y agregar nuevamente el ítem.
            matchedItem.setQuantity(quantity);
            cart.getItems().add(matchedItem);

            return repository.save(cart);
        } else {
            // Si no existe, construir el ítem y agregarlo

            // Cómo verificar si un producto existe o no? Debería agregar el servicio o el repositorio de
            // Product? En el mientras tanto, pruebo una solución más ambigua.
            try {
                cart.getItems().add(new Item(
                        quantity,
                        cart,
                        new Product(productId)
                ));
                return repository.save(cart);
            } catch (Exception e) {
                throw new NotFoundException("No se encontró el producto N°" + productId);
            }
        }
    }

    /**
     *  A repeating version of {@link CartService#addItemToCart(Long, Long, Integer)}.
     *  <p>Adds the contents of an Item Set to a Cart's Item Set, while verifying that none of the Items received
     *  have an ID so they can be properly initialize them with {@code addItemToCart()}.</p>
     *  <p>This is because Items with non-null IDs will not be properly loaded into the database, while the API will
     *  still respond with HTTP 200.</p>
     * @param cartId The ID to match a Cart with.
     * @param itemSet An Item Set containing new Items.
     * @throws BadRequestException If an Item on the receiveing Item Set contains an ID.
     */
    @Transactional
    public void addMultipleItemsToCart(Long cartId, Set<Item> itemSet) {

        for (Item element: itemSet){
            if (element.getId() != null){
                throw new BadRequestException("Uno o más elementos ingresados incluyen un ID de ítem que no corresponde.");
            }
        }

        // Como se confirma que está todo en orden, añadir los elementos al carrito.
        for (Item element: itemSet){
            this.addItemToCart(
                    cartId,
                    element.getReferencedProduct().getId(),
                    element.getQuantity());
        }
    }

    // Delete methods

    /**
     * Deletes an Item from a Cart's Item Set
     *<p>This method will verify that the Cart exists through {@link CartService#findById(Long)} (inheriting its
     * behavior), after which it will search the itemId in the matched Cart's Item Set.</p>
     * @param cartId The {@code cartId} to match with.
     * @param itemId The {@code itemId} to match with.
     * @return A copy of the updated Cart object.
     * @throws NotFoundException If no matching Item is found.
     */
    @Transactional
    public Cart deleteItemFromCart(Long cartId, Long itemId){

       Cart cart = findById(cartId);

        // Verificar que el ítem ingresado existe
        Item matchedItem = new Item();

        for (Item element: cart.getItems()){
            if (element.getId().equals(itemId)){
                matchedItem = element;
            }
        }

        if (matchedItem.getId() == null){
            throw new NotFoundException("No se encontró el ítem N°" + itemId);
        } else {
            cart.getItems().remove(matchedItem);
            return repository.save(cart);
        }


    }

    /**
     * Deletes a cart
     * <p>Executes {@link LocalUtilityMethods#verifyElementExists(JpaRepository, Object, String, HttpStatus)} to verify
     * that the Cart exists. If the method does not fail, executes a deletion by ID.</p>
     * @param id ID of the Cart to delete.
     */
    @Transactional
    public void deleteCart(Long id){
        verifyElementExists(repository, id, "No se encontró el carrito N° " + id, HttpStatus.NOT_FOUND);
        repository.deleteById(id);
    }

    /**
     * Deletes a Cart by matching its {@code userId}.
     * @param userId The ID to match a Cart with.
     * @throws NotFoundException If {@code userId} matches no Carts.
     */
    @Transactional
    public void deleteCartByUserId(Integer userId){
        Optional<Cart> optionalCart = repository.findByUserId(userId);
        if (optionalCart.isEmpty()){
            throw new NotFoundException("No se encontró un carrito con ése ID de usuario.");
        }
        repository.deleteById(optionalCart.get().getId());
    }
}
