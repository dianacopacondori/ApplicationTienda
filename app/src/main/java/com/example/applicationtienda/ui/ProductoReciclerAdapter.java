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
import com.example.applicationtienda.domain.model.Product;

import java.util.List;

public class ProductoReciclerAdapter extends RecyclerView.Adapter<ProductoReciclerAdapter.ProductoViewHolder> {

    private List<Product> productos;
    private OnAgregarClickListener listener;

    public ProductoReciclerAdapter(List<Product> productos, OnAgregarClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Product producto = productos.get(position);

        holder.tvNombre.setText(producto.getName());
        holder.tvCategoria.setText("Categoría: " + producto.getCategory());
        holder.tvPrecio.setText("S/. " + String.format("%.2f", producto.getPrice()));

        // Cargar imagen según la categoría
        cargarImagenPorCategoria(holder.ivProducto, producto.getCategory());

        holder.btnAgregar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAgregar(producto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void actualizarProductos(List<Product> nuevosProductos) {
        this.productos = nuevosProductos;
        notifyDataSetChanged();
    }

    // Método para cargar imágenes según la categoría
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
        } else if (catLower.contains("zapatilla")) {
            imageView.setImageResource(R.drawable.zapatillas);
        } else if (catLower.contains("audífono")) {
            imageView.setImageResource(R.drawable.audifonos);
        } else {
            imageView.setImageResource(R.drawable.default_product);
        }
    }

    public interface OnAgregarClickListener {
        void onAgregar(Product producto);
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProducto;
        TextView tvNombre;
        TextView tvCategoria;
        TextView tvPrecio;
        Button btnAgregar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivProducto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnAgregar = itemView.findViewById(R.id.btnAgregar);
        }
    }
}