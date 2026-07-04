package com.example.applicationtienda.patterns.behavioral;

import com.example.applicationtienda.domain.model.Product;

public class PremiumPricing implements PricingStrategy {
    @Override
    public double calculatePrice(Product product) {
        return product.getPrice() * 1.20; // +20%
    }

    @Override
    public String getStrategyName() {
        return "Premium";
    }
}
