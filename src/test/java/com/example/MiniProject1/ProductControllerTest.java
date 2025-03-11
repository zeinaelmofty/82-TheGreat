package com.example.MiniProject1;

import com.example.controller.ProductController;
import com.example.model.Product;
import com.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product sampleProduct;
    private UUID productId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productId = UUID.randomUUID();
        sampleProduct = new Product(productId, "Sample Product", 100.0);
    }

    // Add Product Tests
    @Test
    void shouldAddProductSuccessfully() {
        when(productService.addProduct(sampleProduct)).thenReturn(sampleProduct);
        Product result = productController.addProduct(sampleProduct);
        assertNotNull(result);
        assertEquals(sampleProduct, result);
    }

    @Test
    void shouldReturnNullIfProductIsInvalid() {
        when(productService.addProduct(null)).thenReturn(null);
        assertNull(productController.addProduct(null));
    }

    @Test
    void shouldVerifyAddProductServiceCall() {
        productController.addProduct(sampleProduct);
        verify(productService, times(1)).addProduct(sampleProduct);
    }

    //  Get All Products Tests
    @Test
    void shouldReturnAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(sampleProduct);
        when(productService.getProducts()).thenReturn(products);

        ArrayList<Product> result = productController.getProducts();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListIfNoProductsExist() {
        when(productService.getProducts()).thenReturn(new ArrayList<>());
        assertTrue(productController.getProducts().isEmpty());
    }

    @Test
    void shouldVerifyGetProductsServiceCall() {
        productController.getProducts();
        verify(productService, times(1)).getProducts();
    }

    // Get Specific Product Tests
    @Test
    void shouldReturnProductById() {
        when(productService.getProductById(productId)).thenReturn(sampleProduct);
        Product result = productController.getProductById(productId);
        assertNotNull(result);
        assertEquals(sampleProduct, result);
    }

    @Test
    void shouldReturnNullForNonExistingProduct() {
        when(productService.getProductById(productId)).thenReturn(null);
        assertNull(productController.getProductById(productId));
    }

    @Test
    void shouldVerifyGetProductByIdServiceCall() {
        productController.getProductById(productId);
        verify(productService, times(1)).getProductById(productId);
    }

    // Update Product Tests
    @Test
    void shouldSuccessfullyUpdateProduct() {
        Product updatedProduct = new Product(productId, "Updated Product", 80.0);
        when(productService.updateProduct(productId, "Updated Product", 80.0)).thenReturn(updatedProduct);

        Map<String, Object> body = Map.of("newName", "Updated Product", "newPrice", 80.0);
        Product result = productController.updateProduct(productId, body);
        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals(80.0, result.getPrice());
    }

    @Test
    void shouldReturnNullIfProductToUpdateDoesNotExist() {
        when(productService.updateProduct(productId, "Updated Product", 80.0)).thenReturn(null);
        Map<String, Object> body = Map.of("newName", "Updated Product", "newPrice", 80.0);
        assertNull(productController.updateProduct(productId, body));
    }

    @Test
    void shouldVerifyUpdateProductServiceCall() {
        Map<String, Object> body = Map.of("newName", "Updated Product", "newPrice", 80.0);
        productController.updateProduct(productId, body);
        verify(productService, times(1)).updateProduct(productId, "Updated Product", 80.0);
    }

    // Apply Discount Tests
    @Test
    void shouldSuccessfullyApplyDiscount() {
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(productId);
        doNothing().when(productService).applyDiscount(20.0, productIds);

        String response = productController.applyDiscount(20.0, productIds);
        assertEquals("Discount applied successfully", response);
    }

    @Test
    void shouldReturnSuccessIfProductListIsEmptyForDiscount() {
        ArrayList<UUID> productIds = new ArrayList<>();
        doNothing().when(productService).applyDiscount(20.0, productIds);

        String response = productController.applyDiscount(20.0, productIds);
        assertEquals("Discount applied successfully", response);
    }

    @Test
    void shouldVerifyApplyDiscountServiceCall() {
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(productId);
        productController.applyDiscount(20.0, productIds);
        verify(productService, times(1)).applyDiscount(20.0, productIds);
    }

    // Delete Product Tests
    @Test
    void shouldSuccessfullyDeleteProduct() {
        doNothing().when(productService).deleteProductById(productId);
        String response = productController.deleteProductById(productId);
        assertEquals("Product deleted successfully", response);
    }

    @Test
    void shouldThrowErrorIfProductToDeleteDoesNotExist() {
        doThrow(new IllegalArgumentException("Product not found")).when(productService).deleteProductById(productId);
        assertThrows(IllegalArgumentException.class, () -> productController.deleteProductById(productId));
    }

    @Test
    void shouldVerifyDeleteProductServiceCall() {
        productController.deleteProductById(productId);
        verify(productService, times(1)).deleteProductById(productId);
    }
}
