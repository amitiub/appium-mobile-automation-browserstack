package com.amit.apptest.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public CartPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public boolean isDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//*[contains(@text,'YOUR CART')]")
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getCartItemCount() {
        List<WebElement> items = driver.findElements(
                AppiumBy.xpath("//*[contains(@content-desc,'test-Description')]")
        );
        return items.size();
    }
}