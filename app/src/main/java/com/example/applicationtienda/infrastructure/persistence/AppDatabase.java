package com.example.applicationtienda.infrastructure.persistence;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                ProductEntity.class,
                OrderEntity.class,
                OrderItemEntity.class
        },
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // Métodos abstractos para obtener los DAOs
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
    public abstract OrderItemDao orderItemDao();

    // Método singleton para obtener la instancia de la base de datos
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "tienda_virtual_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
