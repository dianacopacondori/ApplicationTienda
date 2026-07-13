package com.example.applicationtienda.infrastructure.persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class OrderEntity {

    @PrimaryKey
    @NonNull
    private String id;

    private String userId;
    private String fecha;
    private String metodoPago;
    private double total;
    private String estado;

    public OrderEntity() {
    }
@Ignore
    public OrderEntity(@NonNull String id, String userId, String fecha,
                       String metodoPago, double total, String estado) {
        this.id = id;
        this.userId = userId;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.total = total;
        this.estado = estado;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
