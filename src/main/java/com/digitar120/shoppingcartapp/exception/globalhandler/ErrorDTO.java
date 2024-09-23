package com.digitar120.shoppingcartapp.exception.globalhandler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDTO {
    public String status;
    public String message;
    public String time;
}
