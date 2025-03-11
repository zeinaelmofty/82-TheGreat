package com.example.controller;

import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    // Constructor with Dependency Injection
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 8.4.2.1 Add Order
    @PostMapping("/")
    public void addOrder(@RequestBody Order order) {
        orderService.addOrder(order);
    }

    // 8.4.2.2 Get a Specific Order
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId);
    }

    // 8.4.2.3 Get All Orders
    @GetMapping("/")
    public ArrayList<Order> getOrders() {
        return orderService.getOrders();
    }

    // 8.4.2.4 Delete a Specific Order
    @DeleteMapping("/delete/{orderId}")
    public String deleteOrderById(@PathVariable UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "Order not found";
        }

        orderService.deleteOrderById(orderId);
        return "Order deleted successfully";
    }


}
