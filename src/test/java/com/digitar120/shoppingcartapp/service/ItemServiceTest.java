package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.repository.ItemRepository;
import com.digitar120.shoppingcartapp.service.ItemService;
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
public class ItemServiceTest {

    public static final String LAPIZ = "Lápiz";
    public static final String LAPICERA = "Lapicera";

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository repository;

    @Test
    @DisplayName("Test findAll not empty OK")
    public void test_when_findAll_then_listNotEmpty(){
        // Mock del repositorio
        Item item = new Item(1L, LAPIZ, 1);
        Item item2 = new Item(2L, LAPICERA, 2);
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
        Item item = new Item(1L, LAPIZ, 1);
        Item item2 = new Item(2L, LAPICERA, 2);
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
        Item item = new Item(1L, LAPIZ, 1);

        when(repository.findByDescription(LAPIZ)).thenReturn(item);

        // Ejecución de la llamada
        Item returned_item = itemService.findByDescription(LAPIZ);

        // Asserts
        assertEquals(LAPIZ, returned_item.getDescription());
    }

}
