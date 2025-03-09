package pl.sankouski.boarditfront.selenium.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pl.sankouski.boarditfront.selenium.LoginPage;
import pl.sankouski.boarditfront.selenium.ProfilePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfilePageTest {
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
    public void testEditProfile() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("s@gmail.com", "1");

        ProfilePage profilePage = new ProfilePage(webDriver);
        profilePage.open();

        profilePage
                .fillFirstName("John")
                .fillLastName("Doe")
                .fillMiddleName("M.")
                .fillPassword("newSecurePassword123")
                .saveChanges();

        profilePage.open();
        assertTrue(profilePage.isFirstNameUpdated("John"));
        assertTrue(profilePage.isLastNameUpdated("Doe"));
        assertTrue(profilePage.isMiddleNameUpdated("M."));
    }
}