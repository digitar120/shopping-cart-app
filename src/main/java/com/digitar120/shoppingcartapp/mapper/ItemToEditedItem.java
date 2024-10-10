package com.digitar120.shoppingcartapp.mapper;

import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.dto.EditedItemDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ItemToEditedItem implements IMapper<EditedItemDTO, Item>{

    @Override
    public Item map(EditedItemDTO in){
        // Afecta solo cantidad del ítem. El ID se maneja en la capa de servicio.
        Item item = new Item();
        item.setQuantity(in.getQuantity());

        return item;
    }
}
