package com.example.honeyapp.model;

public class UserCart {

    private int quantity;
    private Products p;

    public UserCart() {

    }

    public UserCart(int quantity, Products p) {
        this.quantity = quantity;
        this.p = p;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Products getP(Products p) {
        return this.p;
    }

    public void setP(Products p) {
        this.p = p;
    }
}

