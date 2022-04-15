package com.testws.driver;

import com.testws.core.TestLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Wrapper for Chrome Browser Driver class, support for common control methods with test logging
 */
public class ChromeBrowserDriver {
    private WebDriver driver;

    TestLogger testLogger;

    /**
     * Initialize the Chrome browser driver, setting up chromedriver using WebDriverManager
     * Opens and maximizes the Chrome browser
     *
     * Note: this is not done in the constructor in case multiple browsers are used for factory test cases, they must be started separately from object instantiation
     * @param testLogger            Test Logger to attach to the browser
     */
    public ChromeBrowserDriver initialize(TestLogger testLogger) {
        this.testLogger = testLogger;

        WebDriverManager.chromedriver().setup();

        testLogger.logTestStep("Open Chrome Web Browser");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        return this;
    }

    /** Close chrome */
    public void quit() {
        testLogger.logTestStep("Close Chrome Browser");

        driver.quit();
    }

    /** Initialize the page factory elements for page model classes
     *
     * (Page Model classes must call this method if they use @FindBy annotations for web elements)
     */
    public void initPageFactoryElements(Object page) {
        PageFactory.initElements(driver, page);
    }

    public TestLogger getTestLogger() {
        return testLogger;
    }

    /** Go to the given URL, logging a test step
     *
     * @param urlText       URL string to navigate to
     */
    public void gotoUrl(String urlText) throws Exception {
        testLogger.logTestStep("Go to URL '" + urlText + "'");

        try {
            driver.get(urlText);
        } catch (Exception exception) {
            testLogger.fail("Failure opening URL '" + urlText + "'", exception);
        }
    }

    /** Find the selenium web element with given locator, optionally failing if not found
     *  Normally we would change the selenium timeout if not expecting the web element so that we don't have an unnecessary wait of the default wait time
     * */
    public WebElement findWebElement(By locator, boolean failIfNotFound) throws Exception {
        WebElement webElement = null;

        // If we don't have to fail if not found, use short timeout as we probably aren't expecting the element to be there
        driver.manage().timeouts().implicitlyWait(failIfNotFound ? 30 : 1, TimeUnit.SECONDS);

        try {
            webElement = driver.findElement(locator);

            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        } catch (Exception exception) {
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            if (failIfNotFound) {
                testLogger.fail("Web Element not found with locator '" + locator + "'", exception);
            }
        }

        return webElement;
    }

    /** Return True if the given web element is valid and displayed within specified time limit
     * @param webElement                Web element to check if displayed
     * @param timeLimitInSeconds        Time limit for the check, in seconds
     */
    public boolean webElementIsDisplayed(WebElement webElement, int timeLimitInSeconds) {
        boolean isDisplayed = false;

        // If we don't have to fail if not found, use short timeout as we probably aren't expecting the element to be there
        driver.manage().timeouts().implicitlyWait(timeLimitInSeconds, TimeUnit.SECONDS);

        try {
            isDisplayed = webElement.isDisplayed();
        } catch (Exception exception) {}

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        return isDisplayed;
    }

    /** Find the list of web elements with the given selenium locator */
    public List<WebElement> findWebElementList(By locator) {
        return driver.findElements(locator);
    }

    /** Click the given selenium web element, logging a step to repro using the given control's logging description */
    public void clickWebElement(String controlDescription, WebElement webElement) throws Exception {
        testLogger.logTestStep("Click " + controlDescription);

        try {
            Timer timer = new Timer(60000, null);
            timer.start();

            while(timer.isRunning()) {
                try {
                    webElement.click();

                    break;
                }
                catch (ElementNotInteractableException exception) {
                    if (!timer.isRunning()) {
                        break;
                    }
                }

                Thread.sleep(100);
            }

        } catch (Exception exception) {
            testLogger.fail("Error clicking " + controlDescription, exception);
        }
    }

    /** Click the given selenium web element using javascript click, logging a step to repro using the given control's logging description */
    public void clickWebElementJS(String controlDescription, WebElement webElement) throws Exception {
        testLogger.logTestStep("Click (using JS) " + controlDescription);

        try {
            JavascriptExecutor javascriptExecutor = new EventFiringWebDriver(driver);

            javascriptExecutor.executeScript("arguments[0].click()", webElement);
        } catch (Exception exception) {
            testLogger.fail("Error clicking (using JS) " + controlDescription, exception);
        }
    }

    /** Type text in the given selenium web element, logging a step to repro using the given control's logging description */
    public void sendKeysToWebElement(String text, String controlDescription, WebElement webElement) throws Exception {
        testLogger.logTestStep("Enter text '" + text + "' into "+ controlDescription);

        try {
            webElement.sendKeys(text);
        } catch (Exception exception) {
            testLogger.fail("Error entering text '" + text + "' into '"+ controlDescription + "'", exception);
        }
    }
}
