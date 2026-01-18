# appium-mobile-automation-browserstack
This automation project uses Appium, Java, TestNG, Maven, single sign-on in BrowserStack.

**Author: Amit Das**


**DriverFactory:**

* Responsible for creating AppiumDriver sessions on BrowserStack.
* Reads deviceName, platformVersion, testName
* Builds BrowserStack capabilities
* Creates a new session only when required
* Used by BaseTest to ensure a single driver per device



**BaseTest:**
* Load TestNG parameters
* Create and reuse drivers per device
* Ensure one-time login per device
* Annotate BrowserStack test results
* Quit the driver only after all tests for that device finish



**Key Mechanisms:**

Device Key:
Each device is uniquely identified by:
deviceKey = deviceName + ":" + platformVersion

Used in a ConcurrentHashMap:
protected static final ConcurrentHashMap<String, AppiumDriver> DRIVER_POOL = new ConcurrentHashMap<>();

So:
Each <test> block from testng.xml = ONE driver
All test classes for that device share this driver


**LoginOnceHelper**
Ensures login happens only once per device session.
How it works:
When driver is first created
Login page is executed
SessionManager marks the device session as logged in
All later tests see the logged-in state and skip login


Result:
* Faster test runs
* Tests start in authenticated state
* Stable, consistent sessions


**SessionManager:**
Simple in-memory storage:
Tracks login status per device
Used by LoginOnceHelper to avoid repeated logins


**Page Object Model (POM)**
Each screen:
Represents UI elements
Contains methods for actions
Abstracts away Appium interactions
Tests become simple, readable, and maintainable.

Example:
ProductsPage products = new ProductsPage(driver);
products.addItemToCart("Amit-Backpack");


**Parallel Execution Flow**
TestNG XML defines one <test> per device:
<test name="Pixel9">
  <parameter name="deviceName" value="Google Pixel 9"/>
    <classes>...</classes>
</test>


Because the TestNG suite uses:
parallel="tests"
thread-count="3"


Each <test> executes:
On its own thread
On its own BrowserStack device
With its own reused driver
With one-time login


**Execution Sequence (per device)**
1. @BeforeClass

Reads deviceName and platformVersion
Prepares deviceKey

2. @BeforeMethod
Creates driver on first test
Reuses driver for all subsequent tests
Performs one-time login
Annotates BrowserStack with test start message

3. Test Method Execution
Uses POM to perform tests
Fast because no repeated login

4. @AfterMethod
Annotates pass/fail status in BrowserStack session logs

5. @AfterSuite
Runs once when all tests for that device finish
Sets session name
Quits driver


**BrowserStack Reporting**

BrowserStack App Automate runs one session per device.
Inside the session:
Video recording of full execution
Per-test annotations: (This is not working yet)
“verifyAddToCartFlow: passed”
“verifyCartVisibleAfterLogin: failed”
Console logs
Network logs
Appium logs

This gives a complete test picture for each device.


**Advantages of this Framework**
* One login per device → more than 70% faster runs
* One driver per device → stable & efficient
* Parallel execution across devices
* Clean POM layer for future expansion
* Consistent BrowserStack session naming
* Each method result clearly visible in BrowserStack logs
=======
Please remember to add the following info in DriverFactory class:

    * private static final String BS_USER = "<YOUR_BS_USER>";
    * private static final String BS_KEY = "<YOUR_BS_KEY>";
    * private static final String BS_URL = "https://hub.browserstack.com/wd/hub";
    * private static final String BS_APP_ID = "<YOUR_BS_APP_ID>";
