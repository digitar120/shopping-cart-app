package com.digitar120.shoppingcartapp.util;

import com.digitar120.shoppingcartapp.exception.globalhandler.BadRequestException;
import com.digitar120.shoppingcartapp.exception.globalhandler.NotFoundException;
import com.digitar120.shoppingcartapp.persistence.entity.Product;
import com.digitar120.shoppingcartapp.persistence.repository.ProductRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class LocalUtilityMethodsTest {

    public static final Optional<Product> NO_RESULT = Optional.empty();
    public static final Optional<Product> MATCH_FOUND = Optional.of(new Product());
    @InjectMocks
    private LocalUtilityMethodsTest myMethods;

    @Mock
    ProductRepository repository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup(){}

    @Test
    @DisplayName("verifiyElementExists llama para buscar")
    public void test_when_verifyElementExists_then_repositorySearchCallMade(){
        when(repository.findById(1L)).thenReturn(MATCH_FOUND);

        LocalUtilityMethods.verifyElementExists(repository, 1L, "Mensaje", HttpStatus.NOT_FOUND);

        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("verifyElementExists arroja excepción")
    public void test_when_verifyElementExists_and_elementNotFound_then_throwException(){
        when(repository.findById(1L)).thenReturn(NO_RESULT);

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("Mensaje");

        LocalUtilityMethods.verifyElementExists(repository, 1L, "Mensaje", HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("verifyElementExistsAndReturn llama para buscar")
    public void test_when_verifyElementExistsAndReturn_then_repositorySearchCallMade(){
        when(repository.findById(1L)).thenReturn(MATCH_FOUND);

        LocalUtilityMethods.verifyElementExistsAndReturn(repository, 1L, "Mensaje", HttpStatus.NOT_FOUND);

        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("verifyElementExistsAndReturn arroja excepción")
    public void test_when_verifyElementExistsAndReturn_and_elementNotFound_then_throwException(){
        when(repository.findById(1L)).thenReturn(NO_RESULT);

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("Mensaje");

        LocalUtilityMethods.verifyElementExistsAndReturn(repository, 1L, "Mensaje", HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("verifyElementExistsAndReturn devuelve el elemento correcto")
    public void test_when_verifyElementExistsAndReturn_then_returnsCorrectObject(){
        when(repository.findById(1L)).thenReturn(MATCH_FOUND);

        assertEquals(
                MATCH_FOUND.get(),
                LocalUtilityMethods.verifyElementExistsAndReturn(repository, 1L, "Mensaje", HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("verifyElementNotExists llama para buscar")
    public void test_when_verifiyElementNotExists_then_repositorySearchCallMade(){
        when(repository.findById(1L)).thenReturn(NO_RESULT);

        LocalUtilityMethods.verifyElementNotExists(repository, 1L, "Mensaje", HttpStatus.BAD_REQUEST);

        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("verifyElementNotExists arroja excepción")
    public void test_when_verifyElementNotExists_and_elementFound_then_throwsException(){
        when(repository.findById(1L)).thenReturn(MATCH_FOUND);

        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Mensaje");

        LocalUtilityMethods.verifyElementNotExists(repository, 1L, "Mensaje", HttpStatus.BAD_REQUEST);
    }
}
