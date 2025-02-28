package com.digitar120.shoppingcartapp.mapper;

import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A mapper for creating new Cart objects.
 * @author Gabriel Pérez (digitar120)
 * @see IMapper
 * @see Cart
 * @see NewCartDTO
 * @see com.digitar120.shoppingcartapp.service.CartService
 */
@Component
public class CartDTOtoCart implements IMapper<NewCartDTO, Cart>{

    @Override
    public Cart map(NewCartDTO in){
        Cart cart = new Cart();
        cart.setDescription(in.getDescription());
        cart.setUserId(in.getUserId());
        return cart;
    }
}
