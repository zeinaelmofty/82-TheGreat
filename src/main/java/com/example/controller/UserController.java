package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import com.example.service.CartService;
import com.example.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CartService cartService;
    private final ProductRepository productRepository;

    // Constructor for Dependency Injection
    public UserController(UserService userService, CartService cartService, ProductRepository productRepository) {
        this.userService = userService;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    // 8.1.2.1 Add User
    @PostMapping("/")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // 8.1.2.2 Get All Users
    @GetMapping("/")
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    // 8.1.2.3 Get Specific User
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    // 8.1.2.4 Get a User’s Orders
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        return userService.getOrdersByUserId(userId);
    }

    // 8.1.2.5 Check Out (Issue a new order for the user)
    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId) {
        userService.addOrderToUser(userId);
        return "Order added successfully" ;
    }

    // 8.1.2.6 Remove Order
    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {
        userService.removeOrderFromUser(userId, orderId);
        return "Order removed successfully";
    }

    // 8.1.2.7 Empty Cart
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        userService.emptyCart(userId);
        return "Cart emptied successfully";
    }

    // 8.1.2.8 Add Product To the Cart
    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        // Check if the product exists
        Product product = productRepository.getProductById(productId);
        if (product == null) {
            return "Product not found";  // Handle missing product case
        }

        // Check if the user has a cart
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            // If no cart exists, create a new one
            cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
            cartService.addCart(cart);
        }

        // Add the product to the cart
        cartService.addProductToCart(cart.getId(), product);

        return "Product added to cart";
    }


    // 8.1.2.9 Delete Product from the Cart
    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        // Fetch the product by ID
        Product product = productRepository.getProductById(productId);
        if (product == null) {
            return "Product not found";  // Handle missing product case
        }

        // Fetch the cart by user ID
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getProducts().isEmpty()) {
            return "Cart is empty"; // Return if cart doesn't exist or has no products
        }

        // Remove the product from the cart
        cartService.deleteProductFromCart(cart.getId(), product);
        return "Product deleted from cart";
    }


    // 8.1.2.10 Delete User

    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return "User not found";
        }

        userService.deleteUserById(userId);
        return "User deleted successfully";
    }

}
