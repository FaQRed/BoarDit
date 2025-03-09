package pl.sankouski.boarditfront.selenium.test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pl.sankouski.boarditfront.selenium.AdminMechanicsPage;
import pl.sankouski.boarditfront.selenium.LoginPage;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminMechanicsTest {
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
    @Order(1)
    public void testAddMechanic() {
        new LoginPage(webDriver)
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminMechanicsPage mechanicsPage = new AdminMechanicsPage(webDriver);
        mechanicsPage.open();

        mechanicsPage.addMechanic("Test Mechanic");

        assertTrue(mechanicsPage.isSuccessMessageDisplayed());
        assertTrue(mechanicsPage.isMechanicPresent("Test Mechanic"));
    }

    @Test
    @Order(2)
    public void testEditMechanic() {
        new LoginPage(webDriver)
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminMechanicsPage mechanicsPage = new AdminMechanicsPage(webDriver);
        mechanicsPage.open();

        String newMechanicName = "Updated Mechanic";
        mechanicsPage.editMechanic("Test Mechanic", newMechanicName);

        mechanicsPage.open();
        assertTrue(mechanicsPage.isMechanicPresent(newMechanicName));
    }

    @Test
    @Order(3)
    public void testDeleteMechanic() {
        new LoginPage(webDriver)
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminMechanicsPage mechanicsPage = new AdminMechanicsPage(webDriver);
        mechanicsPage.open();

        String lastMechanicId = mechanicsPage.getLastMechanicId();
        mechanicsPage.deleteMechanic(lastMechanicId);

        mechanicsPage.open();
        assertFalse(mechanicsPage.isMechanicPresent("Updated Mechanic"));
    }
}