package pl.sankouski.boarditfront.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

public class ProfilePage {
    private WebDriver webDriver;

    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    @FindBy(id = "middleName")
    private WebElement middleNameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement saveChangesButton;

    public ProfilePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public ProfilePage open() {
        webDriver.get("http://localhost:8081/profile");
        return this;
    }

    public ProfilePage fillFirstName(String firstName) {
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        return this;
    }

    public ProfilePage fillLastName(String lastName) {
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        return this;
    }

    public ProfilePage fillMiddleName(String middleName) {
        middleNameInput.clear();
        middleNameInput.sendKeys(middleName);
        return this;
    }

    public ProfilePage fillPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
        return this;
    }

    public void saveChanges() {
        saveChangesButton.click();
    }

    public boolean isFirstNameUpdated(String expectedFirstName) {
        return Objects.equals(firstNameInput.getAttribute("value"), expectedFirstName);
    }

    public boolean isLastNameUpdated(String expectedLastName) {
        return Objects.equals(lastNameInput.getAttribute("value"), expectedLastName);
    }

    public boolean isMiddleNameUpdated(String expectedMiddleName) {
        return Objects.equals(middleNameInput.getAttribute("value"), expectedMiddleName);
    }
}