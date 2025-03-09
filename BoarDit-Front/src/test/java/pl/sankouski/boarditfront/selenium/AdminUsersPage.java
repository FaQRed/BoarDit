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

public class AdminUsersPage {
    private WebDriver webDriver;

    @FindBy(tagName = "h2")
    private WebElement pageTitle;

    @FindBy(css = ".btn-success")
    private WebElement addUserButton;

    @FindBy(css = ".table tbody tr")
    private List<WebElement> userRows;

    @FindBy(css = "input[name='filterText']")
    private WebElement searchInput;

    @FindBy(css = "button[type='submit']")
    private WebElement searchButton;

    public AdminUsersPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public AdminUsersPage open() {
        webDriver.get("http://localhost:8081/admin/users");
        return this;
    }

    public boolean isPageLoaded() {
        return pageTitle.getText().equals("Users Management");
    }

    public int getUserCount() {
        return userRows.size();
    }

    public void clickAddUserButton() {
        addUserButton.click();
    }

    public void searchUser(String username) {
        searchInput.clear();
        searchInput.sendKeys(username);
        searchButton.click();
    }

    public boolean isUserPresent(String username) {
        return userRows.stream().anyMatch(row ->
                row.findElement(By.xpath(".//td[2]")).getText().equals(username));
    }

    public WebElement getUserRowByUsername(String username) {
        return userRows.stream().filter(row ->
                        row.findElement(By.xpath(".//td[2]")).getText().equals(username))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void editUser(String username, String newFirstName, String newLastName, String newStatus) {
        WebElement userRow = getUserRowByUsername(username);
        userRow.findElement(By.cssSelector(".btn-primary")).click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement editUserModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editUserModal")));

        editUserModal.findElement(By.id("editFirstName")).clear();
        editUserModal.findElement(By.id("editFirstName")).sendKeys(newFirstName);

        editUserModal.findElement(By.id("editLastName")).clear();
        editUserModal.findElement(By.id("editLastName")).sendKeys(newLastName);

        editUserModal.findElement(By.id("editStatus")).sendKeys(newStatus);

        editUserModal.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public void clickDeleteUser(String username) {
        WebElement userRow = getUserRowByUsername(username);
        userRow.findElement(By.cssSelector(".btn-danger")).click();
    }
}