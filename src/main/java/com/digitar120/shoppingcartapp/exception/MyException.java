package com.digitar120.shoppingcartapp.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Legacy generic exception class.
 * @author Gabriel Pérez (digitar120)
 */
@Data
public class MyException extends RuntimeException{

    private String message;
    private HttpStatus httpStatus;

    public MyException(String message, HttpStatus httpStatus){
        super (message);

        this.message =message;
        this.httpStatus = httpStatus;
    }
}
