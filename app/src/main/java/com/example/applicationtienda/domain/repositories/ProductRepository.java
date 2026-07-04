package com.example.applicationtienda.domain.repositories;

import com.example.applicationtienda.domain.model.Product;
import java.util.List;

public interface ProductRepository {
    List<Product> getAllProducts();
    Product getProductById(String productId);
    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(String productId);
}
