package pl.sankouski.boarditfront.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AdminCategoriesPage {
    private WebDriver webDriver;

    @FindBy(tagName = "h2")
    private WebElement pageTitle;

    @FindBy(css = ".btn-primary.mb-3")
    private WebElement addCategoryButton;

    @FindBy(css = ".table tbody tr")
    private List<WebElement> categoryRows;

    @FindBy(css = ".alert-success")
    private WebElement successAlert;

    @FindBy(css = ".alert-danger")
    private WebElement errorAlert;

    public AdminCategoriesPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public AdminCategoriesPage open() {
        webDriver.get("http://localhost:8081/admin/categories");
        return this;
    }

    public boolean isPageLoaded() {
        return pageTitle.getText().equals("Manage Categories");
    }

    public void addCategory(String name) {
        addCategoryButton.click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCategoryModal")));

        modal.findElement(By.id("addCategoryName")).sendKeys(name);
        modal.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public void editCategory(String categoryName, String newCategoryName) {
        WebElement categoryRow = getCategoryRowByName(categoryName);

        WebElement editButton = categoryRow.findElement(By.cssSelector(".btn-primary"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(editButton).click().perform();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement editCategoryModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editCategoryModal")));

        WebElement nameInput = editCategoryModal.findElement(By.id("editCategoryName"));
        nameInput.clear();
        nameInput.sendKeys(newCategoryName);

        editCategoryModal.findElement(By.cssSelector("button[type='submit']")).click();

        // Перезагрузка страницы для получения обновленных данных
        open();
    }

    public void deleteCategory(String name) {
        WebElement categoryRow = getCategoryRowByName(name);
        WebElement deleteButton = categoryRow.findElement(By.cssSelector(".btn-danger"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(deleteButton).click().perform();
    }

    public WebElement getCategoryRowByName(String name) {
        return categoryRows.stream()
                .filter(row -> row.findElement(By.xpath(".//td[2]")).getText().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + name));
    }

    public boolean isCategoryPresent(String name) {
        return categoryRows.stream()
                .anyMatch(row -> row.findElement(By.xpath(".//td[2]")).getText().equals(name));
    }

    public boolean isSuccessMessageDisplayed() {
        return successAlert != null && successAlert.isDisplayed();
    }

    public boolean isErrorMessageDisplayed() {
        return errorAlert != null && errorAlert.isDisplayed();
    }

    public String getSuccessMessage() {
        return successAlert.getText();
    }

    public String getErrorMessage() {
        return errorAlert.getText();
    }
}