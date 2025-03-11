package com.example.model;

import com.example.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class User {
    private UUID id;
    private String name;
    private List<Order> orders;
    private static OrderRepository orderRepository;

    public User() {
        this.id = UUID.randomUUID();
        this.orders = new ArrayList<>();
    }

    public User(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.orders = new ArrayList<>();
    }

    public User(UUID id, String name, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.orders = new ArrayList<>();

        if (orders != null) {
            this.orders.addAll(orders);

                for (Order order : orders) {
                    orderRepository.addOrder(order);
                }

        }
    }

    public static void setOrderRepository(OrderRepository repo) {
        orderRepository = repo;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;

        if (orderRepository != null) {
            for (Order order : orders) {
                orderRepository.addOrder(order);
            }
        }
    }
}
