package com.digitar120.shoppingcartapp.persistence.repository;

import com.digitar120.shoppingcartapp.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByDescription(String description);
}
