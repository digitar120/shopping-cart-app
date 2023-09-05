package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.exception.MyException;
import com.digitar120.shoppingcartapp.mapper.CartDTOtoCart;
import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.persistence.repository.CartRepository;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService {

    private final CartRepository repository;
    private final CartDTOtoCart mapper;
    public CartService(CartRepository repository, CartDTOtoCart mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // Listar carritos
    public List<Cart> findAll() {
        return repository.findAll();
    }

    // Encontrar carrito mediante ID
    public Cart findById(Long id){
        return repository.findById(id).orElseThrow( () -> new MyException("No se encontró el carrito con ID "+ id, HttpStatus.NOT_FOUND));
    }

    // Listar contenidos de un carrito
    public Set<Item> getContent(Long id){
        Optional<Cart> optionalCart = repository.findById(id);
        if (optionalCart.isEmpty()){
            throw new MyException("No se encontró el carrito con ID "+ id, HttpStatus.NOT_FOUND);
        } else {
            return repository.findById(id).get().getItems();
        }
    }

    // Crear nuevo carrito
    public Cart newCart(NewCartDTO cartDTO){
        Cart newCart = mapper.map(cartDTO);

        return repository.save(newCart);
    }

    // Eliminar un carrito


    // Agregar elementos a un carrito
    // Eliminar elementos de un carrito
}
