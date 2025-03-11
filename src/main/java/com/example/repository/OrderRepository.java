package com.example.repository;

import com.example.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("rawtypes")
public class OrderRepository extends MainRepository<Order> {

    public OrderRepository() {
        super();
    }

    @Override
    protected String getDataPath() {
        String dockerPath = "data/orders.json";  // Path inside Docker
        String localPath = "src/main/java/com/example/data/orders.json"; // Path for local testing

        // Check if running in Docker (customize this condition if needed)
        return System.getenv("DOCKER_ENV") != null ? dockerPath : localPath;
    }


    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class; // Define the type for JSON deserialization
    }

    // 6.5.2.1 Add Order
    public void addOrder(Order order) {
        save(order); // Ensure this method correctly updates orders.json
    }


    // 6.5.2.2 Get All Orders
    public ArrayList<Order> getOrders() {
        ArrayList<Order> orders = findAll();
        return orders;
    }


    // 6.5.2.3 Get a Specific Order
    public Order getOrderById(UUID orderId) {
        return findAll().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }


    // 6.5.2.4 Delete a Specific Order
    public void deleteOrderById(UUID orderId) {
        ArrayList<Order> orders = findAll()
                .stream()
                .filter(order -> !order.getId().equals(orderId))
                .collect(Collectors.toCollection(ArrayList::new));

        saveAll(orders); // Save updated list without deleted order
    }
}
