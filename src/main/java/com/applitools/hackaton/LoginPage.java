package com.applitools.hackaton;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.applitools.hackaton.misc.PageNotFoundException;

public class LoginPage {

    @FindBy(css = "#username")
    private WebElement userInput;

    @FindBy(css = "#password")
    private WebElement passwordInput;

    @FindBy(css = "#log-in")
    private WebElement loginButton;

    @FindBy(css = ".form-check-label")
    private WebElement rememberMeButton;

    @FindBy(css = ".logo-w img")
    private WebElement logo;

    @FindBy(css = ".alert.alert-warning")
    private WebElement warningMessage;

    @FindBy(css = "div label[for]")
    private List<WebElement> fieldLabels;

    @FindBy(css = ".buttons-w a img")
    private List<WebElement> socialMediaIcons;

    protected WebDriver driver;
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    public LoginPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        driver.get(baseUrl);
        PageFactory.initElements(driver, this);
        waitForLoginPageToLoad();
    }

    /* Wait for username input to be visible on the page */
    public void waitForLoginPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOf(userInput));
    }

    /* Valid log in */
    public Homepage login(String username, String password) throws PageNotFoundException {
        userInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();

        try {
            return new Homepage(driver);
        } catch (TimeoutException e) {
            LOGGER.warn("Timeout Exception at login");
            LOGGER.debug(e);
        }
        throw new PageNotFoundException("Login was not successful");
    }

    public List<WebElement> getFieldLabels() {
        return fieldLabels;
    }

    public List<WebElement> getSocialMediaIcons() {
        return socialMediaIcons;
    }

    public WebElement getPasswordInput() {
        return passwordInput;
    }

    public WebElement getUserInput() {
        return userInput;
    }

    public WebElement getLoginButton() {
        return loginButton;
    }

    public WebElement getRememberMeButton() {
        return rememberMeButton;
    }

    public WebElement getLogo() {
        return logo;
    }

    public String getWarningMessage() {
        return warningMessage.getText();
    }

    public boolean isWarningMessageDisplayed() {
        try {
            return warningMessage.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getWarningMessageStyle() {
        return warningMessage.getAttribute("style");
    }
}