package com.anitour.dao;

import com.anitour.model.Booking;

public interface IBookingRepository {
    int save(Booking booking);
    int count();
    boolean checkAvailability(int tourId, int requiredSeats);
}