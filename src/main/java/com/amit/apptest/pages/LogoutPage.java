package com.amit.apptest.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LogoutPage {

    public final AppiumDriver driver;
    private final WebDriverWait wait;

    public LogoutPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private WebElement menuButton() {
        return (WebElement) wait.until(
                ExpectedConditions.elementToBeClickable(
                        AppiumBy.accessibilityId("test-Menu")
                )
        );
    }

    private WebElement logoutButton() {
        return (WebElement) wait.until(
                ExpectedConditions.elementToBeClickable(
                        AppiumBy.accessibilityId("test-LOGOUT")
                )
        );
    }

    public void logout() {
        menuButton().click();
        logoutButton().click();
    }
}
