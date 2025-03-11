package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {

    public CartRepository() {
        super();
    }
    String dockerPath = "data/carts.json";  // Path for Docker
    String localPath = "src/main/java/com/example/data/carts.json";
    @Override
    protected String getDataPath() {
        return System.getenv("DOCKER_ENV") != null ? dockerPath : localPath;// Path to JSON file
    }

    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class; // Define the type for JSON deserialization
    }

    // 6.4.2.1 Add New Cart
    public Cart addCart(Cart cart) {
        save(cart); // Uses MainRepository's save() method
        return cart;
    }

    // 6.4.2.2 Get All Carts
    public ArrayList<Cart> getCarts() {
        return findAll();
    }

    // 6.4.2.3 Get Specific Cart
    public Cart getCartById(UUID cartId) {
        return findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

    // 6.4.2.4 Get User’s Cart
    public Cart getCartByUserId(UUID userId) {
        return findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // 6.4.2.5 Add Product to Cart
    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        boolean updated = false;

        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.getProducts().add(product);
                updated = true;
                break;
            }
        }
        if (updated) {
            System.out.println("Overriding data in carts.json: " + carts);
            overrideData(carts);
        }
    }



    // 6.4.2.6 Delete Product from Cart
    public void deleteProductFromCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();

        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.setProducts(cart.getProducts().stream()
                        .filter(p -> !p.getId().equals(product.getId()))
                        .collect(Collectors.toList()));

                saveAll(carts); // Save updated list to JSON
                return;
            }
        }
    }

    // 6.4.2.7 Delete the Cart
    public void deleteCartById(UUID cartId) {
        ArrayList<Cart> carts = findAll()
                .stream()
                .filter(cart -> !cart.getId().equals(cartId))
                .collect(Collectors.toCollection(ArrayList::new));

        saveAll(carts); // Save updated list without deleted cart
    }
}
