package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private final ItemRepository repository;
    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> findAll(){
        return this.repository.findAll();
    }

    public Item findByDescription(String description){
        return repository.findByDescription(description);
    }


}
