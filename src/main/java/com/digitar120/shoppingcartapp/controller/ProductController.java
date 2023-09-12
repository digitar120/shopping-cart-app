package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Listar todos
    @GetMapping
    public List<Product> listAllProducts(){
        return productService.listAllProducts();
    }

    // Encontrar por descripción
    @GetMapping("/{description}")
    public List<Product> findByDescription(@PathVariable String description){
        return productService.findByDescription(description);
    }


    // Agregar
    @PostMapping
    public Product newProduct(@PathVariable String description){
        return productService.newProduct(description);
    }

    // Eliminar
    @DeleteMapping
    public void deleteProduct(Long id){
        productService.deleteProduct(id);
    }
}
