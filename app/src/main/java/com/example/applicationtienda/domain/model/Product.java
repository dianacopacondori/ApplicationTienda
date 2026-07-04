package com.example.applicationtienda.domain.model;
import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {
    private String id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String category;
    private String imageUrl;
//constructor
    public Product(String id, String name, String description, double price, int stock, String category){
        this.id= id;
        this.name= name;
        this.description= description;
        this.price= price;
        this.stock= stock;
        this.category= category;
    }
//getters and setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    //metodo para aplicar descuento (GRASP - Expert)
    public double getDiscountedPrice(double percentage){
        return this.price - (this.price * percentage / 100);
    }
    //verificar si hay stock disponible
    public boolean isAvailable(){
        return this.stock > 0;
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
    @Override
    public String toString(){
        return "Product{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", price=" + price + ", stock=" +stock + '}';
    }
}
