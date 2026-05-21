package com.myproject.tests;

import com.myproject.config.ConfigReader;
import com.myproject.utils.ExtentReportManager;
import com.myproject.utils.ScreenshotUtil;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {

        String browser = ConfigReader.getBrowser().trim().toLowerCase();
        boolean headless = ConfigReader.isHeadless();

        if (browser.equals("firefox")) {

            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("--headless");
            }
            driver = new FirefoxDriver(options);

        } else {

            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if (headless) {
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
            }
            options.addArguments("--window-size=1920,1080");

            // FIX: Disable Chrome's aggressive connection cleanup on Chrome 148
            options.addArguments("--disable-features=NetworkServiceInProcess");
            options.addArguments("--remote-allow-origins=*");

            driver = new ChromeDriver(options);
        }

        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        // FIX: Guard screenshot capture against dead/closed browser sessions
        if (result.getStatus() == ITestResult.FAILURE) {

            try {
                String screenshotPath = ScreenshotUtil.capture(driver, result.getName());

                if (ExtentReportManager.getTest() != null) {
                    try {
                        String relativePath = ".." + File.separator + screenshotPath;
                        ExtentReportManager.getTest()
                                .fail("Test FAILED - screenshot: " + screenshotPath)
                                .addScreenCaptureFromPath(relativePath);
                    } catch (Exception e) {
                        ExtentReportManager.getTest()
                                .fail("Test FAILED (screenshot attach failed): " + e.getMessage());
                    }
                }

            } catch (Exception e) {
                // Browser session was already dead — log but don't rethrow
                System.out.println("WARN: Could not capture screenshot (session closed): "
                        + e.getMessage());
                if (ExtentReportManager.getTest() != null) {
                    ExtentReportManager.getTest()
                            .fail("Test FAILED (browser session lost before screenshot)");
                }
            }
        }

        // FIX: Guard quit() against already-closed sessions
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("WARN: driver.quit() failed (session already closed): "
                        + e.getMessage());
            }
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        ExtentReportManager.flushReport();
        System.out.println("\n========================================");
        System.out.println(" Report saved to: reports/TestReport.html");
        System.out.println("========================================\n");
    }
}
