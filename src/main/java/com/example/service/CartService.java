package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // 7.4.2.1 Add Cart
    public Cart addCart(Cart cart) {
        Cart existingCart = cartRepository.getCartByUserId(cart.getUserId());
        if (existingCart != null) {
            throw new RuntimeException("User already has a cart. Cannot create a new one.");
        }
        cartRepository.addCart(cart);
        return cart;
    }

    // 7.4.2.2 Get All Carts
    public ArrayList<Cart> getCarts() {
        return cartRepository.findAll();
    }

    // 7.4.2.3 Get a Specific Cart
    public Cart getCartById(UUID cartId) {

        return cartRepository.getCartById(cartId);
    }



    // 7.4.2.4 Get a User’s Cart
    public Cart getCartByUserId(UUID userId) {
        return cartRepository.findAll()
                .stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // 7.4.2.5 Add Product to the Cart
    public void addProductToCart(UUID cartId, Product product) {
        cartRepository.addProductToCart(cartId,product);
    }


    // 7.4.2.6 Delete Product from the Cart
    public void deleteProductFromCart(UUID cartId, Product product) {

        cartRepository.deleteProductFromCart(cartId,product);
    }


    // 7.4.2.7 Delete the Cart
    public void deleteCartById(UUID cartId) {
//
        cartRepository.deleteCartById(cartId);
    }

    // 7.4.2.6 Empty Cart - Fix for UserService
    public void emptyCart(UUID userId) {
        Cart cart = getCartByUserId(userId);
        if (cart != null) {
            cart.getProducts().clear(); // Remove all products from the cart

            // Fetch all carts, remove the old one, and add the updated one
            ArrayList<Cart> carts = cartRepository.findAll();
            carts.removeIf(c -> c.getId().equals(cart.getId())); // Remove old cart
            carts.add(cart); // Add updated cart

            cartRepository.overrideData(carts); // Persist the changes properly
        }
    }

}
