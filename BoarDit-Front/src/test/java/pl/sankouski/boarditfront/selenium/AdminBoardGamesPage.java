package pl.sankouski.boarditfront.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AdminBoardGamesPage {
    private WebDriver webDriver;

    @FindBy(tagName = "h2")
    private WebElement pageTitle;

    @FindBy(css = ".table tbody tr")
    private List<WebElement> gameRows;

    @FindBy(css = ".btn-success")
    private WebElement addGameButton;

    public AdminBoardGamesPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public AdminBoardGamesPage open() {
        webDriver.get("http://localhost:8081/admin/boardgames");
        return this;
    }

    public boolean isPageLoaded() {
        return pageTitle.getText().equals("Board Games Management");
    }

    public boolean isGamePresent(String gameId) {
        return gameRows.stream().anyMatch(row ->
                row.findElement(By.xpath(".//td[1]")).getText().equals(gameId));
    }

    public void deleteGameById(String gameId) {
        WebElement gameRow = gameRows.stream()
                .filter(row -> row.findElement(By.xpath(".//td[1]")).getText().equals(gameId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        gameRow.findElement(By.cssSelector(".btn-danger")).click();
    }

    public String getLastAddedGameId() {
        if (gameRows.isEmpty()) {
            throw new IllegalStateException("No games found in the table.");
        }
        WebElement lastGameRow = gameRows.get(gameRows.size() - 1);
        return lastGameRow.findElement(By.xpath(".//td[1]")).getText();
    }
}