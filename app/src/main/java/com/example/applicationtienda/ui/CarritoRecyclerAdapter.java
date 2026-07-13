package com.example.applicationtienda.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

        // Cargar la imagen según la categoría del producto
        String categoria = item.getProduct().getCategory();
        cargarImagenPorCategoria(holder.ivProducto, categoria);

        // Acción del botón eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminar(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void actualizarItems(List<CartItem> nuevosItems) {
        this.items = nuevosItems;
        notifyDataSetChanged();
    }

    // Método para cargar imágenes según la categoría (Misma lógica que en Productos)
    private void cargarImagenPorCategoria(ImageView imageView, String categoria) {
        if (categoria == null) {
            imageView.setImageResource(R.drawable.default_product);
            return;
        }

        String catLower = categoria.toLowerCase();

        if (catLower.contains("laptop")) {
            imageView.setImageResource(R.drawable.laptop);
        } else if (catLower.contains("smartphone") || catLower.contains("celular")) {
            imageView.setImageResource(R.drawable.smartphone);
        } else if (catLower.contains("camisa") || catLower.contains("ropa")) {
            imageView.setImageResource(R.drawable.camisa);
        } else if (catLower.contains("calzado") || catLower.contains("zapatilla")) {
            imageView.setImageResource(R.drawable.zapatillas);
        } else if (catLower.contains("audífono")) {
            imageView.setImageResource(R.drawable.audifonos);
        } else {
            imageView.setImageResource(R.drawable.default_product);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProducto;
        TextView tvNombre, tvCantidad, tvPrecio;
        Button btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivProductoCarrito);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}