package pl.sankouski.boarditfront.selenium.test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pl.sankouski.boarditfront.selenium.BoardGameUpdaterPage;
import pl.sankouski.boarditfront.selenium.AdminBoardGamesPage;
import pl.sankouski.boarditfront.selenium.LoginPage;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardGameUpdaterTest {
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
    public void testAddGameSuccessfully() {
        // Логин
        new LoginPage(webDriver)
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        // Переход на апдейтер и добавление игры
        BoardGameUpdaterPage updaterPage = new BoardGameUpdaterPage(webDriver);
        updaterPage.open();
        updaterPage.enterGameId("437306");
        updaterPage.clickUpdateGameButton();

        // Проверка успешного сообщения
        assertTrue(updaterPage.isSuccessMessageDisplayed());
        assertTrue(updaterPage.getSuccessMessage().contains("Was added/updated Parks (second edition) game"));

        // Проверка, что игра добавлена
        AdminBoardGamesPage boardGamesPage = new AdminBoardGamesPage(webDriver);
        boardGamesPage.open();
        assertTrue(boardGamesPage.isGamePresent(boardGamesPage.getLastAddedGameId()));
    }

    @Test
    @Order(2)
    public void testAddGameWithInvalidId() {
        // Логин
        new LoginPage(webDriver)
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        // Попытка добавить игру с несуществующим ID
        BoardGameUpdaterPage updaterPage = new BoardGameUpdaterPage(webDriver);
        updaterPage.open();
        updaterPage.enterGameId("9999999"); // Несуществующий ID
        updaterPage.clickUpdateGameButton();

        // Проверка сообщения об ошибке
        assertTrue(updaterPage.isErrorMessageDisplayed());
        assertTrue(updaterPage.getErrorMessage().contains("Game not found: {\"message\":\"Board game with ID 9999999 not found on BoardGameGeek\"}"));
    }

    @Test
    @Order(3)
    public void testDeleteAddedGame() {
        // Логин
        new LoginPage(webDriver)
                .open()
                .loginAs("admin@gmail.com", "adminpassword");

        // Проверка наличия игры
        AdminBoardGamesPage boardGamesPage = new AdminBoardGamesPage(webDriver);
        boardGamesPage.open();
        assertTrue(boardGamesPage.isGamePresent(boardGamesPage.getLastAddedGameId()));
        String id = boardGamesPage.getLastAddedGameId();
        // Удаление игры
        boardGamesPage.deleteGameById(id);

        boardGamesPage.open();
        assertFalse(boardGamesPage.isGamePresent(id));
    }
}