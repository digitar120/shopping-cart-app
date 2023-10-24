package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.service.ProductService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Listar todos
    @GetMapping
    @Operation(summary = "Listar productos", description = "Listar todos los productos almacenados.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Solicitud correcta"),
            @ApiResponse(code = 500, message = "Algo salió mal")
    })
    public List<Product> listAllProducts(){
        return productService.listAllProducts();
    }

    // Buscar por ID
    @Operation(summary = "Buscar por ID", description = "Buscar un producto mediante ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Solicitud correcta"),
            @ApiResponse(code = 404, message = "No se encontró el producto"),
            @ApiResponse(code = 500, message = "Algo salió mal")
    })
    @GetMapping("/{productId}")
    public Product findById(@PathVariable Long productId){
        return productService.findById(productId);
    }

    // Encontrar por descripción
    @Operation(summary = "Buscar por descripción", description = "Buscar un producto mediante una descripción.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Solicitud correcta"),
            @ApiResponse(code = 404, message = "No se encontró un producto con esa descripción."),
            @ApiResponse(code = 500, message = "Algo salió mal")
    })
    @GetMapping("/{description}")
    public List<Product> findByDescription(@PathVariable String description){
        return productService.findByDescription(description);
    }

    // Agregar
    @Operation(summary = "Agregar un producto", description = "Agregar un producto mediante una descripción, mediante URL.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Elemento agregado"),
            @ApiResponse(code = 400, message = "Ya existe un producto con esa descripción."),
            @ApiResponse(code = 500, message = "Algo salió mal")
    })
    @PostMapping("/{description}")
    public Product newProduct(@PathVariable String description){
        return productService.newProduct(description);
    }

    // Eliminar
    @Operation(summary = "Eliminar un elemento", description = "Eliminar un elemento, mediante ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Elemento eliminado"),
            @ApiResponse(code = 404, message = "No se encontró el producto"),
            @ApiResponse(code = 500, message = "Algo salió mal")
    })
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }
}
