package pl.sankouski.boarditfront.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AdminPanelPage {
    private WebDriver webDriver;

    @FindBy(tagName = "h1")
    private WebElement header;

    @FindBy(linkText = "Go to Users")
    private WebElement usersSectionLink;

    @FindBy(linkText = "Go to Updater")
    private WebElement updaterSectionLink;

    @FindBy(linkText = "Go to Board Games")
    private WebElement boardGamesSectionLink;

    @FindBy(linkText = "Go to Mechanics")
    private WebElement mechanicsSectionLink;

    @FindBy(linkText = "Go to Categories")
    private WebElement categoriesSectionLink;

    public AdminPanelPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public AdminPanelPage open() {
        webDriver.get("http://localhost:8081/admin");
        return this;
    }

    public boolean isPageLoaded() {
        return header.getText().equals("Welcome to Admin Panel");
    }

    public void goToUsersSection() {
        usersSectionLink.click();
    }

    public void goToUpdaterSection() {
        updaterSectionLink.click();
    }

    public void goToBoardGamesSection() {
        boardGamesSectionLink.click();
    }

    public void goToMechanicsSection() {
        mechanicsSectionLink.click();
    }

    public void goToCategoriesSection() {
        categoriesSectionLink.click();
    }
}