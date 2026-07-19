package com.example.applicationtienda.infrastructure.persistence;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RoomDatabaseTest {

    private AppDatabase db;
    private ProductDao productDao;
    private OrderDao orderDao;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Usamos una base de datos en memoria para que las pruebas sean rápidas y no dejen basura
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        productDao = db.productDao();
        orderDao = db.orderDao();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testRoom_GuardarYLeerProducto() {
        // CORREGIDO: Usamos los 6 parámetros exactos de tu constructor @Ignore
        ProductEntity producto = new ProductEntity(
                "PROD_001",
                "Laptop Gaming",
                "Descripción de prueba",
                1500.0,
                10,
                "Electrónica"
        );

        // Act: Insertar
        productDao.insertProduct(producto);

        // Act: Leer
        List<ProductEntity> productos = productDao.getAllProducts();

        // Assert
        assertEquals(1, productos.size());
        assertEquals("Laptop Gaming", productos.get(0).getName());
        assertEquals(1500.0, productos.get(0).getPrice(), 0.01);
    }

    @Test
    public void testRoom_GuardarYLeerPedido() {
        // CORREGIDO: Usamos los 6 parámetros exactos de tu constructor @Ignore
        OrderEntity pedido = new OrderEntity(
                "ORD_001",
                "USER_001",
                "2026-07-19",       // fecha (String)
                "Efectivo",         // metodoPago
                1550.0,             // total
                "Pending"           // estado
        );

        // Act: Insertar
        orderDao.insertOrder(pedido);

        // Act: Leer
        List<OrderEntity> pedidos = orderDao.getAllOrders();

        // Assert
        assertEquals(1, pedidos.size());
        assertEquals("ORD_001", pedidos.get(0).getId());
        // CORREGIDO: El método se llama getEstado(), no getStatus()
        assertEquals("Pending", pedidos.get(0).getEstado());
    }
}