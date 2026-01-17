# AniTour - Sistema di Prenotazione Tour

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)

**AniTour** è una piattaforma software progettata per la gestione e la prenotazione di visite guidate. Il sistema garantisce la robustezza delle transazioni di acquisto, gestendo con precisione la concorrenza (stock biglietti), i pagamenti simulati e la persistenza dei dati.

---

## Indice

1. [Panoramica del Progetto](#panoramica-del-progetto)
2. [Architettura e Tecnologie](#architettura-e-tecnologie)
3. [Funzionalità Principali](#funzionalità-principali)
4. [Testing e Qualità](#testing-e-qualità)
5. [Prerequisiti](#prerequisiti)
6. [Guida all'Installazione](#guida-allinstallazione)
7. [Autori](#autori)

---

## Panoramica del Progetto

Il focus principale del progetto è il **Booking Subsystem**, che gestisce il flusso critico di acquisto dei biglietti. Il sistema è progettato per:

* **Utenti**: Visualizzazione catalogo, gestione carrello, checkout sicuro.
* **Sistema**: Gestione atomica delle transazioni, prevenzione dell'overbooking (Sold Out), integrazione con gateway di pagamento esterni.

---

## Architettura e Tecnologie

Il progetto segue il pattern architetturale **MVC (Model-View-Controller)** con un approccio modulare.

### Stack Tecnologico

* **Linguaggio**: Java 17
* **Build Tool**: Maven
* **Database**: MySQL 8.0
* **Pattern di Persistenza**: DAO (Data Access Object)
* **Testing**: JUnit 5 (Unit & Integration), Mockito (Mocking dipendenze)

### Struttura dei Package

* `com.anitour.model`: Entity (Booking, Cart, User) e logica di dominio.
* `com.anitour.control`: Controller (BookingControl) che gestiscono i flussi.
* `com.anitour.dao`: Interfacce e implementazioni per l'accesso al DB.

---

## Funzionalità Principali

* **Gestione Carrello**: Aggiunta, rimozione e validazione degli articoli in sessione.
* **Processo di Checkout**: Flusso transazionale che converte il carrello in un ordine confermato.
* **Controllo Concorrenza**: Gestione intelligente dello stato "Sold Out" per evitare vendite di biglietti esauriti durante richieste simultanee.
* **Simulazione Pagamenti**: Integrazione con Mock Payment Gateway per gestire scenari di successo e fallimento (es. carta rifiutata).

---

## Testing e Qualità

Il progetto pone un forte accento sulla qualità del software attraverso diverse strategie di test:

* **Unit Testing (White Box)**: Copertura logica delle classi core (`BookingControl`, `Cart`) utilizzando **Mockito** per isolare le dipendenze esterne.
* **Integration Testing (Sandwich)**: Verifica dell'interazione tra Controller e Database reale (DAO).
* **System Testing (Black Box)**: Applicazione del **Category Partition Method** per derivare casi di test funzionali completi (Happy Path, Error Cases, Boundary Values).

---

## Prerequisiti

Assicurati di avere installato sulla tua macchina:

* **Java JDK 17** o superiore.
* **MySQL Server 8.0** (o Docker container equivalente).
* **IntelliJ IDEA** (Consigliato per la compatibilità con JUnit 5).
* **Git** per il versionamento.

---

## Guida all'Installazione

### 1. Configurazione Database
1.  Creare un database MySQL vuoto denominato `AniTour`.
2.  Importare lo schema database presente nel file db/schema.sql per creare la struttura iniziale su MySQL.
3.  Configurare le credenziali nel file di properties o nella classe di connessione DB.

### 2. Importazione Progetto
1.  Aprire IntelliJ IDEA.
2.  Selezionare **File > Open** e scegliere la cartella del progetto.
3.  Attendere che Maven scarichi tutte le dipendenze (JUnit, Mockito, MySQL Connector).

### 3. Esecuzione dei Test
Per verificare che tutto funzioni correttamente:
* In IntelliJ, fare clic destro sulla cartella `src/test/java`.
* Selezionare **Run 'All Tests'**.
* Verificare che la barra sia verde (100% Pass).

---

## Autori

Progetto sviluppato per il corso di Ingegneria del Software (UNISA - 2025/2026).

* **Vincenzo Chiocca**
* **Salvatore Merola**
