package com.anitour.model;

public class Booking {
    private int id;
    private String status;

    public static final String CONFIRMED = "CONFIRMED";
    public static final String PENDING = "PENDING";

    public Booking() {}

    public Booking(Cart cart, String status) {
        this.status = status;
    }

    public Booking(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() { return id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public void setId(int id) { this.id = id; }
}