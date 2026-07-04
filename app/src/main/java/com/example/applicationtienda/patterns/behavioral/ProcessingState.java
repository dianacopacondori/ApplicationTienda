package com.example.applicationtienda.patterns.behavioral;

import android.util.Log;
import com.example.applicationtienda.domain.model.Order;

public class ProcessingState implements OrderState {
    private static final String TAG = "OrderState";

    @Override
    public void process(Order order) {
        Log.d(TAG, "El pedido ya está siendo procesado");
    }

    @Override
    public void ship(Order order) {
        Log.d(TAG, "Enviando pedido " + order.getId());
        order.setState(new ShippedState());
    }

    @Override
    public void deliver(Order order) {
        Log.d(TAG, "No se puede entregar. El pedido aún no ha sido enviado.");
    }

    @Override
    public void cancel(Order order) {
        Log.d(TAG, "❌ Pedido " + order.getId() + " cancelado durante procesamiento");
        order.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "PROCESANDO";
    }
}
