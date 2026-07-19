package com.example.applicationtienda;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtienda.domain.model.Order;
import com.example.applicationtienda.domain.model.Product;
import com.example.applicationtienda.patterns.structural.ShopFacade;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Etiqueta para filtrar los mensajes en el Logcat
    private static final String TAG = "TiendaVirtual";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ejecutamos las pruebas de los patrones
        probarPatrones();
        probarCommandYState();
        probarRoomDatabase();
    }

    private void probarPatrones() {
        try {
            ShopFacade shop = new ShopFacade();

            Log.d(TAG, "========== PRUEBA 1: CREACIONALES + ESTRUCTURALES ==========");
            Product laptop = shop.createElectronicProduct("Laptop Gamer", 1200.00);
            Product camisa = shop.createClothingProduct("Camisa Polo", 25.00);
            Product laptopOferta = shop.createDiscountedProduct("Laptop Gamer", 1200.00, 20.0);
            Order pedido = shop.createOrder("USER_123", laptop, camisa);

            Log.d(TAG, "Producto: " + laptop.getName() + " | $" + laptop.getPrice());
            Log.d(TAG, "Producto Oferta: " + laptopOferta.getName() + " | $" + laptopOferta.getPrice());
            Log.d(TAG, "Pedido: " + pedido.toString());

            Log.d(TAG, "\n========== PRUEBA 3: OBSERVER (Carrito con notificaciones) ==========");
            com.example.applicationtienda.domain.model.Cart cart =
                    new com.example.applicationtienda.domain.model.Cart("CART_001");

            // Registrar observadores
            cart.addObserver(new com.example.applicationtienda.patterns.behavioral.NotificationObserver());
            cart.addObserver(new com.example.applicationtienda.patterns.behavioral.InventoryObserver());

            // Operaciones que disparan notificaciones
            cart.addItem(laptop, 1);
            cart.addItem(camisa, 2);
            Log.d(TAG, "Total del carrito: $" + cart.getTotal());

            cart.removeItem(camisa.getId());
            Log.d(TAG, "Total después de quitar camisa: $" + cart.getTotal());

            cart.clear();
            Log.d(TAG, "\n========== TODAS LAS PRUEBAS COMPLETADAS ==========");

            Toast.makeText(this, "¡Patrones ejecutados! Revisa Logcat", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void probarCommandYState() {
        try {
            Log.d(TAG, "\n========== PRUEBA 4: COMMAND (Undo/Redo) ==========");

            ShopFacade shop = new ShopFacade();
            Product laptop = shop.createElectronicProduct("Laptop Gamer", 1200.00);
            Product camisa = shop.createClothingProduct("Camisa Polo", 25.00);

            com.example.applicationtienda.domain.model.Cart cart =
                    new com.example.applicationtienda.domain.model.Cart("CART_002");

            com.example.applicationtienda.patterns.behavioral.CommandHistory history =
                    new com.example.applicationtienda.patterns.behavioral.CommandHistory();

            // Ejecutar comandos
            history.executeCommand(new com.example.applicationtienda.patterns.behavioral.AddToCartCommand(cart, laptop, 1));
            history.executeCommand(new com.example.applicationtienda.patterns.behavioral.AddToCartCommand(cart, camisa, 2));
            Log.d(TAG, "Carrito después de agregar: $" + cart.getTotal());

            // Deshacer último comando
            history.undo();
            Log.d(TAG, "Carrito después de UNDO: $" + cart.getTotal());

            // Rehacer
            history.redo();
            Log.d(TAG, "Carrito después de REDO: $" + cart.getTotal());

            history.printHistory();

            Log.d(TAG, "\n========== PRUEBA 5: STATE (Máquina de estados del pedido) ==========");

            Order pedido = shop.createOrder("USER_789", laptop);
            Log.d(TAG, "Estado inicial: " + pedido.getStatus());

            pedido.process();
            Log.d(TAG, "Estado después de process(): " + pedido.getStatus());

            pedido.ship();
            Log.d(TAG, "Estado después de ship(): " + pedido.getStatus());

            pedido.deliver();
            Log.d(TAG, "Estado después de deliver(): " + pedido.getStatus());

            // Intentar cancelar un pedido entregado (no debería permitirlo)
            pedido.cancel();
            Log.d(TAG, "Estado final: " + pedido.getStatus());

            Log.d(TAG, "\n========== PRUEBA 6: STATE (Cancelar pedido) ==========");
            Order pedido2 = shop.createOrder("USER_999", camisa);
            Log.d(TAG, "Estado inicial: " + pedido2.getStatus());

            pedido2.cancel();
            Log.d(TAG, "Estado después de cancel(): " + pedido2.getStatus());

            // Intentar procesar un pedido cancelado (no debería permitirlo)
            pedido2.process();
            Log.d(TAG, "Estado final: " + pedido2.getStatus());

            Log.d(TAG, "\n========== TODOS LOS PATRONES COMPLETADOS ==========");

            Toast.makeText(this, "¡Todos los patrones funcionan! Revisa Logcat", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void probarRoomDatabase() {
        new Thread(() -> {
            try {
                Log.d(TAG, "\n========== PRUEBA 7: ROOM DATABASE ==========");

                com.example.applicationtienda.infrastructure.persistence.RoomProductRepository roomRepo =
                        new com.example.applicationtienda.infrastructure.persistence.RoomProductRepository(this);

                roomRepo.addSampleProducts();

                List<Product> productos = roomRepo.getAllProducts();
                Log.d(TAG, "Total de productos en BD: " + productos.size());

                for (Product p : productos) {
                    Log.d(TAG, "- " + p.getName() + " | $" + p.getPrice() + " | Stock: " + p.getStock());
                }

                runOnUiThread(() -> {
                    Toast.makeText(this, "¡Room Database funcionando! Revisa Logcat", Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                Log.e(TAG, "Error en Room: " + e.getMessage(), e);
            }
        }).start();
    }
}

