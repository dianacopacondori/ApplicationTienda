package com.example.applicationtienda.ui;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtienda.R;
import com.example.applicationtienda.domain.model.Cart;
import com.example.applicationtienda.domain.model.CartItem;
import com.example.applicationtienda.patterns.behavioral.CommandHistory;
import com.example.applicationtienda.patterns.behavioral.RemoveFromCartCommand;
import com.example.applicationtienda.patterns.creational.CartManager;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView rvCarrito;
    private TextView tvTotal;
    private CommandHistory commandHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Quitar la barra de título
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_carrito);

        rvCarrito = findViewById(R.id.rvCarrito);
        tvTotal = findViewById(R.id.tvTotal);
        Button btnComprar = findViewById(R.id.btnComprar);
        Button btnDeshacer = findViewById(R.id.btnDeshacer); // Agrega este botón en el layout

        rvCarrito.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar historial de comandos (Patrón Command)
        commandHistory = new CommandHistory();

        Cart carrito = CartManager.getInstance().getCart();

        // Adapter con listener para eliminar items
        CarritoRecyclerAdapter adapter = new CarritoRecyclerAdapter(
                carrito.getItems(),
                item -> {
                    // Crear comando para eliminar (Patrón Command)
                    RemoveFromCartCommand command = new RemoveFromCartCommand(carrito, item.getProduct());
                    commandHistory.executeCommand(command);
                    actualizarVista();
                }
        );

        rvCarrito.setAdapter(adapter);
        actualizarVista();

        // Botón comprar
        btnComprar.setOnClickListener(v -> {
            if (carrito.getItems().isEmpty()) {
                Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, CheckoutActivity.class));
            }
        });
            // Botón de volver
            Button btnSeguirComprando = findViewById(R.id.btnSeguirComprando);
            btnSeguirComprando.setOnClickListener(v -> {
                // Volver a ProductosActivity
                Intent intent = new Intent(this, ProductosActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            });

        // Botón deshacer (Patrón Command)
        if (btnDeshacer != null) {
            btnDeshacer.setOnClickListener(v -> {
                if (commandHistory.undo()) {
                    actualizarVista();
                    Toast.makeText(this, "Acción deshecha", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No hay acciones para deshacer", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarVista();
    }

    private void actualizarVista() {
        Cart carrito = CartManager.getInstance().getCart();
        tvTotal.setText("Total: S/. " + String.format("%.2f", carrito.getTotal()));

        // Actualizar adapter
        CarritoRecyclerAdapter adapter = (CarritoRecyclerAdapter) rvCarrito.getAdapter();
        if (adapter != null) {
            adapter.actualizarItems(carrito.getItems());
        }
    }
}