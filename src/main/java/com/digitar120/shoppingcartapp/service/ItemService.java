package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.mapper.ItemToEditedItem;
import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.ItemRepository;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.digitar120.shoppingcartapp.util.LocalUtilityMethods.*;

/**
 * Business logic for the Item endpoint.
 * <p>This structure is not as important as the others, but it was written nonetheless to have a way to
 * directly manipulate Item entries.</p>
 * @author Gabriel Pérez (digitar120)
 * @see ItemRepository
 * @see ItemToEditedItem
 */
@Service
public class ItemService {

    @Autowired
    private final ItemRepository repository;
    private final ItemToEditedItem mapper;
    public ItemService(ItemRepository repository, ItemToEditedItem mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // Read operations

    /**
     * Lists all Items in the database, from all Carts.
     * @return The contents of the repository.
     */
    public List<Item> findAll(){
        return this.repository.findAll();
    }

    /**
     * Finds an Item by its ID.
     * <p>Executes {@link com.digitar120.shoppingcartapp.util.LocalUtilityMethods#verifyElementExistsAndReturn(JpaRepository, Object, String, HttpStatus)}
     * and returns its contents.</p>
     * @param id The Item ID to match.
     * @return An matching Item object, if the search is positive.
     * @throws com.digitar120.shoppingcartapp.exception.globalhandler.NotFoundException If no matching Item is found.
     */
    public Item findById(Long id){
        return verifyElementExistsAndReturn(repository, id, "No se encontró un ítem de N° " + id, HttpStatus.NOT_FOUND);
    }

    // Create operations

    /**
     * Creates a new orphaned Item.
     * <p>Executes {@link com.digitar120.shoppingcartapp.util.LocalUtilityMethods#verifyElementNotExists(JpaRepository, Object, String, HttpStatus)}.</p>
     * @param item The Item object to save in the repository.
     * @return A copy of the inputted object.
     */
    public Item saveToRepo(Item item){
        verifyElementNotExists(repository, item.getId(), "El ítem N° " + item.getId() + " ya existe.", HttpStatus.BAD_REQUEST);
        return repository.save(item);
    }

    /**
     * Creates a new, full Item.
     * @param cartId A Cart ID to associate the Item with.
     * @param productId A Product ID to associate the Item with.
     * @param quantity A quantity associated to the Product.
     * @return A copy of the saved Item.
     */
    public Item newItem(Long cartId, Long productId, Integer quantity){
        return repository.save(new Item(
                quantity,
                new Cart(cartId),
                new Product(productId)
        ));
    }

    // Update operations

    /**
     * Edits an Item in the database.
     * <p>Executes {@link com.digitar120.shoppingcartapp.util.LocalUtilityMethods#verifyElementExists(JpaRepository, Object, String, HttpStatus)},
     * after which it will build a new Item based on the </p>
     * @param editedItemDTO A DTO containing the edited information of an Item.
     * @param id ID of the Item to edit.
     * @return A copy of the edited Item.
     */
    @Transactional
    public Item editItem(EditedItemDTO editedItemDTO, Long id){
        verifyElementExists(repository, id, "No se encontró un ítem de N°" + id, HttpStatus.NOT_FOUND);

        Item item = mapper.map(editedItemDTO);
        item.setId(id);

        return repository.save(item);
    }

    // Delete operations

    /**
     * Deletes an Item matching its ID.
     * Executes {@link com.digitar120.shoppingcartapp.util.LocalUtilityMethods#verifyElementExists(JpaRepository, Object, String, HttpStatus)},
     * and then it executes a deletion with the provided ID.
     * @param id The ID to execute the deletion with.
     */
    @Transactional
    public void deleteById(Long id) {
        verifyElementExists(repository, id, "No se encontró un ítem de N°" + id, HttpStatus.NOT_FOUND);
        repository.deleteById(id);
    }

}
