package com.amit.apptest.tests;

import com.amit.apptest.tests.core.BaseTest;
import com.amit.apptest.pages.LogoutPage;
import com.amit.apptest.pages.ProductsPage;
import com.amit.apptest.core.LoginOnceHelper;
import com.amit.apptest.core.SessionManager;
import org.testng.annotations.Test;

public class LogoutTest extends BaseTest {

    @Test
    public void verifyLogout() {
        // Login only once per device
        LoginOnceHelper.ensureLoggedIn(driver, deviceName);

        // Open the menu from the products page
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.openMenu();

        // Click logout
        LogoutPage logoutPage = new LogoutPage(driver);
        logoutPage.logout();

        SessionManager.markLoggedOut(deviceKey);
        System.out.println("Logout done for: " + deviceKey);
    }
}
