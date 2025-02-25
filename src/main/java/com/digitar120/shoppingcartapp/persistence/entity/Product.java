package com.digitar120.shoppingcartapp.persistence.entity;

import lombok.*;

import javax.persistence.*;

/**
 * This is a simple class to hold information about a Product's name. It is used as a reference on {@link Item}s.
 * <p>{@code @AllArgsConstructor} is added for testing purposes.</p>
 * @author Gabriel Pérez (digitar120)
 */
@Entity
@Table(name = "PRODUCT")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor // Testing
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

    /**
     * This constructor is used to easily create an Item in a Cart, by simply referencing a Product's ID and not with a
     * manually created object.
     * @param id
     */
    public Product(Long id){ this.id = id;}
}
