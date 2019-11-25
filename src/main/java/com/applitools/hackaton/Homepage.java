package com.applitools.hackaton;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Homepage {

    @FindBy(css = ".logo-w")
    private WebElement logo;

    @FindBy(css = "#transactionsTable")
    private WebElement transactionsTable;

    @FindBy(css = "#transactionsTable #amount")
    private WebElement amountHeader;

    @FindBy(css = "#showExpensesChart")
    private WebElement compareExpensesButton;

    @FindBy(css = "#flashSale>img")
    private WebElement flashSaleAd1;

    @FindBy(css = "#flashSale2>img")
    private WebElement flashSaleAd2;

    @FindBy(css = "#transactionsTable tbody>tr")
    private List<WebElement> transactionEntries;

    @FindBy(css = "#transactionsTable tbody>tr td:last-of-type")
    private List<WebElement> amountValueRows;

    protected WebDriver driver;
    private static final String HOMEPAGE_URL = "hackathonApp";
    private static final Logger LOGGER = LogManager.getLogger(Homepage.class);

    public Homepage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        waitForHomepageToLoad();
    }

    /* Wait for logo to be visible on the page */
    public boolean waitForHomepageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(logo));
        return wait.until(wd -> driver.getCurrentUrl().contains(HOMEPAGE_URL));
    }

    public List<String> getListOfTransactionsRows() {
        List<String> listOfRows = new ArrayList<>();
        for (WebElement webElement : transactionEntries) {
            listOfRows.add(webElement.getText());
        }
        return listOfRows;
    }

    public List<Double> getListOfAmountEntriesValues() {
        List<Double> amountEntryList = new ArrayList<>();

        for (WebElement webElement : amountValueRows) {
            String amount = webElement.getText();
            String sign = amount.split(" ")[0];
            String number = amount.split(" ")[1].replace(",", "");
            if (sign.equals("-")) {
                number = "-".concat(number);
            }
            amountEntryList.add(Double.parseDouble(number));
        }

        return amountEntryList;
    }

    public void clickOnAmountHeader() {
        amountHeader.click();
    }

    public ChartPage goToCompareExpensesPage() {
        compareExpensesButton.click();
        return new ChartPage(driver);
    }

    public boolean isAdVisible(int adNumber) {
        try {
            if (adNumber == 1) {
                return flashSaleAd1.isDisplayed();
            } else if (adNumber == 2) {
                return flashSaleAd2.isDisplayed();
            }
        } catch (NoSuchElementException e) {
            LOGGER.warn("Ad element number " + adNumber + " not found in page");
        }
        return false;
    }
}