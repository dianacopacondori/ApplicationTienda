package com.example.applicationtienda;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtienda.domain.model.Order;
import com.example.applicationtienda.domain.model.Product;
import com.example.applicationtienda.patterns.structural.ShopFacade;

public class MainActivity extends AppCompatActivity {

    // Etiqueta para filtrar los mensajes en el Logcat
    private static final String TAG = "TiendaVirtual";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ejecutamos las pruebas de los patrones
        probarPatrones();
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

            Log.d(TAG, "\n========== PRUEBA 2: STRATEGY (Estrategias de precios) ==========");
            com.example.applicationtienda.domain.services.PricingService pricingService =
                    new com.example.applicationtienda.domain.services.PricingService(
                            new com.example.applicationtienda.patterns.behavioral.StandardPricing()
                    );

            Log.d(TAG, "Precio Estándar: $" + pricingService.calculateFinalPrice(laptop) +
                    " [" + pricingService.getCurrentStrategyName() + "]");

            pricingService.setStrategy(new com.example.applicationtienda.patterns.behavioral.PremiumPricing());
            Log.d(TAG, "Precio Premium: $" + pricingService.calculateFinalPrice(laptop) +
                    " [" + pricingService.getCurrentStrategyName() + "]");

            pricingService.setStrategy(new com.example.applicationtienda.patterns.behavioral.SeasonalPricing());
            Log.d(TAG, "Precio Temporada: $" + pricingService.calculateFinalPrice(laptop) +
                    " [" + pricingService.getCurrentStrategyName() + "]");

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
}

