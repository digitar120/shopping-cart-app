package com.digitar120.shoppingcartapp.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;

    @Column(name = "ITEM_QUANTITY")
    private Integer quantity;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CART_ID", nullable = false)
    private Cart owningCart;

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product referencedProduct;

    // Guardado de ítems nuevos desde /item y /cart
    public Item(Integer quantity, Cart owningCart, Product referencedProduct) {
        this.quantity = quantity;
        this.owningCart = owningCart;
        this.referencedProduct = referencedProduct;
    }

    public Item(Long id, Cart owningCart) {
        this.id = id;
        this.owningCart = owningCart;
    }

    public Item(Long id) {
        this.id = id;
    }
}
