package com.example.applicationtienda.patterns.behavioral;

import org.junit.jupiter.api.BeforeEach; // Para JUnit 5
import org.junit.jupiter.api.Test;       // Para JUnit 5
import static org.junit.Assert.*;

import com.example.applicationtienda.domain.model.Cart;
import com.example.applicationtienda.domain.model.Product;

public class CartObserverTest {

    private Cart cart;
    private TestCartObserver observer;

    // Clase auxiliar para capturar notificaciones
    // CORREGIDO: Ahora implementa los métodos exactos de tu interfaz CartObserver
    private static class TestCartObserver implements CartObserver {
        public boolean wasNotified = false;
        public String lastEvent = "";

        @Override
        public void onProductAdded(Product product) {
            wasNotified = true;
            lastEvent = "onProductAdded";
        }

        @Override
        public void onProductRemoved(Product product) {
            wasNotified = true;
            lastEvent = "onProductRemoved";
        }

        @Override
        public void onCartCleared() {
            wasNotified = true;
            lastEvent = "onCartCleared";
        }
    }

    @BeforeEach
    public void setUp() {
        cart = new Cart("CART_TEST_002");
        observer = new TestCartObserver();
        cart.addObserver(observer); // Asumiendo que tu Cart tiene este método
    }

    @Test
    public void testObserver_CarritoAgregaProducto() {
        Product producto = new Product("P004", "Smartphone", "Desc", 800.0, 20, "Electrónica");

        // Act: Agregar producto (asumiendo que tu Cart tiene addItem o similar que dispara el observer)
        cart.addItem(producto, 1);

        // Assert
        assertTrue("El observer debió ser notificado", observer.wasNotified);
        assertEquals("onProductAdded", observer.lastEvent);
    }

    @Test
    public void testObserver_CarritoEliminaProducto() {
        Product producto = new Product("P005", "Tablet", "Desc", 500.0, 15, "Electrónica");
        cart.addItem(producto, 1);
        observer.wasNotified = false; // Resetear

        // Act: Eliminar producto
        cart.removeItem(producto.getId()); // Ajusta el nombre del método si es diferente en tu Cart

        // Assert
        assertTrue("El observer debió ser notificado al eliminar", observer.wasNotified);
        assertEquals("onProductRemoved", observer.lastEvent);
    }
}