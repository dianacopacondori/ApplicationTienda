package com.example.applicationtienda.patterns.behavioral;

import com.example.applicationtienda.domain.model.Cart;
import com.example.applicationtienda.domain.model.Product;

public class RemoveFromCartCommand implements Command {
    private Cart cart;
    private Product product;

    public RemoveFromCartCommand(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
    }

    @Override
    public void execute() {
        cart.removeItem(product.getId());
    }

    @Override
    public void undo() {
        // Para deshacer, volvemos a agregar el producto con cantidad 1
        cart.addItem(product, 1);
    }

    @Override
    public String getDescription() {
        return "Quitar " + product.getName() + " del carrito";
    }
}
