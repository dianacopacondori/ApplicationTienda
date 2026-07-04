package com.example.applicationtienda.infrastructure.network;

// Simula una clase de una API externa o sistema legacy que NO podemos modificar
public class LegacyExternalProduct {
    private String productName;
    private double productCost;
    private String supplierCode;

    public LegacyExternalProduct(String productName, double productCost, String supplierCode) {
        this.productName = productName;
        this.productCost = productCost;
        this.supplierCode = supplierCode;
    }

    public String getProductName() { return productName; }
    public double getProductCost() { return productCost; }
    public String getSupplierCode() { return supplierCode; }
}
