package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.ItemService;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Item endpoint configuration
 * @author Gabriel Pérez (digitar120)
 * @see Item
 * @see ItemService
 */
@RestController
@RequestMapping("/items")
@ConditionalOnExpression("${controller.item.enabled:false}")
public class ItemController {

    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Read operations

    /**
     * List all entries.
     * @return A list of entries in the database
     */
    @GetMapping
    public List<Item> findAll(){
        return this.itemService.findAll();
    }

    /**
     * Find an item by matching its ID.
     * @param id
     * @return A matching item, if the search is positive
     */
    @GetMapping("/{id}")
    public Item findById(@PathVariable Long id){ return itemService.findById(id);}

    // Create operations
    /**
     * Create a new item by reading the parameters in JSON format.
     * @param item Item object read in JSON format.
     * @return A copy of a new item entry.
     */
    @PostMapping
    public Item newItem(@RequestBody Item item){
        return itemService.saveToRepo(item);
    }

    /**
     * Create a new Item by reading parameters passed in the URI.
     * @param cartId ID of the cart to associate the item to.
     * @param productId ID of the product to associate the item to.
     * @param quantity Quantity relative to the product.
     * @return A copy of a new item created in the database.
     */
    @PostMapping("/cart/{cartId}/product/{productId}/quantity/{quantity}")
    public Item newItem(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity){
        return itemService.newItem(cartId, productId, quantity);
    }

    // Update operations

    /**
     * Edit an item in the database.
     * @param item DTO, read from a JSON file, containing new data for an item.
     * @param id ID of the item to affect.
     * @return A copy of the edited item in the database.
     */
    @PutMapping("/{id}")
    public Item editItem(@RequestBody EditedItemDTO item, @PathVariable Long id){
        return itemService.editItem(item, id);
    }

    // Delete operations

    /**
     * Delete an item in the database.
     * @param id ID of the item to affect.
     */
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id){
        itemService.deleteById(id);
    }
}
