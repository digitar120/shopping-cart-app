package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.CartService;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
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
    @GetMapping("/{id}/items")
    public Set<Item> getCartItems (@PathVariable Long id){
        return service.getContent(id);
    }

    // Crear nuevo
    @PostMapping
    public Cart newCart(@RequestBody NewCartDTO newCartDTO){
        return service.newCart(newCartDTO);
    }

    // Agregar elementos
    // Eliminar elementos
    // Eliminar carrito

}
