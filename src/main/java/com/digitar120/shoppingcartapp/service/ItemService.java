package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.MyException;
import com.digitar120.shoppingcartapp.mapper.ItemToEditedItem;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.repository.ItemRepository;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private final ItemRepository repository;
    private final ItemToEditedItem mapper;
    public ItemService(ItemRepository repository, ItemToEditedItem mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // Listar todos
    public List<Item> findAll(){
        return this.repository.findAll();
    }

    // Buscar por id
    public Item findById(Long id){
        return repository.findById(id).orElseThrow( () -> new MyException("No se encontró un elemento con ID " + id, HttpStatus.NOT_FOUND));
    }

    // Agregar
    public Item saveToRepo(Item item){
        return repository.save(item);
    }

    // Agregar, con verificación
    public Item saveToRepoIfNotPresent(Item item){
        Optional<Item> optionalItem = this.repository.findById(item.getId());

        if(optionalItem.isEmpty()){
            return repository.save(item);
        } else {
            throw new MyException("Ya existe un elemento con ID N°" + item.getId(), HttpStatus.BAD_REQUEST);
        }
    }

    // Editar
    @Transactional
    public Item editItem(EditedItemDTO edited_item_fields, Long id){
        Item item = mapper.map(edited_item_fields);
        item.setId(id);

        Optional<Item> optionalItem = this.repository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new MyException("No se encontró un elemento con ID " + id, HttpStatus.NOT_FOUND);
        } else {
            return repository.save(item);
        }
    }

    // Eliminar
    @Transactional
    public void deleteById(Long id) {
        Optional<Item> optionalItem = this.repository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new MyException("No se encontró un elemento con ID " + id, HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

}
