package com.myproject.pages;

import com.myproject.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {

    private final WebDriver driver;

    // --- Locators ---
    private final By flashMessage = By.id("flash");
    private final By logoutLink   = By.cssSelector("a[href='/logout']");
    private final By pageHeading  = By.tagName("h2");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    // Returns the flash/success message text after login
    public String getSuccessMessage() {
        return WaitUtil.waitForVisible(driver, flashMessage).getText().trim();
    }

    // Returns true if the h2 heading contains "Secure Area"
    public boolean isOnSecureArea() {
        try {
            String heading = WaitUtil.waitForVisible(driver, pageHeading).getText();
            return heading.contains("Secure Area");
        } catch (Exception e) {
            return false;
        }
    }

    // Clicks logout and waits for redirect to /login
    // Uses JS click as fallback for headless Chrome 148 where normal click
    // sometimes doesn't trigger navigation
    public void logout() {
        WebElement link = WaitUtil.waitForClickable(driver, logoutLink);

        try {
            link.click();
        } catch (Exception e) {
            // Fallback: JS click for headless mode
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }

        // Wait for redirect to login page after logout
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.urlContains("/login"));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
