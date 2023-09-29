package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.ItemService;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> findAll(){
        return this.itemService.findAll();
    }

    @GetMapping("/{id}")
    public Item findById(@PathVariable Long id){ return itemService.findById(id);}

    @PostMapping
    public Item newItem(@RequestBody Item item){
        return itemService.saveToRepo(item);
    }

    @PostMapping("/cart/{cartId}/product/{productId}/quantity/{quantity}")
    public Item newItem(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity){
        return itemService.newItem(cartId, productId, quantity);
    }

    @PutMapping("/{id}")
    public Item editItem(@RequestBody EditedItemDTO item, @PathVariable Long id){
        return itemService.editItem(item, id);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id){
        itemService.deleteById(id);
    }
}
