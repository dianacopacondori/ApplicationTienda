package com.example.applicationtienda.patterns.behavioral;

import android.util.Log;
import com.example.applicationtienda.domain.model.Order;

public class PendingState implements OrderState {
    private static final String TAG = "OrderState";

    @Override
    public void process(Order order) {
        Log.d(TAG, "Procesando pedido " + order.getId());
        order.setState(new ProcessingState());
    }

    @Override
    public void ship(Order order) {
        Log.d(TAG, "No se puede enviar un pedido pendiente. Debe procesarse primero.");
    }

    @Override
    public void deliver(Order order) {
        Log.d(TAG, "No se puede entregar un pedido pendiente.");
    }

    @Override
    public void cancel(Order order) {
        Log.d(TAG, "❌ Pedido " + order.getId() + " cancelado");
        order.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "PENDIENTE";
    }
}