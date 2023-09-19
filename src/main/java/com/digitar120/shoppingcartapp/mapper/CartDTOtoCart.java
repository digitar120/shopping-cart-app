package com.digitar120.shoppingcartapp.mapper;

import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class CartDTOtoCart implements IMapper<NewCartDTO, Cart>{

    @Override
    public Cart map(NewCartDTO in){
        Cart cart = new Cart();
        cart.setDescription(in.getDescription());
        return cart;
    }
}
