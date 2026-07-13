package com.example.applicationtienda.infrastructure.persistence;

import android.content.Context;
import android.util.Log;
import com.example.applicationtienda.domain.model.Product;
import com.example.applicationtienda.domain.repositories.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomProductRepository implements ProductRepository {

    private static final String TAG = "RoomProductRepository";
    private ProductDao productDao;
    private Context context;

    public RoomProductRepository(Context context) {
        this.context = context;
        AppDatabase db = AppDatabase.getInstance(context);
        this.productDao = db.productDao();
    }

    @Override
    public List<Product> getAllProducts() {
        List<ProductEntity> entities = productDao.getAllProducts();
        List<Product> products = new ArrayList<>();

        for (ProductEntity entity : entities) {
            products.add(mapToDomain(entity));
        }

        Log.d(TAG, "Obtenidos " + products.size() + " productos de Room");
        return products;
    }

    @Override
    public Product getProductById(String productId) {
        ProductEntity entity = productDao.getProductById(productId);
        if (entity != null) {
            return mapToDomain(entity);
        }
        return null;
    }

    @Override
    public void addProduct(Product product) {
        ProductEntity entity = mapToEntity(product);
        productDao.insertProduct(entity);
        Log.d(TAG, "Producto agregado: " + product.getName());
    }

    @Override
    public void updateProduct(Product product) {
        ProductEntity entity = mapToEntity(product);
        productDao.updateProduct(entity);
        Log.d(TAG, "Producto actualizado: " + product.getName());
    }

    @Override
    public void deleteProduct(String productId) {
        ProductEntity entity = productDao.getProductById(productId);
        if (entity != null) {
            productDao.deleteProduct(entity);
            Log.d(TAG, "Producto eliminado: " + productId);
        }
    }

    // Método para agregar productos de prueba
    public void addSampleProducts() {
        List<ProductEntity> sampleProducts = new ArrayList<>();

        sampleProducts.add(new ProductEntity(
                UUID.randomUUID().toString(),
                "Laptop Gamer Pro",
                "Laptop de alto rendimiento para gaming",
                1500.00,
                10,
                "Laptop"
        ));

        sampleProducts.add(new ProductEntity(
                UUID.randomUUID().toString(),
                "Smartphone X",
                "Último modelo con cámara profesional",
                800.00,
                25,
                "Smartphone"
        ));

        sampleProducts.add(new ProductEntity(
                UUID.randomUUID().toString(),
                "Camisa Polo Premium",
                "Camisa de algodón de alta calidad",
                45.00,
                50,
                "Ropa"
        ));

        sampleProducts.add(new ProductEntity(
                UUID.randomUUID().toString(),
                "Zapatillas Running",
                "Zapatillas deportivas para correr",
                120.00,
                30,
                "Calzado"
        ));

        productDao.insertAllProducts(sampleProducts);
        Log.d(TAG, "Agregados " + sampleProducts.size() + " productos de prueba");
    }

    // Mapeo de Entity a Domain
    private Product mapToDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.getCategory()
        );
    }

    // Mapeo de Domain a Entity
    private ProductEntity mapToEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStock(product.getStock());
        entity.setCategory(product.getCategory());
        return entity;
    }
    public boolean isEmpty() {
        List<ProductEntity> products = productDao.getAllProducts();
        return products == null || products.isEmpty();
    }
}
