package com.example.applicationtienda.patterns.creational;

import com.example.applicationtienda.domain.model.Order;
import com.example.applicationtienda.domain.model.Product;
import java.util.UUID;

public class OrderBuilder {
    private Order order;

    public OrderBuilder() {
        this.order = new Order(); // Inicia el objeto vacío
    }

    // Método fluido (Fluent Interface) para encadenar llamadas
    public OrderBuilder setUserId(String userId) {
        order.setUserId(userId);
        return this;
    }

    public OrderBuilder addProduct(Product product) {
        if (product != null && product.getStock() > 0) {
            order.addProduct(product);
        } else {
            throw new IllegalArgumentException("Producto inválido o sin stock");
        }
        return this;
    }

    // Método final que valida y "construye" el objeto
    public Order build() {
        // Validaciones (SRP - El builder se encarga de validar la construcción)
        if (order.getUserId() == null || order.getUserId().isEmpty()) {
            throw new IllegalStateException("El pedido debe tener un usuario asignado");
        }
        if (order.getProducts().isEmpty()) {
            throw new IllegalStateException("El pedido debe tener al menos un producto");
        }

        // Asignar ID final y calcular total antes de retornar
        order.setId(UUID.randomUUID().toString());
        order.calculateTotal();

        return order; // Retorna el objeto inmutable y listo para usar
    }
}
