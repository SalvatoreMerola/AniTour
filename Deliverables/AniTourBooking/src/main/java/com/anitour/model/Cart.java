package com.anitour.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Tour> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addTour(int tourId, String tourName, double price) {
        items.add(new Tour(tourId, tourName, price));
    }

    public void removeTour(int tourId) {
        items.removeIf(item -> item.getId() == tourId);
    }

    public void clear() {
        this.items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getTotal() {
        return items.stream().mapToDouble(Tour::getPrice).sum();
    }

    public int getItemCount() {
        return items.size();
    }

    public List<Tour> getTours() {
        return items;
    }
}