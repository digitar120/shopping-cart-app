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
    @Transactional
    public Set<Item> getContent(Long id){
        Optional<Cart> optionalCart = repository.findById(id);
        if (optionalCart.isEmpty()){
            throw new MyException("No se encontró el carrito con ID "+ id, HttpStatus.NOT_FOUND);
        } else {
            return repository.findById(id).get().getItems();
        }
    }

    // Crear nuevo carrito
    public Cart newCart(NewCartDTO cartDTO){
        Cart newCart = mapper.map(cartDTO);
        return repository.save(newCart);
    }

    // Agregar elementos a un carrito
    // Trabajar el tema alterar la cantidad de un item
    public Cart addItemToCart(Long cartId, Long productId, Integer quantity){
        Cart cart = this.findById(cartId);

        cart.getItems().add(new Item(
                quantity,
                cart,
                new Product(productId)
        ));

        return repository.save(cart);

    }

    // Eliminar elementos de un carrito
    public Cart deleteItemFromCart(Long cartId, Long itemId){
        Cart cart = repository.findById(cartId).get();
        Item matchedItem = new Item();

        for (Item element: cart.getItems()){
            if (element.getId().equals(itemId)){
                matchedItem = element;
            }
        }

        cart.getItems().remove(matchedItem);

        return repository.save(cart);

    }

    // Eliminar un carrito
    public void deleteCart(Long id){
        repository.deleteById(id);
    }
}
