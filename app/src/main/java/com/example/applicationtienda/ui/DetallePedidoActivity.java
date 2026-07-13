package com.example.applicationtienda.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtienda.R;
import com.example.applicationtienda.infrastructure.persistence.AppDatabase;
import com.example.applicationtienda.infrastructure.persistence.OrderEntity;
import com.example.applicationtienda.infrastructure.persistence.OrderItemEntity;

import java.util.List;

public class DetallePedidoActivity extends AppCompatActivity {

    private RecyclerView rvDetalleItems;
    private TextView tvNumeroPedido;
    private TextView tvFechaEmision;
    private TextView tvTotalBoleta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_detalle_pedido);

        rvDetalleItems = findViewById(R.id.rvDetalleItems);
        tvNumeroPedido = findViewById(R.id.tvNumeroPedido);
        tvFechaEmision = findViewById(R.id.tvFechaEmision);
        tvTotalBoleta = findViewById(R.id.tvTotalBoleta);

        ImageButton btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        Button btnDescargar = findViewById(R.id.btnDescargarBoleta);
        btnDescargar.setOnClickListener(v ->
                Toast.makeText(this, "Boleta descargada", Toast.LENGTH_SHORT).show());

        String orderId = getIntent().getStringExtra("ORDER_ID");
        cargarPedido(orderId);
    }

    private void cargarPedido(String orderId) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            OrderEntity order = db.orderDao().getOrderById(orderId);
            List<OrderItemEntity> items = db.orderItemDao().getItemsByOrder(orderId);

            runOnUiThread(() -> {
                if (order != null) {
                    tvNumeroPedido.setText(order.getId().substring(0, 8));
                    tvFechaEmision.setText(order.getFecha());
                    tvTotalBoleta.setText("S/. " + String.format("%.2f", order.getTotal()));
                }

                if (items != null && !items.isEmpty()) {
                    rvDetalleItems.setLayoutManager(new LinearLayoutManager(this));
                    DetallePedidoAdapter adapter = new DetallePedidoAdapter(items);
                    rvDetalleItems.setAdapter(adapter);
                }
            });
        }).start();
    }
}
