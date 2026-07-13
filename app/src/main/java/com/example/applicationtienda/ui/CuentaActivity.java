package com.example.applicationtienda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applicationtienda.R;
import com.google.android.material.textfield.TextInputEditText;

public class CuentaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Quitar la barra de título
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cuenta);

        // Campos
        TextInputEditText etNombre = findViewById(R.id.etNombre);
        TextInputEditText etCorreo = findViewById(R.id.etCorreo);
        TextInputEditText etDireccion = findViewById(R.id.etDireccion);

        // Datos simulados del usuario
        etNombre.setText("Romina Quispe");
        etCorreo.setText("romina@gmail.com");
        etDireccion.setText("Arequipa, Perú");

        // Botones inferiores
        Button btnInicio = findViewById(R.id.btnInicio);
        Button btnCuenta = findViewById(R.id.btnCuenta);
        Button btnCarrito = findViewById(R.id.btnCarrito);

        // Botones principales
        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnMisCompras = findViewById(R.id.btnMisCompras);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnInicio.setOnClickListener(v ->
                startActivity(new Intent(this, ProductosActivity.class)));

        btnCarrito.setOnClickListener(v ->
                startActivity(new Intent(this, CarritoActivity.class)));

        btnCuenta.setOnClickListener(v -> {
            // Ya estamos en esta pantalla
        });

        btnGuardar.setOnClickListener(v ->
                Toast.makeText(this,
                        "Datos guardados correctamente",
                        Toast.LENGTH_SHORT).show());

        btnMisCompras.setOnClickListener(v ->
                startActivity(new Intent(this, MisComprasActivity.class)));

        btnCerrarSesion.setOnClickListener(v -> {
            Toast.makeText(this,
                    "Sesión cerrada",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, ProductosActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}