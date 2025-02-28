package com.digitar120.shoppingcartapp.exception.globalhandler;

import org.springframework.http.HttpStatus;

/**
 * HTTP 404 exception subclass.
 * @author Gabriel Pérez (digitar120)
 * @see CustomHttpStatusException
 */
public class NotFoundException extends CustomHttpStatusException{
    public NotFoundException(String message){
        super(message);
    }

    public HttpStatus getStatus(){
        return HttpStatus.NOT_FOUND;
    }
}
