package com.example.applicationtienda.infrastructure.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    List<ProductEntity> getAllProducts();

    @Query("SELECT * FROM products WHERE id = :productId")
    ProductEntity getProductById(String productId);

    @Query("SELECT * FROM products WHERE category = :category")
    List<ProductEntity> getProductsByCategory(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(ProductEntity product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllProducts(List<ProductEntity> products);

    @Update
    void updateProduct(ProductEntity product);

    @Delete
    void deleteProduct(ProductEntity product);

    @Query("DELETE FROM products")
    void deleteAllProducts();
}
