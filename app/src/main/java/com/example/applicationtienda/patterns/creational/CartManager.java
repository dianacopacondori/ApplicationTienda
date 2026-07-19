package com.example.applicationtienda.patterns.creational;

import com.example.applicationtienda.domain.model.Cart;
import com.example.applicationtienda.domain.model.Product;
import com.example.applicationtienda.patterns.behavioral.CartObserver;
import com.example.applicationtienda.patterns.behavioral.CommandHistory;
import com.example.applicationtienda.patterns.behavioral.NotificationObserver;

public class CartManager {

    private static CartManager instance;
    private final Cart cart;
    private final CommandHistory commandHistory;

    private CartManager() {
        cart = new Cart("CART001");
        this.commandHistory = new CommandHistory();
        // Registrar observadores (Patrón Observer)
        cart.addObserver(new NotificationObserver());
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public Cart getCart() {
        return cart;
    }

    // <-- Getter para que el Adapter pueda acceder al historial
    public CommandHistory getCommandHistory() {
        return commandHistory;
    }

    public void agregarProducto(Product producto) {
        cart.addItem(producto, 1);
    }

    public void removerProducto(String productId) {
        cart.removeItem(productId);
    }

    public void limpiarCarrito() {
        cart.clear();
    }

    public double getTotal() {
        return cart.getTotal();
    }
}