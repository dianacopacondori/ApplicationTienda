package com.example.applicationtienda.infrastructure.persistence;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ProductEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // Método abstracto para obtener el DAO
    public abstract ProductDao productDao();

    // Método singleton para obtener la instancia de la base de datos
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "tienda_virtual_database"
                    )
                    .fallbackToDestructiveMigration() // Borra y recrea si cambia la versión
                    .build();
        }
        return instance;
    }
}
