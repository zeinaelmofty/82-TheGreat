package com.example.MiniProject1;

import com.example.controller.OrderController;
import com.example.model.Order;
import com.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Order sampleOrder;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderId = UUID.randomUUID();
        sampleOrder = new Order(orderId, UUID.randomUUID(), 100.0, new ArrayList<>());
    }

    // Add Order Tests
    @Test
    void shouldAddOrderSuccessfully() {
        doNothing().when(orderService).addOrder(sampleOrder);
        assertDoesNotThrow(() -> orderController.addOrder(sampleOrder));
    }

    @Test
    void shouldCallServiceMethodWhenAddingOrder() {
        doNothing().when(orderService).addOrder(sampleOrder);
        orderController.addOrder(sampleOrder);
        verify(orderService, times(1)).addOrder(sampleOrder);
    }

    @Test
    void shouldHandleNullOrderAddition() {
        doNothing().when(orderService).addOrder(null);
        assertDoesNotThrow(() -> orderController.addOrder(null));
    }

    // Get Order By ID Tests
    @Test
    void shouldReturnOrderById() {
        when(orderService.getOrderById(orderId)).thenReturn(sampleOrder);
        Order result = orderController.getOrderById(orderId);
        assertNotNull(result);
        assertEquals(sampleOrder, result);
    }

    @Test
    void shouldReturnNullForNonExistingOrder() {
        when(orderService.getOrderById(orderId)).thenReturn(null);
        assertNull(orderController.getOrderById(orderId));
    }

    @Test
    void shouldVerifyGetOrderByIdServiceCall() {
        orderController.getOrderById(orderId);
        verify(orderService, times(1)).getOrderById(orderId);
    }

    // Get All Orders Tests
    @Test
    void shouldReturnAllOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(sampleOrder);
        when(orderService.getOrders()).thenReturn(new ArrayList<>(orders));

        List<Order> result = orderController.getOrders();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersExist() {
        when(orderService.getOrders()).thenReturn(new ArrayList<>());
        List<Order> result = orderController.getOrders();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldVerifyGetOrdersServiceCall() {
        orderController.getOrders();
        verify(orderService, times(1)).getOrders();
    }

    // Delete Order Tests
    @Test
    void shouldDeleteOrderIfExists() {
        when(orderService.getOrderById(orderId)).thenReturn(sampleOrder);
        doNothing().when(orderService).deleteOrderById(orderId);

        String response = orderController.deleteOrderById(orderId);
        assertEquals("Order deleted successfully", response);
    }

    @Test
    void shouldReturnOrderNotFoundIfOrderDoesNotExist() {
        when(orderService.getOrderById(orderId)).thenReturn(null);
        String response = orderController.deleteOrderById(orderId);
        assertEquals("Order not found", response);
    }

    @Test
    void shouldVerifyDeleteOrderServiceCall() {
        when(orderService.getOrderById(orderId)).thenReturn(sampleOrder);
        orderController.deleteOrderById(orderId);
        verify(orderService, times(1)).deleteOrderById(orderId);
    }
}
