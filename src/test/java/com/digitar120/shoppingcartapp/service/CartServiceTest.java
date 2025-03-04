package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.globalhandler.BadRequestException;
import com.digitar120.shoppingcartapp.exception.globalhandler.NotFoundException;
import com.digitar120.shoppingcartapp.exception.globalhandler.ServiceUnavailableException;
import com.digitar120.shoppingcartapp.feignclient.UserClient;
import com.digitar120.shoppingcartapp.feignclient.response.UserResponse;
import com.digitar120.shoppingcartapp.mapper.CartDTOtoCart;
import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.CartRepository;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.text.html.Option;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Unit testing for the Cart endpoint business logic.
 * <p>Uses Mockito Spies for testing methods that depend on other methods in the same class, such as
 * {@link CartService#getContent(Long)}, which depends on {@link CartService#findById(Long)}.</p>
 * @author Gabriel Pérez (digitar120)
 * @see CartService
 * @see Cart
 * @see ExpectedException
 * @see CartDTOtoCart
 * @see UserClient
 */
@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    private final static Long ID_CART_1 = 1L;
    private final static String DESCRIPTION_CART_1 = "Cart";
    private final static Integer USERID_CART_1 = 1;

    private final static Cart CART_1 = new Cart(ID_CART_1, DESCRIPTION_CART_1, new HashSet<>(), USERID_CART_1);

    private final static Cart TEST_CART_2 = new Cart(2L, "Carrito 2", null, 2);


    @InjectMocks
    private CartService service;

    /**
     * A Mockito Spy copy of {@code CartService service}, initialized with {@code setup()}.
     */
    private CartService serviceSpy;

    @Mock
    private CartRepository repository;
    @Mock
    private UserClient userServiceConnection;

    @Mock
    private CartDTOtoCart mapper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup(){
        serviceSpy = Mockito.spy(service);

        CART_1.getItems().add(new Item(1L, 5, null, new Product(1L) ));
    }

    // Helper methods

    private void findByIdReturnsCart() {
        when(repository.findById(ID_CART_1)).thenReturn(Optional.of(CART_1));
    }

    private void findByUserIdReturnsCart() {
        when(repository.findByUserId(anyInt())).thenReturn(Optional.of(new Cart()));
    }

    private void spyFindByIdReturnsCart() {
        Mockito.doReturn(CART_1).when(serviceSpy).findById(any());
    }

    private void returnCartOnRepositorySave() {
        Mockito
                .doAnswer(AdditionalAnswers.returnsFirstArg())
                .when(repository).save(any(Cart.class));
    }

    // findAll

    /**
     * Assert that {@link CartService#findAll()} executes {@link CartRepository#findAll()}.
     */
    @Test
    @DisplayName("findAll llama al reposositorio")
    public void test_when_findAll_then_repositorySearchCallMade(){
        when(repository.findAll()).thenReturn(List.of(CART_1, TEST_CART_2));

        service.findAll();

        verify(repository, times(1)).findAll();
    }

    /**
     * Assert that {@link CartService#findAll()} returns a non-empty list.
     */
    @Test
    @DisplayName("findAll no devuelve una lista vacía")
    public void test_when_findAll_and_repositoryResponseContainsElements_then_responseContainsElements(){
        when(repository.findAll()).thenReturn(List.of(CART_1, TEST_CART_2));

        assertFalse(service.findAll().isEmpty());
    }

    // findById

    /**
     * With {@link CartRepository#findById(Object)} returning a valid cart, assert that {@link CartService#findById(Long)}
     * executes it.
     */
    @Test
    @DisplayName("findById llama al repositorio para buscar")
    public void test_when_findById_then_repositorySearchCallMade(){
        findByIdReturnsCart();

        service.findById(ID_CART_1);

        verify(repository, times(1)).findById(anyLong());
    }

    /**
     * With {@link CartRepository#findById(Object)} returning a valid cart, assert that its returned element matches
     * the element returned by {@link CartService#findById(Long)}.
     */
    @Test
    @DisplayName("findById devuelve elemento correcto")
    public void test_when_findById_then_returnsCorrectElement(){
        findByIdReturnsCart();

        assertEquals(CART_1, service.findById(ID_CART_1));
    }

    // findByUserId

    /**
     * With: <ul>
     *     <li>{@link CartRepository#findById(Object)} returning a valid cart.</li>
     *     <li>An {@link UserClient} connection returning <b>a positive response</b>.</li>
     * </ul>
     *
     * <p>Assert that {@link CartService#findById(Long)} executes calls to both of those items.</p>
     */
    @Test
    @DisplayName("findByUserId llama al repositorio y al servicio de usuarios")
    public void test_when_findByUserId_then_repositoryAndUserServiceCallsMade(){
        // Devuelve correctamente un carrito
        findByUserIdReturnsCart();

        // Resultado afirmativo
        when(userServiceConnection.getUserByUserId(anyInt())).thenReturn(new UserResponse(1));

        service.findByUserId(1);

        verify(repository, times(1)).findByUserId(anyInt());
        verify(userServiceConnection, times(1)).getUserByUserId(1);
    }

    /**
     * With:
     * <ul>
     *     <li>{@link CartRepository#findById(Object)} returning a valid cart.</li>
     *     <li>{@link UserClient#getUserByUserId(Integer)} returning an error response.</li>
     * </ul>
     * <p>Assert that {@link CartService#findByUserId(Integer)} throws an appropriate exception.</p>
     */
    @Test
    @DisplayName("findByUserId arroja excepción al no poder conectar")
    public void test_when_findByUserId_and_cantConnect_then_exceptionThrown(){
        findByUserIdReturnsCart();
        when(userServiceConnection.getUserByUserId(anyInt())).thenReturn(new UserResponse(-1));

        expectedException.expect(ServiceUnavailableException.class);
        expectedException.expectMessage("Error interno. No se pudo verificar que el usuario existe. Intente más tarde.");

        service.findByUserId(1);
    }

    /**
     * With:
     * <ul>
     *  <li>{@link CartRepository#findByUserId(Integer)} returning an empty response.</li>
     * <li>{@link UserClient#getUserByUserId(Integer)} returns a valid user.</li>
     * </ul>
     *
     * <p>Assert that {@link CartService#findByUserId(Integer)} throws an appropriate exception.</p>
     */
    @Test
    @DisplayName("findByUserId arroja excepción al no encontrar un elemento")
    public void test_when_findByUserId_and_elementNotFound_then_throwException(){
        when(repository.findByUserId(anyInt())).thenReturn(Optional.empty());
        when(userServiceConnection.getUserByUserId(anyInt())).thenReturn(new UserResponse(1));

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("No existe un carrito asignado a ése usuario.");

        service.findByUserId(1);
    }

    /**
     * With:
     * <ul>
     * <li>{@link CartRepository#findByUserId(Integer)} returning a valid cart.</li>
     * <li>{@link UserClient#getUserByUserId(Integer)} returning a valid user.</li>
     * </ul>
     *
     * <p>Assert that the cart returned by the repository is the same as the cart returned by {@link CartService#findByUserId(Integer)}.</p>
     */
    @Test
    @DisplayName("findByUserId devuelve elemento correcto")
    public void test_when_findByUserId_then_returnsCorrectElement(){
        when(repository.findByUserId(anyInt())).thenReturn(Optional.of(CART_1));
        when(userServiceConnection.getUserByUserId(anyInt())).thenReturn(new UserResponse(1));

        assertEquals(CART_1, service.findByUserId(1));
    }

    /**
     * With a spy copy of an instance of {@code CartService} executing {@link CartService#findById(Long)} and returning a valid
     * cart, assert that {@link CartService#getContent(Long)} executes that method.
     * @see Spy
     */
    @Test
    @DisplayName("getContent llama para buscar")
    public void test_when_getContent_then_repositorySearchCallMade(){
        spyFindByIdReturnsCart();

        serviceSpy.getContent(1L);

        verify(serviceSpy, times(1)).findById(any());
    }


    /**
     * Given a spy copy of an instance of {@code CartService}, executing {@link CartService#findById(Long)} and
     * returning a valid cart, assert that the return of {@link CartService#getContent(Long)} is not empty.
     */
    @Test
    @DisplayName("getContent no devuelve lista vacía")
    public void test_when_getContent_then_returnedListNotEmpty(){
        spyFindByIdReturnsCart();

        assertFalse(serviceSpy.getContent(1L).isEmpty());
    }

    /**
     * Given a spy copy of an instance of {@code CartService}, executing {@link CartService#findById(Long)} and
     * returning a valid cart, assert that {@link CartService#getContent(Long)} returns the same item set as that
     * method.
     */
    @Test
    @DisplayName("getContent devuelve lista correcta")
    public void test_when_getContent_then_retunrnsCorrectList(){
        spyFindByIdReturnsCart();

        assertEquals(CART_1.getItems(), serviceSpy.getContent(1L));
    }

    /**
     * Given {@link CartDTOtoCart#map(NewCartDTO)} and {@link CartRepository#save(Object)} returning valid carts,
     * assert that {@link CartService#newCart(NewCartDTO)} executes those two methods.
     */
    @Test
    @DisplayName("newCart llama al rpeositorio y al mapper")
    public void test_when_newCart_then_repositoryAndMapperCallsMade(){
        NewCartDTO cartDTO = new NewCartDTO("Description", 1);

        when(mapper.map(cartDTO)).thenReturn(CART_1);
        returnCartOnRepositorySave();

        service.newCart(cartDTO);

        verify(repository, times(1)).save(any());
        verify(mapper, times(1)).map(any());
    }

    /**
     * Given {@link CartDTOtoCart#map(NewCartDTO)} and {@link CartRepository#save(Object)} returning valid carts,
     * assert that the return of {@link CartService#newCart(NewCartDTO)} matches with the return of the repository save
     * call.
     */
    @Test
    @DisplayName("newCart devuelve el elemento correcto")
    public void test_when_newCart_then_returnsCorrectElement(){
        NewCartDTO cartDTO = new NewCartDTO("Description", 1);

        when(mapper.map(cartDTO)).thenReturn(CART_1);
        returnCartOnRepositorySave();

        assertEquals(CART_1, service.newCart(cartDTO));
    }

    // addItemToCart

    /**
     * Assert that {@link CartService#addItemToCart(Long, Long, Integer)} throws an appropriate exception if
     * {@code quantity} equals or is below 0.
     */
    @Test
    @DisplayName("addItemToCart arroja excepción si la quantity es negativa")
    public void test_when_addItemToCart_and_quantityIsNegative_then_throwException(){
        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("La cantidad ingresada es inválida.");

        service.addItemToCart(null, null, -1);
    }

    /**
     * Given a spy copy of {@code CartService} executing {@link CartService#findById(Long)} and returning a valid
     * cart, assert that {@link CartService#addItemToCart(Long, Long, Integer)} executes that method.
     */
    @Test
    @DisplayName("addItemToCart verifica que existe un carrito de código cartId")
    public void test_when_addItemToCart_then_verificationCallMade(){
        spyFindByIdReturnsCart();

        serviceSpy.addItemToCart(CART_1.getId(), 1L, 5);

        verify(serviceSpy, times(1)).findById(CART_1.getId());
    }

    /**
     * Given a spy copy of {@code CartSerivce} executing {@link CartService#findById(Long)} and
     * {@link CartRepository#save(Object)} returning valid carts, assert that
     * {@link CartService#addItemToCart(Long, Long, Integer)} returns a cart edited cart object.
     */
    @Test
    @DisplayName("addItemToCart actualiza cantidad de un ítem")
    public void test_when_addItemToCart_and_referendedProductExists_then_quantityUpdated(){
        spyFindByIdReturnsCart();
        returnCartOnRepositorySave();

        // Copia de CART_1 con itemSet actualizado
        Cart expectedCart = CART_1;

        Set<Item> expectedResult = new HashSet<>();
        expectedResult.add(new Item(1L, 10, null, new Product(1L) ));
        expectedCart.setItems(expectedResult);

        assertEquals(expectedCart, serviceSpy.addItemToCart(CART_1.getId(), 1L, 10));
    }

    /**
     * Given a spy copy of {@code CartService} executing {@link CartService#findById(Long)} and returning cart without
     * items, and {@link CartRepository#save(Object)} returning a valid cart, assert that the spy copy executing
     * {@link CartService#addItemToCart(Long, Long, Integer)} produces a proper result.
     */
    @Test
    @DisplayName("addItemToCart crea un nuevo item si no existía previamente")
    public void test_when_addItemToCart_and_referencedProductNotExists_then_newItemCreated(){
        Cart emptyCart = CART_1;
        emptyCart.setItems(new HashSet<>());

        Mockito.doReturn(emptyCart).when(serviceSpy).findById(emptyCart.getId());
        returnCartOnRepositorySave();

        assertEquals(CART_1, serviceSpy.addItemToCart(emptyCart.getId(), 1L, 5));
    }

    // addMultipleItemsToCart

    /**
     * Given an invalid item set, assert that {@link CartService#addMultipleItemsToCart(Long, Set)} throws an
     * appropriate exception.
     * <p>Items added with this method must have null IDs, as they are added by the repository.</p>
     */
    @Test
    @DisplayName("addMultipleItemsToCart arroja excepción si un ítem ingresado tiene Id")
    // No deben tener Id porque éste es asignado por el repositorio.
    public void test_when_addMultipleItemsToCart_and_notNullItemIdExists_then_throwException(){
        Set<Item> incorrectItemSet = new HashSet<>();
        incorrectItemSet.add(new Item(1L));

        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Uno o más elementos ingresados incluyen un ID de ítem que no corresponde.");

        service.addMultipleItemsToCart(1L, incorrectItemSet);
    }

    /**
     * Given an item set containing two elements, and given a spy copy of {@code CartService} executing
     * {@link CartService#addItemToCart(Long, Long, Integer)} returning valid carts, assert that
     * {@link CartService#addMultipleItemsToCart(Long, Set)} executes that method two times.
     */
    @Test
    @DisplayName("addMultipleItemsToCart ejecuta addItemToCart por cada item proveído")
    public void test_when_addMultipleItemsToCart_then_executesAddItemToCartForEachItemProvided(){
        Set<Item> items = new HashSet<>();
        items.add(new Item(null, 5, null, new Product(1L)));
        items.add(new Item(null, 10, null, new Product(2L)));

        Mockito.doReturn(new Cart()).when(serviceSpy).addItemToCart(anyLong(), anyLong(), anyInt());

        serviceSpy.addMultipleItemsToCart(1L, items);

        verify(serviceSpy, times(2)).addItemToCart(anyLong(), anyLong(), anyInt());
    }

    // deleteItemFromCart

    /**
     * Given a spy copy of {@code CartService} executing {@link CartService#findById(Long)} and
     * {@link CartRepository#save(Object)} returning valid carts, assert that {@link CartService#deleteItemFromCart(Long, Long)}
     * executes that method.
     */
    @Test
    @DisplayName("deleteItemFromCart llama para buscar")
    public void test_when_deleteItemFromCart_then_serviceSearchCallMade(){
        spyFindByIdReturnsCart();
        returnCartOnRepositorySave();

        serviceSpy.deleteItemFromCart(1L, 1L);

        verify(serviceSpy, times(1)).findById(1L);
    }

    /**
     * Given a spy copy of {@code CartService} executing {@link CartService#findById(Long)} that returns a valid cart,
     * assert that executing {@link CartService#deleteItemFromCart(Long, Long)} with an invalid item ID triggers an
     * appropriate exception.
     */
    @Test
    @DisplayName("deleteItemFromCart arroja excepción si no encuentra el ítem")
    public void test_when_deleteItemFromCart_then_exceptionThrown(){
        spyFindByIdReturnsCart();

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("No se encontró el ítem N°" + 2L);

        serviceSpy.deleteItemFromCart(1L, 2L);
    }

    /**
     * Given {@link CartService#findById(Long)} and {@link CartRepository#save(Object)} both returning valid carts,
     * assert that the cart returned by {@link CartService#deleteItemFromCart(Long, Long)} lacks a previously deleted
     * item.
     */
    @Test
    @DisplayName("deleteItemFromCart borra efectivamente un ítem")
    public void test_when_deleteItemFromCart_then_itemIsDeleted(){
        findByIdReturnsCart();
        returnCartOnRepositorySave();

        assertTrue(serviceSpy.deleteItemFromCart(1L, 1L).getItems().isEmpty());
    }

    // deleteCart

    /**
     * Given {@link CartRepository#findById(Object)} returning a valid cart, verify that executing
     * {@link CartService#deleteCart(Long)} executes that method.
     * <p>Note: {@link CartRepository#deleteById(Object)}, a {@code void} method, needs to be marked with the
     * {@code Mockito.doNothing()} flag.</p>
     */
    @Test
    @DisplayName("deleteCart llama para borrar")
    public void test_when_deleteCart_then_repositoryDeleteCallMade(){
        Mockito.doNothing().when(repository).deleteById(anyLong());
        findByIdReturnsCart();

        service.deleteCart(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    // deleteCartByUserId

    /**
     * Given {@link CartRepository#findById(Object)} returning a valid cart, assert that
     * {@link CartService#deleteCartByUserId(Integer)} executes that method, and also
     * {@link CartRepository#deleteById(Object)}.
     */
    @Test
    @DisplayName("deleteCartByUserId llama para buscar y para borrar")
    public void test_when_deleteCartByUserId_then_repositorySearchAndDeletionCallsMade(){
        Mockito.doReturn(Optional.of(CART_1)).when(repository).findByUserId(USERID_CART_1);

        service.deleteCartByUserId(USERID_CART_1);

        verify(repository, times(1)).findByUserId(anyInt());
        verify(repository, times(1)).deleteById(anyLong());
    }

    /**
     * Given {@link CartRepository#findByUserId(Integer)} returning an empty response, assert that
     * {@link CartService#deleteCartByUserId(Integer)} throws an appropriate exception.
     *
     */
    @Test
    @DisplayName("deleteCartByUserId arroja excepción si no encuentra el carrito")
    public void test_when_deleteCartByUserId_and_cartNotFound_then_throwException(){
        Mockito.doReturn(Optional.empty()).when(repository).findByUserId(USERID_CART_1);

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("No se encontró un carrito con ése ID de usuario.");

        service.deleteCartByUserId(USERID_CART_1);
    }

}
