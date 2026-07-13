package com.example.applicationtienda.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtienda.R;
import com.example.applicationtienda.domain.model.CartItem;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private List<CartItem> items;

    public CheckoutAdapter(List<CartItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);

        holder.tvNombre.setText(item.getProduct().getName());
        holder.tvCantidad.setText("x" + item.getQuantity());
        holder.tvPrecio.setText("S/. " + String.format("%.2f", item.getSubtotal()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCantidad, tvPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvItemNombre);
            tvCantidad = itemView.findViewById(R.id.tvItemCantidad);
            tvPrecio = itemView.findViewById(R.id.tvItemPrecio);
        }
    }
}
