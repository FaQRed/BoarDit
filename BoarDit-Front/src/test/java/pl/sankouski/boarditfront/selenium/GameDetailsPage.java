package pl.sankouski.boarditfront.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GameDetailsPage {
    private WebDriver webDriver;

    @FindBy(css = ".card-header h1")
    private WebElement gameNameHeader;

    @FindBy(css = ".card-body p:nth-of-type(1) span")
    private WebElement yearPublished;

    @FindBy(css = ".card-body p:nth-of-type(2) span")
    private WebElement players;

    @FindBy(css = ".card-body p:nth-of-type(3) span")
    private WebElement playingTime;

    @FindBy(css = ".card-body p:nth-of-type(4)")
    private WebElement description;

    public GameDetailsPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public String getGameName() {
        return gameNameHeader.getText();
    }

    public String getYearPublished() {
        return yearPublished.getText();
    }

    public String getPlayers() {
        return players.getText();
    }

    public String getPlayingTime() {
        return playingTime.getText();
    }

    public String getDescription() {
        return description.getText();
    }
}