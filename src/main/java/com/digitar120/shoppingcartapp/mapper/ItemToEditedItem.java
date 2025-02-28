package com.digitar120.shoppingcartapp.mapper;

import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * A mapper for editing an item's quantity.
 * @author Gabriel Pérez (digitar120)
 * @see Item
 * @see com.digitar120.shoppingcartapp.service.ItemService
 * @see IMapper
 */
@Component
public class ItemToEditedItem implements IMapper<EditedItemDTO, Item>{

    /**
     * Affects only the item quantity. The ID is managed in the service layer.
     * @param in DTO containing new information.
     * @return A newly created Item without ID.
     */
    @Override
    public Item map(EditedItemDTO in){
        Item item = new Item();
        item.setQuantity(in.getQuantity());

        return item;
    }
}
