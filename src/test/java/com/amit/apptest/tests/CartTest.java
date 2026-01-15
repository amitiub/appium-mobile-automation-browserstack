package com.amit.apptest.tests;

import com.amit.apptest.tests.core.BaseTest;
import com.amit.apptest.pages.CartPage;
import com.amit.apptest.pages.ProductsPage;
import com.amit.apptest.core.LoginOnceHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test
    public void verifyCartVisibleAfterLogin() {
        LoginOnceHelper.ensureLoggedIn(driver, deviceName);

        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.openCart();

        try {
            CartPage cartPage = new CartPage(driver);
            Assert.assertTrue(cartPage.isDisplayed(), "Cart page should be visible");
            productsPage.continueShopping();
            System.out.println("Cart Test Passed!");
        }
        catch (Exception e) {
            e.getStackTrace();
            System.out.println("Cart Test Failed!");
        }
    }
}
