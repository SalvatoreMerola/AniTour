CREATE DATABASE IF NOT EXISTS anitourdb;
USE anitourdb;

-- ==========================================
-- 1. PULIZIA
-- ==========================================
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS tour;
DROP TABLE IF EXISTS users;

-- ==========================================
-- 2. CREAZIONE TABELLE
-- ==========================================

-- Tabella Utenti
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100),
                       surname VARCHAR(100),
                       email VARCHAR(150),
                       password VARCHAR(255)
);

-- Tabella Tour
CREATE TABLE tour (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      description TEXT,
                      price DOUBLE NOT NULL,
                      available_seats INT NOT NULL,
                      image_path VARCHAR(500)
);

-- Tabella Prenotazioni
CREATE TABLE bookings (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT NOT NULL,
                          tour_id INT NOT NULL,
                          customer_email VARCHAR(150),
                          price DOUBLE,
                          status VARCHAR(50) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (tour_id) REFERENCES tour(id)
);

-- ==========================================
-- 3. DATI DI INIZIALIZZAZIONE
-- ==========================================

-- Utente di test (ID 99)
INSERT INTO users (id, name, surname, email, password)
VALUES (99, 'Mario', 'Rossi', 'test@anitour.com', 'password123');

-- ==========================================
-- 3. DATI DI INIZIALIZZAZIONE (SEED DATA)
-- ==========================================

-- Utente di test (ID 99)
INSERT INTO users (id, name, surname, email, password)
VALUES (99, 'Mario', 'Rossi', 'test@anitour.com', 'password123');

INSERT INTO tour (id, name, description, price, available_seats, image_path)
VALUES (1,
        'Tour Yharnam: La Notte della Caccia',
        'Un viaggio oscuro tra le guglie gotiche di Yharnam. Visita la Clinica di Iosefka, la Grande Cattedrale e sopravvivi alla notte della caccia.',
        1250.00,
        50,
        'images/bloodborne.jpg');

-- TOUR ESAURITO
INSERT INTO tour (id, name, description, price, available_seats, image_path)
VALUES (2,
        'Sekiro: Shadows Die Twice Tour',
        'Esplora il castello di Ashina e le montagne innevate del Giappone feudale. Attenzione: difficoltà elevata! (Posti Esauriti)',
        3000.00,
        0,
        'images/sekiro.jpg');

INSERT INTO tour (id, name, description, price, available_seats, image_path)
VALUES (3,
        'Persona 5: Tokyo Phantom Thieves',
        'Vivi la vita di uno studente a Tokyo. Visita Shibuya, Yongen-Jaya e prova il famoso curry del Leblanc.',
        4500.00,
        30,
        'images/persona5.jpg');