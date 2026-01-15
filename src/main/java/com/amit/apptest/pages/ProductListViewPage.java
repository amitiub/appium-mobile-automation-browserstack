package com.amit.apptest.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductListViewPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public ProductListViewPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void menuButton() {
        WebElement menu = (WebElement) wait.until(
                ExpectedConditions.elementToBeClickable(AppiumBy.xpath("//*[contains(@content-desc,'test-Toggle')]"))
        );
        menu.click();
    }

    public int getProductListCount() {
        List<WebElement> items = driver.findElements(
                AppiumBy.xpath("//*[contains(@content-desc, 'test-Item title')]")
        );
        return items.size();
    }
}