package com.example.applicationtienda.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtienda.R;
import com.example.applicationtienda.infrastructure.persistence.OrderEntity;

import java.util.List;

public class PedidoRecyclerAdapter extends RecyclerView.Adapter<PedidoRecyclerAdapter.ViewHolder> {

    private List<OrderEntity> pedidos;
    private OnPedidoClickListener listener;

    public interface OnPedidoClickListener {
        void onPedidoClick(OrderEntity pedido);
    }

    public PedidoRecyclerAdapter(List<OrderEntity> pedidos, OnPedidoClickListener listener) {
        this.pedidos = pedidos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderEntity pedido = pedidos.get(position);

        // 1. Actualizar datos básicos
        holder.tvNumeroPedido.setText("Pedido #" + pedido.getId().substring(0, 8));
        holder.tvFecha.setText(pedido.getFecha());
        holder.tvTotal.setText("Total: S/. " + String.format("%.2f", pedido.getTotal()));

        // 2. NUEVO: Actualizar el método de pago con el dato real de la BD
        String metodo = pedido.getMetodoPago() != null ? pedido.getMetodoPago() : "No especificado";
        holder.tvMetodoPago.setText("Método: " + metodo);

        // 3. Actualizar estado con color dinámico
        String estado = pedido.getEstado() != null ? pedido.getEstado() : "PENDIENTE";
        holder.tvEstado.setText(estado);

        // Colorear el texto del estado según corresponda
        switch (estado) {
            case "PENDIENTE":
                holder.tvEstado.setTextColor(0xFFFF9800); // Naranja
                break;
            case "PROCESANDO":
                holder.tvEstado.setTextColor(0xFF0288D1); // Azul
                break;
            case "ENVIADO":
                holder.tvEstado.setTextColor(0xFF4CAF50); // Verde
                break;
            case "ENTREGADO":
                holder.tvEstado.setTextColor(0xFF9E9E9E); // Gris
                break;
            default:
                holder.tvEstado.setTextColor(0xFF212121); // Negro por defecto
                break;
        }

        // Click para ir al detalle
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPedidoClick(pedido);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumeroPedido;
        TextView tvFecha;
        TextView tvTotal;
        TextView tvMetodoPago; // <-- Agregado
        TextView tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumeroPedido = itemView.findViewById(R.id.tvNumeroPedido);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvMetodoPago = itemView.findViewById(R.id.tvMetodoPago); // <-- Agregado
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}