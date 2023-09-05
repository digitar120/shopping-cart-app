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

    @Column(name="ITEM_DESCRIPTION")
    private String description;

    @Column(name = "ITEM_QUANTITY")
    private Integer quantity;

    // Constructor con ID automático
    public Item (String description, Integer quantity){
        this.description = description;
        this.quantity = quantity;
    }

    @ManyToOne
    @JoinColumn(name = "CART_ID", nullable = false)
    private Cart cart;
}
