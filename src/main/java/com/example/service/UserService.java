package com.example.service;

import com.example.model.Cart;
import com.example.model.User;
import com.example.model.Order;
import com.example.model.Product;
import com.example.repository.UserRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User> {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;


    // Constructor for Dependency Injection
    public UserService(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        User.setOrderRepository(orderRepository);
    }

    // 7.2.2.1 Add New User
    public User addUser(User user) {
        userRepository.addUser(user);
        return user;
    }

    // 7.2.2.2 Get the Users
    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    // 7.2.2.3 Get a Specific User
    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    // 7.2.2.4 Get the User’s Orders
    public List<Order> getOrdersByUserId(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }

    // 7.2.2.5 Add a New Order (User checks out the cart)
    public void addOrderToUser(UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getProducts().isEmpty()) {
            throw new RuntimeException("Cart is empty or does not exist.");
        }

        double totalPrice = cart.getProducts().stream().mapToDouble(Product::getPrice).sum();

        // Create a new order
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUserId(userId);
        order.setProducts(cart.getProducts());
        order.setTotalPrice(totalPrice);

        // Save the order
       // orderRepository.addOrder(order);
userRepository.addOrderToUser(userId, order);
        // Fetch the user and update their order list
        User user = userRepository.getUserById(userId);
        if (user != null) {
            user.getOrders().add(order);
            userRepository.save(user);  // Save updated user data with new order
        }

        cartService.emptyCart(userId);
    }


    // 7.2.2.6 Empty Cart
    public void emptyCart(UUID userId) {
        cartService.emptyCart(userId);
    }

    // 7.2.2.7 Remove Order
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        List<Order> orders = userRepository.getOrdersByUserId(userId);
        orders.removeIf(order -> order.getId().equals(orderId));
        userRepository.removeOrderFromUser(userId, orderId);
    }

    // 7.2.2.8 Delete the User
    public void deleteUserById(UUID userId) {
        userRepository.getOrdersByUserId(userId).forEach(order -> orderRepository.deleteOrderById(order.getId()));
        userRepository.deleteUserById(userId);
    }


}
