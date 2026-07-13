package com.example.applicationtienda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtienda.R;
import com.example.applicationtienda.domain.model.Product;
import com.example.applicationtienda.infrastructure.persistence.RoomProductRepository;
import com.example.applicationtienda.patterns.creational.CartManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductosActivity extends AppCompatActivity {

    private RecyclerView rvProductos;
    private EditText etBuscar;
    private RoomProductRepository repository;
    private List<Product> todosLosProductos = new ArrayList<>();
    private ProductoReciclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
            // Quitar la barra de título
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

        setContentView(R.layout.activity_productos);

        // Referencias
        rvProductos = findViewById(R.id.rvProductos);
        etBuscar = findViewById(R.id.etBuscar);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));

        // Repositorio
        repository = new RoomProductRepository(this);

        // Cargar productos desde Room
        new Thread(() -> {
            if (repository.isEmpty()) {
                repository.addSampleProducts();
            }

            todosLosProductos = repository.getAllProducts();

            runOnUiThread(() -> {
                adapter = new ProductoReciclerAdapter(todosLosProductos, producto -> {
                    CartManager.getInstance().agregarProducto(producto);
                    Toast.makeText(
                            ProductosActivity.this,
                            producto.getName() + " agregado al carrito",
                            Toast.LENGTH_SHORT
                    ).show();
                });

                rvProductos.setAdapter(adapter);
            });
        }).start();

        // Búsqueda en tiempo real
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarProductos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Botones de navegación
        Button btnInicio = findViewById(R.id.btnInicio);
        Button btnCuenta = findViewById(R.id.btnCuenta);
        Button btnCarrito = findViewById(R.id.btnCarrito);

        btnInicio.setOnClickListener(v -> {
            // Ya estamos en Inicio
        });

        btnCuenta.setOnClickListener(v ->
                startActivity(new Intent(this, CuentaActivity.class)));

        btnCarrito.setOnClickListener(v ->
                startActivity(new Intent(this, CarritoActivity.class)));
    }

    // Método para filtrar productos
    private void filtrarProductos(String texto) {
        if (adapter == null) return;

        List<Product> productosFiltrados;

        if (texto.isEmpty()) {
            productosFiltrados = todosLosProductos;
        } else {
            String textoLower = texto.toLowerCase();
            productosFiltrados = todosLosProductos.stream()
                    .filter(p -> p.getName().toLowerCase().contains(textoLower) ||
                            p.getCategory().toLowerCase().contains(textoLower))
                    .collect(Collectors.toList());
        }

        adapter.actualizarProductos(productosFiltrados);
    }
}
