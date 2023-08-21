package com.digitar120.shoppingcartapp.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;

    @Column(name="ITEM_DESCRIPTION")
    private String description;

    @Column(name = "ITEM_QUANTITY")
    private Integer quantity;
}
