package com.digitar120.shoppingcartapp.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
@Setter
@Getter
@ToString
@NoArgsConstructor // org.hibernate.InstantiationException: No default constructor for entity
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

    public Product(Long id){ this.id = id;}
        // Para poder agregar un ítem a un carrito simplemente mediante el ID, y no con un objeto entero
}
