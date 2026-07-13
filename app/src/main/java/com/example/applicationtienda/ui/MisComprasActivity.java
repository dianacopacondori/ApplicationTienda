package com.example.applicationtienda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtienda.R;
import com.example.applicationtienda.infrastructure.persistence.AppDatabase;
import com.example.applicationtienda.infrastructure.persistence.OrderEntity;

import java.util.List;

public class MisComprasActivity extends AppCompatActivity {

    private RecyclerView rvPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Quitar la barra de título
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mis_compras);

        rvPedidos = findViewById(R.id.rvPedidos);
        rvPedidos.setLayoutManager(new LinearLayoutManager(this));

        cargarPedidos();

        Button btnInicio = findViewById(R.id.btnInicio);
        Button btnCuenta = findViewById(R.id.btnCuenta);
        Button btnMisCompras = findViewById(R.id.btnMisCompras);

        btnInicio.setOnClickListener(v ->
                startActivity(new Intent(this, ProductosActivity.class)));

        btnCuenta.setOnClickListener(v ->
                startActivity(new Intent(this, CuentaActivity.class)));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarPedidos() {

        new Thread(() -> {

            List<OrderEntity> pedidos =
                    AppDatabase.getInstance(this)
                            .orderDao()
                            .getAllOrders();

            runOnUiThread(() -> {

                PedidoRecyclerAdapter adapter =
                        new PedidoRecyclerAdapter(pedidos, pedido -> {

                            Intent intent = new Intent(
                                    MisComprasActivity.this,
                                    DetallePedidoActivity.class);

                            intent.putExtra("ORDER_ID", pedido.getId());

                            startActivity(intent);

                        });

                rvPedidos.setAdapter(adapter);

            });

        }).start();
    }
}
