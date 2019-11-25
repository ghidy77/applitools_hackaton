package com.applitools.hackaton;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

public class BaseTest {

    protected WebDriver driver;

    private static final int IMPLICIT_WAIT_SECONDS = 1;
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    protected void setUp() {

        Capabilities caps = new ChromeOptions();
        LOGGER.info("Starting browser");

        try {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), caps);
        } catch (MalformedURLException e) {
            LOGGER.warn(e);
        }
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        // maximize browser window
        driver.manage().window().maximize();
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDown() {
        // Closing driver
        LOGGER.info("Closing browser");
        driver.quit();
    }

    @DataProvider(name = "DataDrivenTest")
    public static Object[][] credentials() {

        return new Object[][] { { "Scenario number 1","", "", "Both Username and Password must be present" },
                { "Scenario number 2","testuser", "", "Password must be present" }, 
                { "Scenario number 3","", "testpassword", "Username must be present" },
                { "Scenario number 4","testuser", "testpassword", "OK" } };
    }

}
