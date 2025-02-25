package com.digitar120.shoppingcartapp.service.dto;

import lombok.*;

/**
 * A simple DTO to create new Carts.
 * @author Gabriel Pérez (digitar120)
 * @see com.digitar120.shoppingcartapp.persistence.entity.Cart
 * @see com.digitar120.shoppingcartapp.service.CartService#newCart(NewCartDTO)
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class NewCartDTO {
    private String description;

    /**
     * Referenced User, listed in the Users Service.
     */
    private Integer userId;
}
