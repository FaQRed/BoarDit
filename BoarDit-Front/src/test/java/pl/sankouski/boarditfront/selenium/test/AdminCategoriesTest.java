package pl.sankouski.boarditfront.selenium.test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pl.sankouski.boarditfront.selenium.AdminCategoriesPage;
import pl.sankouski.boarditfront.selenium.LoginPage;


import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminCategoriesTest {
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
    public void testAddCategory() {
        new LoginPage(webDriver)
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminCategoriesPage categoriesPage = new AdminCategoriesPage(webDriver);
        categoriesPage.open();

        categoriesPage.addCategory("Test Category");

        assertTrue(categoriesPage.isSuccessMessageDisplayed());
        assertTrue(categoriesPage.getSuccessMessage().contains("Category added successfully!"));
        assertTrue(categoriesPage.isCategoryPresent("Test Category"));
    }

    @Test
    @Order(2)
    public void testEditCategory() {
        new LoginPage(webDriver)
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminCategoriesPage categoriesPage = new AdminCategoriesPage(webDriver);
        categoriesPage.open();

        categoriesPage.editCategory("Test Category", "Updated Category");

        assertTrue(categoriesPage.isCategoryPresent("Updated Category"));
        assertFalse(categoriesPage.isCategoryPresent("Test Category"));
    }

    @Test
    @Order(3)
    public void testDeleteCategory() {
        new LoginPage(webDriver)
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        AdminCategoriesPage categoriesPage = new AdminCategoriesPage(webDriver);
        categoriesPage.open();

        assertTrue(categoriesPage.isCategoryPresent("Updated Category"));

        categoriesPage.deleteCategory("Updated Category");

        assertFalse(categoriesPage.isCategoryPresent("Updated Category"));
    }
}