package com.anitour.control;

import com.anitour.dao.BookingDAO;
import com.anitour.dao.IBookingRepository;
import com.anitour.dao.IPaymentGateway;
import com.anitour.model.Booking;
import com.anitour.model.Cart;
import com.anitour.model.PaymentDTO;
import com.anitour.model.Tour;

public class BookingControl {

    private IBookingRepository bookingRepo;
    private IPaymentGateway paymentGateway;

    // Dependency Injection tramite costruttore per facilitare il testing
    public BookingControl(IBookingRepository repo, IPaymentGateway gateway) {
        this.bookingRepo = repo;
        this.paymentGateway = gateway;
    }

    /**
     * Gestisce l'intero flusso di checkout: validazione, pagamento e persistenza.
     */
    public Booking processCheckout(Cart cart, PaymentDTO paymentData, String customerEmail, int userId) throws Exception {

        // 1. Validazione Carrello (Pre-condition)
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Errore: Il carrello è vuoto!");
        }

        // 2. Validazione Dati Pagamento
        if (paymentData == null || !paymentData.isValid()) {
            throw new IllegalArgumentException("Errore: Dati della carta non validi.");
        }

        // 3. Controllo Disponibilità (Business Logic)
        // Verifica che ci siano ancora posti disponibili per i tour selezionati
        for (Tour t : cart.getTours()) {
            if (!bookingRepo.checkAvailability(t.getId(), 1)) {
                throw new Exception("SoldOutException: Ci dispiace, i posti per " + t.getName() + " sono esauriti.");
            }
        }

        // 4. Elaborazione Pagamento con la Banca
        // Se il gateway esterno rifiuta la carta, interrompiamo il flusso
        if (!paymentGateway.processPayment(cart.getTotal(), paymentData)) {
            throw new Exception("PaymentFailedException: La transazione è stata rifiutata dal circuito bancario.");
        }

        // 5. Creazione dell'oggetto Ordine
        Booking newBooking = new Booking(cart, Booking.CONFIRMED);
        newBooking.setCustomerEmail(customerEmail);
        newBooking.setUserId(userId);

        // Associa l'ID del tour principale all'ordine (per semplicità database)
        if (!cart.getTours().isEmpty()) {
            newBooking.setTourId(cart.getTours().get(0).getId());
        }

        // 6. Persistenza su Database (Salvataggio Ordine)
        int newId = bookingRepo.save(newBooking);
        newBooking.setId(newId);

        // 7. Aggiornamento Inventario (Decremento Posti)
        // Aggiorna il DB sottraendo i posti acquistati
        if (bookingRepo instanceof BookingDAO) {
            for (Tour t : cart.getTours()) {
                ((BookingDAO) bookingRepo).decrementSeats(t.getId(), 1);
            }
        }

        // 8. Pulizia della sessione dopo l'acquisto
        cart.clear();

        return newBooking;
    }
}