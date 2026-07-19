package com.example.applicationtienda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private LinearLayout opcionEfectivo, opcionTarjeta, opcionYape;
    private String metodoPagoSeleccionado = "Efectivo";

    // Formulario de tarjeta
    private MaterialCardView cardDatosTarjeta;
    private TextInputEditText etNumeroTarjeta, etNombreTitular, etFechaVencimiento, etCVV;
    //Formulario de Yape
    private MaterialCardView cardDatosYape;
    private TextInputEditText etCelularYape, etNombreYape;
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

        // 1. Declarar ambos botones
        Button btnVolverCarrito = findViewById(R.id.btnVolverCarrito);
        Button btnFinalizar = findViewById(R.id.btnFinalizarCompra);

        opcionEfectivo.setOnClickListener(v -> seleccionarMetodo("Efectivo"));
        opcionTarjeta.setOnClickListener(v -> seleccionarMetodo("Tarjeta"));
        opcionYape.setOnClickListener(v -> seleccionarMetodo("Yape"));

        // 2. Lógica del botón VOLVER (Método infalible)
        if (btnVolverCarrito != null) {
            btnVolverCarrito.setOnClickListener(v -> {
                // Opción A: Intenta cerrar la pantalla actual (lo normal)
                // finish();

                // Opción B: Fuerza la apertura del Carrito y limpia el historial superior
                Intent intent = new Intent(CheckoutActivity.this, CarritoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Cierra el checkout para que no se acumule
            });
        } else {
            // Si ves este Toast, significa que el XML no tiene el ID correcto
            Toast.makeText(this, "Error: No se encontró el botón btnVolverCarrito", Toast.LENGTH_LONG).show();
        }

        // 3. Lógica del botón FINALIZAR
        btnFinalizar.setOnClickListener(v -> procesarCompra());
    }

    private void inicializarVistas() {
        rvCheckoutItems = findViewById(R.id.rvCheckoutItems);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDelivery = findViewById(R.id.tvDelivery);
        tvTotalPagar = findViewById(R.id.tvTotalPagar);

        // Formulario de tarjeta
        cardDatosTarjeta = findViewById(R.id.cardDatosTarjeta);
        etNumeroTarjeta = findViewById(R.id.etNumeroTarjeta);
        etNombreTitular = findViewById(R.id.etNombreTitular);
        etFechaVencimiento = findViewById(R.id.etFechaVencimiento);
        etCVV = findViewById(R.id.etCVV);

        //Formulario de Yape
        cardDatosYape = findViewById(R.id.cardDatosYape);
        etCelularYape = findViewById(R.id.etCelularYape);
        etNombreYape = findViewById(R.id.etNombreYape);

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
        double delivery = 15.00;
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

        // Validar según el método de pago seleccionado
        if (metodoPagoSeleccionado.equals("Tarjeta")) {
            if (!validarDatosTarjeta()) {
                return;
            }
        } else if (metodoPagoSeleccionado.equals("Yape")) {
            if (!validarDatosYape()) {
                return;
            }
        }

        // Si todo está OK, procesar la compra
        new Thread(() -> {
            try {
                double subtotal = carrito.getTotal();
                double delivery = 15.00;
                double totalFinal = subtotal + delivery;

                OrderBuilder builder = new OrderBuilder();
                builder.setUserId("USER_001");

                List<Product> productos = carrito.getItems().stream()
                        .map(item -> item.getProduct())
                        .toList();

                for (Product producto : productos) {
                    builder.addProduct(producto);
                }

                Order pedido = builder.build();
                pedido.process();
                pedido.ship();

                AppDatabase db = AppDatabase.getInstance(this);
                RoomOrderRepository repository = new RoomOrderRepository(db);

                repository.guardarPedido(
                        "USER_001",
                        metodoPagoSeleccionado,
                        carrito.getItems(),
                        totalFinal
                );

                runOnUiThread(() -> {
                    Toast.makeText(
                            this,
                            "Compra realizada correctamente.\nMétodo: " + metodoPagoSeleccionado,
                            Toast.LENGTH_LONG
                    ).show();

                    CartManager.getInstance().limpiarCarrito();

                    Intent intent = new Intent(this, MisComprasActivity.class);
                    intent.putExtra("COMPRA_REALIZADA", true);
                    startActivity(intent);
                    finish();
                });

            } catch (Exception e) {
                Log.e("CheckoutError", "Error al procesar la compra: " + e.getMessage(), e);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void seleccionarMetodo(String metodo) {
        metodoPagoSeleccionado = metodo;

        // Resetear fondos
        opcionEfectivo.setBackgroundResource(R.drawable.bg_metodo_pago_unselected);
        opcionTarjeta.setBackgroundResource(R.drawable.bg_metodo_pago_unselected);
        opcionYape.setBackgroundResource(R.drawable.bg_metodo_pago_unselected);

        // Resaltar el seleccionado
        switch (metodo) {
            case "Efectivo":
                opcionEfectivo.setBackgroundResource(R.drawable.bg_metodo_pago_selected);
                cardDatosTarjeta.setVisibility(View.GONE); // Ocultar formulario Tarjeta
                cardDatosYape.setVisibility(View.GONE); //Ocultar formulario Yape
                break;
            case "Tarjeta":
                opcionTarjeta.setBackgroundResource(R.drawable.bg_metodo_pago_selected);
                cardDatosTarjeta.setVisibility(View.VISIBLE); // Mostrar formulario
                cardDatosYape.setVisibility(View.GONE); //Ocultar formulario Yape
                break;
            case "Yape":
                opcionYape.setBackgroundResource(R.drawable.bg_metodo_pago_selected);
                cardDatosTarjeta.setVisibility(View.GONE); //Ocultar formulario tarjeta
                cardDatosYape.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean validarDatosTarjeta() {
        String numero = etNumeroTarjeta.getText().toString().trim();
        String nombre = etNombreTitular.getText().toString().trim();
        String fecha = etFechaVencimiento.getText().toString().trim();
        String cvv = etCVV.getText().toString().trim();

        if (numero.isEmpty() || numero.length() != 16) {
            Toast.makeText(this, "Ingresa un número de tarjeta válido (16 dígitos)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingresa el nombre del titular", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (fecha.isEmpty() || !fecha.contains("/")) {
            Toast.makeText(this, "Ingresa una fecha de vencimiento válida (MM/AA)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cvv.isEmpty() || cvv.length() != 3) {
            Toast.makeText(this, "Ingresa un CVV válido (3 dígitos)", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private boolean validarDatosYape() {
        String celular = etCelularYape.getText().toString().trim();
        String nombre = etNombreYape.getText().toString().trim();

        if (celular.isEmpty() || celular.length() < 9) {
            Toast.makeText(this, "⚠Ingresa un número de celular válido (9 dígitos)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nombre.isEmpty()) {
            Toast.makeText(this, "⚠Ingresa el nombre del titular", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}