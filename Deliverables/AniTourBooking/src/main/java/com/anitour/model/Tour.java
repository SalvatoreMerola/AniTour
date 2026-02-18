package com.anitour.model;

public class Tour {
    private int id;
    private String name;
    private double price;
    private String description;
    private String imagePath;

    public Tour(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Tour(int id, String name, double price, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
    }

    // Getters necessari per il Controller
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
}