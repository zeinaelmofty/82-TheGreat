package com.example.repository;

import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Repository
@SuppressWarnings("rawtypes")
public class UserRepository extends MainRepository<User> {

    public UserRepository(OrderRepository orderRepository) {
        super();
        this.orderRepository = orderRepository;
    }
    private final OrderRepository orderRepository;
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/users.json"; // Path to JSON file
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class; // Define the type for JSON deserialization
    }

    // 6.2.2.1 Get Users
    public ArrayList<User> getUsers() {
        return findAll(); // Uses MainRepository's findAll() method
    }

    // 6.2.2.2 Get User By ID
    public User getUserById(UUID userId) {
        return findAll().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // 6.2.2.3 Add User
    public User addUser(User user) {
        save(user); // Uses MainRepository's save() method
        return user;
    }
    public void addOrderToUser(UUID userId, Order order) {
        User user = getUserById(userId);
        if (user != null) {
            List<Order> userOrders = user.getOrders();
            userOrders.add(order); // Add the new order to the list
            user.setOrders(userOrders); // Update the user's order list
            save(user); // Save updated user to the JSON file
            orderRepository.addOrder(order); // Save order in orders.json
        }
    }


    // 6.2.2.4 Get The Orders of a User
    public List<Order> getOrdersByUserId(UUID userId) {
        // Fetch all orders from orders.json
        ArrayList<Order> allOrders = orderRepository.findAll();

        // Filter by the given userId
        return allOrders.stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
    }


    public void deleteUserById(UUID userId) {
        ArrayList<User> users = getUsers();
        users.removeIf(user -> user.getId().equals(userId));
        saveAll(users);// Assuming there's a method to update the JSON file
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        User user = getUserById(userId);
        if (user != null) {
            user.getOrders().removeIf(order -> order.getId().equals(orderId));
            save(user);
        }
    }

}
