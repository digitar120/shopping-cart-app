package com.digitar120.shoppingcartapp.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ItemException extends RuntimeException{

    private String message;
    private HttpStatus httpStatus;

    public ItemException(String message, HttpStatus httpStatus){
        super (message);

        this.message =message;
        this.httpStatus = httpStatus;
    }
}
