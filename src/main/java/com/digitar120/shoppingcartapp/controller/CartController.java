package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.CartService;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Cart endpoint configuration.
 * @author Gabriel Pérez (digitar120)
 */
@RestController
@RefreshScope
@RequestMapping("/cart")
@SuppressWarnings("never used")
public class CartController {

    private final CartService service;
    public CartController(CartService service) {
        this.service = service;
    }

    // Read operations

    /**
     * List all entries.
     * @return A cart list of all entries in the database.
     */
    @ApiOperation(value = "List all carts.", notes = "Produces a JSON object containing all registered carts and their items.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search successful."),
            @ApiResponse(code = 404, message = "Cart not found."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping
    public List<Cart> findAll(){
        return this.service.findAll();
    }

    /**
     * Find an entry by ID.
     * @param id ID to match the cart with.
     * @return If the search is positive, a matching cart.
     */
    @ApiOperation(value = "Find a cart by ID.", notes = "Produces a JSON object with details about the requested cart.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search successful."),
            @ApiResponse(code = 404, message = "Cart not found."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/{id}")
    public Cart findById(@PathVariable Long id){
        return service.findById(id);
    }

    /**
     * Find an entry matching its referenced user ID.
     * @param userId Referenced user ID to match the cart with.
     * @return A matching cart, if the search is positive.
     */
    @ApiOperation(value = "Find a cart by its user ID", notes = "Produce a JSON object with details about the matching cart, verifying beforehand that the user exists by running a search against the Users microservice.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search successful."),
            @ApiResponse(code=404, message = "User or cart not found.")
    })
    @GetMapping("/by-userid/{userId}")
    public Cart findByUserId(@PathVariable Integer userId){
        return service.findByUserId(userId);
    }

    /**
     * List the contents of a cart's item set, matching the cart by ID.
     * @param id ID to match the cart with.
     * @return A matching cart's item set.
     */
    @ApiOperation(value = "Show the contents of a car.t", notes = "Produce a JSON object with details about a cart's items.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request completed successfully."),
            @ApiResponse(code = 404, message = "Cart not found."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/{id}/items")
    public Set<Item> getCartItems (@PathVariable Long id){
        return service.getContent(id);
    }

    // Create opeartions

    /**
     * Create a new cart.
     * @param newCartDTO DTO containing information for a new cart, read in JSON format.
     * @return A copy of the new cart object.
     */
    @PostMapping
    @ApiOperation(value = "Create a new cart.", notes = "Given a JSON object containing a string named \"description\", create a new cart.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request completed successfully."),
            @ApiResponse(code = 400, message = "Bad request. Try again.")
    })
    public Cart newCart(@RequestBody NewCartDTO newCartDTO){
        return service.newCart(newCartDTO);
    }

    // Update operations

    /**
     * Add an item to a cart's item set.
     * @param cart_id ID of the cart to affect.
     * @param product_id ID of the product to add to the set.
     * @param quantity Quantity of the product to add to the set.
     * @return An updated cart entry.
     */
    @ApiOperation(value = "Add an item to a cart.", notes = "Given a cart's ID, a product's ID and a quantity, add a new item (associated to a product) to a cart's item set.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Element added."),
            @ApiResponse(code = 400, message = "Bad request. Invalid quantity."),
            @ApiResponse(code = 404, message = "Referenced product not found."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("/{cart_id}/product/{product_id}/quantity/{quantity}")
    public Cart addItemToCart(@PathVariable Long cart_id, @PathVariable Long product_id, @PathVariable Integer quantity)    {
        return service.addItemToCart(cart_id, product_id, quantity);
    }

    /**
     * Add several items to a cart's item set.
     * @param cartId ID of the cart to affect.
     * @param itemSet Item set to add to the cart. Read from JSON.
     */
    @ApiOperation(value = "Add multiple items to a cart.", notes = "Given a JSON object containing one or more item objects with full information, add those items to a cart's item set.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Elements added."),
            @ApiResponse(code = 404, message = "Could not find a referenced product from one of the items."),
            @ApiResponse(code = 400, message = "An item has an invalid quantity."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("/{cartId}")
    public void addMultipleItemsToCart(@PathVariable Long cartId, @RequestBody Set<Item> itemSet){
        service.addMultipleItemsToCart(cartId, itemSet);
    }

    // Delete operations

    /**
     * Delete an item from a cart.
     * @param cartId ID of the cart to affect.
     * @param itemId ID of the item to delete.
     * @return A copy of the updated cart entry.
     */
    @ApiOperation(value = "Delete a cart's item.", notes = "Given a cart ID and an item ID, remove that item from the cart's item set.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item removed."),
            @ApiResponse(code = 404, message = "Cart or item not found."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @DeleteMapping("/{cartId}/item/{itemId}")
    public Cart deleteItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId){
        return service.deleteItemFromCart(cartId, itemId);
    }

    /**
     * Delete a cart.
     * @param cartId ID of the cart to delete.
     */
    @ApiOperation(value = "Delete a cart", notes = "Given a cart ID, delete a cart and its registered items.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cart deleted."),
            @ApiResponse(code = 404, message = "Cart not found."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @DeleteMapping("/{cartId}")
    public void deleteCart(@PathVariable Long cartId){
        service.deleteCart(cartId);
    }
}
