package com.anitour;

import com.anitour.dao.DatabaseConnection;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions; // IMPORTANTE
import org.openqa.selenium.support.ui.WebDriverWait;     // IMPORTANTE

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemTest {

    private WebDriver driver;
    // Modifica se necessario
    private static final String BASE_URL = "http://localhost:8080/anitour";

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        // RESET DATI NEL DB
        updateTourSeats(3, 50); // Persona 5
        updateTourSeats(2, 0);  // Sekiro
    }

    @Test
    @Order(1)
    @DisplayName("TC-BOOK-01: Happy Path")
    public void testAcquistoConSuccesso() {
        System.out.println("--- ESECUZIONE TC-BOOK-01: Happy Path ---");

        driver.get(BASE_URL + "/home");

        // Click su Persona 5
        WebElement personaCardBtn = driver.findElement(By.cssSelector("a[href*='tourId=3'] button"));
        personaCardBtn.click();

        // Compila form
        driver.findElement(By.name("shippingEmail")).sendKeys("mariorossi@email.com");
        driver.findElement(By.name("cardNumber")).sendKeys("1234-5678-9012-3456");

        // Click Conferma
        driver.findElement(By.className("btn-place-order")).click();

        // --- ATTESA ESPLICITA ---
        // Aspettiamo che appaia l'icona di successo o il testo specifico
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("confirmation-icon")));
        } catch (Exception e) {
            System.out.println("Timeout: La pagina di successo non è apparsa in tempo.");
        }

        // Verifica
        String pageSource = driver.getPageSource();
        boolean isSuccess = pageSource.contains("Ordine completato") || pageSource.contains("CODICE ORDINE");

        if(isSuccess) {
            System.out.println("RISULTATO: Test Passato ✅. Pagina di conferma visualizzata.");
        } else {
            System.out.println("RISULTATO: Test Fallito ❌. Siamo rimasti sul checkout?");
        }

        assertTrue(isSuccess, "Il test Happy Path è fallito: non siamo atterrati sulla pagina di successo.");
    }

    @Test
    @Order(2)
    @DisplayName("TC-BOOK-02: Sold Out")
    public void testSoldOut() {
        System.out.println("--- ESECUZIONE TC-BOOK-02: Sold Out ---");

        driver.get(BASE_URL + "/prenota?tourId=2"); // Sekiro (0 posti)

        driver.findElement(By.name("shippingEmail")).sendKeys("temerario@email.com");
        driver.findElement(By.name("cardNumber")).sendKeys("1234-5678-9012-3456");

        driver.findElement(By.className("btn-place-order")).click();

        // --- CORREZIONE: ATTESA ESPLICITA ---
        // Aspettiamo che appaia il banner di errore
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error-banner")));

        String pageSource = driver.getPageSource();
        boolean isSoldOutMessage = pageSource.contains("SoldOutException") || pageSource.contains("esauriti");

        if(isSoldOutMessage) {
            System.out.println("RISULTATO: Test Passato ✅. Banner Sold Out visualizzato.");
        } else {
            System.out.println("RISULTATO: Test Fallito ❌.");
        }

        assertTrue(isSoldOutMessage, "Doveva apparire l'errore di Sold Out.");
    }

    @Test
    @Order(3)
    @DisplayName("TC-BOOK-03: Pagamento Rifiutato")
    public void testPagamentoRifiutato() {
        System.out.println("--- ESECUZIONE TC-BOOK-03: Pagamento Rifiutato ---");

        driver.get(BASE_URL + "/prenota?tourId=1");

        driver.findElement(By.name("shippingEmail")).sendKeys("povero@email.com");
        // Carta che attiva il rifiuto nel RealPaymentGateway
        driver.findElement(By.name("cardNumber")).sendKeys("1234-1234-1234-9999");

        driver.findElement(By.className("btn-place-order")).click();

        // --- ATTESA ESPLICITA ---
        // Aspettiamo il banner di errore
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error-banner")));

        String pageSource = driver.getPageSource();
        // Cerchiamo il messaggio generico di errore o l'eccezione
        boolean isPaymentError = pageSource.contains("PaymentFailedException") ||
                pageSource.contains("rifiutato") ||
                pageSource.contains("Rifiutato"); // Case insensitive check

        if(isPaymentError) {
            System.out.println("RISULTATO: Test Passato ✅. Errore Pagamento visualizzato.");
        } else {
            System.out.println("RISULTATO: Test Fallito ❌. Pagina: " + pageSource);
        }

        assertTrue(isPaymentError, "Doveva apparire l'errore PaymentFailedException.");
    }

    @Test
    @Order(4)
    @DisplayName("TC-BOOK-04: Carrello Vuoto")
    public void testCarrelloVuoto() {
        System.out.println("--- ESECUZIONE TC-BOOK-04: Carrello Vuoto ---");

        driver.get(BASE_URL + "/prenota?tourId=1");

        // Email che attiva il carrello vuoto nel BookingServlet
        driver.findElement(By.name("shippingEmail")).sendKeys("empty@test.com");
        driver.findElement(By.name("cardNumber")).sendKeys("1234-5678-9012-3456");

        driver.findElement(By.className("btn-place-order")).click();

        // --- ATTESA ESPLICITA ---
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error-banner")));

        String pageSource = driver.getPageSource();
        // Controlliamo minuscolo/maiuscolo per sicurezza
        boolean isEmptyError = pageSource.contains("Carrello vuoto") || pageSource.contains("carrello è vuoto");

        if(isEmptyError) {
            System.out.println("RISULTATO: Test Passato ✅. Errore Carrello Vuoto visualizzato.");
        } else {
            System.out.println("RISULTATO: Test Fallito ❌.");
        }

        assertTrue(isEmptyError, "Doveva apparire l'errore per carrello vuoto.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void updateTourSeats(int tourId, int seats) {
        String sql = "UPDATE tour SET available_seats = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seats);
            ps.setInt(2, tourId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}