package pl.sankouski.boarditfront.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BoardGameUpdaterPage {
    private WebDriver webDriver;

    @FindBy(id = "gameId")
    private WebElement gameIdInput;

    @FindBy(css = "button[type='submit']")
    private WebElement updateGameButton;

    @FindBy(css = ".alert-success")
    private WebElement successAlert;

    @FindBy(css = ".alert-danger")
    private WebElement errorAlert;

    public BoardGameUpdaterPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public BoardGameUpdaterPage open() {
        webDriver.get("http://localhost:8081/admin/updater");
        return this;
    }

    public void enterGameId(String gameId) {
        gameIdInput.clear();
        gameIdInput.sendKeys(gameId);
    }

    public void clickUpdateGameButton() {
        updateGameButton.click();
    }

    public boolean isSuccessMessageDisplayed() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOf(successAlert)).isDisplayed();
    }

    public boolean isErrorMessageDisplayed() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOf(errorAlert)).isDisplayed();
    }

    public String getSuccessMessage() {
        return successAlert.getText();
    }

    public String getErrorMessage() {
        return errorAlert.getText();
    }
}