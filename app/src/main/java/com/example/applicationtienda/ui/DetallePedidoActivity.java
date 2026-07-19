package com.example.applicationtienda.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetallePedidoActivity extends AppCompatActivity {

    private RecyclerView rvDetalleItems;
    private TextView tvNumeroPedido;
    private TextView tvFechaEmision;
    private TextView tvTotalBoleta;

    // Views del timeline
    private View circleConfirmado, circleProceso, circleEnvio, circleEntregado;
    private View line1, line2, line3;
    private TextView tvFechaConfirmado, tvFechaProceso, tvFechaEnvio, tvFechaEntregado;
    private TextView tvTextoEnvio, tvTextoEntregado;

    private Button btnSimularAvance;
    private String orderId;
    private String estadoActual;

    // NUEVO: Handler para la actualización automática cada 30 segundos
    private Handler handler;
    private Runnable runnableAvance;
    private static final long INTERVALO_MS = 30000; // 30 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_detalle_pedido);

        inicializarVistas();

        ImageButton btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        Button btnDescargar = findViewById(R.id.btnDescargarBoleta);
        btnDescargar.setOnClickListener(v ->
                Toast.makeText(this, "Boleta descargada", Toast.LENGTH_SHORT).show());

        btnSimularAvance = findViewById(R.id.btnSimularAvance);
        btnSimularAvance.setOnClickListener(v -> avanzarEstadoManual());

        orderId = getIntent().getStringExtra("ORDER_ID");
        cargarPedido(orderId);

        // Iniciar actualización automática
        iniciarActualizacionAutomatica();
    }

    private void inicializarVistas() {
        rvDetalleItems = findViewById(R.id.rvDetalleItems);
        tvNumeroPedido = findViewById(R.id.tvNumeroPedido);
        tvFechaEmision = findViewById(R.id.tvFechaEmision);
        tvTotalBoleta = findViewById(R.id.tvTotalBoleta);

        circleConfirmado = findViewById(R.id.circleConfirmado);
        circleProceso = findViewById(R.id.circleProceso);
        circleEnvio = findViewById(R.id.circleEnvio);
        circleEntregado = findViewById(R.id.circleEntregado);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);

        tvFechaConfirmado = findViewById(R.id.tvFechaConfirmado);
        tvFechaProceso = findViewById(R.id.tvFechaProceso);
        tvFechaEnvio = findViewById(R.id.tvFechaEnvio);
        tvFechaEntregado = findViewById(R.id.tvFechaEntregado);

        tvTextoEnvio = findViewById(R.id.tvTextoEnvio);
        tvTextoEntregado = findViewById(R.id.tvTextoEntregado);
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
                    estadoActual = order.getEstado();
                    actualizarTimeline(estadoActual);
                }

                if (items != null && !items.isEmpty()) {
                    rvDetalleItems.setLayoutManager(new LinearLayoutManager(this));
                    DetallePedidoAdapter adapter = new DetallePedidoAdapter(items);
                    rvDetalleItems.setAdapter(adapter);
                }
            });
        }).start();
    }

    // NUEVO: Iniciar actualización automática cada 30 segundos
    private void iniciarActualizacionAutomatica() {
        handler = new Handler(Looper.getMainLooper());
        runnableAvance = new Runnable() {
            @Override
            public void run() {
                avanzarEstadoAutomatico();
                // Programar la siguiente ejecución
                handler.postDelayed(this, INTERVALO_MS);
            }
        };
        // Iniciar después de 30 segundos
        handler.postDelayed(runnableAvance, INTERVALO_MS);
    }

    // NUEVO: Avance automático (con notificación al usuario)
    private void avanzarEstadoAutomatico() {
        if (estadoActual == null || estadoActual.equals("ENTREGADO")) {
            detenerActualizacionAutomatica();
            return;
        }

        String nuevoEstado;
        switch (estadoActual) {
            case "PENDIENTE":
                nuevoEstado = "PROCESANDO";
                break;
            case "PROCESANDO":
                nuevoEstado = "ENVIADO";
                break;
            case "ENVIADO":
                nuevoEstado = "ENTREGADO";
                break;
            default:
                return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            db.orderDao().updateOrderState(orderId, nuevoEstado);

            runOnUiThread(() -> {
                estadoActual = nuevoEstado;
                actualizarTimeline(nuevoEstado);
                Toast.makeText(this, "Estado actualizado: " + nuevoEstado, Toast.LENGTH_SHORT).show();

                if (nuevoEstado.equals("ENTREGADO")) {
                    detenerActualizacionAutomatica();
                }
            });
        }).start();
    }

    // Avance manual (cuando el usuario presiona el botón)
    private void avanzarEstadoManual() {
        if (estadoActual == null) return;

        String nuevoEstado;
        switch (estadoActual) {
            case "PENDIENTE":
                nuevoEstado = "PROCESANDO";
                break;
            case "PROCESANDO":
                nuevoEstado = "ENVIADO";
                break;
            case "ENVIADO":
                nuevoEstado = "ENTREGADO";
                break;
            case "ENTREGADO":
                Toast.makeText(this, "El pedido ya fue entregado", Toast.LENGTH_SHORT).show();
                return;
            default:
                nuevoEstado = "PROCESANDO";
                break;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            db.orderDao().updateOrderState(orderId, nuevoEstado);

            runOnUiThread(() -> {
                estadoActual = nuevoEstado;
                actualizarTimeline(nuevoEstado);
                Toast.makeText(this, "Estado actualizado a: " + nuevoEstado, Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    // NUEVO: Detener la actualización automática
    private void detenerActualizacionAutomatica() {
        if (handler != null && runnableAvance != null) {
            handler.removeCallbacks(runnableAvance);
        }
        btnSimularAvance.setEnabled(false);
        btnSimularAvance.setText("Pedido Entregado");
        btnSimularAvance.setBackgroundColor(0xFF9E9E9E);
    }

    // NUEVO: Detener el Handler cuando se cierra la Activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        detenerActualizacionAutomatica();
    }

    private void actualizarTimeline(String estado) {
        String fechaActual = new SimpleDateFormat("dd MMM yyyy, hh:mm a",
                Locale.getDefault()).format(new Date());

        circleConfirmado.setBackgroundResource(R.drawable.bg_circle_pending);
        circleProceso.setBackgroundResource(R.drawable.bg_circle_pending);
        circleEnvio.setBackgroundResource(R.drawable.bg_circle_pending);
        circleEntregado.setBackgroundResource(R.drawable.bg_circle_pending);

        line1.setBackgroundColor(0xFFE0E0E0);
        line2.setBackgroundColor(0xFFE0E0E0);
        line3.setBackgroundColor(0xFFE0E0E0);

        tvTextoEnvio.setTextColor(0xFF757575);
        tvTextoEntregado.setTextColor(0xFF757575);

        tvFechaConfirmado.setText("Pendiente");
        tvFechaProceso.setText("Pendiente");
        tvFechaEnvio.setText("Pendiente");
        tvFechaEntregado.setText("Pendiente");

        switch (estado) {
            case "PROCESANDO":
                circleConfirmado.setBackgroundResource(R.drawable.bg_circle_success);
                tvFechaConfirmado.setText(fechaActual);
                circleProceso.setBackgroundResource(R.drawable.bg_circle_success);
                tvFechaProceso.setText(fechaActual);
                line1.setBackgroundColor(0xFF0288D1);
                break;

            case "ENVIADO":
                circleConfirmado.setBackgroundResource(R.drawable.bg_circle_success);
                tvFechaConfirmado.setText(fechaActual);
                circleProceso.setBackgroundResource(R.drawable.bg_circle_success);
                tvFechaProceso.setText(fechaActual);
                circleEnvio.setBackgroundResource(R.drawable.bg_circle_success);
                tvFechaEnvio.setText(fechaActual);
                tvTextoEnvio.setTextColor(0xFF212121);
                line1.setBackgroundColor(0xFF0288D1);
                line2.setBackgroundColor(0xFF0288D1);
                break;

            case "ENTREGADO":
                circleConfirmado.setBackgroundResource(R.drawable.bg_circle_success);
                tvFechaConfirmado.setText(fechaActual);
                circleProceso.setBackgroundResource(R.drawable.bg_circle_success);
                tvFechaProceso.setText(fechaActual);
                circleEnvio.setBackgroundResource(R.drawable.bg_circle_success);
                tvFechaEnvio.setText(fechaActual);
                tvTextoEnvio.setTextColor(0xFF212121);
                circleEntregado.setBackgroundResource(R.drawable.bg_circle_success);
                tvFechaEntregado.setText(fechaActual);
                tvTextoEntregado.setTextColor(0xFF212121);
                line1.setBackgroundColor(0xFF0288D1);
                line2.setBackgroundColor(0xFF0288D1);
                line3.setBackgroundColor(0xFF0288D1);
                detenerActualizacionAutomatica();
                break;

            default:
                circleConfirmado.setBackgroundResource(R.drawable.bg_circle_success);
                tvFechaConfirmado.setText(fechaActual);
                break;
        }
    }
}