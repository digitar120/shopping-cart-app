package com.digitar120.shoppingcartapp.mapper;

/**
 * A simple mapping interface that restricts the input and output of a mapper.
 * @author Sacavix Tech
 * @author Gabriel Pérez (digitar120)
 * @param <I> Input object type.
 * @param <O> Output object type.
 */
public interface IMapper <I, O>{
    public O map(I in);
}
