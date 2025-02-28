package com.digitar120.shoppingcartapp.exception.globalhandler;

import lombok.Getter;
import lombok.Setter;

/**
 * Exception building DTO for the {@link GlobalExceptionHandler} class.
 * @author Gabriel Pérez (digitar120)
 */
@Getter
@Setter
public class ErrorDTO {
    public String status;
    public String message;
    public String time;
}
