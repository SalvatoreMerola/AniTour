# AniTour - Booking System (Vertical Slice Demo)

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-Frontend-orange?style=for-the-badge)

**AniTour** è un sistema di e-commerce per la prenotazione di viaggi a tema (Nerd/Anime).

---

## 📄 Documentazione di Progetto (Deliverables)

Tutti i documenti di analisi e progettazione sono disponibili nella cartella `Deliverables`:

| Documento | Descrizione | File |
| :--- | :--- | :--- |
| **PS** | **Problem Statement**: Definizione del dominio e obiettivi. | [PS_AniTour.pdf](Deliverables/PS_AniTour.pdf) |
| **RAD** | **Requirements Analysis**: Requisiti funzionali, non funzionali e casi d'uso. | [RAD_AniTour.pdf](Deliverables/RAD_AniTour.pdf) |
| **SDD** | **Software Design**: Architettura di sistema e scelte tecnologiche. | [SDD_AniTour.pdf](Deliverables/SDD_AniTour.pdf) |
| **ODD** | **Object Design**: Diagrammi delle classi, sequence diagram e pattern. | [ODD_AniTour.pdf](Deliverables/ODD_AniTour.pdf) |
| **TP** | **Test Plan**: Strategia di testing e pianificazione. | [TP_AniTour.pdf](Deliverables/TP_AniTour.pdf) |
| **TCS** | **Test Case Spec**: Specifica dettagliata dei casi di test. | [TCS_AniTour.pdf](Deliverables/TCS_AniTour.pdf) |
| **TER** | **Test Exec Report**: Report finale dell'esecuzione dei test (JUnit/Selenium). | [TER_AniTour.pdf](Deliverables/TER_AniTour.pdf) |

---

## 🎯 Obiettivo della Demo

La demo copre il sottosistema critico di **Booking** implementando:
1.  **Catalogo Dinamico**: Caricamento tour da DB (Bloodborne, Sekiro, ecc.).
2.  **Logica Transazionale**: Controllo disponibilità posti (Inventory Check).
3.  **Persistenza**: Salvataggio ordini su MySQL.
4.  **System Testing**: Verifica automatizzata dei flussi tramite **Selenium WebDriver**.

---

## 🏗️ Architettura e Tecnologie

Il sistema rispetta l'architettura **ECB** definita nell'**SDD**:

* **Frontend**: JSP (JavaServer Pages) + CSS3.
* **Backend**: Servlet Java (`BookingServlet`) e Logic Layer (`BookingControl`).
* **Persistenza**: JDBC puro su Database MySQL 8.0.
* **Testing**: 
    * **Selenium**: System Testing (Black Box).
    * **JUnit 5**: Unit & Integration Testing.

---

## 🧪 Strategia di Testing (Selenium Automation)

In accordo con il **Test Plan (TP)**, sono stati automatizzati i seguenti scenari critici (`SystemTest.java`):

| ID Test | Scenario | Descrizione |
| :--- | :--- | :--- |
| **TC-01** | **Happy Path** | Acquisto completato con successo (Verifica decremento posti DB). |
| **TC-02** | **Sold Out** | Tentativo di acquisto tour esaurito (es. Sekiro). Verifica blocco. |
| **TC-03** | **Payment Fail** | Carta rifiutata dal gateway simulato. Verifica gestione eccezione. |
| **TC-04** | **Empty Cart** | Tentativo di checkout con carrello vuoto. |

---

## 🛠️ Guida all'Installazione

1.  **Database**: Eseguire lo script `db/schema.sql` per creare il DB `anitourdb` e popolare i dati.
2.  **Deployment**: Configurare Tomcat con artifact `war exploded` e context `/anitour`.
3.  **Esecuzione**: Avviare il server e navigare su `http://localhost:8080/anitour/home`.

---

## 👥 Autori

Progetto sviluppato per il corso di Ingegneria del Software (Unisa).

* **Vincenzo Chiocca**
* **Salvatore Merola**
