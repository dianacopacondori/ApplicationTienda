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
import com.example.applicationtienda.patterns.behavioral.Command;
import com.example.applicationtienda.patterns.behavioral.DisminuirCantidadCommand;
import com.example.applicationtienda.patterns.behavioral.RemoveFromCartCommand;
import com.example.applicationtienda.patterns.creational.CartManager;
import java.util.List;

public class CarritoRecyclerAdapter extends RecyclerView.Adapter<CarritoRecyclerAdapter.ViewHolder> {

    private List<CartItem> items;
    private OnCarritoChangeListener listener;

    public interface OnCarritoChangeListener {
        void onCarritoActualizado();
    }

    public CarritoRecyclerAdapter(List<CartItem> items, OnCarritoChangeListener listener) {
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
        CartManager cartManager = CartManager.getInstance();

        holder.tvNombre.setText(item.getProduct().getName());
        holder.tvCantidad.setText("Cantidad: " + item.getQuantity());
        holder.tvPrecio.setText("S/. " + String.format("%.2f", item.getSubtotal()));

        cargarImagenPorCategoria(holder.ivProducto, item.getProduct().getCategory());

        // BOTÓN DISMINUIR
        holder.btnDisminuir.setOnClickListener(v -> {
            Command command = new DisminuirCantidadCommand(cartManager.getCart(), item);
            cartManager.getCommandHistory().executeCommand(command);

            if (listener != null) {
                listener.onCarritoActualizado();
            }
        });

        // BOTÓN ELIMINAR
        holder.btnEliminar.setOnClickListener(v -> {
            Command command = new RemoveFromCartCommand(cartManager.getCart(), item.getProduct());
            cartManager.getCommandHistory().executeCommand(command);

            if (listener != null) {
                listener.onCarritoActualizado();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    // Método clave para refrescar la lista desde la Activity
    public void actualizarItems(List<CartItem> nuevosItems) {
        this.items = nuevosItems;
        notifyDataSetChanged();
    }

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
        Button btnDisminuir, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivProductoCarrito);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnDisminuir = itemView.findViewById(R.id.btnDisminuir);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}