package com.anitour.control;

import com.anitour.dao.IBookingRepository;
import com.anitour.dao.IPaymentGateway;
import com.anitour.model.Booking;
import com.anitour.model.Cart;
import com.anitour.model.PaymentDTO;

public class BookingControl {

    private IBookingRepository bookingRepo;
    private IPaymentGateway paymentGateway;

    // Dependency Injection
    // Passaggio di interfacce nel costruttore, fondamentale per test con Mockito
    public BookingControl(IBookingRepository repo, IPaymentGateway gateway) {
        this.bookingRepo = repo;
        this.paymentGateway = gateway;
    }

    /**
     Implementazione di processCheckout come da ODD Sezione 3.1.1
     @param cart carrello dell'utente (recuperato dalla sessione)
     @param paymentData dati della carta
     */
    public Booking processCheckout(Cart cart, PaymentDTO paymentData) throws Exception {

        // 1. Pre-condition ODD: Carrello non vuoto
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Il carrello è vuoto!");
        }

        // 2. Pre-condition ODD: Dati pagamento validi
        if (paymentData == null || !paymentData.isValid()) {
            throw new IllegalArgumentException("Dati pagamento non validi");
        }

        // 3. Calcolo importo
        double amount = cart.getTotal();

        // 4. Chiamata al Payment Gateway (Sezione 3.2.1)
        boolean authorized = paymentGateway.processPayment(amount, paymentData);

        if (!authorized) {
            throw new Exception("Pagamento rifiutato dalla banca");
        }

        // 5. Creazione Ordine
        Booking newBooking = new Booking();
        newBooking.setStatus(Booking.CONFIRMED);

        // 6. Salvataggio nel DB (Sezione 3.3.1)
        int newId = bookingRepo.save(newBooking);
        newBooking.setId(newId);

        // 7. Post-condition ODD: Svuota carrello
        cart.clear();

        return newBooking;
    }
}