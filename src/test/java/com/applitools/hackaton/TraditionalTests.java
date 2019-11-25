package com.applitools.hackaton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.applitools.hackaton.misc.PageNotFoundException;

public class TraditionalTests extends BaseTest {

    private Homepage homepage;
    private LoginPage loginPage;

    private static final String username = "test_user";
    private static final String password = "test123#";
    private static final String SHOW_ADS_FLAG = "?showAd=true";
     private static final String BASE_URL = "https://demo.applitools.com/hackathon.html";
//    private static final String BASE_URL = "https://demo.applitools.com/hackathonV2.html";

    @Test
    public void loginPageElementsTest() throws PageNotFoundException {
        // Open Login Page
        loginPage = new LoginPage(driver, BASE_URL);

        // check user and passwords inputs to be visible
        Assert.assertTrue(loginPage.getUserInput().isDisplayed(), "User input is not displayed");
        Assert.assertTrue(loginPage.getPasswordInput().isDisplayed(), "Password input is not displayed");
        Assert.assertTrue(loginPage.getLoginButton().isDisplayed(), "Login button is not displayed");
        Assert.assertTrue(loginPage.getRememberMeButton().isDisplayed(), "Remember me button is not displayed");

        // check input labels
        List<WebElement> fieldLabels = loginPage.getFieldLabels();
        Assert.assertEquals(fieldLabels.get(0).getText(), "Username", "First field label is incorrect");
        Assert.assertEquals(fieldLabels.get(1).getText(), "Password", "Second field label is incorrect");
        Assert.assertTrue(fieldLabels.get(0).isDisplayed(), "First field label is not displayed");
        Assert.assertTrue(fieldLabels.get(1).isDisplayed(), "Second field label is not displayed");
        Assert.assertEquals(loginPage.getLoginButton().getText(), "Log In", "Login button label is incorrect");
        Assert.assertEquals(loginPage.getRememberMeButton().getText(), "Remember Me",
                "Login button label is incorrect");

        // check input placeholder
        Assert.assertEquals(loginPage.getPasswordInput().getAttribute("placeholder"), "Enter your password",
                "Password input placeholder is incorrect");
        Assert.assertEquals(loginPage.getUserInput().getAttribute("placeholder"), "Enter your username",
                "Username input placeholder is incorrect");

        // check logo
        Assert.assertTrue(loginPage.getLogo().isDisplayed(), "Logo is not displayed");

        // check social media icons visibility and validate links
        List<WebElement> socialMediaIcons = loginPage.getSocialMediaIcons();
        for (WebElement icon : socialMediaIcons) {
            Assert.assertTrue(icon.isDisplayed(),
                    "Social icon with index " + socialMediaIcons.indexOf(icon) + " is not displayed");
            Assert.assertTrue(icon.getAttribute("src").matches(".+\\/img\\/social-icons\\/[a-z]+\\.png"),
                    "Social icon with index " + socialMediaIcons.indexOf(icon) + " has incorrect src attribute");
        }
    }

    @Test(dataProvider = "DataDrivenTest", dataProviderClass = BaseTest.class)
    public void dataDrivenTest(String scenarioName, String username, String password, String expectedResult)
            throws PageNotFoundException {

        loginPage = new LoginPage(driver, BASE_URL);
        String actualResultText = "OK";
        boolean isWarningMessageDisplayed = false;
        try {
            loginPage.login(username, password);
        } catch (PageNotFoundException e) {
            isWarningMessageDisplayed = loginPage.isWarningMessageDisplayed();
            if (isWarningMessageDisplayed) {
                actualResultText = loginPage.getWarningMessage();
            }
        }

        // for scenario 4 don't check the warning message
        if (!"OK".equals(expectedResult)) {
            Assert.assertTrue(isWarningMessageDisplayed, "Warning message is not displayed");
            Assert.assertEquals(actualResultText, expectedResult,
                    "Incorrect message for: " + scenarioName);
            String warningMessageStyle = loginPage.getWarningMessageStyle();
            Assert.assertEquals(warningMessageStyle, "display: block;",
                    "Warning message doesn't have the expected style");
        } else {
            Assert.assertTrue(new Homepage(driver).waitForHomepageToLoad(), "Homepage was not loaded");
        }

    }

    @Test
    public void tableSortTest() throws PageNotFoundException {

        // Open Login Page
        loginPage = new LoginPage(driver, BASE_URL);

        // login
        homepage = loginPage.login(username, password);
        homepage.waitForHomepageToLoad();

        List<String> listOfTransactionsUnsorted = homepage.getListOfTransactionsRows();

        homepage.clickOnAmountHeader();
        List<String> listOfTransactionsSorted = homepage.getListOfTransactionsRows();

        Assert.assertTrue(listOfTransactionsUnsorted.containsAll(listOfTransactionsSorted),
                "Data from rows is not intact after sorting");

        Assert.assertNotEquals(listOfTransactionsUnsorted, listOfTransactionsSorted,
                "Rows after sorting should be different than rows before sorting");

        List<Double> amountsEntryList = homepage.getListOfAmountEntriesValues();

        // create a copy to keep the original values before forcing a sort
        List<Double> amountsListAfterSort = new ArrayList<>();
        amountsListAfterSort.addAll(amountsEntryList);

        // amounts should be already sorted, so after forcing a sort() they should be still the same
        Collections.sort(amountsListAfterSort);

        Assert.assertTrue(amountsListAfterSort.equals(amountsEntryList),
                "Values were not sorted correctly. Actual values: " + amountsEntryList);

    }

    @Test
    public void canvasChartTest() throws PageNotFoundException {

        // I assumed I got this "Expected data" from a test data, database or from a csv file,
        // so I can compare it with the data used by the Chart
        final String chartData2017 = "[10, 20, 30, 40, 50, 60, 70]";
        final String chartData2018 = "[8, 9, -10, 10, 40, 60, 40]";
        final String chartData2019 = "[5, 10, 15, 20, 25, 30, 35]";

        // Open Login Page
        loginPage = new LoginPage(driver, BASE_URL);

        // login and go to Compare Expenses Page
        homepage = loginPage.login(username, password);
        homepage.waitForHomepageToLoad();
        ChartPage chartpage = homepage.goToCompareExpensesPage();

        // Validate the chart appears in page
        Assert.assertTrue(chartpage.isChartDisplayed(),
                "Chart is not displayed in Compare Expenses page");

        // Compare the data used by the chart with some expected data from a db or a file.
        List<String> datasetsList = chartpage.getDataFromChart();
        Assert.assertEquals(datasetsList.size(), 2, "Chart should contain data for 2 years");
        Assert.assertEquals(datasetsList.get(0), chartData2017, "Invalid data found in chart for 2017");
        Assert.assertEquals(datasetsList.get(1), chartData2018, "Invalid data found in chart for 2018");

        // add another year
        chartpage.addDataForNextYear();

        // check again the data from chart
        datasetsList = chartpage.getDataFromChart();
        Assert.assertEquals(datasetsList.size(), 3, "Chart should contain data for 3 years");
        Assert.assertEquals(datasetsList.get(2), chartData2019, "Invalid data found in chart for 2019");

    }

    @Test
    public void dynamicContentTest() throws PageNotFoundException {

        loginPage = new LoginPage(driver, BASE_URL + SHOW_ADS_FLAG);
        homepage = loginPage.login(username, password);
        homepage.waitForHomepageToLoad();

        Assert.assertTrue(homepage.isAdVisible(1), "Flash Sale Ad 1 is not displayed");
        Assert.assertTrue(homepage.isAdVisible(2), "Flash Sale Ad 2 is not displayed");
    }

}
