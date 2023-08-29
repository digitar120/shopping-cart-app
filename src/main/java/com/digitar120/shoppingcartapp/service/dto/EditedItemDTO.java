package com.digitar120.shoppingcartapp.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditedItemDTO {
    private String description;
    private Integer quantity;
}
