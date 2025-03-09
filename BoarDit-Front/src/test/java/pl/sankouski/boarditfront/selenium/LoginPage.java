package pl.sankouski.boarditfront.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private WebDriver webDriver;

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(className = "alert-danger")
    private WebElement errorAlert;

    public LoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public LoginPage open() {
        webDriver.get("http://localhost:8081/auth/login");
        return this;
    }

    public LoginPage fillUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    public LoginPage fillPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    public HomePage submitValidLogin() {
        loginButton.click();
        return new HomePage(webDriver);
    }

    public LoginPage submitInvalidLogin() {
        loginButton.click();
        return this;
    }

    public boolean isErrorAlertVisible() {
        return errorAlert.isDisplayed();
    }

    public HomePage loginAs(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
        return new HomePage(webDriver);
    }
}