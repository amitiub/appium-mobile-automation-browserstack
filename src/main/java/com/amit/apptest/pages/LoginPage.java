package com.amit.apptest.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    private final By username = By.xpath("//android.widget.EditText[@content-desc='test-Username']");
    private final By password = By.xpath("//android.widget.EditText[@content-desc='test-Password']");
    private final By loginBtn = By.xpath("//android.view.ViewGroup[@content-desc='test-LOGIN']");

    public LoginPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void performLogin() {

        boolean onProductsPage = new ProductsPage(driver).isDisplayed();
        if (onProductsPage) {
            System.out.println("Already logged in! skipping login");
            return;
        }

        WebElement usernameField = (WebElement) wait.until(
                ExpectedConditions.elementToBeClickable(username));
        usernameField.clear();
        usernameField.sendKeys("standard_user");

        WebElement passwordField = (WebElement) wait.until(
                ExpectedConditions.elementToBeClickable(password));
        passwordField.clear();
        passwordField.sendKeys("secret_sauce");

        WebElement loginButton = (WebElement) wait.until(
                ExpectedConditions.elementToBeClickable(loginBtn));
        loginButton.click();
    }
}
