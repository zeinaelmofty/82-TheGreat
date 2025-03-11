package com.example.repository;

import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {

    public ProductRepository() {
        super();
    }


    @Override
    protected String getDataPath() {
        String dockerPath = "data/products.json";  // Path inside Docker
        String localPath = "src/main/java/com/example/data/products.json"; // Path for local testing

        // Check if running in Docker using an environment variable
        return System.getenv("DOCKER_ENV") != null ? dockerPath : localPath;
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class; // Define the type for JSON deserialization
    }

    // 6.3.2.1 Add Product
    public Product addProduct(Product product) {
        save(product); // Uses MainRepository's save() method
        return product;
    }

    // 6.3.2.2 Get All Products
    public ArrayList<Product> getProducts() {
        return findAll();
    }

    // 6.3.2.3 Get Specific Product
    public Product getProductById(UUID productId) {
        return findAll().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    // 6.3.2.4 Update a Product
    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                saveAll(products); // Save updated list to JSON
                return product;
            }
        }
        return null; // Product not found
    }

    // 6.3.2.5 Apply Discount
    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                double newPrice = product.getPrice() * (1 - discount / 100);
                product.setPrice(newPrice);
            }
        }

        saveAll(products); // Save updated products to JSON
    }

    // 6.3.2.6 Delete a Product
    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll()
                .stream()
                .filter(product -> !product.getId().equals(productId))
                .collect(Collectors.toCollection(ArrayList::new));

        saveAll(products); // Save updated list without deleted product
    }
}
