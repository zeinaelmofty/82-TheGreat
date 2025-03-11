package com.example.MiniProject1;

import com.example.controller.UserController;
import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.UserService;
import com.example.service.CartService;
import com.example.repository.ProductRepository;
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

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private UserController userController;

    private User sampleUser;
    private UUID userId;
    private UUID orderId;
    private UUID productId;
    private Product sampleProduct;
    private Cart sampleCart;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        productId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        sampleProduct = new Product(productId, "Test Product", 50.0);
        sampleCart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        sampleUser = new User(userId, "Test User", new ArrayList<>());
    }

    // Add User Tests
    @Test
    void shouldAddUserSuccessfully() {
        when(userService.addUser(sampleUser)).thenReturn(sampleUser);
        User result = userController.addUser(sampleUser);
        assertNotNull(result);
        assertEquals(sampleUser, result);
        verify(userService, times(1)).addUser(sampleUser);
    }

    @Test
    void shouldReturnNullIfUserIsInvalid() {
        when(userService.addUser(null)).thenReturn(null);
        assertNull(userController.addUser(null));
    }

    @Test
    void shouldVerifyAddUserServiceCall() {
        userController.addUser(sampleUser);
        verify(userService, times(1)).addUser(sampleUser);
    }

    // Get All Users Tests
    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(sampleUser);
        when(userService.getUsers()).thenReturn(new ArrayList<>(users));

        List<User> result = userController.getUsers();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListIfNoUsersExist() {
        when(userService.getUsers()).thenReturn(new ArrayList<>());
        assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    void shouldVerifyGetUsersServiceCall() {
        userController.getUsers();
        verify(userService, times(1)).getUsers();
    }

    // Get Specific User Tests
    @Test
    void shouldReturnUserById() {
        when(userService.getUserById(userId)).thenReturn(sampleUser);
        User result = userController.getUserById(userId);
        assertNotNull(result);
        assertEquals(sampleUser, result);
    }

    @Test
    void shouldReturnNullForNonExistingUser() {
        when(userService.getUserById(userId)).thenReturn(null);
        assertNull(userController.getUserById(userId));
    }


    @Test
    void shouldHandleExceptionWhenGettingUserById() {
        when(userService.getUserById(userId)).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> userController.getUserById(userId));
    }

    //Get Orders by User ID
    @Test
    void shouldHandleExceptionWhenFetchingOrdersByUserId() {
        when(userService.getOrdersByUserId(userId)).thenThrow(new RuntimeException("Error retrieving orders"));
        assertThrows(RuntimeException.class, () -> userController.getOrdersByUserId(userId));
    }

    @Test
    void shouldReturnEmptyListIfNoOrders() {
        when(userService.getOrdersByUserId(userId)).thenReturn(new ArrayList<>());
        assertTrue(userController.getOrdersByUserId(userId).isEmpty());
    }

    @Test
    void shouldVerifyGetOrdersByUserIdServiceCall() {
        userController.getOrdersByUserId(userId);
        verify(userService, times(1)).getOrdersByUserId(userId);
    }

    // Check Out (Add Order to User)
    @Test
    void shouldSuccessfullyCheckOutUser() {
        doNothing().when(userService).addOrderToUser(userId);
        String response = userController.addOrderToUser(userId);
        assertEquals("Order added successfully", response);
    }

    @Test
    void shouldReturnCustomMessageOnCheckoutFailure() {
        doThrow(new RuntimeException("User has insufficient balance")).when(userService).addOrderToUser(userId);
        Exception exception = assertThrows(RuntimeException.class, () -> userController.addOrderToUser(userId));
        assertEquals("User has insufficient balance", exception.getMessage());
    }

    @Test
    void shouldVerifyCheckoutServiceCall() {
        userController.addOrderToUser(userId);
        verify(userService, times(1)).addOrderToUser(userId);
    }

    // remove order

    @Test
    void shouldFailToRemoveOrderIfServiceThrowsException() {
        doThrow(new RuntimeException("Database error")).when(userService).removeOrderFromUser(userId, orderId);

        Exception exception = assertThrows(RuntimeException.class, () -> userController.removeOrderFromUser(userId, orderId));
        assertEquals("Database error", exception.getMessage());
    }


    @Test
    void shouldHandleUnexpectedExceptionOnRemoveOrder() {
        doThrow(new RuntimeException("Unexpected error")).when(userService).removeOrderFromUser(userId, orderId);
        assertThrows(RuntimeException.class, () -> userController.removeOrderFromUser(userId, orderId));
    }

    @Test
    void shouldReturnErrorMessageIfOrderIdIsInvalid() {
        UUID invalidOrderId = UUID.randomUUID(); // Simulating an invalid order ID
        doThrow(new IllegalArgumentException("Invalid order ID")).when(userService).removeOrderFromUser(userId, invalidOrderId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userController.removeOrderFromUser(userId, invalidOrderId));
        assertEquals("Invalid order ID", exception.getMessage());

        verify(userService, times(1)).removeOrderFromUser(userId, invalidOrderId);
    }






    // Empty Cart Tests
    @Test
    void shouldSuccessfullyEmptyCart() {
        doNothing().when(userService).emptyCart(userId);
        String response = userController.emptyCart(userId);
        assertEquals("Cart emptied successfully", response);
    }

    @Test
    void shouldHandleCartEmptyFailure() {
        doThrow(new RuntimeException("Cart already empty")).when(userService).emptyCart(userId);
        assertThrows(RuntimeException.class, () -> userController.emptyCart(userId));
    }

    @Test
    void shouldVerifyEmptyCartServiceCall() {
        userController.emptyCart(userId);
        verify(userService, times(1)).emptyCart(userId);
    }

    //  Add Product to Cart Tests
    @Test
    void shouldSuccessfullyAddProductToCart() {
        when(productRepository.getProductById(productId)).thenReturn(sampleProduct);
        when(cartService.getCartByUserId(userId)).thenReturn(sampleCart);

        String response = userController.addProductToCart(userId, productId);
        assertEquals("Product added to cart", response);
        verify(cartService, times(1)).addProductToCart(sampleCart.getId(), sampleProduct);
    }

    @Test
    void shouldReturnErrorIfProductDoesNotExist() {
        when(productRepository.getProductById(productId)).thenReturn(null);

        String response = userController.addProductToCart(userId, productId);
        assertEquals("Product not found", response);
        verify(cartService, never()).addProductToCart(any(), any());
    }

    @Test
    void shouldFailToAddProductToCartIfCartServiceFails() {
        when(productRepository.getProductById(productId)).thenReturn(sampleProduct);
        when(cartService.getCartByUserId(userId)).thenReturn(sampleCart);
        doThrow(new RuntimeException("Cart Service failure")).when(cartService).addProductToCart(any(), any());

        Exception exception = assertThrows(RuntimeException.class, () -> userController.addProductToCart(userId, productId));
        assertEquals("Cart Service failure", exception.getMessage());
    }

    // Delete Product from Cart Tests
    @Test
    void shouldNotDeleteProductIfCartIsEmpty() {
        when(productRepository.getProductById(productId)).thenReturn(sampleProduct);
        when(cartService.getCartByUserId(userId)).thenReturn(new Cart(UUID.randomUUID(), userId, new ArrayList<>())); // Empty cart

        String response = userController.deleteProductFromCart(userId, productId);
        assertEquals("Cart is empty", response);

        verify(cartService, never()).deleteProductFromCart(any(), any());
    }



    @Test
    void shouldReturnErrorIfProductNotInCart() {
        when(productRepository.getProductById(productId)).thenReturn(sampleProduct);
        when(cartService.getCartByUserId(userId)).thenReturn(new Cart(UUID.randomUUID(), userId, new ArrayList<>()));

        String response = userController.deleteProductFromCart(userId, productId);
        assertEquals("Cart is empty", response);
        verify(cartService, never()).deleteProductFromCart(any(), any());
    }

    @Test
    void shouldReturnErrorIfProductDoesNotExistForDeletion() {
        when(productRepository.getProductById(productId)).thenReturn(null);

        String response = userController.deleteProductFromCart(userId, productId);
        assertEquals("Product not found", response);
        verify(cartService, never()).deleteProductFromCart(any(), any());
    }


    // Delete User Tests
    @Test
    void shouldSuccessfullyDeleteUser() {
        when(userService.getUserById(userId)).thenReturn(sampleUser);
        doNothing().when(userService).deleteUserById(userId);

        String response = userController.deleteUserById(userId);
        assertEquals("User deleted successfully", response);
    }

    @Test
    void shouldFailToDeleteNonExistingUser() {
        when(userService.getUserById(userId)).thenReturn(null);
        assertEquals("User not found", userController.deleteUserById(userId));
    }

    @Test
    void shouldVerifyDeleteUserServiceCall() {
        when(userService.getUserById(userId)).thenReturn(sampleUser);
        userController.deleteUserById(userId);
        verify(userService, times(1)).deleteUserById(userId);
    }
}
