package com.anitour.dao;

import com.anitour.model.PaymentDTO;

public class RealPaymentGateway implements IPaymentGateway {
    @Override
    public boolean processPayment(double amount, PaymentDTO paymentData) {
        // Controllo base di validità
        if (paymentData == null || !paymentData.isValid()) {
            return false;
        }

        // DEMO:
        // Se la carta finisce con "9999", la banca rifiuta la transazione (es. fondi insufficienti)
        if (paymentData.getCardNumber().endsWith("9999")) {
            System.out.println("Banca reale: Pagamento RIFIUTATO (Simulazione Fondi Insufficienti)");
            return false;
        }

        System.out.println("Banca reale: Pagamento di " + amount + " autorizzato.");
        return true;
    }
}