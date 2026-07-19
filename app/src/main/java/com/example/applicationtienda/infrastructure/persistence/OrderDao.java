package com.example.applicationtienda.infrastructure.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
import com.example.applicationtienda.infrastructure.persistence.OrderEntity;

@Dao
public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrder(OrderEntity order);

    @Query("SELECT * FROM orders ORDER BY fecha DESC")
    List<OrderEntity> getAllOrders();

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    OrderEntity getOrderById(String orderId);

    @Query("DELETE FROM orders")
    void deleteAllOrders();

    @Query("UPDATE orders SET estado = :estado WHERE id = :orderId")
    void updateOrderState(String orderId, String estado);
}