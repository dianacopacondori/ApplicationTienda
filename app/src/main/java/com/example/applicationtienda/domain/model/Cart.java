package com.example.applicationtienda.domain.model;

import com.example.applicationtienda.patterns.behavioral.CartObserver;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String id;
    private List<CartItem> items;
    private double total;
    private List<CartObserver> observers; // Lista de observadores

    public Cart(String id) {
        this.id = id;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.observers = new ArrayList<>();
    }

    // Métodos para gestionar observadores
    public void addObserver(CartObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CartObserver observer) {
        observers.remove(observer);
    }

    private void notifyProductAdded(Product product) {
        for (CartObserver observer : observers) {
            observer.onProductAdded(product);
        }
    }

    private void notifyProductRemoved(Product product) {
        for (CartObserver observer : observers) {
            observer.onProductRemoved(product);
        }
    }

    private void notifyCartCleared() {
        for (CartObserver observer : observers) {
            observer.onCartCleared();
        }
    }

    // Agregar producto al carrito (notifica a los observadores)
    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Producto inválido o cantidad incorrecta");
        }

        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                calculateTotal();
                notifyProductAdded(product); // Notificar
                return;
            }
        }

        CartItem newItem = new CartItem(product, quantity);
        items.add(newItem);
        calculateTotal();
        notifyProductAdded(product); // Notificar
    }

    public void removeItem(String productId) {
        Product removedProduct = null;
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                removedProduct = item.getProduct();
                break;
            }
        }

        items.removeIf(item -> item.getProduct().getId().equals(productId));
        calculateTotal();

        if (removedProduct != null) {
            notifyProductRemoved(removedProduct); // Notificar
        }
    }

    public void clear() {
        items.clear();
        total = 0.0;
        notifyCartCleared(); // Notificar
    }

    private void calculateTotal() {
        this.total = 0.0;
        for (CartItem item : items) {
            this.total += item.getProduct().getPrice() * item.getQuantity();
        }
    }

    // Getters
    public String getId() { return id; }
    public List<CartItem> getItems() { return new ArrayList<>(items); }
    public double getTotal() { return total; }
    public int getItemCount() { return items.size(); }

    @Override
    public String toString() {
        return "Cart{id='" + id + "', items=" + items.size() + ", total=" + total + "}";
    }
}
