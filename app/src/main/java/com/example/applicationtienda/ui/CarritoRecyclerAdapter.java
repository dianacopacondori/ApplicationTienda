package com.example.applicationtienda.ui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtienda.R;
import com.example.applicationtienda.domain.model.CartItem;

import java.util.List;

public class CarritoRecyclerAdapter extends RecyclerView.Adapter<CarritoRecyclerAdapter.ViewHolder> {

    private List<CartItem> items;
    private OnEliminarClickListener listener;

    public interface OnEliminarClickListener {
        void onEliminar(CartItem item);
    }

    public CarritoRecyclerAdapter(List<CartItem> items, OnEliminarClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);

        holder.tvNombre.setText(item.getProduct().getName());
        holder.tvCantidad.setText("Cantidad: " + item.getQuantity());
        holder.tvPrecio.setText("S/. " + String.format("%.2f", item.getSubtotal()));

        if (holder.btnEliminar != null) {
            holder.btnEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEliminar(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void actualizarItems(List<CartItem> nuevosItems) {
        this.items = nuevosItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCantidad, tvPrecio;
        Button btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnEliminar = itemView.findViewById(R.id.btnEliminar); // Agrega este botón en item_carrito.xml
        }
    }
}