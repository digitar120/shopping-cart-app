package com.digitar120.shoppingcartapp.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;

    @Column(name = "ITEM_QUANTITY")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "CART_ID", nullable = false)
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product referencedProduct;

    public Item(Integer quantity, Cart cart, Product referencedProduct) {
        this.quantity = quantity;
        this.cart = cart;
        this.referencedProduct = referencedProduct;
    }
}
