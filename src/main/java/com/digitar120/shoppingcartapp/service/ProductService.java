package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.globalhandler.BadRequestException;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.digitar120.shoppingcartapp.util.LocalUtilityMethods.verifyElementExists;
import static com.digitar120.shoppingcartapp.util.LocalUtilityMethods.verifyElementExistsAndReturn;

/**
 * Business logic for the Product endpoint.
 * @author Gabriel Pérez (digitar120)
 * @see Product
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Read Operations

    /**
     * Get the full contents of the repository.
     * @return A list of the contents of the repository.
     */
    public List<Product> listAllProducts(){
        return productRepository.findAll();
    }

    /**
     * Get a list of products that match a description.
     * @param description String to match products with.
     * @return A list of matching products.
     */
    public List<Product> findByDescription(String description){
        return productRepository.findByDescription(description);
    }

    /**
     * Search for a product matching its ID.
     * <p>Executes {@link com.digitar120.shoppingcartapp.util.LocalUtilityMethods#verifyElementExistsAndReturn(JpaRepository, Object, String, HttpStatus)}.</p>
     * @param id ID to match the product with.
     * @return A matching product, if the search ispositive.
     * @throws com.digitar120.shoppingcartapp.exception.globalhandler.NotFoundException If the search is negative.
     */
    public Product findById(Long id){
        return verifyElementExistsAndReturn(productRepository, id, "No se encontró el producto de ID " + id, HttpStatus.NOT_FOUND);
    }

    // Create operations

    /**
     * Creates a new product in the database.
     * @param description A description for the new entry.
     * @return A copy of a newly created entry.
     * @throws BadRequestException If a product with the same description already exists.
     */
    public Product newProduct(String description){
        if(this.findByDescription(description).isEmpty()){
            return productRepository.save(new Product(description));
        } else {
            throw new BadRequestException("Ya existe un producto \"" + description + "\".");
        }
    }

    // Delete operations

    /**
     * Removes a matching product.
     * <p>Executes {@link com.digitar120.shoppingcartapp.util.LocalUtilityMethods#verifyElementExists(JpaRepository, Object, String, HttpStatus)}.</p>
     * @param id ID to match the product with.
     */
    @Transactional
    public void deleteProduct(Long id){
        verifyElementExists(productRepository, id, "No se encontró el producto N°" + id, HttpStatus.NOT_FOUND);
        productRepository.deleteById(id);
    }
}
