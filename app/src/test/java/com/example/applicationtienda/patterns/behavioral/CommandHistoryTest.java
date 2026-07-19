package com.example.applicationtienda.patterns.behavioral;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.applicationtienda.domain.model.Cart;
import com.example.applicationtienda.domain.model.Product;

public class CommandHistoryTest {

    private CommandHistory commandHistory;
    private Cart cart;

    @Before
    public void setUp() {
        commandHistory = new CommandHistory();
        // CORREGIDO: Cart ahora requiere un String (id)
        cart = new Cart("CART_TEST_001");
    }

    @Test
    public void testCommand_UndoEnCarrito() {
        // CORREGIDO: Product ahora requiere 6 parámetros
        Product producto = new Product("P001", "Laptop", "Laptop Gaming", 1500.0, 10, "Electrónica");

        // CORREGIDO: AddToCartCommand recibe (Cart, Product, int)
        Command agregarCommand = new AddToCartCommand(cart, producto, 1);

        // Act: Ejecutar el comando
        commandHistory.executeCommand(agregarCommand);

        // Assert: Verificar que se agregó (asumiendo que Cart tiene getItems())
        assertEquals(1, cart.getItems().size());

        // Act: Deshacer el comando
        commandHistory.undo();

        // Assert: Verificar que se removió
        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void testCommand_MultipleUndo() {
        Product prod1 = new Product("P001", "Laptop", "Desc", 1500.0, 10, "Electrónica");
        Product prod2 = new Product("P002", "Mouse", "Desc", 50.0, 50, "Electrónica");
        Product prod3 = new Product("P003", "Teclado", "Desc", 100.0, 30, "Electrónica");

        commandHistory.executeCommand(new AddToCartCommand(cart, prod1, 1));
        commandHistory.executeCommand(new AddToCartCommand(cart, prod2, 1));
        commandHistory.executeCommand(new AddToCartCommand(cart, prod3, 1));

        assertEquals(3, cart.getItems().size());

        // Act: Deshacer 2 veces
        commandHistory.undo();
        commandHistory.undo();

        // Assert: Debería quedar 1 producto
        assertEquals(1, cart.getItems().size());
    }

    @Test
    public void testCommand_EmptyHistory() {
        // Act & Assert: No debe lanzar excepción al deshacer con historial vacío
        boolean resultado = commandHistory.undo();
        assertFalse("Debería retornar false si no hay nada que deshacer", resultado);
    }
}