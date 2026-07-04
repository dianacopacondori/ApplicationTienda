package com.example.applicationtienda.patterns.behavioral;

import android.util.Log;
import com.example.applicationtienda.domain.model.Product;

public class InventoryObserver implements CartObserver {
    private static final String TAG = "InventoryObserver";

    @Override
    public void onProductAdded(Product product) {
        Log.d(TAG, "📦 Inventario: Stock reducido para '" + product.getName() + "'");
        // Aquí iría la lógica para reducir el stock en BD
    }

    @Override
    public void onProductRemoved(Product product) {
        Log.d(TAG, "📦 Inventario: Stock restaurado para '" + product.getName() + "'");
    }

    @Override
    public void onCartCleared() {
        Log.d(TAG, "📦 Inventario: Todos los stocks restaurados");
    }
}
