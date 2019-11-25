package com.applitools.hackaton;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChartPage {

    @FindBy(css = "#canvas")
    private WebElement chart;

    @FindBy(css = "#addDataset")
    private WebElement nextYearButton;

    private WebDriver driver;
    private JavascriptExecutor js;
    private WebDriverWait wait;
    private static final String COMPARE_EXPENSES_URL = "hackathonChart";
    private String numberOfDatasetsInChartScript = "return Chart.instances[0].chart.config.data.datasets.length";

    public ChartPage(WebDriver driver) {
        this.driver = driver;
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, 5);
        PageFactory.initElements(driver, this);
        waitForChartPageToLoad();
    }

    /* Wait for the expected link */
    public void waitForChartPageToLoad() {
        wait.until(wd -> driver.getCurrentUrl().contains(COMPARE_EXPENSES_URL));
    }

    public boolean isChartDisplayed() {
        return chart.isDisplayed();
    }

    public void addDataForNextYear() {
        
        int numberOfDatasets = Integer.parseInt(
                js.executeScript(numberOfDatasetsInChartScript).toString());
        nextYearButton.click();
        wait.until(wd -> numberOfDatasets < Integer.parseInt(
                js.executeScript(numberOfDatasetsInChartScript).toString()));
    }

    public List<String> getDataFromChart() {

        // I had no other choice to take this data, as #canvas doesn't have any child elements so I couldn't get some
        // values to compare, I digged into console and found the Chart object
        List<String> datasetsList = new ArrayList<>();
        boolean isChartLoaded = Boolean.parseBoolean(js.executeScript("return Chart != undefined").toString());
        if (isChartLoaded) {
            int numberOfDatasets = Integer.parseInt(
                    js.executeScript(numberOfDatasetsInChartScript).toString());
            // continue only if there is a set of data available
            if (numberOfDatasets > 0) {
                for (int i = 0; i < numberOfDatasets; i++) {
                    datasetsList.add(
                            js.executeScript("return Chart.instances[0].chart.config.data.datasets[" + i + "].data")
                                    .toString());
                }
            }
        }
        return datasetsList;
    }

}