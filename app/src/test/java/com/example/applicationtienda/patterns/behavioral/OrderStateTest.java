package com.example.applicationtienda.patterns.behavioral;

import org.junit.jupiter.api.BeforeEach; // Para JUnit 5
import org.junit.jupiter.api.Test;       // Para JUnit 5
import static org.junit.Assert.*;

import com.example.applicationtienda.domain.model.Order;


public class OrderStateTest {

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
    }

    @Test
    public void testState_TransicionDePedido() {
        // Act: Ejecutar transiciones de estado
        order.process();  // De Pending a Processing
        order.ship();     // De Processing a Shipped
        order.deliver();  // De Shipped a Delivered

        // Assert: Verificamos que no lance excepciones y el estado final sea el esperado.
        // NOTA: Reemplaza "order.getState().toString()" por el getter real de tu clase Order
        // si tienes uno diferente (ej: order.getEstadoNombre() o similar).
        assertTrue(order.getState().toString().contains("Delivered") ||
                order.getState().getClass().getSimpleName().equals("DeliveredState"));
    }

    @Test
    public void testState_NoPuedeCancelarEntregado() {
        // Arrange: Llevar el pedido a estado Delivered
        order.process();
        order.ship();
        order.deliver();

        // Act: Intentar cancelar (tu método cancel() devuelve void, no boolean)
        // Simplemente llamamos al método. Si tu lógica interna lo impide,
        // el estado no debería cambiar a Cancelled.
        order.cancel();

        // Assert: Verificar que sigue siendo DeliveredState
        assertTrue(order.getState().getClass().getSimpleName().equals("DeliveredState"));
    }

    @Test
    public void testState_PuedeCancelarPendiente() {
        // Arrange: Pedido inicia en PendingState (según tu constructor)
        assertTrue(order.getState().getClass().getSimpleName().equals("PendingState"));

        // Act: Cancelar
        order.cancel();

        // Assert: Debería haber cambiado a CancelledState
        assertTrue(order.getState().getClass().getSimpleName().equals("CancelledState"));
    }
}