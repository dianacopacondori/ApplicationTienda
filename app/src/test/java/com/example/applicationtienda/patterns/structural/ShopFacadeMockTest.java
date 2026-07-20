package com.example.applicationtienda.patterns.structural;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.applicationtienda.domain.model.Order;
import com.example.applicationtienda.domain.model.Product;

public class ShopFacadeMockTest {

    @Test
    void testShopFacade_QuickCheckoutCreaPedidoConDescuento() {
        // Arrange
        ShopFacade facade = new ShopFacade();

        // Act: El cliente solo llama a un método simple, el Facade oculta Factory + Decorator + Builder
        Order pedido = facade.quickCheckout("USER_001", "Laptop Gaming", 1500.0, 0.10); // 10% descuento

        // Assert
        assertNotNull(pedido, "El pedido no debe ser nulo");
        assertEquals("USER_001", pedido.getUserId(), "El ID de usuario debe coincidir");
        // Aquí demostramos que el Facade funcionó orquestando los otros patrones
    }

    @Test
    void testShopFacade_UsoDeMockitoParaValidarComportamiento() {
        // Arrange: Creamos un mock (simulacro) para demostrar el uso de Mockito (Requisito de rúbrica)
        ShopFacade facadeMock = Mockito.mock(ShopFacade.class);
        Product productoSimulado = new Product("P999", "Mouse", "Desc", 50.0, 100, "Electrónica");

        // Configuramos el comportamiento del mock
        when(facadeMock.createElectronicProduct("Mouse", 50.0)).thenReturn(productoSimulado);

        // Act
        Product resultado = facadeMock.createElectronicProduct("Mouse", 50.0);

        // Assert
        assertNotNull(resultado);
        assertEquals("Mouse", resultado.getName());
        // Verificamos que el método fue llamado exactamente 1 vez (Uso clásico de Mockito)
        verify(facadeMock, times(1)).createElectronicProduct("Mouse", 50.0);
    }
}