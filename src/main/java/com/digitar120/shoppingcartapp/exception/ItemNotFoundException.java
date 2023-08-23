package com.digitar120.shoppingcartapp.exception;

public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException (Long id){
        super ("No se encontró el registro de ID N° " + id);
    }

    public ItemNotFoundException(String description){
        super ("No se encontró el registro con descripción \"" + description + "\"");
    }

    public ItemNotFoundException(){
        super ("No hay ítems registrados");
    }
}
