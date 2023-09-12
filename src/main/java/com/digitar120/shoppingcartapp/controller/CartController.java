package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService service;
    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cart> findAll(){
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public Cart findById(@PathVariable Long id){
        return service.findById(id);
    }

    @GetMapping("/{id}/items")
    public Set<Item> getCartItems (@PathVariable Long id){
        return service.getContent(id);
    }

}
