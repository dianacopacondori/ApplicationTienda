package com.example.applicationtienda.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtienda.R;
import com.example.applicationtienda.infrastructure.persistence.OrderItemEntity;

import java.util.List;

public class DetallePedidoAdapter extends RecyclerView.Adapter<DetallePedidoAdapter.ViewHolder> {

    private List<OrderItemEntity> items;

    public DetallePedidoAdapter(List<OrderItemEntity> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detalle_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItemEntity item = items.get(position);

        holder.tvNombre.setText(item.getProductName());
        holder.tvCantidad.setText("Cantidad: " + item.getQuantity());
        holder.tvPrecio.setText("S/. " + String.format("%.2f", item.getPrice() * item.getQuantity()));

        // Cargar imagen según el nombre del producto
        cargarImagen(holder.ivProducto, item.getProductName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void cargarImagen(ImageView imageView, String nombreProducto) {
        if (nombreProducto == null) {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            return;
        }

        String nombreLower = nombreProducto.toLowerCase();

        if (nombreLower.contains("laptop") || nombreLower.contains("computador")) {
            imageView.setImageResource(android.R.drawable.ic_menu_camera);
        } else if (nombreLower.contains("camisa") || nombreLower.contains("pantalón") || nombreLower.contains("ropa")) {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        } else if (nombreLower.contains("celular") || nombreLower.contains("smartphone")) {
            imageView.setImageResource(android.R.drawable.ic_menu_call);
        } else if (nombreLower.contains("audífono")) {
            imageView.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
        } else {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProducto;
        TextView tvNombre;
        TextView tvCantidad;
        TextView tvPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivItemProducto);
            tvNombre = itemView.findViewById(R.id.tvItemNombre);
            tvCantidad = itemView.findViewById(R.id.tvItemCantidad);
            tvPrecio = itemView.findViewById(R.id.tvItemPrecio);
        }
    }
}
