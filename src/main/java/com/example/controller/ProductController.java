package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    // Constructor with Dependency Injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 8.2.2.1 Add Product
    @PostMapping("/")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    // 8.2.2.2 Get All Products
    @GetMapping("/")
    public ArrayList<Product> getProducts() {
        return productService.getProducts();
    }

    // 8.2.2.3 Get a Specific Product
    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable UUID productId) {
        return productService.getProductById(productId);
    }

    // 8.2.2.4 Update the Product
    @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable UUID productId, @RequestBody Map<String, Object> body) {
        String newName = (String) body.get("newName");
        Double newPrice = (body.get("newPrice") != null) ? ((Number) body.get("newPrice")).doubleValue() : null;

        return productService.updateProduct(productId, newName, newPrice);
    }


    // 8.2.2.5 Apply Discount to Products
    @PutMapping("/applyDiscount")
    public String applyDiscount(@RequestParam double discount, @RequestBody ArrayList<UUID> productIds) {
        productService.applyDiscount(discount, productIds);
        return "Discount applied successfully";
    }

    // 8.2.2.6 Delete Product
    @DeleteMapping("/delete/{productId}")
    public String deleteProductById(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        return "Product deleted successfully";
    }
}
