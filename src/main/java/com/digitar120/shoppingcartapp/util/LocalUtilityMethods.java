package com.digitar120.shoppingcartapp.util;

import com.digitar120.shoppingcartapp.exception.globalhandler.BadRequestException;
import com.digitar120.shoppingcartapp.exception.globalhandler.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * This class contains utility methods for the API, mainly abstractions of JPA repository searches.
 * <p>As an utility class, this follows some <a href="https://stackoverflow.com/questions/25223553/how-can-i-create-an-utility-class">on-topic guidelines</a>.</p>
 * @author Gabriel Pérez (digitar120)
 */
public final class LocalUtilityMethods {

    //

    private LocalUtilityMethods(){}


    /**
     * Verifies that an element exists within a referenced {@link JpaRepository}.
     * <p>This method and its variations in this class are means to abstract repeating verifications on repositories
     * when there's a need to check if an element exists or not, across multiple object and indexing types.</p>
     * <p>This method is meant to be executed in a standalone manner. It throws an exception and it stops the execution
     * of a method if an element <i>doesn't</i>
     * exist.</p>
     * @param repository The actual repository object to execute the search in.
     * @param id The ID to search.
     * @param exceptionMessage The exception message to display in case the result is <b>negative</b>.
     * @param httpStatusCode The {@code HttpStatus} code to return in case the result is <b>negative</b>.
     * @param T The type of object that the repository holds and returns (Cart, Product, etc.).
     * @param S The repository's interface (CartRepository, ProductRepository, etc.). It must extend {@code JpaRepository}.
     * @param U ID type of the repository (Integer, Long, etc.).
     */
    public static <T,S extends JpaRepository<T,U>, U> void verifyElementExists(S repository, U id, String exceptionMessage, HttpStatus httpStatusCode){

        Optional<T> optionalElement = repository.findById(id);

        if(optionalElement.isEmpty() && httpStatusCode.equals(HttpStatus.NOT_FOUND)) {

            throw new NotFoundException(exceptionMessage);
        }
    }

    // Verificar que un elemento existe, y devolverlo.

    /**
     * Verifies that an element exists <b>and returns it</b>.
     * <p>This is a simple variation of {@link LocalUtilityMethods#verifyElementExists(JpaRepository, Object, String, HttpStatus)}.
     * It's purpose is to retrieve an object, on top of verifying if it exists in the database.</p>
     * @param repository The actual repository object to execute the search in.
     * @param id The ID to search.
     * @param exceptionMessage The exception message to display in case the result is <b>negative</b>.
     * @param httpStatusCode The {@code HttpStatus} code to return in case the result is <b>negative</b>.
     * @return A matching object, if found.
     * @param T The type of object that the repository holds and returns (Cart, Product, etc.).
     * @param S The repository's interface (CartRepository, ProductRepository, etc.). It must extend {@code JpaRepository}.
     * @param U ID type of the repository (Integer, Long, etc.).
     */
    public static <T,S extends JpaRepository<T,U>, U> T verifyElementExistsAndReturn(S repository, U id, String exceptionMessage, HttpStatus httpStatusCode){
        Optional<T> optionalElement = repository.findById(id);

        if(optionalElement.isEmpty() && httpStatusCode.equals(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException(exceptionMessage);
        }

        return optionalElement.get();
    }

    // Verificar que un elemento NO existe.

    /**
     * Another variation of {@link LocalUtilityMethods#verifyElementExists(JpaRepository, Object, String, HttpStatus)},
     * but it fails if a search is <b>positive</b>.
     * <p>This method is meant to be used when there's a need to verify if an element exists before creating entries in
     * a repository.</p>
     * @param repository The actual repository object to execute the search in.
     * @param id The ID to search.
     * @param exceptionMessage The exception message to display in case the result is <b>positive</b>.
     * @param httpStatusCode The {@code HttpStatus} code to return in case the result is <b>positive</b>.
     * @param T The type of object that the repository holds and returns (Cart, Product, etc.).
     * @param S The repository's interface (CartRepository, ProductRepository, etc.). It must extend {@code JpaRepository}.
     * @param U ID type of the repository (Integer, Long, etc.).
     */
    public static <T,S extends JpaRepository<T,U>, U> void verifyElementNotExists(S repository, U id, String exceptionMessage, HttpStatus httpStatusCode){
        Optional<T> optionalElement = repository.findById(id);

        if(optionalElement.isPresent() && httpStatusCode.equals(HttpStatus.BAD_REQUEST)) {
            throw new BadRequestException(exceptionMessage);
        }
    }

    /**
     * Returns a random line. Thought out as a quick and funny way to test if a part of the program gets to a certain
     * point by printing to stdout.
     * <p>The quotes are from the videogames Team Fortress 2 and Portal 2, and from the metal band Wind Rose.</p>
     * @return A funny quote.
     */
    public static String quickTest() {
        List<String> lines = Arrays.asList(
                "I AM A DWARF AND I'M DIGGING A HOLE, DIGGY DIGGY HOLE, DIGGY DIGGY HOOOOLE",
                "SCOTLAND IS NOT A REAL COUNTRY. YOU'RE AN ENGLISHMAN IN A DRESS",
                "THIS. IS A BUCKET. -- DEAR GOD... -- THERE'S MORE. --- NO...",
                "I DON'T WANT YOUR DAMN LEMONS, WHAT AM I SUPPOSED TO DO WITH THESE?",
                "I'VE DONE NOTHING BUT TELEPORT BREAD FOR THREE DAYS"
        );

        Random random = new Random();

        Integer randomPos = random.nextInt(lines.size());

        return lines.get(randomPos);
    }
}
