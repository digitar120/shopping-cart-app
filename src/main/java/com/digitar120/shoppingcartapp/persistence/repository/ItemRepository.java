package com.digitar120.shoppingcartapp.persistence.repository;

import com.digitar120.shoppingcartapp.persistence.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long> {

    public Item findByDescription(String description);
}
