package com.wesleyreisz.productweb;

/**
 * Created by wesleyreisz on 3/29/16.
 */
public class Product {
    private int id;
    private String name;
    private String category;
    private double price;

    public Product(){}
    public Product(int id,String name,String category,double price){
        this.id=id;
        this.name=name;
        this.category=category;
        this.price=price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                '}';
    }
}
