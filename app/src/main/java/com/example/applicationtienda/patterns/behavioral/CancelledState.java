package com.example.applicationtienda.patterns.behavioral;

import android.util.Log;
import com.example.applicationtienda.domain.model.Order;

public class CancelledState implements OrderState {
    private static final String TAG = "OrderState";

    @Override
    public void process(Order order) {
        Log.d(TAG, "No se puede procesar un pedido cancelado");
    }

    @Override
    public void ship(Order order) {
        Log.d(TAG, "No se puede enviar un pedido cancelado");
    }

    @Override
    public void deliver(Order order) {
        Log.d(TAG, "No se puede entregar un pedido cancelado");
    }

    @Override
    public void cancel(Order order) {
        Log.d(TAG, "El pedido ya está cancelado");
    }

    @Override
    public String getStateName() {
        return "CANCELADO";
    }
}