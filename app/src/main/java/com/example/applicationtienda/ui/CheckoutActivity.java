package com.example.applicationtienda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtienda.R;
import com.example.applicationtienda.domain.model.Cart;
import com.example.applicationtienda.domain.model.CartItem;
import com.example.applicationtienda.domain.model.Order;
import com.example.applicationtienda.domain.model.Product;
import com.example.applicationtienda.infrastructure.persistence.AppDatabase;
import com.example.applicationtienda.infrastructure.persistence.RoomOrderRepository;
import com.example.applicationtienda.patterns.creational.CartManager;
import com.example.applicationtienda.patterns.creational.OrderBuilder;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private LinearLayout opcionEfectivo, opcionTarjeta, opcionYape;
    private String metodoPagoSeleccionado = "Efectivo";

    // Views para mostrar datos reales
    private RecyclerView rvCheckoutItems;
    private TextView tvTotalItems;
    private TextView tvSubtotal;
    private TextView tvDelivery;
    private TextView tvTotalPagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_checkout);

        // Inicializar vistas
        inicializarVistas();

        // Cargar datos del carrito
        cargarDatosCarrito();

        // Métodos de pago
        opcionEfectivo = findViewById(R.id.opcionEfectivo);
        opcionTarjeta = findViewById(R.id.opcionTarjeta);
        opcionYape = findViewById(R.id.opcionYape);
        Button btnFinalizar = findViewById(R.id.btnFinalizarCompra);

        opcionEfectivo.setOnClickListener(v -> seleccionarMetodo("Efectivo"));
        opcionTarjeta.setOnClickListener(v -> seleccionarMetodo("Tarjeta"));
        opcionYape.setOnClickListener(v -> seleccionarMetodo("Yape"));

        btnFinalizar.setOnClickListener(v -> procesarCompra());
    }

    private void inicializarVistas() {
        rvCheckoutItems = findViewById(R.id.rvCheckoutItems);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDelivery = findViewById(R.id.tvDelivery);
        tvTotalPagar = findViewById(R.id.tvTotalPagar);

        rvCheckoutItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void cargarDatosCarrito() {
        Cart carrito = CartManager.getInstance().getCart();
        List<CartItem> items = carrito.getItems();

        if (items.isEmpty()) {
            Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configurar adapter del RecyclerView
        CheckoutAdapter adapter = new CheckoutAdapter(items);
        rvCheckoutItems.setAdapter(adapter);

        // Calcular totales
        double subtotal = carrito.getTotal();
        double delivery = 15.00; // Delivery fijo
        double total = subtotal + delivery;
        int totalItems = items.stream().mapToInt(CartItem::getQuantity).sum();

        // Mostrar datos en pantalla
        tvTotalItems.setText(String.valueOf(totalItems));
        tvSubtotal.setText("S/. " + String.format("%.2f", subtotal));
        tvDelivery.setText("S/. " + String.format("%.2f", delivery));
        tvTotalPagar.setText("S/. " + String.format("%.2f", total));
    }

    private void procesarCompra() {
        Cart carrito = CartManager.getInstance().getCart();

        if (carrito.getItems().isEmpty()) {
            Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                // Crear pedido usando Builder (Patrón Builder)
                OrderBuilder builder = new OrderBuilder();
                builder.setUserId("USER_001");

                List<Product> productos = carrito.getItems().stream()
                        .map(item -> item.getProduct())
                        .toList();

                for (Product producto : productos) {
                    builder.addProduct(producto);
                }

                Order pedido = builder.build();

                // Procesar pedido usando State (Patrón State)
                pedido.process(); // Pendiente -> Procesando
                pedido.ship();    // Procesando -> Enviado

                // Guardar en base de datos
                AppDatabase db = AppDatabase.getInstance(this);
                RoomOrderRepository repository = new RoomOrderRepository(db);

                repository.guardarPedido(
                        "USER_001",
                        metodoPagoSeleccionado,
                        carrito.getItems(),
                        carrito.getTotal()
                );

                runOnUiThread(() -> {
                    Toast.makeText(
                            this,
                            "Compra realizada correctamente.\nMétodo de pago: " + metodoPagoSeleccionado +
                                    "\nEstado: " + pedido.getStatus(),
                            Toast.LENGTH_LONG
                    ).show();

                    // Limpiar carrito
                    CartManager.getInstance().limpiarCarrito();

                    // Ir a MisComprasActivity
                    Intent intent = new Intent(this, MisComprasActivity.class);
                    intent.putExtra("COMPRA_REALIZADA", true);
                    startActivity(intent);
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void seleccionarMetodo(String metodo) {
        metodoPagoSeleccionado = metodo;

        opcionEfectivo.setBackgroundResource(R.drawable.bg_metodo_pago_unselected);
        opcionTarjeta.setBackgroundResource(R.drawable.bg_metodo_pago_unselected);
        opcionYape.setBackgroundResource(R.drawable.bg_metodo_pago_unselected);

        switch (metodo) {
            case "Efectivo":
                opcionEfectivo.setBackgroundResource(R.drawable.bg_metodo_pago_selected);
                break;
            case "Tarjeta":
                opcionTarjeta.setBackgroundResource(R.drawable.bg_metodo_pago_selected);
                break;
            case "Yape":
                opcionYape.setBackgroundResource(R.drawable.bg_metodo_pago_selected);
                break;
        }
    }
}