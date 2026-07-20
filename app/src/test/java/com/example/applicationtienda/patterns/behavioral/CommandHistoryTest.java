package com.example.applicationtienda.patterns.behavioral;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.applicationtienda.domain.model.Cart;
import com.example.applicationtienda.domain.model.Product;

public class CommandHistoryTest {

    private CommandHistory commandHistory;
    private Cart cart;

    @BeforeEach // <--- ¡Cambiado de @Before a @BeforeEach!
    public void setUp() {
        commandHistory = new CommandHistory();
        cart = new Cart("CART_TEST_001");
    }

    @Test
    public void testCommand_UndoEnCarrito() {
        Product producto = new Product("P001", "Laptop", "Laptop Gaming", 1500.0, 10, "Electrónica");

        Command agregarCommand = new AddToCartCommand(cart, producto, 1);

        commandHistory.executeCommand(agregarCommand);

        assertEquals(1, cart.getItems().size(), "El carrito debe tener 1 item");

        commandHistory.undo();

        assertEquals(0, cart.getItems().size(), "El carrito debe estar vacío tras el undo");
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

        commandHistory.undo();
        commandHistory.undo();

        assertEquals(1, cart.getItems().size());
    }

    @Test
    public void testCommand_EmptyHistory() {
        boolean resultado = commandHistory.undo();
        assertFalse(resultado, "Debería retornar false si no hay nada que deshacer");
    }
}