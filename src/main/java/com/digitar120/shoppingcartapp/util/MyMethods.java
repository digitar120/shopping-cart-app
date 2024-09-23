package com.digitar120.shoppingcartapp.util;

import com.digitar120.shoppingcartapp.exception.globalhandler.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.Optional;


public final class MyMethods {
    public static final String NO_ENCONTRADO = "No encontrado";


    // https://stackoverflow.com/questions/25223553/how-can-i-create-an-utility-class
    // El nombre de la clase de utilidad debería ser genérico, para métodos en general, o específico, para un grupo
    // específico de utilidades.

    // Cualquier clase anidada debe ser de utilidad también.


    // Las clases de utilidad deben ser públicas y finales. Adicionalmente, el constructor debe ser privado.
    private MyMethods(){}

    // Todos los métodos deben ser static y no abstract.
    // Cualquier método utilizado sólo por la clase de utilidad debe ser privado.

    // Si la clase de utilidad tiene atributos, deben ser static y final.

    // Convención para letras para tipos genéricos: T, S, U, V, etc.


    //-----------------------------------------------------------------------------------------------------------


    // Verificar que un elemento de una tabla existe

    // Al consultar una tabla por un registro mediante ID debe confirmarse si se adiquirió o no un elemento.
    // El siguiente método es una refactorización de un mismo algoritmo que aparece en todas las clases de servicio
    // de la app.

    // En principio, el repositorio y el tipo de dato del ID se dan como genéricos. T es el tipo que se corresponde con
    // la clase (la tabla en la base de datos), y U con el tipo de dato del ID, que puede ser Integer, Long, etc.

    // Entonces se puede usar cualquier interfase de repositorio JpaRepository, indexado mediante cualquier tipo de
    // dato.
    public static <T,S extends JpaRepository<T,U>, U> void verifyElementExists(S repository, U id, String exceptionMessage, HttpStatus httpStatusCode){

        // El método findById(N) devuelve un Optional del tipo T. Por ejemplo, productRepository.findById(productId) va
        // a devolver un elemento de tipo Optional<Product>.
        Optional<T> optionalElement = repository.findById(id);

        // Si se encuentra un elemento, el objeto optionalElement va a efectivamente contener un objeto Product,
        // fallando la siguiente condición, en otras palabras declarando que efectivamente se encontró un
        // elemento único.
        if(optionalElement.isEmpty() && httpStatusCode.equals(HttpStatus.NOT_FOUND)) {

            // En caso de no ser así, se debe arrojar una excepción correspondiente. Por ejemplo: cuando no se encuentra
            // un elemento, se puede devolver "No se encontró el elemento N° 1", junto con 404 (HttpStatus.NOT_FOUND).
            // Si se quiere agregar un elemento y este ya existe, se puede devolver "El elemento ya existe", y 400
            // (bad request)
            throw new NotFoundException(exceptionMessage);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------


    // Verificar que un elemento existe, y devolver ése elemento.

    // En CartService -> deleteItemFromCart, se tiene que verificar que el elemento existe, pero a diferencia de en
    // una eliminación o una edición, se tiene que usar el elemento.

    public static <T,S extends JpaRepository<T,U>, U> T verifyElementExistsAndReturn(S repository, U id, String exceptionMessage, HttpStatus httpStatusCode){
        Optional<T> optionalElement = repository.findById(id);

        if(optionalElement.isEmpty() && httpStatusCode.equals(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException(exceptionMessage);
        }

        return optionalElement.get();
    }

    // ---------------------------------------------------------------------------------------------------------------

    // Verificar que un elemento NO existe.
    public static <T,S extends JpaRepository<T,U>, U> void verifyElementNotExists(S repository, U id, String exceptionMessage, HttpStatus httpStatusCode){
        Optional<T> optionalElement = repository.findById(id);

        if(optionalElement.isPresent() && httpStatusCode.equals(HttpStatus.BAD_REQUEST)) {
            throw new NotFoundException(exceptionMessage);
        }
    }
}
