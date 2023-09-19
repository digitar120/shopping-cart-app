package com.digitar120.shoppingcartapp.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
@Setter
@Getter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor // org.hibernate.InstantiationException: No default constructor for entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;
    @Column(name="PRODUCT_DESCRIPTION")
    private String description;

    public Product(String description) {
        this.description = description;
    }
}
