package com.digitar120.shoppingcartapp.persistence.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;
    @Column(name="PRODUCT_DESCRIPTION")
    private String description;
}
