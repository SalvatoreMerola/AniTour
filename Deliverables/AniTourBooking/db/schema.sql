CREATE DATABASE IF NOT EXISTS anitourdb;
USE anitourdb;

-- 1. Tabella Utenti (Necessaria per la Foreign Key di bookings)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    surname VARCHAR(100),
    email VARCHAR(150),
    password VARCHAR(255)
    );

-- 2. Tabella Tour (Necessaria per gestire disponibilità e info)
CREATE TABLE IF NOT EXISTS tour (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DOUBLE NOT NULL,
    available_seats INT NOT NULL
    );

-- 3. Tabella Prenotazioni (Aggiornata con i campi del Java Bean)
CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    tour_id INT NOT NULL,
    customer_email VARCHAR(150),
    price DOUBLE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Vincoli di integrità referenziale
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (tour_id) REFERENCES tour(id)
    );

-- ==========================================
-- DATI DI INIZIALIZZAZIONE (SEED DATA)
-- ==========================================

-- Inseriamo l'Utente ID 99 (usato nel BookingDAOIntegrationTest)
INSERT INTO users (id, name, surname, email, password)
VALUES (99, 'Mario', 'Rossi', 'test@anitour.com', 'password123')
    ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Inseriamo il Tour ID 1 (usato nella Demo Happy Path)
-- Prezzo: 100.0, Posti: 50
INSERT INTO tour (id, name, description, price, available_seats)
VALUES (1, 'Tour Demo', 'Un tour dimostrativo per il test', 100.0, 50)
    ON DUPLICATE KEY UPDATE available_seats=50;

-- Inseriamo il Tour ID 2 (usato per il test Sold Out)
-- Prezzo: 150.0, Posti: 0 (Già esaurito)
INSERT INTO tour (id, name, description, price, available_seats)
VALUES (2, 'Tour SoldOut', 'Tour per testare la concorrenza', 150.0, 0)
    ON DUPLICATE KEY UPDATE available_seats=0;