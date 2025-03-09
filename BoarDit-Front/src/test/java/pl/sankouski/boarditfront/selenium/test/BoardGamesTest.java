package pl.sankouski.boarditfront.selenium.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pl.sankouski.boarditfront.selenium.GameDetailsPage;
import pl.sankouski.boarditfront.selenium.HomePage;
import pl.sankouski.boarditfront.selenium.LoginPage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardGamesTest {
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
    public void testHomePageDisplaysGamesAfterLogin() {
        LoginPage loginPage = new LoginPage(webDriver);
        HomePage homePage = loginPage
                .open()
                .loginAs("s@gmail.com", "1");

        assertTrue(homePage.isPageLoaded());
        assertFalse(homePage.getGameTitles().isEmpty());
    }

    @Test
    public void testViewDetailsButtonRedirectsToDetailsPage() {
        LoginPage loginPage = new LoginPage(webDriver);
        HomePage homePage = loginPage
                .open()
                .loginAs("s@gmail.com", "1");

        homePage.clickViewDetailsByGameIndex(0);

        GameDetailsPage detailsPage = new GameDetailsPage(webDriver);

        String gameName = detailsPage.getGameName();
        assertTrue(gameName != null && !gameName.isEmpty());

        String description = detailsPage.getDescription();
        assertTrue(description != null && !description.isEmpty());
    }

    @Test
    public void testAllGamesHaveTitleAndImage() {
        LoginPage loginPage = new LoginPage(webDriver);
        HomePage homePage = loginPage
                .open()
                .loginAs("s@gmail.com", "1");

        assertFalse(homePage.getGameTitles().isEmpty());

        for (int i = 0; i < homePage.getGameTitles().size(); i++) {
            String gameTitle = homePage.getGameTitles().get(i).getText();
            assertFalse(gameTitle.isEmpty());

            boolean hasImage = homePage.doesGameHaveImage(i);
            assertTrue(hasImage);
        }
    }
}