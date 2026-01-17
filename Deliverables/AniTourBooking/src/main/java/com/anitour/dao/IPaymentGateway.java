package com.anitour.dao;

import com.anitour.model.PaymentDTO;

public interface IPaymentGateway {
    boolean processPayment(double amount, PaymentDTO cardDetails);
}