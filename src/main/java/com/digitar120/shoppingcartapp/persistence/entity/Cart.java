package com.digitar120.shoppingcartapp.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ID")
    private Long id;

    @Column(name = "CART_DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "cart")
    private Set<Item> items;
}
