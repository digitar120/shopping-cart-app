package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.globalhandler.BadRequestException;
import com.digitar120.shoppingcartapp.exception.globalhandler.NotFoundException;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.ProductRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    private static final String PENCIL_AMBIGUOUS = "Pencil";
    private static final String PENCIL_2B = "2B Pencil";
    private static final String PENCIL_2H = "2H Pencil";

    private static final String PEN = "Pen";

    private static final Long CODE_PENCIL_2B = 1L;
    private static final Long CODE_PENCIL_2H = 2L;
    private static final Long CODE_PEN = 3L;

    private static final Product PRODUCT_PENCIL_2B = new Product(CODE_PENCIL_2B, PENCIL_2B);
    private static final Product PRODUCT_PENCIL_2H = new Product(CODE_PENCIL_2H, PENCIL_2H);
    private static final Product PRODUCT_PEN = new Product(CODE_PEN, PEN);

    private static final List<Product> sampleProductList = Arrays.asList(PRODUCT_PENCIL_2B, PRODUCT_PEN);

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository repository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    // listAllProducts
    @Test
    @DisplayName("Test when listAllProducts() then a call is made")
    public void test_when_listAllProducts_then_callMade(){
        productService.listAllProducts();

        verify(repository,  times(1)).findAll();
    }

    @Test
    @DisplayName("listAllProducts produces a non-empty response")
    public void test_when_listAllProducts_then_responseNotEmpty(){
        when(repository.findAll()).thenReturn(sampleProductList);

        assertFalse(productService.listAllProducts().isEmpty());
    }

    @Test
    @DisplayName("listAllProducts delivers the same output as the repository")
    public void test_when_listAllProducts_then_responseAsGivenByRepository(){
        when(repository.findAll()).thenReturn(sampleProductList);

        assertEquals(sampleProductList, productService.listAllProducts());
    }


    // findByDescritpion
    @Test
    @DisplayName("findByDescription produces a repository call with given argument")
    public void test_when_findByDescription_then_callIsMadeWithGivenArgument(){
        productService.findByDescription(PENCIL_2B);

        verify(repository, times(1)).findByDescription(PENCIL_2B);
    }

    @Test
    @DisplayName("findByDescription produces correct list when argument is ambiguous")
    public void test_when_findByDescriptionWithAmbiguousArguments_then_listContainsAMatch(){
        when(repository.findByDescription(PENCIL_AMBIGUOUS))
                .thenReturn(sampleProductList);

        assertTrue(
                productService.findByDescription(PENCIL_AMBIGUOUS)
                        .contains(PRODUCT_PENCIL_2B));
    }

    @Test
    @DisplayName("findByDescription produces correct list when argument is exact")
    public void test_when_findByDescriptionWithExactArguments_then_listContainsExactMatch(){
        when(repository.findByDescription(PENCIL_2B))
                .thenReturn(Arrays.asList(PRODUCT_PENCIL_2B));

        assertTrue(
                productService.findByDescription(PENCIL_2B)
                        .contains(PRODUCT_PENCIL_2B)
        );
    }

    @Test
    @DisplayName("findById produces repository call with given argument")
    public void test_when_findById_then_repositoryCalledWithGivenArgument(){
        when(repository.findById(CODE_PENCIL_2B)).thenReturn(Optional.of(PRODUCT_PENCIL_2B));

        productService.findById(CODE_PENCIL_2B);

        verify(repository, times(1)).findById(CODE_PENCIL_2B);
    }

    @Test
    @DisplayName("findById produces NotFoundException when argument doesn't match anything")
    public void test_when_findByIdWithMismatchingArgument_then_NotFoundException(){
        when(repository.findById(0L)).thenReturn(Optional.empty());

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("No se encontró el producto de ID " + 0L);

        productService.findById(0L);
    }

    @Test
    @DisplayName("findById produces correct match with given Id")
    public void test_when_findById_then_accurateMatch(){
        when(repository.findById(CODE_PENCIL_2B)).thenReturn(Optional.of(PRODUCT_PENCIL_2B));

        assertTrue(productService.findById(CODE_PENCIL_2B).equals(PRODUCT_PENCIL_2B));
    }




    // newProduct
    @Test
    @DisplayName("newProduct makes verification and saving repository calls")
    public void test_when_newProduct_then_repositoryVerificationAndSavingCallsMade(){
        Product newProduct = new Product(PENCIL_2B);
        when(repository.findByDescription(PENCIL_2B)).thenReturn(List.of());
        when(repository.save(newProduct)).thenReturn(newProduct);

        productService.newProduct(PENCIL_2B);

        verify(repository, times(1)).findByDescription(PENCIL_2B);
        verify(repository, times(1)).save(any(Product.class));
    }


    @Test
    @DisplayName("newProduct returns correct object")
    public void test_when_newProduct_then_returnsCorrectObject(){
        when(repository.findByDescription(PENCIL_2B)).thenReturn(List.of());
        when(repository.save(any(Product.class))).thenReturn(PRODUCT_PENCIL_2B);

        assertEquals(PRODUCT_PENCIL_2B, productService.newProduct(PENCIL_2B));
    }

    // If product already exists, expect a BadRequestException
    @Test
    @DisplayName("newProduct produces a BadRequestException, with given message, if the product already exists")
    public void test_when_newProductWithExistingDescription_then_throwsBadRequestExceptionWithGivenMessage(){
        when(repository.findByDescription(PENCIL_2B)).thenReturn(List.of(PRODUCT_PENCIL_2B));

        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Ya existe un producto \"" + PENCIL_2B + "\".");

        productService.newProduct(PENCIL_2B);

    }

    // deleteProduct

    @Test
    @DisplayName("deleteProduct verifies if the product already exists")
    public void test_when_deleteProduct_then_verifiesIfProductAlreadyExists(){
        when(repository.findById(CODE_PENCIL_2B)).thenReturn(Optional.of(PRODUCT_PENCIL_2B));

        productService.deleteProduct(CODE_PENCIL_2B);

        verify(repository, times(1)).findById(CODE_PENCIL_2B);
    }

    @Test
    @DisplayName("deleteProduct produces repository call")
    public void test_when_deleteProduct_then_repositoryCallMade(){
        when(repository.findById(CODE_PENCIL_2B)).thenReturn(Optional.of(PRODUCT_PENCIL_2B));

        productService.deleteProduct(CODE_PENCIL_2B);
        verify(repository, times(1)).deleteById(1L);
    }




}
