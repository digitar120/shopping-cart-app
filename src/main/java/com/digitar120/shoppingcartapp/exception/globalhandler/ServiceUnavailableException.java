package com.digitar120.shoppingcartapp.exception.globalhandler;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends CustomHttpStatusException{
    public ServiceUnavailableException(String message){
        super(message);
    }

    public HttpStatus getStatus(){
        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}
