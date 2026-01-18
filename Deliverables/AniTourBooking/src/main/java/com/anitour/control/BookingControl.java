package com.anitour.control;

import com.anitour.dao.IBookingRepository;
import com.anitour.dao.IPaymentGateway;
import com.anitour.model.Booking;
import com.anitour.model.Cart;
import com.anitour.model.PaymentDTO;
import com.anitour.model.Tour;

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

        // 3. Verifica disponibilità posti (Sezione 3.3.2 - Concurrency)
        // Prima di procedere al pagamento, verifichiamo che i tour siano ancora disponibili
        for (Tour t : cart.getTours()) {
            // Supponiamo 1 posto per biglietto. Se il metodo restituisce false, il tour è Sold Out.
            boolean isAvailable = bookingRepo.checkAvailability(t.getId(), 1);

            if (!isAvailable) {
                // Lanciamo eccezione specifica per bloccare il processo
                throw new Exception("SoldOutException: Posti esauriti per il tour " + t.getName());
            }
        }

        // 4. Calcolo importo totale
        double amount = cart.getTotal();

        // 5. Chiamata al Payment Gateway (Sezione 3.2.1)
        boolean authorized = paymentGateway.processPayment(amount, paymentData);

        if (!authorized) {
            throw new Exception("Pagamento rifiutato dalla banca");
        }

        // 6. Creazione dell'Ordine
        Booking newBooking = new Booking(cart, Booking.CONFIRMED);

        // 7. Salvataggio persistente nel DB (Sezione 3.3.1)
        int newId = bookingRepo.save(newBooking);
        newBooking.setId(newId);

        // 8. Post-condition ODD: Svuota carrello a transazione conclusa
        cart.clear();

        return newBooking;
    }
}