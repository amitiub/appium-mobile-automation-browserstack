package com.amit.apptest.tests;

import com.amit.apptest.tests.core.BaseTest;
import com.amit.apptest.pages.ProductsPage;
import com.amit.apptest.core.LoginOnceHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void verifyLoginOncePerDevice() {
        LoginOnceHelper.ensureLoggedIn(driver, deviceName);
        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isDisplayed(), "Products page should be visible after login");
        System.out.println("Login Test Passed for: " + deviceKey);
    }
}