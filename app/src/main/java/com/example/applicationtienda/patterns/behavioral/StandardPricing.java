package com.example.applicationtienda.patterns.behavioral;

import com.example.applicationtienda.domain.model.Product;

public class StandardPricing implements PricingStrategy {
    @Override
    public double calculatePrice(Product product) {
        return product.getPrice(); // Precio sin modificaciones
    }

    @Override
    public String getStrategyName() {
        return "Estándar";
    }
}
