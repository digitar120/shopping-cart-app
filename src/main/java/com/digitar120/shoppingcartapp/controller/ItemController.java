package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.exception.ItemNotFoundException;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.ItemService;
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

    @PostMapping
    public Item newItem(@RequestBody Item item){
        return itemService.saveToRepoIfNotPresent(item);
    }

    @PutMapping("/{id}")
    public Item editItem(@RequestBody Item item, @PathVariable Long id){
        return itemService.editItem(item, id);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id){
        itemService.deleteById(id);
    }
}
