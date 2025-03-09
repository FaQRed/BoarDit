package pl.sankouski.boarditfront.selenium.test;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pl.sankouski.boarditfront.selenium.RegistrationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationTest {
    private WebDriver webDriver;

    @BeforeEach
    public void setup() {
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    public void testSuccessfulRegistration() {
        RegistrationPage registrationPage = new RegistrationPage(webDriver);

        registrationPage
                .open()
                .fillFirstName("John")
                .fillLastName("Doe")
                .fillMiddleName("M.")
                .fillEmail("john.doe@example.com")
                .fillPassword("SecurePassword123")
                .submitForm();

        assertTrue(registrationPage.isFormValid());
    }

    @Test
    public void testEmptyFieldsValidation() {
        RegistrationPage registrationPage = new RegistrationPage(webDriver);

        registrationPage
                .open()
                .submitForm();

        assertEquals("Please enter your first name.", registrationPage.getFirstNameError());
        assertEquals("Please enter your last name.", registrationPage.getLastNameError());
        assertEquals("Please enter your middle name.", registrationPage.getMiddleNameError());
        assertEquals("Please enter a valid email address.", registrationPage.getEmailError());
        assertEquals("Please enter your password.", registrationPage.getPasswordError());
    }

    @Test
    public void testInvalidEmailValidation() {
        RegistrationPage registrationPage = new RegistrationPage(webDriver);

        registrationPage
                .open()
                .fillFirstName("John")
                .fillLastName("Doe")
                .fillMiddleName("M.")
                .fillEmail("invalid-email")
                .fillPassword("SecurePassword123")
                .submitForm();
        assertEquals("Please enter a valid email address.", registrationPage.getEmailError());
    }
}