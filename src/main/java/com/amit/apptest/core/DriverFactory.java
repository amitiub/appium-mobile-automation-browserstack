package com.amit.apptest.core;

import java.time.Duration;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static final String BS_USER = "akd_BoQ7IJqftm1";
    private static final String BS_KEY = "tcdM1hX4xqQtQ3qsCuqs";
    private static final String BS_URL = "https://hub.browserstack.com/wd/hub";
    private static final String BS_APP_ID = "bs://fb13ff48279675c3ffc2b1217cf2bb6c05ae063d";

    public static AppiumDriver create(String deviceName, String platformVersion, String testName) throws Exception {

        Map<String, Object> bstackOptions = new HashMap<>();
        //bstackOptions.put("sessionName", testName);
        bstackOptions.put("sessionName", deviceName + " - Full Test Suite");
        bstackOptions.put("userName", BS_USER);
        bstackOptions.put("accessKey", BS_KEY);
        bstackOptions.put("projectName", "SwagLabs App Tests");
        bstackOptions.put("buildName", "SwagLabs App Tests");
        bstackOptions.put("deviceName", deviceName);
        bstackOptions.put("osVersion", platformVersion);
        bstackOptions.put("appiumVersion", "2.0.0");
        bstackOptions.put("networkLogs", false);
        bstackOptions.put("video", true);
        bstackOptions.put("deviceLogs", false);
        bstackOptions.put("appiumLogs", false);
        bstackOptions.put("interactiveDebugging", false);

        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setApp(BS_APP_ID);
        options.setCapability("bstack:options", bstackOptions);
        options.setAppPackage("com.swaglabsmobileapp");
        options.setAppActivity("com.swaglabsmobileapp.SplashActivity");
        options.setNewCommandTimeout(Duration.ofSeconds(120));

        return new AndroidDriver(new URL(BS_URL), options);
    }
}
