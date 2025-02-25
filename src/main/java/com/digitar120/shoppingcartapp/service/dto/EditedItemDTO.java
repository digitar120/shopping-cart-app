package com.digitar120.shoppingcartapp.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A simple DTO to update an Item's quantity.
 * @author Gabriel Pérez (digitar120)
 * @see com.digitar120.shoppingcartapp.persistence.entity.Item
 * @see com.digitar120.shoppingcartapp.service.ItemService#editItem(EditedItemDTO, Long)
 */
@Data
@AllArgsConstructor
public class EditedItemDTO {
    private Integer quantity;
}
