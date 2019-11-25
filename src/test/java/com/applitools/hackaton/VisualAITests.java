package com.applitools.hackaton;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.hackaton.misc.PageNotFoundException;

public class VisualAITests extends BaseTest {

    private static Eyes eyes;
    private Homepage homepage;
    private LoginPage loginPage;

    private static final String username = "test_user";
    private static final String password = "test123#";
    private static final String SHOW_ADS_FLAG = "?showAd=true";
    private static final String APLITOOLS_API_KEY = "Q5x5tJK25vuZJ4KtEsJMAdN2D1071gqChu8H9OuqIrzQ4110";
     private static final String BASE_URL = "https://demo.applitools.com/hackathon.html";
//    private static final String BASE_URL = "https://demo.applitools.com/hackathonV2.html";

    @BeforeTest
    public void setup() {
        eyes = new Eyes();
        eyes.setApiKey(APLITOOLS_API_KEY);
    }

    @AfterTest
    public void close() {
        eyes.abortIfNotClosed();
    }

    @Test
    public void loginPageElementsTest() throws PageNotFoundException {

        // Open Login Page
        loginPage = new LoginPage(driver, BASE_URL);

        eyes.open(driver, "Applitools Demo", "Login Page Elements Test",
                new RectangleSize(1200, 800));

        eyes.checkWindow("Login Page");
        TestResults result = eyes.close();
        Assert.assertTrue(result.isPassed(), "Verify if screenshots match");
    }

    @Test(dataProvider = "DataDrivenTest", dataProviderClass = VisualAITests.class)
    public void dataDrivenTest(String scenarioName, String username, String password, String expectedResult)
            throws PageNotFoundException {

        eyes.open(driver, "Applitools Demo", scenarioName,
                new RectangleSize(1200, 800));

        // Open login page
        loginPage = new LoginPage(driver, BASE_URL);

        try {
            // try to login
            loginPage.login(username, password);
        } catch (PageNotFoundException e) {
            // ignore Exception when testing the login page with negative tests
        }
        eyes.checkWindow("Login Page");
        TestResults result = eyes.close();
        Assert.assertTrue(result.isPassed(), "Verify if screenshots match");
    }

    @Test
    public void tableSortTest() throws PageNotFoundException {

        eyes.setForceFullPageScreenshot(true);

        // Open Login Page and perform login
        loginPage = new LoginPage(driver, BASE_URL);
        homepage = loginPage.login(username, password);
        homepage.waitForHomepageToLoad();

        // click on Amount header of the table to sort the rows by Amount
        homepage.clickOnAmountHeader();

        eyes.open(driver, "Applitools Demo", "Table sort Test",
                new RectangleSize(1200, 800));

        // Check the transactions table page
        eyes.checkWindow("Transactions Table");
        TestResults result = eyes.close();
        Assert.assertTrue(result.isPassed(), "Verify if screenshots match");
    }

    @Test
    public void canvasChartTest() throws PageNotFoundException {

        // Open Login page and perform login
        loginPage = new LoginPage(driver, BASE_URL);
        homepage = loginPage.login(username, password);
        homepage.waitForHomepageToLoad();

        // go to Compare Expenses Page
        ChartPage chartpage = homepage.goToCompareExpensesPage();

        eyes.open(driver, "Applitools Demo", "Canvas Chart Test",
                new RectangleSize(1200, 800));

        // Validate the chart page
        eyes.checkWindow("Years 2017-2018");

        // add another year
        chartpage.addDataForNextYear();

        // Validate chart after adding another year
        eyes.checkWindow("Year 2019");
        TestResults result = eyes.close();
        Assert.assertTrue(result.isPassed(), "Verify if screenshots match");
    }

    @Test
    public void dynamicContentTest() throws PageNotFoundException {

        // Open Login page and perform login
        loginPage = new LoginPage(driver, BASE_URL + SHOW_ADS_FLAG);
        homepage = loginPage.login(username, password);
        homepage.waitForHomepageToLoad();

        eyes.open(driver, "Applitools Demo", "Dynamic Content Test",
                new RectangleSize(1200, 800));

        eyes.checkWindow("Login Page");
        TestResults result = eyes.close();
        Assert.assertTrue(result.isPassed(), "Verify if screenshots match");

    }

    @DataProvider(name = "DataDrivenTest")
    public static Object[][] credentials() {
        eyes.setBatch(new BatchInfo("Data Driven Test"));
        return BaseTest.credentials();
    }

}
