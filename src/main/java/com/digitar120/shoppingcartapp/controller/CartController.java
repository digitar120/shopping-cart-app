package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.persistence.entity.Cart;
import com.digitar120.shoppingcartapp.persistence.entity.Item;
import com.digitar120.shoppingcartapp.service.CartService;
import com.digitar120.shoppingcartapp.service.dto.NewCartDTO;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Cart endpoint configuration.
 * @author Gabriel Pérez (digitar120)
 */
@RestController
@RefreshScope
@RequestMapping("/cart")
@SuppressWarnings("never used")
public class CartController {

    private final CartService service;
    public CartController(CartService service) {
        this.service = service;
    }

    // Read operations

    /**
     * List all entries.
     * @return A cart list of all entries in the database.
     */
    @Operation(summary = "Listar todos los carritos", description = "Devuelve todos los carritos almacenados.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Completado correctamente"),
            @ApiResponse(code = 404, message = "No se encontró el carrito"),
            // Debería incluir error 400 para todos los métodos? Es correcto también incluir una
            // descripción para un error 500?
            @ApiResponse(code = 500, message = "Algo salió mal.")
    })
    @GetMapping
    public List<Cart> findAll(){
        return this.service.findAll();
    }

    /**
     * Find an entry by ID.
     * @param id ID to match the cart with.
     * @return If the search is positive, a matching cart.
     */
    @Operation(summary = "Encontrar un carrito mediante ID.", description = "Devuelve un carrito junto con su lista de ítems y sus productos referenciados..")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Completado correctamente"),
            @ApiResponse(code = 404, message = "No se encontró el carrito"),
            @ApiResponse(code = 500, message = "Algo salió mal.")
    })
    @GetMapping("/{id}")
    public Cart findById(@PathVariable Long id){
        return service.findById(id);
    }

    /**
     * Find an entry matching its referenced user ID.
     * @param userId Referenced user ID to match the cart with.
     * @return A matching cart, if the search is positive.
     */
    @Operation(summary = "Encontrar mediante ID de usuario", description = "Devuelve un objeto carrito mediante ID de usuario.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Completado correctamente."),
            @ApiResponse(code=404, message = "No se encontró al usuario o al carrito.")
    })
    @GetMapping("/by-userid/{userId}")
    public Cart findByUserId(@PathVariable Integer userId){
        return service.findByUserId(userId);
    }

    /**
     * List the contents of a cart's item set, matching the cart by ID.
     * @param id ID to match the cart with.
     * @return A matching cart's item set.
     */
    @Operation(summary = "Contenidos de un carrito.", description = "Listar contenidos de un carrito, mediante ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Completado correctamente"),
            @ApiResponse(code = 404, message = "No se encontró el carrito"),
            @ApiResponse(code = 500, message = "Algo salió mal.")
    })
    @GetMapping("/{id}/items")
    public Set<Item> getCartItems (@PathVariable Long id){
        return service.getContent(id);
    }

    // Create opeartions

    /**
     * Create a new cart.
     * @param newCartDTO DTO containing information for a new cart, read in JSON format.
     * @return A copy of the new cart object.
     */
    @PostMapping
    @Operation(summary = "Crear un carrito.", description = "Crear un nuevo carrito, introduciendo una descripción.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Completado correctamente"),
            @ApiResponse(code = 400, message = "Solicitud errónea")
    })
    public Cart newCart(@RequestBody NewCartDTO newCartDTO){
        return service.newCart(newCartDTO);
    }

    // Update operations

    /**
     * Add an item to a cart's item set.
     * @param cart_id ID of the cart to affect.
     * @param product_id ID of the product to add to the set.
     * @param quantity Quantity of the product to add to the set.
     * @return An updated cart entry.
     */
    @Operation(summary = "Agregar elemento", description = "Agregar un único elemento al carrito, utilizando una URL.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Elemento agregado"),
            @ApiResponse(code = 400, message = "La cantidad de producto ingresada es inválida."),
            @ApiResponse(code = 404, message = "No se encontró el producto referenciado."),
            @ApiResponse(code = 500, message = "Algo salió mal")
    })
    @PostMapping("/{cart_id}/product/{product_id}/quantity/{quantity}")
    public Cart addItemToCart(@PathVariable Long cart_id, @PathVariable Long product_id, @PathVariable Integer quantity)    {
        return service.addItemToCart(cart_id, product_id, quantity);
    }

    /**
     * Add several items to a cart's item set.
     * @param cartId ID of the cart to affect.
     * @param itemSet Item set to add to the cart. Read from JSON.
     */
    @Operation(summary = "Agregar varios elementos", description = "Agregar varios elementos, mediante JSON.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Elementos agregados"),
            @ApiResponse(code = 404, message = "No se encontró el producto referenciado en uno de los ítems."),
            @ApiResponse(code = 400, message = "La cantidad ingresada en uno de los ítems es inválida"),
            @ApiResponse(code = 500, message = "Algo salió mal")
    })
    @PostMapping("/{cartId}")
    public void addMultipleItemsToCart(@PathVariable Long cartId, @RequestBody Set<Item> itemSet){
        service.addMultipleItemsToCart(cartId, itemSet);
    }

    // Delete operations

    /**
     * Delete an item from a cart.
     * @param cartId ID of the cart to affect.
     * @param itemId ID of the item to delete.
     * @return A copy of the updated cart entry.
     */
    @Operation(summary = "Eliminar elemento", description = "Eliminar un elemento de un carrito, mediante ID del carrito e ID del elemento.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ítem eliminado"),
            @ApiResponse(code = 404, message = "No se encontró el carrito o el elemento."),
            @ApiResponse(code = 500, message = "Algo salió mal")
    })
    @DeleteMapping("/{cartId}/item/{itemId}")
    public Cart deleteItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId){
        return service.deleteItemFromCart(cartId, itemId);
    }

    /**
     * Delete a cart.
     * @param cartId ID of the cart to delete.
     */
    @Operation(summary = "Eliminar carrito", description = "Eliminar un carrito, mediante ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Carrito eliminado"),
            @ApiResponse(code = 404, message = "No se encontró el carrito."),
            @ApiResponse(code = 500, message = "Algo salió mal")
    })
    @DeleteMapping("/{cartId}")
    public void deleteCart(@PathVariable Long cartId){
        service.deleteCart(cartId);
    }
}
