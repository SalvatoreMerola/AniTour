package com.anitour.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<TourItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addTour(int tourId, String tourName, double price) {
        items.add(new TourItem(tourId, tourName, price));
    }

    public void removeTour(int tourId) {
        items.removeIf(item -> item.getId() == tourId);
    }

    // Necessario per soddisfare la post-condizione dell'ODD (Sez 3.1.1)
    public void clear() {
        this.items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getTotal() {
        return items.stream().mapToDouble(TourItem::getPrice).sum();
    }

    // Getter utile per i test
    public int getItemCount() { return items.size(); }

    // Classe interna semplice
    public static class TourItem {
        private int id;
        private String name;
        private double price;

        public TourItem(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
        public int getId() { return id; }
        public double getPrice() { return price; }
    }
}