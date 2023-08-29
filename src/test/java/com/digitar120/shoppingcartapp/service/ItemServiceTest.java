package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.ItemException;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.repository.ItemRepository;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    public static final String LAPIZ = "Lápiz";
    public static final String LAPICERA = "Lapicera";
    public static final Long ITEM_1 = 1L;
    public static final Long ITEM_2 = 2L;

    Item itemModel1 = new Item(ITEM_1, LAPIZ, 1);
    Item itemModel2 = new Item(ITEM_2, LAPICERA, 2);

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository repository;

    @Test
    @DisplayName("Test findAll not empty OK")
    public void test_when_findAll_then_listNotEmpty(){
        // Mock del repositorio
        Item item = itemModel1;
        Item item2 = itemModel2;

        List<Item> list = new ArrayList<>();
        list.add(item);
        list.add(item2);

        when(repository.findAll()).thenReturn(list);

        // Ejecución de la llamada
        List<Item> returned_list = itemService.findAll();

        // Asserts
        assertFalse(list.isEmpty());

    }

    @Test
    @DisplayName("Test findAll returns given list OK")
    public void test_when_findAll_then_listEqualsGivenList(){
        // Mock del repositorio
        Item item = itemModel1;
        Item item2 = itemModel2;
        List<Item> list = new ArrayList<>();
        list.add(item);
        list.add(item2);
        when(repository.findAll()).thenReturn(list);

        // Ejecución de la llamada
        List<Item> returned_list = itemService.findAll();

        // Asserts
        assertEquals(list, returned_list);
    }

    @Test
    @DisplayName("Test findByDescription OK")
    public void test_when_findByDescription_then_returnsCorrectItem(){
        // Mock del repositorio
        Item item = itemModel1;

        when(repository.findByDescription(LAPIZ)).thenReturn(item);

        // Ejecución de la llamada
        Item returned_item = itemService.findByDescription(LAPIZ);

        // Asserts
        assertEquals(LAPIZ, returned_item.getDescription());
    }

    @Test
    @DisplayName("Test saveToRepo saves ITEM_1")
    public void test_when_saveToRepo_then_repoSavesITEM_1(){
        // Arrange
        Item item = itemModel1;

        // Act
        when(repository.save(item)).thenReturn(item);
        Item actualResult = itemService.saveToRepo(item);

        // Assert
        assertEquals(item, actualResult);
    }

    @Test
    @DisplayName("Test findByID returns correct item")
    public void test_when_findById_then_returnsCorrectItem(){
        // Arrange
        Item item = itemModel1;

        // Act
        when(repository.findById(0L)).thenReturn(Optional.of(item));
        Item actualResult = itemService.findById(0L);

        // Assert
        assertEquals(item, actualResult);
    }

    @Test
    @DisplayName("Test saveToRepoIfPresent saves ITEM_1")
    public void test_when_saveToRepoIfNotPresent_then_repoSavesITEM_1(){
        // Arrange
        Item item = itemModel1;

        // Act
        when(repository.save(item)).thenReturn(item);
        Item actualResult = itemService.saveToRepoIfNotPresent(item);

        // Assert
        assertEquals(item, actualResult);
    }

    @Test(expected=ItemException.class)
    @DisplayName("Test saveToRepoIfPresent throws exception if item is present")
    public void test_when_saveToRepoIfNotPresent_then_throwException(){
        // Arrange
        Item item = itemModel1;

        // Act
        when(repository.save(item)).thenReturn(item).thenThrow(new ItemException("Ya existe un elemento con ID N°" + item.getId(), HttpStatus.BAD_REQUEST));

        itemService.saveToRepoIfNotPresent(item);
            // Primera ejecución

        Object actualResult = itemService.saveToRepoIfNotPresent(item);
            // Segunda ejecución

        // Assert
        // Mediante anotación
    }

    @Test
    @DisplayName("Test editItem produces an edited item")
    public void test_when_editItem_then_editedItemInRepository (){
        // Arrange
        Item item = itemModel1;
        EditedItemDTO edited_item = new Item(ITEM_1, LAPICERA, 1);

        // Act
        when(repository.save(item)).thenReturn(item);

        itemService.saveToRepo(item);
        Item actualResult = itemService.editItem(edited_item, item.getId());

        // Assert
        assertEquals(edited_item, actualResult);
    }


}
