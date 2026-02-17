package com.anitour.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TourTest {

    @Test
    void testCostruttoreEGetters() {
        Tour tour = new Tour(1, "Tour Sekiro", 3000.0);

        assertEquals(1, tour.getId());
        assertEquals("Tour Sekiro", tour.getName());
        assertEquals(3000.0, tour.getPrice());
    }
}