package com.digitar120.shoppingcartapp.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

/**
 * This class holds a reference to a Product, an owning Cart object and a quantity value. A referenced Product is intended to be
 * unique.
 * <p>As cited in {@link Cart}, the JSON tools and relation rules provide a correct behavior for Cart queries on Items.</p>
 * @author Gabriel Pérez (digitar120)
 * @see Cart
 * @see Item
 * @see Product
 */

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

    /**
     * This field should be unique amongst the Items in the set of a Cart.
     * @see Cart#getItems()
     */
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product referencedProduct;

    /**
     * This constructor is used for saving new Items from the Cart and Item endpoints.
     * @param quantity
     * @param owningCart
     * @param referencedProduct
     */
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
