package com.anitour.model;

public class Booking {
    private int id;
    private String status;
    // costanti di stato
    public static final String CONFIRMED = "CONFIRMED";
    public static final String PENDING = "PENDING";

    public Booking(int id, String status) {
        this.id = id;
        this.status = status;
    }

    // Costruttore vuoto
    public Booking() {}

    public int getId() { return id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public void setId(int id) { this.id = id; }
}