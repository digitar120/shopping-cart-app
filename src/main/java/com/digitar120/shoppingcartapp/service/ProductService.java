package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.globalhandler.BadRequestException;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.digitar120.shoppingcartapp.util.LocalUtilityMethods.verifyElementExists;
import static com.digitar120.shoppingcartapp.util.LocalUtilityMethods.verifyElementExistsAndReturn;

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


    // Buscar por id
    public Product findById(Long id){
        return verifyElementExistsAndReturn(productRepository, id, "No se encontró el producto de ID " + id, HttpStatus.NOT_FOUND);
    }

    // Agregar
    public Product newProduct(String description){
        if(this.findByDescription(description).isEmpty()){
            return productRepository.save(new Product(description));
        } else {
            throw new BadRequestException("Ya existe un producto \"" + description + "\".");
        }
    }

    // Eliminar
    @Transactional
    public void deleteProduct(Long id){
        verifyElementExists(productRepository, id, "No se encontró el producto N°" + id, HttpStatus.NOT_FOUND);
        productRepository.deleteById(id);
    }
}
