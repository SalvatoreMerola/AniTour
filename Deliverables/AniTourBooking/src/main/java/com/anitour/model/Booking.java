package com.anitour.model;

import java.sql.Timestamp;

public class Booking {
    private int id;
    private int userId;
    private int tourId;
    private double price;
    private String customerEmail;
    private String status;
    private Timestamp bookingDate;

    // Costanti per lo stato
    public static final String CONFIRMED = "CONFIRMED";
    public static final String PENDING = "PENDING";

    // Costruttore Vuoto
    public Booking() {}

    public Booking(Cart cart, String status) {
        this.status = status;
        this.price = cart.getTotal();
    }

    public Booking(int id, String status) {
        this.id = id;
        this.status = status;
    }

    // --- GETTERS E SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", userId=" + userId +
                ", tourId=" + tourId +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}