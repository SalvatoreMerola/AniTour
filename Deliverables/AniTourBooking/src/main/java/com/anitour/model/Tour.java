package com.anitour.model;

public class Tour {
    private int id;
    private String name;
    private double price;

    public Tour(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters necessari per il Controller
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}