package com.anitour.model;

public class PaymentDTO {
    private String cardNumber;

    public PaymentDTO(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public boolean isValid() {
        // Simulazione validazione
        return cardNumber != null && cardNumber.length() > 10;
    }

    public String getCardNumber() { return cardNumber; }
}