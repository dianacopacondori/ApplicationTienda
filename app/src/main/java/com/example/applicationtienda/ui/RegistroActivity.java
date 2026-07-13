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

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Quitar la barra de título
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        Button btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        btnCrearCuenta.setOnClickListener(v -> {
            // Aquí va la lógica para validar y crear el usuario
            Toast.makeText(this, "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show();

            // Ir a ProductosActivity (Home)
            Intent intent = new Intent(this, ProductosActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
