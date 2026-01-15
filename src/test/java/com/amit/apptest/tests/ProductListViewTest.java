package com.amit.apptest.tests;

import com.amit.apptest.tests.core.BaseTest;
import com.amit.apptest.pages.ProductsPage;
import com.amit.apptest.pages.ProductListViewPage;
import com.amit.apptest.core.LoginOnceHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductListViewTest extends BaseTest {

    @Test
    public void VerifyProductListView() {
        LoginOnceHelper.ensureLoggedIn(driver, deviceName);
        Assert.assertTrue(new ProductsPage(driver).isDisplayed(), "Products page should be visible");

        ProductListViewPage productListPage = new ProductListViewPage(driver);
        productListPage.menuButton();

        try {
            Assert.assertTrue(productListPage.getProductListCount() > 1, "Product list view should have more than one item");
            System.out.println("Product List View Test Passed!");
        }
        catch (Exception e) {
            e.getStackTrace();
            throw new RuntimeException("Product List View Test Failed!");
        }
    }
}
