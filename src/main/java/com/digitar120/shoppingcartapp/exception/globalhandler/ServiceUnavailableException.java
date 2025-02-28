package com.digitar120.shoppingcartapp.exception.globalhandler;

import org.springframework.http.HttpStatus;

/**
 * HTTP 503 exception subclass.
 * @@author Gabriel Pérez (digitar120)
 * @see CustomHttpStatusException
 */
public class ServiceUnavailableException extends CustomHttpStatusException{
    public ServiceUnavailableException(String message){
        super(message);
    }

    public HttpStatus getStatus(){
        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}
