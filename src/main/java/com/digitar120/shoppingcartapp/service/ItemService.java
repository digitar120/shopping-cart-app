package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.mapper.ItemToEditedItem;
import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.ItemRepository;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.digitar120.shoppingcartapp.util.MyMethods.*;

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
        return verifyElementExistsAndReturn(repository, id, "No se encontró un ítem de N° " + id, HttpStatus.NOT_FOUND);
    }

    // Agregar, con verificación
    public Item saveToRepo(Item item){
        verifyElementNotExists(repository, item.getId(), "El ítem N° " + item.getId() + " ya existe.", HttpStatus.BAD_REQUEST);
        return repository.save(item);
    }

    public Item newItem(Long cartId, Long productId, Integer quantity){
        return repository.save(new Item(
                quantity,
                new Cart(cartId),
                new Product(productId)
        ));
    }

    // Editar
    @Transactional
    public Item editItem(EditedItemDTO edited_item_fields, Long id){
        Item item = mapper.map(edited_item_fields);
        item.setId(id);

        verifyElementExists(repository, id, "No se encontró un ítem de N°" + id, HttpStatus.NOT_FOUND);
            // Contiene un throw. Si el elemento no existe, termina en éste punto.
        return repository.save(item);
    }

    // Eliminar
    @Transactional
    public void deleteById(Long id) {
        verifyElementExists(repository, id, "No se encontró un ítem de N°" + id, HttpStatus.NOT_FOUND);
        repository.deleteById(id);
    }

}
