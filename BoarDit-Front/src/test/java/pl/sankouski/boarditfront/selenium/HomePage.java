package pl.sankouski.boarditfront.selenium;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    private WebDriver webDriver;

    @FindBy(tagName = "h1")
    private WebElement pageTitle;

    @FindBy(css = ".card-title")
    private List<WebElement> gameTitles;

    @FindBy(css = ".btn.btn-primary")
    private List<WebElement> viewDetailsButtons;

    @FindBy(css = ".card-img-top")
    private List<WebElement> gameImages;

    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public HomePage open() {
        webDriver.get("http://localhost:8081/");
        return this;
    }

    public String getPageTitle() {
        return pageTitle.getText();
    }

    public List<WebElement> getGameTitles() {
        return gameTitles;
    }

    public void clickViewDetailsByGameIndex(int index) {
        viewDetailsButtons.get(index).click();
    }


    public boolean isPageLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(gameTitles.get(0)));
            return pageTitle.getText().equals("Board Games");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean doesGameHaveImage(int index) {
        WebElement image = gameImages.get(index);
        return image != null && image.getAttribute("src") != null && !image.getAttribute("src").isEmpty();
    }
}