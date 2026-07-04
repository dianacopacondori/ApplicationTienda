package com.example.applicationtienda.patterns.creational;

import com.example.applicationtienda.domain.model.Product;

// INTERFAZ DE LA FÁBRICA (DIP - Dependemos de abstracciones)
public interface ProductFactory {
    Product createProduct(String name, double price, String category);
    String getFactoryType();
}
