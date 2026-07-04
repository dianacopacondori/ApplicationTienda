package com.example.applicationtienda.patterns.structural;

import com.example.applicationtienda.domain.model.Product;
import com.example.applicationtienda.infrastructure.network.LegacyExternalProduct;

// Adapta LegacyExternalProduct para que sea compatible con nuestro sistema (Product)
public class ProductAdapter extends Product {

    private LegacyExternalProduct legacyProduct;

    public ProductAdapter(LegacyExternalProduct legacyProduct) {
        // Mapeamos los datos del sistema legacy a nuestra clase Product
        super(
                "EXT-" + legacyProduct.getSupplierCode(), // Generamos un ID
                legacyProduct.getProductName(),
                "Producto importado de sistema externo",
                legacyProduct.getProductCost() * 1.3, // Ajustamos precio (ej. +30% importación)
                5, // Stock por defecto
                "Importados"
        );
        this.legacyProduct = legacyProduct;
    }

    public String getSupplierCode() {
        return legacyProduct.getSupplierCode();
    }
}
