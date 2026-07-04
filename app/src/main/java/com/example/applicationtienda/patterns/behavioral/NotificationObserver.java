package com.example.applicationtienda.patterns.behavioral;

import android.util.Log;
import com.example.applicationtienda.domain.model.Product;

public class NotificationObserver implements CartObserver {
    private static final String TAG = "NotificationObserver";

    @Override
    public void onProductAdded(Product product) {
        Log.d(TAG, "🔔 Notificación: Se agregó '" + product.getName() + "' al carrito");
    }

    @Override
    public void onProductRemoved(Product product) {
        Log.d(TAG, "🔔 Notificación: Se quitó '" + product.getName() + "' del carrito");
    }

    @Override
    public void onCartCleared() {
        Log.d(TAG, "🔔 Notificación: Carrito vaciado");
    }
}