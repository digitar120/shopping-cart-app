package com.digitar120.shoppingcartapp.exception.globalhandler;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomHttpStatusException{
    public NotFoundException(String message){
        super(message);
    }

    public HttpStatus getStatus(){
        return HttpStatus.NOT_FOUND;
    }
}
