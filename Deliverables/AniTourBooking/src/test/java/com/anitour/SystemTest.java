package com.anitour;

import com.anitour.dao.DatabaseConnection; // Assicurati che l'import sia corretto in base al tuo package
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Configurazione Driver Selenium 4
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Assicuriamoci che il DB sia in uno stato pulito (50 posti disponibili) prima di iniziare
        updateTourSeats(1, 50);
    }

    @Test
    @Order(1)
    @DisplayName("TC-BOOK-01: System Test - Acquisto con Successo (Happy Path)")
    public void testAcquistoConSuccesso() {
        System.out.println("--- ESECUZIONE TC-BOOK-01: Happy Path ---");

        // 1. Apri la pagina
        driver.get("http://localhost:8080/anitour/checkout.jsp");
        System.out.println("Pagina aperta: " + driver.getTitle());

        // 2. Compila il form con dati validi
        driver.findElement(By.name("email")).sendKeys("mariorossi@email.com");
        driver.findElement(By.name("cardNumber")).sendKeys("1234-5678-9012-3456");
        System.out.println("Form compilato con dati validi.");

        // 3. Clicca il bottone di conferma
        WebElement confirmBtn = driver.findElement(By.cssSelector("button[type='submit']")); // O usa By.id se hai messo un id
        confirmBtn.click();
        System.out.println("Bottone 'Conferma e Paga' cliccato.");

        // 4. Verifica il risultato
        String pageSource = driver.getPageSource();
        boolean isSuccess = pageSource.contains("Ordine Confermato") || pageSource.contains("Nuovo ID:");

        if(isSuccess) {
            System.out.println("RISULTATO: Test Passato. Pagina di successo visualizzata.");
        } else {
            System.out.println("RISULTATO: Test Fallito. Messaggio di conferma non trovato.");
        }

        assertTrue(isSuccess, "La pagina non mostra il messaggio di conferma d'ordine.");
    }

    @Test
    @Order(2)
    @DisplayName("TC-BOOK-02: System Test - Fallimento per Sold Out")
    public void testSoldOut() {
        System.out.println("--- ESECUZIONE TC-BOOK-02: Sold Out ---");

        // PRE-CONDITION: Settiamo i posti a 0 nel DB per simulare il Sold Out
        updateTourSeats(1, 0);
        System.out.println("Pre-condition: Posti impostati a 0 nel Database.");

        // 1. Apri la pagina
        driver.get("http://localhost:8080/anitour/checkout.jsp");

        // 2. Compila il form (i dati sono validi, ma il tour è pieno)
        driver.findElement(By.name("email")).sendKeys("sfortunato@email.com");
        driver.findElement(By.name("cardNumber")).sendKeys("1234-5678-9012-3456");

        // 3. Clicca il bottone
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 4. Verifica il risultato (Ci aspettiamo l'eccezione SoldOutException)
        String pageSource = driver.getPageSource();
        // Cerchiamo la stringa dell'errore che la Servlet passa alla JSP
        boolean isSoldOut = pageSource.contains("SoldOutException") || pageSource.contains("Posti esauriti");

        if(isSoldOut) {
            System.out.println("RISULTATO: Test Passato. Errore Sold Out visualizzato correttamente.");
        } else {
            System.out.println("RISULTATO: Test Fallito. L'errore Sold Out non è apparso.");
        }

        // POST-CONDITION: Ripristiniamo i posti per non bloccare altri test
        updateTourSeats(1, 50);

        assertTrue(isSoldOut, "La pagina avrebbe dovuto mostrare l'errore SoldOutException.");
    }

    @Test
    @Order(3)
    @DisplayName("TC-BOOK-03: System Test - Pagamento Rifiutato")
    public void testPagamentoRifiutato() {
        System.out.println("--- ESECUZIONE TC-BOOK-03: Pagamento Rifiutato ---");

        // 1. Apri la pagina
        driver.get("http://localhost:8080/anitour/checkout.jsp");

        // 2. Compila il form con la carta che finisce per 9999 per far scattare l'errore
        driver.findElement(By.name("email")).sendKeys("povero@email.com");
        driver.findElement(By.name("cardNumber")).sendKeys("1234-1234-1234-9999");
        System.out.println("Form compilato con carta destinata al fallimento (9999).");

        // 3. Clicca il bottone
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 4. Verifica il risultato (Ci aspettiamo l'eccezione PaymentFailedException)
        String pageSource = driver.getPageSource();
        boolean isPaymentFailed = pageSource.contains("PaymentFailedException") || pageSource.contains("Pagamento rifiutato");

        if(isPaymentFailed) {
            System.out.println("RISULTATO: Test Passato. Errore Pagamento Rifiutato visualizzato correttamente.");
        } else {
            System.out.println("RISULTATO: Test Fallito. L'errore di pagamento non è apparso.");
        }

        assertTrue(isPaymentFailed, "La pagina avrebbe dovuto mostrare l'errore PaymentFailedException.");
    }

    @AfterEach
    public void tearDown() {
        // Chiudi il browser a fine test
        if (driver != null) {
            driver.quit();
        }
    }

    /* Metodo Helper per manipolare lo stato del Database direttamente dal Test.
       Necessario per creare le pre-condizioni (es. Sold Out) in modo automatico.
    */
    private void updateTourSeats(int tourId, int seats) {
        String sql = "UPDATE tour SET available_seats = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seats);
            ps.setInt(2, tourId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("ERRORE CRITICO NEL SETUP DEL TEST: Impossibile aggiornare i posti nel DB.");
            e.printStackTrace();
        }
    }
}