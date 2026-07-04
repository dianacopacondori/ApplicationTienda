package com.example.applicationtienda.patterns.creational;

import com.example.applicationtienda.domain.model.Product;
import java.util.UUID;

// FÁBRICA CONCRETA 2
public class ClothingFactory implements ProductFactory {

    @Override
    public Product createProduct(String name, double price, String category) {
        return new Product(
                UUID.randomUUID().toString(),
                name,
                "Prenda de vestir cómoda y duradera",
                price,
                50, // Stock inicial
                "Ropa"
        );
    }

    @Override
    public String getFactoryType() {
        return "Clothing";
    }
}
