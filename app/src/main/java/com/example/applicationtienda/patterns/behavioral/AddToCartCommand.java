package com.example.applicationtienda.patterns.behavioral;

import com.example.applicationtienda.domain.model.Cart;
import com.example.applicationtienda.domain.model.Product;

public class AddToCartCommand implements Command {
    private Cart cart;
    private Product product;
    private int quantity;

    public AddToCartCommand(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public void execute() {
        cart.addItem(product, quantity);
    }

    @Override
    public void undo() {
        cart.removeItem(product.getId());
    }

    @Override
    public String getDescription() {
        return "Agregar " + quantity + "x " + product.getName() + " al carrito";
    }
}
