package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.globalhandler.NotFoundException;
import com.digitar120.shoppingcartapp.mapper.ItemToEditedItem;
import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.ItemRepository;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.constraints.AssertTrue;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Item endpoint business logic.
 * @author Gabriel Pérez (digitar120)
 * @see Item
 * @see ItemService
 * @see ItemRepository
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {
    private static final Long ITEMCODE_PENCIL_2B = 1L;

    private static final Cart owningCart = new Cart(1L, "owningCart", null, 1);

    private static final Item ITEM_PENCIL_2B = new Item(
            1L,
            5,
            owningCart,
            new Product(1L, "2B Pencil"));

    private static final Item ITEM_PEN = new Item(
            2L,
            10,
            owningCart,
            new Product(2L, "Pen"));


    private static final Set<Item> itemHashSet = new HashSet<>();
    private static final List<Item> itemList = Arrays.asList(ITEM_PENCIL_2B, ITEM_PEN);


    /**
     * Initialize test item sets and carts.
     */
    @Before
    public void itemSetSetup(){
        itemHashSet.add(ITEM_PENCIL_2B);
        itemHashSet.add(ITEM_PEN);

        owningCart.setItems(itemHashSet);
    }

    @InjectMocks
    private ItemService service;

    @Mock
    private ItemRepository repository;

    @Mock
    private ItemToEditedItem mapper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // Method extractions

    private void findByIdOf2BReturnsItem() {
        when(repository.findById(ITEMCODE_PENCIL_2B)).thenReturn(Optional.of(ITEM_PENCIL_2B));
    }

    private void findById2BReturnsNothing() {
        when(repository.findById(ITEMCODE_PENCIL_2B)).thenReturn(Optional.empty());
    }

    private void repositorySaveReturnsItem() {
        Mockito
                .doAnswer(AdditionalAnswers.returnsFirstArg())
                .when(repository).save(any(Item.class));
    }

    // findAll

    /**
     * Given {@link ItemRepository#findAll()} returning a non-empty list, assert that {@link ItemService#findAll()}
     * executes that method.
     */
    @Test
    @DisplayName("findAll llama al repositorio")
    public void test_when_findAll_then_callsRepository(){
        when(repository.findAll()).thenReturn(itemList);

        service.findAll();

        verify(repository, times(1)).findAll();
    }

    /**
     * Given {@link ItemRepository#findAll()} returning a non-empty list, assert that the return of {@link ItemService#findAll()}
     * is also non-empty.
     */
    @Test
    @DisplayName("findAll no devuelve listas vacías")
    public void test_when_findAll_then_noEmptyListsReturned(){
        when(repository.findAll()).thenReturn(itemList);

        assertFalse(service.findAll().isEmpty());
    }


    //--------------------------------findById-----------------------------

    /**
     * Given {@link ItemRepository#findById(Object)} returning a matching item, assert that {@link ItemService#findById(Long)}
     * executes that method.
     */
    @Test
    @DisplayName("findById llama al repositorio usando el argumento dado")
    public void test_when_findAll_then_callMadeUsingGivenArgument(){
        findByIdOf2BReturnsItem();

        service.findById(ITEMCODE_PENCIL_2B);

        verify(repository, times(1)).findById(ITEMCODE_PENCIL_2B);
    }

    /**
     * Given {@link ItemRepository#findById(Object)} returning nothing, assert that {@link ItemService#findById(Long)}
     * throws an appropriate exception.
     */
    @Test
    @DisplayName("findById arroja excepción correctamente al no encontrar un elemento")
    public void test_when_findById_and_elementNotFound_then_throwException(){
        findById2BReturnsNothing();

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("No se encontró un ítem de N° " + ITEMCODE_PENCIL_2B);

        service.findById(ITEMCODE_PENCIL_2B);
    }

    /**
     * Given {@link ItemRepository#findById(Object)} returning a matching item, assert that its return and the return of
     * {@link ItemService#findById(Long)} are the same.
     */
    @Test
    @DisplayName("findById devuelve elemento correcto con búsqueda correcta")
    public void test_when_findById_and_argumentIsCorrect_then_returnCorrectElement(){
        findByIdOf2BReturnsItem();

        assertEquals(service.findById(ITEMCODE_PENCIL_2B), ITEM_PENCIL_2B);
    }

    // saveToRepo -------------------------------------------------------------------------------------

    /**
     * Given {@link ItemRepository#findById(Object)} returning nothing and {@link ItemRepository#save(Object)} returning
     * a valid item, assert that {@link ItemService#saveToRepo(Item)} executes the later method.
     */
    @Test
    @DisplayName("saveToRepo llama al repositorio para guardar un item")
    public void test_when_saveToRepo_then_repositorySaveCallMade(){
        findById2BReturnsNothing();
        repositorySaveReturnsItem();

        service.saveToRepo(ITEM_PENCIL_2B);

        verify(repository, times(1)).save(ITEM_PENCIL_2B);
    }

    /**
     * Given {@link ItemRepository#findById(Object)} returning nothing and {@link ItemRepository#save(Object)} returning
     * a valid item, assert that {@link ItemService#saveToRepo(Item)} executes the former method.
     */
    @Test
    @DisplayName("saveToRepo llama al repositorio para verificar existencia")
    public void test_when_saveToRepo_then_repositoryVerificationCallMade(){
        findById2BReturnsNothing();
        repositorySaveReturnsItem();

        service.saveToRepo(ITEM_PENCIL_2B);

        verify(repository, times(1)).findById(ITEMCODE_PENCIL_2B);
    }

    /**
     * Given {@link ItemRepository#findById(Object)} returning nothing, and {@link ItemRepository#save(Object)}
     * returning a valid item, assert its return and the return of {@link ItemService#saveToRepo(Item)} are the same.
     */
    @Test
    @DisplayName("saveToRepo devuelve el objeto correcto")
    public void test_when_saveToRepo_then_returnCorrectObject(){
        findById2BReturnsNothing();
        repositorySaveReturnsItem();

        assertEquals(ITEM_PENCIL_2B, service.saveToRepo(ITEM_PENCIL_2B));
    }

    @Test
    @DisplayName("newItem llama al repositorio para guardar un ítem")
    public void test_when_newItem_then_repositorySaveCallMade(){
        repositorySaveReturnsItem();

        service.newItem(1L, 1L,5);

        verify(repository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("newItem devuelve el objeto correcto")
    public void test_when_newItem_then_returnsCorrectItem(){
        Long cartIdInput = 1L;
        Long productIdInput = 1L;
        Integer quantityInput = 5;

        Item builtItem = new Item(
                quantityInput,
                new Cart(cartIdInput),
                new Product(productIdInput)
        );

        when(repository.save(any(Item.class))).thenReturn(builtItem);

        assertEquals(builtItem, service.newItem(cartIdInput, productIdInput, quantityInput));
    }

    // editItem ---------------------------------------------------------------------------------------

    @Test
    @DisplayName("editItem llama al repositorio para guardar y verificar")
    public void test_when_editItem_then_repositorySaveAndVerificationCallsMade(){
        findByIdOf2BReturnsItem();
        repositorySaveReturnsItem();
        when(mapper.map(any(EditedItemDTO.class))).thenReturn(new Item());

        service.editItem(new EditedItemDTO( 5), ITEMCODE_PENCIL_2B);

        verify(repository, times(1)).save(any(Item.class));
        verify(repository, times(1)).findById(ITEMCODE_PENCIL_2B);
    }

    @Test
    @DisplayName("editItem arroja excepción correctamente")
    public void test_when_editItem_then_exceptionThrownCorrectly(){
        findById2BReturnsNothing();

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("No se encontró un ítem de N°" + ITEMCODE_PENCIL_2B);

        service.editItem(new EditedItemDTO( 5), ITEMCODE_PENCIL_2B);
    }

    @Test
    @DisplayName("editItem devuelve objetos correctamente")
    public void test_when_editItem_then_returnsCorrectObject(){
        findByIdOf2BReturnsItem();
        when(repository.save(any(Item.class))).thenReturn(ITEM_PENCIL_2B);
        when(mapper.map(any(EditedItemDTO.class))).thenReturn(new Item());

        assertEquals(ITEM_PENCIL_2B, service.editItem(new EditedItemDTO( 5), ITEMCODE_PENCIL_2B));
    }

    @Test
    @DisplayName("deleteItem llama al repositorio para verificar y borrar")
    public void test_when_editItem_then_repositoryVerificationAndDeletionCallsMade(){
        findByIdOf2BReturnsItem();

        service.deleteById(ITEMCODE_PENCIL_2B);

        verify(repository, times(1)).findById(ITEMCODE_PENCIL_2B);
        verify(repository, times(1)).deleteById(ITEMCODE_PENCIL_2B);
    }

    @Test
    @DisplayName("deleteItem arroja excepción correctamente")
    public void test_when_deleteItem_then_exceptionThrownCorrectly(){
        findById2BReturnsNothing();

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("No se encontró un ítem de N°" + ITEMCODE_PENCIL_2B);

        service.deleteById(ITEMCODE_PENCIL_2B);
    }
}
