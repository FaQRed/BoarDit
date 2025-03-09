package pl.sankouski.boarditfront.selenium.test;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import pl.sankouski.boarditfront.selenium.HomePage;
import pl.sankouski.boarditfront.selenium.LoginPage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
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
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(webDriver);

        loginPage
                .open()
                .fillUsername("s@gmail.com")
                .fillPassword("1");

        HomePage homePage = loginPage.submitValidLogin();

        assertTrue(homePage.isPageLoaded ());
    }

    @Test
    public void testFailedLogin() {
        LoginPage loginPage = new LoginPage(webDriver);

        loginPage
                .open()
                .fillUsername("invalidUsername")
                .fillPassword("invalidPassword")
                .submitInvalidLogin();


        assertTrue(loginPage.isErrorAlertVisible());
    }
}