

package com.digitar120.shoppingcartapp.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This class holds information about a shopping cart and its linked Items. It is also bound to a User.
 * <p>There are a couple elements that were needed to make queries work, since the relation with Item is bidirectional.
 * There was a problem when querying for the full object of a Cart. At first, the query would simply fail, and when
 * tested without safeties, it would print an infinite loop between the two objects.</p>
 * <p>The JSON tools and relation rules used below solve this issue, and allow queries to behave normally.</p>
 * @author Gabriel Pérez (digitar120)
 * @see Item
 * @see Product
 * @see com.digitar120.shoppingcartapp.context.JsonConfiguration
 */

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ID")
    private Long id;

    @Column(name = "CART_DESCRIPTION")
    private String description;

    /**
     * I chose to use a HashSet so unique listings would be easier to manage. My idea is that a shopping cart should not
     * have multiple instances of one Product.
     * <p>orphanRemoval is included to propagate an Item deletion from the Cart side to the Item database.</p>
     * @see Item
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "owningCart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

    @Column(name="USER_ID")
    private Integer userId;

    public Cart(Long id){
        this.id = id;
    }
}
