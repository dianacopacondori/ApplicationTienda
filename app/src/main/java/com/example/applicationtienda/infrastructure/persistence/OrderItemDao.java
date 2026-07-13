package com.example.applicationtienda.infrastructure.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrderItems(List<OrderItemEntity> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrderItem(OrderItemEntity item);

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    List<OrderItemEntity> getItemsByOrder(String orderId);

    @Query("DELETE FROM order_items")
    void deleteAllItems();
}
