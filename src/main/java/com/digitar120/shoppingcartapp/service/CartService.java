package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.MyException;
import com.digitar120.shoppingcartapp.mapper.CartDTOtoCart;
import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.CartRepository;
import com.digitar120.shoppingcartapp.persistence.repository.ItemRepository;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final ProductService productService;
    private final CartDTOtoCart mapper;

    public CartService(CartRepository cartRepository, ItemRepository itemRepository, ProductService productService, CartDTOtoCart mapper) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.productService = productService;
        this.mapper = mapper;
    }


    // Listar carritos
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    // Encontrar carrito mediante ID
    public Cart findById(Long id){
        return cartRepository.findById(id).orElseThrow( () -> new MyException("No se encontró el carrito con ID "+ id, HttpStatus.NOT_FOUND));
    }

    // Listar contenidos de un carrito
    @Transactional
    public Set<Item> getContent(Long id){
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isEmpty()){
            throw new MyException("No se encontró el carrito con ID "+ id, HttpStatus.NOT_FOUND);
        } else {
            return cartRepository.findById(id).get().getItems();
        }
    }

    // Crear nuevo carrito
    public Cart newCart(NewCartDTO cartDTO){
        Cart newCart = mapper.map(cartDTO);
        return cartRepository.save(newCart);
    }

    // Eliminar un carrito
    public void deleteCart(Long id){
        cartRepository.deleteById(id);
    }

    // Agregar elementos a un carrito
    /*
    public Item addItemToCart(Long productId, Long cartId, Integer quantity){
        Product product = productService.findById(productId);
        Cart cart = this.findById(cartId);

        Item item = new Item (quantity, cart, product);


    }*/

    // Eliminar elementos de un carrito
}
