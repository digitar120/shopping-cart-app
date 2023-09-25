package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.MyException;
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

@Service
public class CartService {

    private final CartRepository repository;
    private final CartDTOtoCart mapper;

    public CartService(CartRepository repository, CartDTOtoCart mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    // Listar carritos
    public List<Cart> findAll() {
        return repository.findAll();
    }

    // Encontrar carrito mediante ID
    public Cart findById(Long id){
        return repository.findById(id).orElseThrow( () -> new MyException("No se encontró el carrito con ID "+ id, HttpStatus.NOT_FOUND));
    }

    // Listar contenidos de un carrito
    public Set<Item> getContent(Long id){
        Optional<Cart> optionalCart = repository.findById(id);
        if (optionalCart.isEmpty()){
            throw new MyException("No se encontró el carrito con ID "+ id, HttpStatus.NOT_FOUND);
        } else {
            return optionalCart.get().getItems();
        }
    }

    // Crear nuevo carrito
    public Cart newCart(NewCartDTO cartDTO){
        Cart newCart = mapper.map(cartDTO);
        return repository.save(newCart);
    }

    // Agregar elementos a un carrito
    // Trabajar el tema alterar la cantidad de un item
    @Transactional
    public Cart addItemToCart(Long cartId, Long productId, Integer quantity){

        // Verificar que el carrito especificado existe
        Optional<Cart> optionalCart = repository.findById(cartId);
        if (optionalCart.isEmpty()){
            throw new MyException("No se encontró el carrito N°" + cartId, HttpStatus.NOT_FOUND);
        } else {

            // Verificar que el valor de cantidad sea válido
            if(quantity <= 0){
                throw new MyException("La cantidad ingresada es inválida.", HttpStatus.BAD_REQUEST);
            } else {
                Cart cart = this.findById(cartId);

                // Existe un ítem con el mismo referencedProduct?
                Item matchedItem = new Item();
                Product matchedProduct = new Product();

                for (Item element: cart.getItems()){
                    if (element.getReferencedProduct().getId().equals(productId)){
                        matchedItem = element;
                        matchedProduct = element.getReferencedProduct();
                    }
                }
                // Si existe, igualar su cantidad a la ingresada
                if (matchedProduct.getId() != null){
                    // Eliminar el ítem encontrado
                    cart.getItems().remove(matchedItem);

                    // Editar y agregar nuevamente el ítem.
                    matchedItem.setQuantity(quantity);
                    cart.getItems().add(matchedItem);

                    return repository.save(cart);
                }
                // Si no existe, construir el ítem y agregarlo
                else {
                    // Cómo verificar si un producto existe o no? Debería agregar el servicio o el repositorio de
                    // Product? En el mientras tanto, pruebo una solución más ambigua.
                    try {
                        cart.getItems().add(new Item(
                                quantity,
                                cart,
                                new Product(productId)
                        ));
                        return repository.save(cart);
                    } catch (Exception e){
                        throw new MyException("No se encontró el producto N°" + productId,HttpStatus.NOT_FOUND);
                    }
                }
            }
        }
    }

    // Eliminar elementos de un carrito
    public Cart deleteItemFromCart(Long cartId, Long itemId){

        // Verificar que el carrito especificado existe
        Optional<Cart> optionalCart = repository.findById(cartId);
        if (optionalCart.isEmpty()){
            throw new MyException("No se encontró el carrito N°" + cartId, HttpStatus.NOT_FOUND);
        } else {
            Cart cart = optionalCart.get();

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

    }

    // Eliminar un carrito
    public void deleteCart(Long id){
        Optional<Cart> optionalCart = repository.findById(id);
        if (optionalCart.isEmpty()){
            throw new MyException("No se encontró el carrito N°" + id, HttpStatus.NOT_FOUND);
        } else {
            repository.deleteById(id);
        }
    }
}
