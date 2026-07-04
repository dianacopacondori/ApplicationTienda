package com.example.applicationtienda.domain.services;

import com.example.applicationtienda.domain.model.Product;
import com.example.applicationtienda.patterns.behavioral.PricingStrategy;

public class PricingService {
    private PricingStrategy strategy;

    // Constructor con inyección de la estrategia (DIP)
    public PricingService(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    // Permite cambiar la estrategia en tiempo de ejecución
    public void setStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculateFinalPrice(Product product) {
        if (strategy == null) {
            throw new IllegalStateException("No hay estrategia de precios configurada");
        }
        return strategy.calculatePrice(product);
    }

    public String getCurrentStrategyName() {
        return strategy != null ? strategy.getStrategyName() : "Ninguna";
    }
}
