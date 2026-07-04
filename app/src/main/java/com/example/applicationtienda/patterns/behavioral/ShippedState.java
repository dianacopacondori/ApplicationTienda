package com.example.applicationtienda.patterns.behavioral;


import android.util.Log;
import com.example.applicationtienda.domain.model.Order;

public class ShippedState implements OrderState {
    private static final String TAG = "OrderState";

    @Override
    public void process(Order order) {
        Log.d(TAG, "El pedido ya fue enviado");
    }

    @Override
    public void ship(Order order) {
        Log.d(TAG, "El pedido ya está en camino");
    }

    @Override
    public void deliver(Order order) {
        Log.d(TAG, "Pedido " + order.getId() + " entregado");
        order.setState(new DeliveredState());
    }

    @Override
    public void cancel(Order order) {
        Log.d(TAG, "No se puede cancelar un pedido ya enviado");
    }

    @Override
    public String getStateName() {
        return "ENVIADO";
    }
}
