package com.example.applicationtienda.patterns.behavioral;

import com.example.applicationtienda.domain.model.Product;

public interface PricingStrategy {
    double calculatePrice(Product product);
    String getStrategyName();
}
