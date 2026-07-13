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

    private final List<OrderEntity> pedidos;
    private final OnPedidoClickListener listener;

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

        holder.tvNumeroPedido.setText("Pedido #" + pedido.getId().substring(0, 8));
        holder.tvFecha.setText(pedido.getFecha());
        holder.tvMetodoPago.setText("Método: " + pedido.getMetodoPago());
        holder.tvEstado.setText("Estado: " + pedido.getEstado());
        holder.tvTotal.setText("Total: S/. " + pedido.getTotal());

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
        TextView tvMetodoPago;
        TextView tvEstado;
        TextView tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumeroPedido = itemView.findViewById(R.id.tvNumeroPedido);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvMetodoPago = itemView.findViewById(R.id.tvMetodoPago);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
