package com.example.applicationtienda.patterns.behavioral;

import android.util.Log;
import com.example.applicationtienda.domain.model.Order;

public class DeliveredState implements OrderState {
    private static final String TAG = "OrderState";

    @Override
    public void process(Order order) {
        Log.d(TAG, "El pedido ya fue entregado");
    }

    @Override
    public void ship(Order order) {
        Log.d(TAG, "El pedido ya fue entregado");
    }

    @Override
    public void deliver(Order order) {
        Log.d(TAG, "El pedido ya fue entregado");
    }

    @Override
    public void cancel(Order order) {
        Log.d(TAG, "No se puede cancelar un pedido entregado");
    }

    @Override
    public String getStateName() {
        return "ENTREGADO";
    }
}
