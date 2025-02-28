package com.digitar120.shoppingcartapp.exception.globalhandler;

import org.springframework.http.HttpStatus;

/**
 * HTTP 400 exception subclass.
 * @author Gabriel Pérez (digitar120)
 */
public class BadRequestException extends CustomHttpStatusException{
    public BadRequestException(String message){
        super(message);
    }

    public HttpStatus getStatus(){
        return HttpStatus.BAD_REQUEST;
    }
}
