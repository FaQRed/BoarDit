package pl.sankouski.boarditfront.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AdminMechanicsPage {
    private WebDriver webDriver;

    @FindBy(tagName = "h2")
    private WebElement pageTitle;

    @FindBy(css = ".btn-primary.mb-3")
    private WebElement addMechanicButton;

    @FindBy(css = ".table tbody tr")
    private List<WebElement> mechanicRows;

    @FindBy(css = ".alert-success")
    private WebElement successAlert;

    public AdminMechanicsPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public AdminMechanicsPage open() {
        webDriver.get("http://localhost:8081/admin/mechanics");
        return this;
    }

    public void addMechanic(String name) {
        scrollToElement(addMechanicButton);
        addMechanicButton.click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addMechanicModal")));

        modal.findElement(By.id("addMechanicName")).sendKeys(name);
        modal.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public void editMechanic(String mechanicName, String newMechanicName) {
        WebElement mechanicRow = mechanicRows.stream()
                .filter(row -> row.findElement(By.xpath(".//td[2]")).getText().equals(mechanicName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Mechanic not found"));

        WebElement editButton = mechanicRow.findElement(By.cssSelector(".btn-primary"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(editButton).click().perform();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement editMechanicModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editMechanicModal")));

        WebElement nameInput = editMechanicModal.findElement(By.id("editMechanicName"));
        nameInput.clear();
        nameInput.sendKeys(newMechanicName);
        editMechanicModal.findElement(By.cssSelector("button[type='submit']")).click();

        open();

        mechanicRow = mechanicRows.stream()
                .filter(row -> row.findElement(By.xpath(".//td[2]")).getText().equals(newMechanicName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Updated mechanic not found"));
    }

    public void deleteMechanic(String id) {
        WebElement mechanicRow = getMechanicRowById(id);
        WebElement deleteButton = mechanicRow.findElement(By.cssSelector(".btn-danger"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(deleteButton).click().perform();
    }

    public WebElement getMechanicRowById(String id) {
        return mechanicRows.stream()
                .filter(row -> row.findElement(By.xpath(".//td[1]")).getText().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Mechanic not found"));
    }

    public boolean isMechanicPresent(String name) {
        return mechanicRows.stream()
                .anyMatch(row -> row.findElement(By.xpath(".//td[2]")).getText().equals(name));
    }

    public boolean isSuccessMessageDisplayed() {
        return successAlert != null && successAlert.isDisplayed();
    }

    public String getLastMechanicId() {
        if (mechanicRows.isEmpty()) {
            throw new IllegalStateException("No mechanics found.");
        }
        return mechanicRows.get(mechanicRows.size() - 1).findElement(By.xpath(".//td[1]")).getText();
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}