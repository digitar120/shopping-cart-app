package com.digitar120.shoppingcartapp.persistence.repository;

import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
