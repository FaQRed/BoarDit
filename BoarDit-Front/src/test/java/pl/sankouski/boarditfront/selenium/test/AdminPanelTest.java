package pl.sankouski.boarditfront.selenium.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pl.sankouski.boarditfront.selenium.AdminPanelPage;
import pl.sankouski.boarditfront.selenium.LoginPage;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminPanelTest {
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
    public void testAdminPanelAccess() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminPanelPage adminPanelPage = new AdminPanelPage(webDriver);
        adminPanelPage.open();

        assertTrue(adminPanelPage.isPageLoaded());
    }

    @Test
    public void testNavigateToUsersSection() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminPanelPage adminPanelPage = new AdminPanelPage(webDriver);
        adminPanelPage.open();
        adminPanelPage.goToUsersSection();

        assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains("/admin/users"));
    }

    @Test
    public void testNavigateToUpdaterSection() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminPanelPage adminPanelPage = new AdminPanelPage(webDriver);
        adminPanelPage.open();
        adminPanelPage.goToUpdaterSection();

        assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains("/admin/updater"));
    }

    @Test
    public void testNavigateToBoardGamesSection() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                    .loginAs("admin@gmail.com", "adminpassword");

        AdminPanelPage adminPanelPage = new AdminPanelPage(webDriver);
        adminPanelPage.open();
        adminPanelPage.goToBoardGamesSection();

        assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains("/admin/boardgames"));
    }

    @Test
    public void testNavigateToMechanicsSection() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminPanelPage adminPanelPage = new AdminPanelPage(webDriver);
        adminPanelPage.open();
        adminPanelPage.goToMechanicsSection();

        assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains("/admin/mechanics"));
    }

    @Test
    public void testNavigateToCategoriesSection() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminPanelPage adminPanelPage = new AdminPanelPage(webDriver);
        adminPanelPage.open();
        adminPanelPage.goToCategoriesSection();

        assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains("/admin/categories"));
    }
}