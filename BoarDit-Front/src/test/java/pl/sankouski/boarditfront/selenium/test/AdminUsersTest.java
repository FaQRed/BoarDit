package pl.sankouski.boarditfront.selenium.test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.sankouski.boarditfront.selenium.AdminPanelPage;
import pl.sankouski.boarditfront.selenium.AdminUsersPage;
import pl.sankouski.boarditfront.selenium.LoginPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminUsersTest {
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
    public void testNavigateToUsersPage() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminPanelPage adminPanelPage = new AdminPanelPage(webDriver);
        adminPanelPage.open();

        AdminUsersPage adminUsersPage = new AdminUsersPage(webDriver);
        adminUsersPage.open();

        assertTrue(adminUsersPage.isPageLoaded());
    }

    @Test
    @Order(2)
    public void testAddUser() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminUsersPage adminUsersPage = new AdminUsersPage(webDriver);
        adminUsersPage.open();
        int initialUserCount = adminUsersPage.getUserCount();

        adminUsersPage.clickAddUserButton();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement addUserModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addUserModal")));

        addUserModal.findElement(By.id("login")).sendKeys("testuser");
        addUserModal.findElement(By.id("password")).sendKeys("password123");
        addUserModal.findElement(By.id("firstName")).sendKeys("Test");
        addUserModal.findElement(By.id("lastName")).sendKeys("User");
        addUserModal.findElement(By.id("status")).sendKeys("ACTIVE");
        addUserModal.findElement(By.cssSelector("button[type='submit']")).click();

        adminUsersPage.open();
        assertTrue(adminUsersPage.getUserCount() > initialUserCount);
    }

    @Test
    @Order(3)
    public void testSearchUser() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminUsersPage adminUsersPage = new AdminUsersPage(webDriver);
        adminUsersPage.open();

        adminUsersPage.searchUser("testuser");
        assertTrue(adminUsersPage.isUserPresent("testuser"));
    }

    @Test
    @Order(4)
    public void testEditUser() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminUsersPage adminUsersPage = new AdminUsersPage(webDriver);
        adminUsersPage.open();

        String originalUsername = "testuser";
        String newFirstName = "UpdatedFirst";
        String newLastName = "UpdatedLast";
        String newStatus = "BANE";

        adminUsersPage.searchUser(originalUsername);
        assertTrue(adminUsersPage.isUserPresent(originalUsername));

        adminUsersPage.editUser(originalUsername, newFirstName, newLastName, newStatus);

        adminUsersPage.open();
        adminUsersPage.searchUser(originalUsername);

        WebElement updatedUserRow = adminUsersPage.getUserRowByUsername(originalUsername);
        String displayedFullName = updatedUserRow.findElement(By.xpath(".//td[3]")).getText();
        String displayedStatus = updatedUserRow.findElement(By.xpath(".//td[4]")).getText();

        assertTrue(displayedFullName.contains(newFirstName));
        assertTrue(displayedFullName.contains(newLastName));
        assertEquals(newStatus, displayedStatus);
    }

    @Test
    @Order(5)
    public void testDeleteUser() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminUsersPage adminUsersPage = new AdminUsersPage(webDriver);
        adminUsersPage.open();
        adminUsersPage.searchUser("testuser");

        assertTrue(adminUsersPage.isUserPresent("testuser"));

        adminUsersPage.clickDeleteUser("testuser");

        adminUsersPage.open();
        adminUsersPage.searchUser("testuser");

        assertFalse(adminUsersPage.isUserPresent("testuser"));
    }
}