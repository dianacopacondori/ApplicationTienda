package com.example.applicationtienda.domain.model;

import com.example.applicationtienda.patterns.behavioral.OrderState;
import com.example.applicationtienda.patterns.behavioral.PendingState;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private String userId;
    private List<Product> products;
    private double totalAmount;
    private OrderState state;

    // Constructor privado para forzar el uso del Builder
    public Order() {
        this.products = new ArrayList<>();
        this.state = new PendingState();//estado inicial
        this.totalAmount = 0.0;
    }

    // Getters (No setters, para mantener inmutabilidad una vez construido)
    public String getId() { return id; }
    public String getUserId() { return userId; } // Cambia a User user si prefieres
    public List<Product> getProducts() { return new ArrayList<>(products) ; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return state != null ? state.getStateName() : "DESCONOCIDO"; }

    // Método para calcular total (GRASP - Expert)
    public void calculateTotal() {
        double total = 0;
        for (Product p : products) {
            total += p.getPrice();
        }
        this.totalAmount = total;
    }

    // Setter interno solo para el Builder
    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void addProduct(Product product) { this.products.add(product); }
    public void setState(OrderState state) { this.state = state; }

    public OrderState getState() {
        return state;
    }

    public void setTotalAmount(double totalAmount){this.totalAmount= totalAmount;}
    //métodos delegados al estado actual
    public void process(){
        state.process(this);
    }
    public void ship(){
        state.ship(this);
    }
    public void deliver(){
        state.deliver(this);
    }
    public void cancel(){
        state.cancel(this);
    }

    @Override
    public String toString(){
        return "Order{" + "id='" + id + '\'' + ", userId='" + userId + '\'' + ", products=" + products.size() + ", total=" + totalAmount + ", status='" + getStatus() + '}';
    }


}

