package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.ItemNotFoundException;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.repository.ItemRepository;
import org.mapstruct.IterableMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
        List<Item> itemList = this.repository.findAll();
        if (itemList.isEmpty()){
            throw new ItemNotFoundException();
        }
        return this.repository.findAll();
    }

    public Item findById(Long id){
        return repository.findById(id).orElseThrow( () -> new ItemNotFoundException(id));
    }

    public Item findByDescription(String description){
        return repository.findByDescription(description);
    }

    public Item saveToRepo(Item item){
        return repository.save(item);
    }

    public Item editItem(Item edited_item, Long id){
        return repository.findById(id)
                .map (item -> {
                    item.setDescription(edited_item.getDescription());
                    item.setQuantity(edited_item.getQuantity());
                    return repository.save(item);
                })
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    public void deleteById(Long id) {
        Optional<Item> optionalItem = this.repository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ItemNotFoundException(id);
        }
        repository.deleteById(id);
    }

}
