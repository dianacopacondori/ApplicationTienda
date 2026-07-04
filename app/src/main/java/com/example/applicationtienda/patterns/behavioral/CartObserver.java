package com.example.applicationtienda.patterns.behavioral;

import com.example.applicationtienda.domain.model.Product;

public interface CartObserver {
    void onProductAdded(Product product);
    void onProductRemoved(Product product);
    void onCartCleared();
}
