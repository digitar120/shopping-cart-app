package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.ItemService;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    public static final String LAPIZ = "Lápiz";
    public static final String LAPICERA = "Lapicera";
    public static final Long ITEM_1 = 1L;
    public static final Long ITEM_2 = 2L;

    Item itemModel1 = new Item(ITEM_1, LAPIZ, 1);
    Item itemModel1Edited = new Item(ITEM_1, LAPICERA, 1);

    List<Item> emptyItemListModel = new ArrayList<>();
    List<Item> itemListModel = new ArrayList<>();

    @Before
    public void setup (){
        itemListModel.add(itemModel1);
    }

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;


    @Test
    @DisplayName("Test newItem calls service save method")
    public void test_when_newItem_then_callsServiceSaveMethod(){
        // Arrange
        Item item = itemModel1;
        List<Item> itemList = itemListModel;

        when(itemService.saveToRepo(item)).thenReturn(item);

        // Act
        itemController.newItem(item);
        List<Item> actualResult = itemController.findAll();

        // Assert
        verify(itemService, times(1)).saveToRepo(item);
    }

    @Test
    @DisplayName("Test findAll returns empty list when repository empty")
    public void test_when_findAll_then_returnsEmptyList(){
        // Arrange
        List<Item> emptyList = new ArrayList<>();
        when(itemService.findAll()).thenReturn(emptyList);

        // Act
        List<Item> actualResult = itemController.findAll();

        // Assert
        assertEquals(0, actualResult.size());
    }

    @Test
    @DisplayName("Test findAll returns list with item")
    public void test_when_findAll_then_returnListWithItem(){
        // Arrange
        List<Item> itemList = itemListModel; // Definir la lista a devolver
        Item item = itemModel1; // Definir un item
        when(itemService.findAll()).thenReturn(itemList);

        // Act
        itemController.newItem(item);
        List<Item> actualResult = itemController.findAll();

        // Assert
        assertEquals(itemList, actualResult);
    }

    @Test
    @DisplayName("Test findById returns item")
    public void test_when_findById_then_returnsItem (){
        // Arrange
        Item item = itemModel1;

        when(itemService.findById(
                any(Long.class)
        )).thenReturn(item);

        // Act
        Item result = itemController.findById(item.getId());

        // Assert
        assertEquals(item, result);
    }

    @Test
    @DisplayName("Test editItem returns edited item using DTO and ID")
    public void test_when_editItem_then_returnsEditedItem(){
        // Arrange
        Item item = itemModel1;
        Item editedItem = itemModel1Edited;
        EditedItemDTO itemDTO = new EditedItemDTO(LAPICERA, 1);

        when(itemService.editItem(
                any(EditedItemDTO.class),
                any(Long.class)
        )).thenReturn(editedItem);

        // Act
        Item result = itemController.editItem(itemDTO, item.getId());

        // Assert
        assertEquals(editedItem, result);
    }

    @Test
    @DisplayName("Test deleteItem calls delete method")
    public void test_when_deleteItem_then_callDeleteMethod (){
        // Arrange
        // --

        // Act
        itemController.deleteItem(ITEM_1);

        // Assert
        verify(itemService, times(1)).deleteById(ITEM_1);
    }
}
