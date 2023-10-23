package com.digitar120.shoppingcartapp.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "owningCart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
        // orphanremoval: al eliminar un ítem desde el controlador de carritos, se devuelve un resultado correcto pero
        // no se propaga el resultado a la base de datos.
    private Set<Item> items = new HashSet<>();

    @Column(name="USER_ID")
    private Integer userId;

    public Cart(Long id){
        this.id = id;
    }
}
