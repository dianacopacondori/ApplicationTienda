package com.example.applicationtienda.patterns.behavioral;

import com.example.applicationtienda.domain.model.Product;

public class SeasonalPricing implements PricingStrategy {
    @Override
    public double calculatePrice(Product product) {
        return product.getPrice() * 0.80; // -20%
    }

    @Override
    public String getStrategyName() {
        return "Temporada";
    }
}
