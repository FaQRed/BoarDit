package pl.sankouski.boarditfront.selenium;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegistrationPage {
    private WebDriver webDriver;

    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    @FindBy(id = "middleName")
    private WebElement middleNameInput;

    @FindBy(id = "login")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement registerButton;

    @FindBy(css = "#firstName + .invalid-feedback")
    private WebElement firstNameError;

    @FindBy(css = "#lastName + .invalid-feedback")
    private WebElement lastNameError;

    @FindBy(css = "#middleName + .invalid-feedback")
    private WebElement middleNameError;

    @FindBy(css = "#login + .invalid-feedback")
    private WebElement emailError;

    @FindBy(css = "#password + .invalid-feedback")
    private WebElement passwordError;

    public RegistrationPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public RegistrationPage open() {
        webDriver.get("http://localhost:8081/registration");
        return this;
    }

    public RegistrationPage fillFirstName(String firstName) {
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        return this;
    }

    public RegistrationPage fillLastName(String lastName) {
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        return this;
    }

    public RegistrationPage fillMiddleName(String middleName) {
        middleNameInput.clear();
        middleNameInput.sendKeys(middleName);
        return this;
    }

    public RegistrationPage fillEmail(String email) {
        emailInput.clear();
        emailInput.sendKeys(email);
        return this;
    }

    public RegistrationPage fillPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
        return this;
    }

    public RegistrationPage submitForm() {
        registerButton.click();
        return this;
    }

    public String getFirstNameError() {
        return firstNameError.getText();
    }

    public String getLastNameError() {
        return lastNameError.getText();
    }

    public String getMiddleNameError() {
        return middleNameError.getText();
    }

    public String getEmailError() {
        return emailError.getText();
    }

    public String getPasswordError() {
        return passwordError.getText();
    }

    public boolean isFormValid() {
        return !webDriver.getPageSource().contains("was-validated");
    }
}