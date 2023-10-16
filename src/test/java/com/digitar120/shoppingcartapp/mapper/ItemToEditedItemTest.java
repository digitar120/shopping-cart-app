package com.digitar120.shoppingcartapp.mapper;

import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ItemToEditedItemTest {

    public static final String LAPIZ = "Lápiz";
    public static final String LAPICERA = "Lapicera";
    public static final Long ITEM_1 = 1L;
    public static final Long ITEM_2 = 2L;

    Product product1 = new Product(LAPIZ);
    Product product2 = new Product(LAPICERA);

    Cart cart = new Cart();
    Set<Item> cartItems = new HashSet<>();

    Item itemModel1 = new Item(ITEM_1, 2, cart, product1);
    Item itemModel2 = new Item(ITEM_2, 5, cart, product2);
    EditedItemDTO editedItemModel = new EditedItemDTO(LAPICERA,2);

    private ItemToEditedItem mapper = new ItemToEditedItem();

    @Before
    public void setup(){
        cartItems.add(itemModel1);
        cartItems.add(itemModel2);
    }




    // Verificar que se fabrica un ítem de tipo Item a partir de un tipo ItemToEditedItem

    @Test
    @DisplayName("Test EditedItem mapper correctly converts DTO into type")
    public void when_EditedItemMapper_then_correctItemTypeCreated (){
        // Arrange
        EditedItemDTO editedItem = editedItemModel;
        Item item = itemModel1;
        item.setId(null);

        // Act
        Item returnedItem = mapper.map(editedItem);

        // Assert
        assertEquals(item, returnedItem);
    }
}
