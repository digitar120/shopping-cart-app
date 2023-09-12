package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Listar
    public List<Product> listAllProducts(){
        return productRepository.findAll();
    }

    // Buscar por descripción
    public List<Product> findByDescription(String description){
        return productRepository.findByDescription(description);
    }

    // Agregar
    public Product newProduct(String description){
        Product product = new Product(description);
        return productRepository.save(product);
    }

    // Eliminar
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
}
