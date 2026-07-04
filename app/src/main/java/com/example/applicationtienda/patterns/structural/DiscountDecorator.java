package com.example.applicationtienda.patterns.structural;

import com.example.applicationtienda.domain.model.Product;

// Hereda de Product para ser compatible con el Carrito (Cart)
public class DiscountDecorator extends Product {

    private Product originalProduct;
    private double discountPercentage;

    public DiscountDecorator(Product originalProduct, double discountPercentage) {
        // Inicializamos la clase padre con los datos del producto original
        super(
                originalProduct.getId(),
                originalProduct.getName(),
                originalProduct.getDescription(),
                originalProduct.getPrice(),
                originalProduct.getStock(),
                originalProduct.getCategory()
        );
        this.originalProduct = originalProduct;
        this.discountPercentage = discountPercentage;
    }

    // Sobrescribimos el método getPrice (OCP - Open/Closed Principle)
    @Override
    public double getPrice() {
        double originalPrice = originalProduct.getPrice();
        return originalPrice - (originalPrice * discountPercentage / 100);
    }

    @Override
    public String getName() {
        return originalProduct.getName() + " (Oferta " + discountPercentage + "%)";
    }

    public double getOriginalPrice() {
        return originalProduct.getPrice();
    }
}
