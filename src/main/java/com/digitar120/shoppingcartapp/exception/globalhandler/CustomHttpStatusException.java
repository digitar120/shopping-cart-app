package com.digitar120.shoppingcartapp.exception.globalhandler;

import org.springframework.http.HttpStatus;

/**
 * Superclass for HTTP error exceptions.
 * @author Gabriel Pérez (digitar120)
 */
public class CustomHttpStatusException extends RuntimeException{

    public CustomHttpStatusException(String message){ super(message);}

    /**
     * This method is meant to be overriden.
     * @return HTTP 204, No Content.
     */
    public HttpStatus getStatus(){
        return HttpStatus.NO_CONTENT;
    }
}
