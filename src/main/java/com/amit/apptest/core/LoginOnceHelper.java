package com.amit.apptest.core;

import com.amit.apptest.pages.LoginPage;
import com.amit.apptest.pages.ProductsPage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.JavascriptExecutor;

public class LoginOnceHelper {

    /*** Just keeping the credentials for record. **/
    private static final String USERNAME = "standard_user";
    private static final String PASSWORD = "secret_sauce";

    /**
     * Amit: Ensuring login is performed only once per device key.
     * The second argument is treated as device identifier
     */
    public static void ensureLoggedIn(AppiumDriver driver, String deviceKey) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver must not be null");
        }
        String key = (deviceKey == null ? "default" : deviceKey);

        // Fast-path: already logged in
        if (SessionManager.isLoggedIn(key)) {
            return;
        }

        synchronized (LoginOnceHelper.class) {
            // Double check inside the synchronized block
            if (SessionManager.isLoggedIn(key)) {
                return;
            }

            try {
                // Performing the login flow
                LoginPage loginPage = new LoginPage(driver);
                if (new ProductsPage(driver).isDisplayed()) {
                    System.out.println("Already logged in, skipping login");
                    SessionManager.markLoggedIn(key);
                    return;
                }

                loginPage.performLogin();
                System.out.println("Login successful for device: " + key);

                // Validate that we landed on the products page.
                ProductsPage productsPage = new ProductsPage(driver);
                if (!productsPage.isDisplayed()) {
                    throw new RuntimeException("Products page not visible after login");
                }

                // Marking this device as logged in.
                SessionManager.markLoggedIn(key);

                setBsStatus(driver, "passed", "Login successful for device " + key);
            } catch (Exception e) {
                System.out.println("Login failed for: " + key);
                setBsStatus(driver, "failed",
                        "Login failed for device " + key + ": " + e.getMessage());
                throw new RuntimeException("Login failed for device " + key, e);
            }
        }
    }

    // Helper to set BrowserStack session status without breaking tests if the executor call fails.
    private static void setBsStatus(AppiumDriver driver, String status, String reason) {
        try {
            String safeReason = reason == null ? "" : reason.replace("\"", "'");
            String script = "browserstack_executor: {\"action\": \"setSessionStatus\", " +
                    "\"arguments\": {\"status\": \"" + status + "\", \"reason\": \"" + safeReason + "\"}}";
            ((JavascriptExecutor) driver).executeScript(script);
        } catch (Exception ignored) {
        }
    }
}