package com.amit.apptest.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductsPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public ProductsPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public boolean isDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//*[contains(@content-desc,'test-Item title')]")
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void addFirstProductToCart() {
        List<WebElement> addButtons = driver.findElements(
                AppiumBy.xpath("//*[contains(@text,'ADD TO CART')]")
        );
        if (!addButtons.isEmpty()) {
            addButtons.get(0).click();
        }
    }

    public void openCart() {
        WebElement cart = (WebElement) wait.until(
                ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("test-Cart"))
        );
        cart.click();
    }

    public void openMenu() {
        WebElement menu = (WebElement) wait.until(
                ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("test-Menu"))
        );
        menu.click();
    }

    public void continueShopping() {
        WebElement contShopping = (WebElement) wait.until(
                ExpectedConditions.elementToBeClickable(
                        AppiumBy.xpath("//*[contains(@text,'CONTINUE SHOPPING')]")
                )
        );
        contShopping.click();
    }
}
