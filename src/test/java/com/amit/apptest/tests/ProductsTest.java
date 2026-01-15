package com.amit.apptest.tests;

import com.amit.apptest.tests.core.BaseTest;
import com.amit.apptest.pages.CartPage;
import com.amit.apptest.pages.ProductsPage;
import com.amit.apptest.core.LoginOnceHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductsTest extends BaseTest {

    @Test
    public void verifyAddToCartFlow() {
        LoginOnceHelper.ensureLoggedIn(driver, deviceName);

        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.addFirstProductToCart();
        productsPage.openCart();

        CartPage cartPage = new CartPage(driver);
        try {
            Assert.assertTrue(cartPage.isDisplayed(), "Cart page should be displayed");
            Assert.assertTrue(cartPage.getCartItemCount() >= 1, "Cart should have at least one item");
            productsPage.continueShopping();
            System.out.println("Product Test Passed!");
        }
        catch (Exception e) {
            e.getStackTrace();
            throw new RuntimeException("Product Test Failed!");
        }
    }
}
