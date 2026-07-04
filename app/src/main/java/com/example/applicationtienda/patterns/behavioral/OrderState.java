package com.example.applicationtienda.patterns.behavioral;

import com.example.applicationtienda.domain.model.Order;

public interface OrderState {
    void process(Order order);
    void ship(Order order);
    void deliver(Order order);
    void cancel(Order order);
    String getStateName();
}
