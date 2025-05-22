package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.service.ProductService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product endpoint configuration.
 * @author Gabriel Pérez (digitar120)
 * @see Product
 * @see ProductService
 */
@RestController
@RefreshScope
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Read operations

    /**
     * List all entries in the database.
     * @return A list of all products registered.
     */
    @GetMapping
    @Operation(summary = "List all products.", description = "Produce a JSON object with details about all registered products.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request completed correctly."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    public List<Product> listAllProducts(){
        return productService.listAllProducts();
    }

    /**
     * Find a product, matching its ID.
     * @param productId ID to match the product with.
     * @return A matching product if the search is positive.
     */
    @Operation(summary = "Find a product by its ID.", description = "Given a product ID, produce a JSON object with a matching product's details.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request completed correctly."),
            @ApiResponse(code = 404, message = "Could not find a product with that ID."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/{productId}")
    public Product findById(@PathVariable Long productId){
        return productService.findById(productId);
    }

    /**
     * Find a product, matching its description
     * @param description Description to match a product with.
     * @return A matching product, if the search is positive.
     */
    @Operation(summary = "Find a product by its description.", description = "Given a description string, produce a JSON object with a matching product's details.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request completed correctly."),
            @ApiResponse(code = 404, message = "Could not find a product with that description."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/{description}")
    public List<Product> findByDescription(@PathVariable String description){
        return productService.findByDescription(description);
    }

    // Create operations

    /**
     * Create a new product with the included description.
     * @param description A description for the new product entry.
     * @return A copy of the new entry in the database.
     */
    @Operation(summary = "Add a product.", description = "Given a description string, register a new product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Element added."),
            @ApiResponse(code = 400, message = "Bad request. Found an existing product with the same description. Try again."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("/{description}")
    public Product newProduct(@PathVariable String description){
        return productService.newProduct(description);
    }

    // Delete operations

    /**
     * Delete an item, matching its ID.
     * @param id ID to match the item with.
     */
    @Operation(summary = "Delete a product.", description = "Given a numeric ID, delete a registered product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Element deleted."),
            @ApiResponse(code = 404, message = "Element not found."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }
}
