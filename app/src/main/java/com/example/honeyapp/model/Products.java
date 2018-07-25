package com.example.honeyapp.model;

public class Products {

    int id;
    private String photo;
    private String name;
    private String price;

    public Products() {
    }

    public Products(int id, String photo, String name, String price) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
