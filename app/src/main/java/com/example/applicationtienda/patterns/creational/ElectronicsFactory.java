package com.example.applicationtienda.patterns.creational;

import com.example.applicationtienda.domain.model.Product;
import java.util.UUID;

// FÁBRICA CONCRETA 1
public class ElectronicsFactory implements ProductFactory {

    @Override
    public Product createProduct(String name, double price, String category) {
        // Crea un producto con ID único y categoría forzada a "Electrónica"
        return new Product(
                UUID.randomUUID().toString(),
                name,
                "Producto electrónico de alta calidad",
                price,
                10, // Stock inicial
                "Electrónica"
        );
    }

    @Override
    public String getFactoryType() {
        return "Electronics";
    }
}
