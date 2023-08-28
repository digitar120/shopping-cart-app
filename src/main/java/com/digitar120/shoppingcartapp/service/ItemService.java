package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.ItemException;
import com.digitar120.shoppingcartapp.exception.ItemNotFoundException;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Item findById(Long id){
        return repository.findById(id).orElseThrow( () -> new ItemException("No se encontró un elemento con ID " + id, HttpStatus.NOT_FOUND));
    }

    public Item findByDescription(String description){
        return repository.findByDescription(description);
    }

    public Item saveToRepo(Item item){
        return repository.save(item);
    }

    public Item saveToRepoIfNotPresent(Item item){
        Optional<Item> optionalItem = this.repository.findById(item.getId());

        if(optionalItem.isEmpty()){
            return repository.save(item);
        } else {
            throw new ItemException("Ya existe un elemento con ID N°" + item.getId(), HttpStatus.BAD_REQUEST);
        }
    }

    public Item editItem(Item edited_item, Long id){
        return repository.findById(id)
                .map (item -> {
                    item.setDescription(edited_item.getDescription());
                    item.setQuantity(edited_item.getQuantity());
                    return repository.save(item);
                })
                .orElseThrow(() -> new ItemException("No se encontró un elemento con ID " + edited_item.getId(), HttpStatus.NOT_FOUND));
    }

    public void deleteById(Long id) {
        Optional<Item> optionalItem = this.repository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ItemException("No se encontró un elemento con ID " + id, HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

}
