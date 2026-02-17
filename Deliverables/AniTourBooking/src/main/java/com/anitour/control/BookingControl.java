package com.anitour.control;

import com.anitour.dao.BookingDAO; // Importiamo la classe concreta per usare decrementSeats
import com.anitour.dao.IBookingRepository;
import com.anitour.dao.IPaymentGateway;
import com.anitour.model.Booking;
import com.anitour.model.Cart;
import com.anitour.model.PaymentDTO;
import com.anitour.model.Tour;

public class BookingControl {

    private IBookingRepository bookingRepo;
    private IPaymentGateway paymentGateway;

    public BookingControl(IBookingRepository repo, IPaymentGateway gateway) {
        this.bookingRepo = repo;
        this.paymentGateway = gateway;
    }

    public Booking processCheckout(Cart cart, PaymentDTO paymentData) throws Exception {
        // 1. Check Carrello Vuoto
        if (cart == null || cart.isEmpty()) throw new IllegalArgumentException("Carrello vuoto!");

        // 2. Check Pagamento formale
        if (paymentData == null || !paymentData.isValid()) throw new IllegalArgumentException("Dati pagamento invalidi");

        // 3. Check Disponibilità (Concurrency)
        for (Tour t : cart.getTours()) {
            if (!bookingRepo.checkAvailability(t.getId(), 1)) {
                throw new Exception("SoldOutException: Posti esauriti per il tour " + t.getName());
            }
        }

        // 4. Pagamento Banca
        if (!paymentGateway.processPayment(cart.getTotal(), paymentData)) {
            throw new Exception("PaymentFailedException: Pagamento rifiutato dalla banca");
        }

        // 5. Crea e Salva Ordine
        Booking newBooking = new Booking(cart, Booking.CONFIRMED);

        // Recuperiamo dati extra dal carrello o input per popolare il DB correttamente
        if (!cart.getTours().isEmpty()) {
            newBooking.setTourId(cart.getTours().get(0).getId());
        }
        // Nota: userId e email dovrebbero venire dalla Sessione, qui il DAO metterà quelli di default se nulli

        int newId = bookingRepo.save(newBooking);
        newBooking.setId(newId);

        // 6. SCALA I POSTI
        if (bookingRepo instanceof BookingDAO) {
            for (Tour t : cart.getTours()) {
                ((BookingDAO) bookingRepo).decrementSeats(t.getId(), 1);
            }
        }

        cart.clear();
        return newBooking;
    }
}