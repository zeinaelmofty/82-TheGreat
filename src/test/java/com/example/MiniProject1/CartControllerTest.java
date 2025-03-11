package com.example.MiniProject1;

import com.example.controller.CartController;
import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.CartService;
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

class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private Cart sampleCart;
    private UUID cartId;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartId = UUID.randomUUID();
        sampleProduct = new Product(UUID.randomUUID(), "Test Product", 50.0);
        sampleCart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
    }

    // Add Cart Tests
    @Test
    void shouldAddCartSuccessfully() {
        when(cartService.addCart(sampleCart)).thenReturn(sampleCart);
        Cart result = cartController.addCart(sampleCart);
        assertNotNull(result);
        assertEquals(sampleCart, result);
    }

    @Test
    void shouldReturnNullWhenCartIsNull() {
        when(cartService.addCart(null)).thenReturn(null);
        assertNull(cartController.addCart(null));
    }

    @Test
    void shouldVerifyAddCartServiceCall() {
        cartController.addCart(sampleCart);
        verify(cartService, times(1)).addCart(sampleCart);
    }

    // Get All Carts Tests
    @Test
    void shouldReturnAllCarts() {
        List<Cart> carts = List.of(sampleCart);
        when(cartService.getCarts()).thenReturn(new ArrayList<>(carts));

        List<Cart> result = cartController.getCarts();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListIfNoCarts() {
        when(cartService.getCarts()).thenReturn(new ArrayList<>());
        assertTrue(cartController.getCarts().isEmpty());
    }

    @Test
    void shouldVerifyGetCartsServiceCall() {
        cartController.getCarts();
        verify(cartService, times(1)).getCarts();
    }

    // Get Specific Cart Tests
    @Test
    void shouldReturnCartById() {
        when(cartService.getCartById(cartId)).thenReturn(sampleCart);
        Cart result = cartController.getCartById(cartId);
        assertNotNull(result);
        assertEquals(sampleCart, result);
    }

    @Test
    void shouldReturnNullForNonExistingCart() {
        when(cartService.getCartById(cartId)).thenReturn(null);
        assertNull(cartController.getCartById(cartId));
    }

    @Test
    void shouldVerifyGetCartByIdServiceCall() {
        cartController.getCartById(cartId);
        verify(cartService, times(1)).getCartById(cartId);
    }

    // Add Product to Cart Tests
    @Test
    void shouldAddProductToCartSuccessfully() {
        doNothing().when(cartService).addProductToCart(cartId, sampleProduct);
        String response = cartController.addProductToCart(cartId, sampleProduct);
        assertEquals("Successfully added product to Cart!", response);
    }

    @Test
    void shouldNotAddProductToNonExistentCart() {
        UUID nonExistentCartId = UUID.randomUUID();
        doThrow(new IllegalArgumentException("Cart not found")).when(cartService).addProductToCart(nonExistentCartId, sampleProduct);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> cartController.addProductToCart(nonExistentCartId, sampleProduct));
        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void shouldVerifyAddProductToCartServiceCall() {
        cartController.addProductToCart(cartId, sampleProduct);
        verify(cartService, times(1)).addProductToCart(cartId, sampleProduct);
    }

    // Delete Cart Tests
    @Test
    void shouldDeleteCartSuccessfully() {
        doNothing().when(cartService).deleteCartById(cartId);
        String response = cartController.deleteCartById(cartId);
        assertEquals("Cart deleted successfully", response);
    }

    @Test
    void shouldHandleDeletingNonExistentCart() {
        UUID nonExistentCartId = UUID.randomUUID();
        doThrow(new IllegalArgumentException("Cart not found")).when(cartService).deleteCartById(nonExistentCartId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> cartController.deleteCartById(nonExistentCartId));
        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void shouldVerifyDeleteCartServiceCall() {
        cartController.deleteCartById(cartId);
        verify(cartService, times(1)).deleteCartById(cartId);
    }
}
