package com.example.applicationtienda.patterns.creational;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.applicationtienda.domain.model.Order;
import com.example.applicationtienda.domain.model.Product;

public class OrderBuilderTest {

    @Test
    void testOrderBuilder_CreacionDePedido() {
        // Arrange
        OrderBuilder builder = new OrderBuilder();
        Product producto = new Product("P001", "Laptop", "Desc", 1500.0, 10, "Electrónica");

        // Act
        builder.setUserId("USER_001");
        builder.addProduct(producto);
        Order pedido = builder.build();

        // Assert
        assertNotNull(pedido, "El pedido no debe ser nulo");
        assertEquals("USER_001", pedido.getUserId(), "El ID de usuario debe coincidir");
    }
}
