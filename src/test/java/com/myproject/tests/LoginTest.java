package com.myproject.tests;

import com.myproject.config.ConfigReader;
import com.myproject.pages.DashboardPage;
import com.myproject.pages.LoginPage;
import com.myproject.utils.ExtentReportManager;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private String baseUrl;

    @BeforeMethod
    public void initPages() {

        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        baseUrl = ConfigReader.getBaseUrl();
    }

    @Test(description = "Valid login navigates to Secure Area")
    public void tc01_validLoginTest() {

        ExtentReportManager.startTest(
                "TC01 - Valid Login",
                "Login with correct credentials");

        ExtentReportManager.getTest()
                .info("Navigating to login page");

        loginPage.navigateTo(baseUrl);

        ExtentReportManager.getTest()
                .info("Entering valid credentials");

        loginPage.login(
                ConfigReader.getUsername(),
                ConfigReader.getPassword());

        ExtentReportManager.getTest()
                .info("Asserting user is on Secure Area");

        Assert.assertTrue(
                dashboardPage.isOnSecureArea(),
                "Expected to be on Secure Area after valid login");

        String message = dashboardPage.getSuccessMessage();

        Assert.assertTrue(
                message.contains("You logged into a secure area"),
                "Expected success flash message, got: " + message);

        ExtentReportManager.getTest()
                .pass("Valid login passed - user reached Secure Area");
    }

    @Test(description = "Invalid password shows error message")
    public void tc02_invalidPasswordTest() {

        ExtentReportManager.startTest(
                "TC02 - Invalid Password",
                "Login with wrong password");

        loginPage.navigateTo(baseUrl);

        ExtentReportManager.getTest()
                .info("Entering valid username, wrong password");

        loginPage.login(
                ConfigReader.getUsername(),
                "WrongPassword123!");

        Assert.assertTrue(
                loginPage.isFlashMessageVisible(),
                "Expected flash error message to be visible");

        String message = loginPage.getFlashMessage();

        Assert.assertTrue(
                message.contains("Your password is invalid"),
                "Expected invalid password message, got: " + message);

        ExtentReportManager.getTest()
                .pass("Error message shown correctly for invalid password");
    }

    @Test(description = "Blank username shows error message")
    public void tc03_blankUsernameTest() {

        ExtentReportManager.startTest(
                "TC03 - Blank Username",
                "Login with empty username field");

        loginPage.navigateTo(baseUrl);

        ExtentReportManager.getTest()
                .info("Submitting login with blank username");

        loginPage.login(
                "",
                ConfigReader.getPassword());

        Assert.assertTrue(
                loginPage.isFlashMessageVisible(),
                "Expected flash error message for blank username");

        String message = loginPage.getFlashMessage();

        Assert.assertTrue(
                message.contains("Your username is invalid"),
                "Expected invalid username message, got: " + message);

        ExtentReportManager.getTest()
                .pass("Error message shown correctly for blank username");
    }

    @Test(description = "User can log out after logging in")
    public void tc04_logoutTest() {

        ExtentReportManager.startTest(
                "TC04 - Logout",
                "Login then logout successfully");

        loginPage.navigateTo(baseUrl);

        ExtentReportManager.getTest()
                .info("Logging in with valid credentials");

        loginPage.login(
                ConfigReader.getUsername(),
                ConfigReader.getPassword());

        Assert.assertTrue(
                dashboardPage.isOnSecureArea(),
                "Should be on Secure Area");

        ExtentReportManager.getTest()
                .info("Clicking logout");

        dashboardPage.logout();

        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(
                currentUrl.contains("/login"),
                "Expected to be back on login page after logout, URL: "
                        + currentUrl);

        ExtentReportManager.getTest()
                .pass("Logout successful - redirected to login page");
    }
}

