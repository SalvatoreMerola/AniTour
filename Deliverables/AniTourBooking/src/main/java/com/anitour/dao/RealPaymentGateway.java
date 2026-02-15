package com.anitour.dao;

public class RealPaymentGateway implements IPaymentGateway {
    @Override
    public boolean processPayment(double amount, com.anitour.model.PaymentDTO paymentData) {
        if (paymentData != null && paymentData.isValid()) {
            System.out.println("Banca reale: Pagamento di " + amount + " autorizzato.");
            return true;
        }
        return false;
    }
}