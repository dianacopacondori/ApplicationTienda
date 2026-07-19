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
import com.example.applicationtienda.patterns.behavioral.CommandHistory;
import com.example.applicationtienda.patterns.creational.CartManager;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView rvCarrito;
    private TextView tvTotal;
    private CommandHistory commandHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_carrito);

        rvCarrito = findViewById(R.id.rvCarrito);
        tvTotal = findViewById(R.id.tvTotal);
        Button btnComprar = findViewById(R.id.btnComprar);
        Button btnDeshacer = findViewById(R.id.btnDeshacer);
        Button btnSeguirComprando = findViewById(R.id.btnSeguirComprando);

        rvCarrito.setLayoutManager(new LinearLayoutManager(this));

        // CORRECCIÓN CRÍTICA: Usar el historial del Singleton, NO crear uno nuevo.
        // Así garantizamos que el botón "Deshacer" lea las acciones que el Adapter guardó.
        commandHistory = CartManager.getInstance().getCommandHistory();
        Cart carrito = CartManager.getInstance().getCart();

        // El Adapter ahora solo notifica que debe actualizarse la vista.
        CarritoRecyclerAdapter adapter = new CarritoRecyclerAdapter(
                carrito.getItems(),
                new CarritoRecyclerAdapter.OnCarritoChangeListener() {
                    @Override
                    public void onCarritoActualizado() {
                        actualizarVista();
                    }
                }
        );

        rvCarrito.setAdapter(adapter);
        actualizarVista();

        btnComprar.setOnClickListener(v -> {
            if (carrito.getItems().isEmpty()) {
                Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, CheckoutActivity.class));
            }
        });

        btnSeguirComprando.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductosActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Botón deshacer (usa el MISMO historial que el Adapter)
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

        CarritoRecyclerAdapter adapter = (CarritoRecyclerAdapter) rvCarrito.getAdapter();
        if (adapter != null) {
            adapter.actualizarItems(carrito.getItems());
        }
    }
}