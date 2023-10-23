package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.MyException;
import com.digitar120.shoppingcartapp.feignclient.UserClient;
import com.digitar120.shoppingcartapp.mapper.CartDTOtoCart;
import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.CartRepository;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.digitar120.shoppingcartapp.util.MyMethods.*;


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


    // Listar carritos
    public List<Cart> findAll() {
        return repository.findAll();
    }

    // Encontrar carrito mediante ID
    public Cart findById(Long id){
        return verifyElementExistsAndReturn(repository, id, "No se encontró el ítem de ID " + id, HttpStatus.NOT_FOUND);
    }

    // Encontrar carrito mediante ID de usuario
    public Cart findByUserId(Integer userId){
        try {
          userServiceConnection.getUserByUserId(userId);
        } catch (Exception e){
            throw new MyException("El usuario ingresado no existe.", HttpStatus.NOT_FOUND);
        }

        Optional<Cart> optionalCart = repository.findByUserId(userId);
        if (optionalCart.isEmpty()){
            throw new MyException("No existe un carrito asignado a ése usuario.", HttpStatus.NOT_FOUND);
        }

        return optionalCart.get();
    }

    // Listar contenidos de un carrito
    public Set<Item> getContent(Long id){
        return findById(id).getItems();
    }

    // Crear nuevo carrito
    public Cart newCart(NewCartDTO cartDTO){
        Cart newCart = mapper.map(cartDTO);

        try {
            userServiceConnection.getUserByUserId(cartDTO.getUserId());
        } catch (Exception e){
            throw new MyException("El usuario no existe.", HttpStatus.NOT_FOUND);
        }

        return repository.save(newCart);
    }

    // Agregar elemento a un carrito
    @Transactional
    public Cart addItemToCart(Long cartId, Long productId, Integer quantity){

        // Verificar que el valor de cantidad sea válido
        if(quantity <= 0){
            throw new MyException("La cantidad ingresada es inválida.", HttpStatus.BAD_REQUEST);
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
                throw new MyException("No se encontró el producto N°" + productId, HttpStatus.NOT_FOUND);
            }
        }
    }

    // Agregar varios elementos a un carrito
    @Transactional
    public void addMultipleItemsToCart(Long cartId, Set<Item> itemSet) {

        // itemSet se recibe desde un ResponseBody, que permite introducir el ID del ítem. Al ingresar un número
        // de ítem que ya existe, la API devuelve 200, pero no se afecta la base de datos. Debe agregarse una
        // excepción para avisar que esto ocurre.

        for (Item element: itemSet){
            if (element.getId() != null){
                throw new MyException("Uno o más elementos ingresados incluyen un ID de ítem que no corresponde.", HttpStatus.BAD_REQUEST);
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

    // Eliminar elementos de un carrito
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
            throw new MyException("No se encontró el ítem N°" + itemId, HttpStatus.NOT_FOUND);
        } else {
            cart.getItems().remove(matchedItem);
            return repository.save(cart);
        }


    }

    // Eliminar un carrito
    @Transactional
    public void deleteCart(Long id){
        verifyElementExists(repository, id, "No se encontró el carrito N° " + id, HttpStatus.NOT_FOUND);
        repository.deleteById(id);
    }

    // Eliminar carrito mediante ID de usuario
    @Transactional
    public void deleteCartByUserId(Integer userId){
        Optional<Cart> optionalCart = repository.findByUserId(userId);
        if (optionalCart.isEmpty()){
            throw new MyException("No se encontró un carrito con ése ID de usuario.", HttpStatus.NOT_FOUND);
        }
        repository.deleteById(optionalCart.get().getId());
    }
}
