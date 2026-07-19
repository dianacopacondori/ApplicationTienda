package com.example.applicationtienda.patterns.behavioral;

import com.example.applicationtienda.domain.model.Cart;
import com.example.applicationtienda.domain.model.CartItem;
import com.example.applicationtienda.domain.model.Product;

public class DisminuirCantidadCommand implements Command {
    private Cart cart;
    private Product product;
    private int cantidadAnterior;
    private boolean fueEliminado; // <-- NUEVO: Para saber si se borró de la lista

    public DisminuirCantidadCommand(Cart cart, CartItem item) {
        this.cart = cart;
        this.product = item.getProduct();
        this.cantidadAnterior = item.getQuantity();
        this.fueEliminado = false;
    }

    @Override
    public void execute() {
        // Si la cantidad era 1, sabemos que el método del cart lo va a eliminar
        if (cantidadAnterior == 1) {
            fueEliminado = true;
        }
        cart.disminuirCantidad(product.getId());
    }

    @Override
    public void undo() {
        if (fueEliminado) {
            // ¡CLAVE! Si se eliminó, hay que VOLVER A AGREGARLO a la lista
            // No podemos usar actualizarCantidad porque el item ya no existe en la lista
            cart.addItem(product, cantidadAnterior);
        } else {
            // Si solo bajó (ej: de 3 a 2), sí podemos actualizar la cantidad
            cart.actualizarCantidad(product.getId(), cantidadAnterior);
        }
    }

    @Override
    public String getDescription() {
        return "Disminuir cantidad de " + product.getName();
    }
}