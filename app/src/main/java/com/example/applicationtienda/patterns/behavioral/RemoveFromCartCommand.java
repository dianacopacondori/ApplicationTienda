package com.example.applicationtienda.patterns.behavioral;

import com.example.applicationtienda.domain.model.Cart;
import com.example.applicationtienda.domain.model.CartItem;
import com.example.applicationtienda.domain.model.Product;

public class RemoveFromCartCommand implements Command {
    private Cart cart;
    private Product product;
    private int cantidadEliminada = 1; // Guardamos la cantidad por si acaso

    public RemoveFromCartCommand(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;

        // Intentamos buscar el item actual para guardar su cantidad antes de borrarlo
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                this.cantidadEliminada = item.getQuantity();
                break;
            }
        }
    }

    @Override
    public void execute() {
        cart.removeItem(product.getId());
    }

    @Override
    public void undo() {
        // Al deshacer, lo volvemos a agregar con la cantidad que tenía
        cart.addItem(product, cantidadEliminada);
    }

    @Override
    public String getDescription() {
        return "Quitar " + product.getName() + " del carrito";
    }
}