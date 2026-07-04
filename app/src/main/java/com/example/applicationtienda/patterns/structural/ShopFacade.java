package com.example.applicationtienda.patterns.structural;

import com.example.applicationtienda.domain.model.Order;
import com.example.applicationtienda.domain.model.Product;
import com.example.applicationtienda.patterns.creational.ClothingFactory;
import com.example.applicationtienda.patterns.creational.ElectronicsFactory;
import com.example.applicationtienda.patterns.creational.OrderBuilder;
import com.example.applicationtienda.patterns.creational.ProductFactory;

public class ShopFacade {

    // 1. Oculta la lógica de qué Fábrica usar (Abstract Factory)
    public Product createElectronicProduct(String name, double price) {
        ProductFactory factory = new ElectronicsFactory();
        return factory.createProduct(name, price, "Electrónica");
    }

    public Product createClothingProduct(String name, double price) {
        ProductFactory factory = new ClothingFactory();
        return factory.createProduct(name, price, "Ropa");
    }

    // 2. Combina Abstract Factory + Decorator sin que el cliente lo sepa
    public Product createDiscountedProduct(String name, double price, double discountPercentage) {
        ProductFactory factory = new ElectronicsFactory();
        Product originalProduct = factory.createProduct(name, price, "Electrónica");

        // Aplicamos el decorador de descuento
        return new DiscountDecorator(originalProduct, discountPercentage);
    }

    // 3. Oculta la complejidad del Builder para crear pedidos
    public Order createOrder(String userId, Product... products) {
        OrderBuilder builder = new OrderBuilder();
        builder.setUserId(userId);

        // Agregamos todos los productos pasados por parámetro (varargs)
        for (Product product : products) {
            builder.addProduct(product);
        }

        return builder.build();
    }

    // 4. Método de alto nivel: "Checkout Rápido" (Combina todo)
    public Order quickCheckout(String userId, String productName, double price, double discount) {
        // El cliente solo llama a un método, el Facade se encarga de crear el producto con descuento y el pedido
        Product discountedProduct = createDiscountedProduct(productName, price, discount);
        return createOrder(userId, discountedProduct);
    }
}
