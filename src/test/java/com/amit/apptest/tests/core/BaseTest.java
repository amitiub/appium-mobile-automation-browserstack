package com.amit.apptest.tests.core;

import com.amit.apptest.core.DriverFactory;
import com.amit.apptest.core.LoginOnceHelper;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class BaseTest {

    // One driver per device (deviceName:platformVersion)
    protected static final ConcurrentHashMap<String, AppiumDriver> DRIVER_POOL = new ConcurrentHashMap<>();

    // Per-device counters for summary in session name
    private static final ConcurrentHashMap<String, Integer> PASS_COUNTS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Integer> FAIL_COUNTS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Integer> SKIP_COUNTS = new ConcurrentHashMap<>();

    protected AppiumDriver driver;
    protected String deviceName;
    protected String platformVersion;
    public String deviceKey;

    // Load TestNG parameters for each test class
    @Parameters({"deviceName", "platformVersion"})
    @BeforeClass(alwaysRun = true)
    public void beforeClass(String deviceName, String platformVersion) {

        if (deviceName == null || platformVersion == null) {
            throw new RuntimeException(
                    "TestNG parameters deviceName/platformVersion are missing! " +
                            "Make sure each <test> block in testng.xml includes BOTH."
            );
        }

        this.deviceName = deviceName;
        this.platformVersion = platformVersion;
        this.deviceKey = deviceName + ":" + platformVersion;

        System.out.println("Loaded parameters for: " + deviceKey);
    }

    // Create/reuse driver & ensure one-time login
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {

        if (deviceKey == null) {
            throw new RuntimeException("deviceKey is NULL! Parameters were not loaded correctly!");
        }

        System.out.println("Setting up driver for method: " + method.getName());

        // Create driver ONCE per device / reuse for all tests
        driver = DRIVER_POOL.computeIfAbsent(deviceKey, key -> {
            try {
                System.out.println("Creating driver for: " + key);
                return DriverFactory.create(deviceName, platformVersion, method.getName());
            } catch (Exception e) {
                throw new RuntimeException("Driver creation failed for: " + key, e);
            }
        });

        // Perform one-time login per device
        LoginOnceHelper.ensureLoggedIn(driver, deviceKey);
    }

    // Per-method pass/fail reporting to BrowserStack
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {

        if (driver == null) {
            System.out.println("Skipping result annotation – driver is null");
            return;
        }

        String testName = result.getName();
        String status;

        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> {
                status = "passed";
                PASS_COUNTS.merge(deviceKey, 1, Integer::sum);
            }
            case ITestResult.FAILURE -> {
                status = "failed";
                FAIL_COUNTS.merge(deviceKey, 1, Integer::sum);
            }
            case ITestResult.SKIP -> {
                status = "skipped";
                SKIP_COUNTS.merge(deviceKey, 1, Integer::sum);
            }
            default -> status = "unknown";
        }

        // Only result, as you requested (B)
        String level = status.equals("failed") ? "error" : "info";
        annotate(testName + " : " + status, level);
    }

    // Close all drivers once after the entire suite finishes
    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {

        System.out.println("=== AfterSuite: Closing all drivers ===");

        DRIVER_POOL.forEach((key, d) -> {
            if (d == null) return;

            // Build session summary for this device (option 2)
            int passed = PASS_COUNTS.getOrDefault(key, 0);
            int failed = FAIL_COUNTS.getOrDefault(key, 0);
            int skipped = SKIP_COUNTS.getOrDefault(key, 0);

            String sessionName = key + " - " + passed + " Passed / " + failed + " Failed";
            if (skipped > 0) {
                sessionName += " / " + skipped + " Skipped";
            }

            try {
                ((JavascriptExecutor) d).executeScript(
                        "browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\": \"" +
                                sessionName + "\"}}"
                );
            } catch (Exception e) {
                System.out.println("BrowserStack setSessionName failed for " + key + ": " + e.getMessage());
            }

            try {
                System.out.println("Quitting driver for device: " + key);
                d.quit();
            } catch (Exception e) {
                System.out.println("Driver already closed for: " + key + " - " + e.getMessage());
            }
        });

        DRIVER_POOL.clear();
        PASS_COUNTS.clear();
        FAIL_COUNTS.clear();
        SKIP_COUNTS.clear();
    }

    // ===== BrowserStack helper =====
    private void annotate(String message, String level) {
        try {
            if (driver == null) {
                System.out.println("Skipping annotate – driver is null");
                return;
            }

            ((JavascriptExecutor) driver).executeScript(
                    "browserstack_executor: {\"action\": \"annotate\", \"arguments\": {\"data\": \"" +
                            message + "\", \"level\": \"" + level + "\"}}"
            );

        } catch (Exception e) {
            System.out.println("BrowserStack annotate failed: " + e.getMessage());
        }
    }
}