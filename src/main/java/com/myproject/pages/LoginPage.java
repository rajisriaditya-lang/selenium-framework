package com.myproject.pages;

import com.myproject.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;

    // --- Locators ---
    private final By usernameField  = By.id("username");
    private final By passwordField  = By.id("password");
    private final By loginButton    = By.cssSelector("button[type='submit']");
    private final By errorMessage   = By.id("flash");
    private final By loginContainer = By.id("login");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Navigate to login page
    public void navigateTo(String url) {
        driver.get(url + "/login");
        WaitUtil.waitForVisible(driver, loginContainer);
    }

    // Enter username — sends empty string safely if blank
    public void enterUsername(String username) {
        WaitUtil.waitForVisible(driver, usernameField).clear();
        if (username != null && !username.isEmpty()) {
            driver.findElement(usernameField).sendKeys(username);
        }
    }

    // Enter password
    public void enterPassword(String password) {
        driver.findElement(passwordField).clear();
        if (password != null && !password.isEmpty()) {
            driver.findElement(passwordField).sendKeys(password);
        }
    }

    // Click the login button and wait for page to settle
    public void clickLogin() {
        WaitUtil.waitForClickable(driver, loginButton).click();

        // Wait for flash message OR login container to appear after submit
        // This prevents Chrome 148 DevTools disconnection on fast redirects
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(errorMessage),
                        ExpectedConditions.urlContains("/secure")
                ));
    }

    // Full login in one call
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // Returns the flash message text (success or error)
    public String getFlashMessage() {
        return WaitUtil.waitForVisible(driver, errorMessage).getText().trim();
    }

    // True if the flash element is visible
    public boolean isFlashMessageVisible() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
