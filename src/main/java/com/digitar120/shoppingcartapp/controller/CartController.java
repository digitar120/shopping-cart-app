package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.CartService;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/cart")
@SuppressWarnings("never used")
public class CartController {

    private final CartService service;
    public CartController(CartService service) {
        this.service = service;
    }

    // Listar todos
    @GetMapping
    public List<Cart> findAll(){
        return this.service.findAll();
    }

    // Encontrar por id
    @GetMapping("/{id}")
    public Cart findById(@PathVariable Long id){
        return service.findById(id);
    }

    // Listar contenidos por id
    @GetMapping("/{id}/item")
    public Set<Item> getCartItems (@PathVariable Long id){
        return service.getContent(id);
    }

    // Crear nuevo
    @PostMapping
    public Cart newCart(@RequestBody NewCartDTO newCartDTO){
        return service.newCart(newCartDTO);
    }

    // Agregar elementos
    @PostMapping("/{cart_id}/product/{product_id}/quantity/{quantity}")
    public Cart addItemToCart(@PathVariable Long cart_id, @PathVariable Long product_id, @PathVariable Integer quantity){
        return service.addItemToCart(cart_id, product_id, quantity);
    }

    // Eliminar elementos
    @DeleteMapping("/{cartId}/item/{itemId}")
    public Cart deleteItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId){
        return service.deleteItemFromCart(cartId, itemId);
    }

    // Eliminar carrito
    @DeleteMapping("/{cartId}")
    public void deleteCart(@PathVariable Long cartId){
        service.deleteCart(cartId);
    }
}
