package com.testws.library.webui;

import com.testws.core.TestLogger;
import com.testws.core.Validation;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Base Class for Web Pages in the Application's Page object model
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 *
 * Provides common low-level selenium methods with logging and validation for use internally in page classes, and access to the test logger and validation objects
 */
public abstract class AbstractWebPage {
    protected final ChromeWebBrowser chromeWebBrowser;
    protected final TestLogger testLogger;
    protected final Validation validation;

    protected WebDriver webDriver;

    public ChromeWebBrowser getChromeBrowserDriver()  { return chromeWebBrowser; }

    /** Consctructor for Abstract Page, sets up chrome browser, test logger, validation, and inits Page Factory elements */
    protected AbstractWebPage(ChromeWebBrowser chromeWebBrowser) {
        this.chromeWebBrowser =         chromeWebBrowser;
        this.testLogger =               chromeWebBrowser.getTestLogger();
        this.validation =               testLogger.getValidation();
        this.webDriver =                chromeWebBrowser.webDriver;

        // Initialize the page factory elements for this page
        PageFactory.initElements(chromeWebBrowser.webDriver, this);
    }

    // Internal DRIVER/PAGE LAYER METHODS to directly access Selenium calls, with logging

    /** Find the selenium web element with given locator, optionally failing if not found
     *  Normally we would change the selenium timeout if not expecting the web element so that we don't have an unnecessary wait of the default wait time
     * */
    protected WebElement findWebElement(By locator, boolean failIfNotFound) throws Exception {
        WebElement webElement = null;

        // If we don't have to fail if not found, use short timeout as we probably aren't expecting the element to be there
        webDriver.manage().timeouts().implicitlyWait(failIfNotFound ? 30 : 1, TimeUnit.SECONDS);

        try {
            webElement = webDriver.findElement(locator);

            webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        } catch (Exception exception) {
            webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

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
    protected boolean webElementIsDisplayed(WebElement webElement, int timeLimitInSeconds) {
        boolean isDisplayed = false;

        // If we don't have to fail if not found, use short timeout as we probably aren't expecting the element to be there
        webDriver.manage().timeouts().implicitlyWait(timeLimitInSeconds, TimeUnit.SECONDS);

        try {
            isDisplayed = webElement.isDisplayed();
        } catch (Exception exception) {}

        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        return isDisplayed;
    }

    /** Find the list of web elements with the given selenium locator */
    protected List<WebElement> findWebElementList(By locator) {
        return webDriver.findElements(locator);
    }

    /** Click the given selenium web element, logging a step to repro using the given control's logging description */
    protected void clickWebElement(String controlDescription, WebElement webElement) throws Exception {
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
    protected void clickWebElementJS(String controlDescription, WebElement webElement) throws Exception {
        testLogger.logTestStep("Click (using JS) " + controlDescription);

        try {
            JavascriptExecutor javascriptExecutor = new EventFiringWebDriver(webDriver);

            javascriptExecutor.executeScript("arguments[0].click()", webElement);
        } catch (Exception exception) {
            testLogger.fail("Error clicking (using JS) " + controlDescription, exception);
        }
    }

    /** Type text in the given selenium web element, logging a step to repro using the given control's logging description */
    protected void sendKeysToWebElement(String text, String controlDescription, WebElement webElement) throws Exception {
        testLogger.logTestStep("Enter text '" + text + "' into "+ controlDescription);

        try {
            webElement.sendKeys(text);
        } catch (Exception exception) {
            testLogger.fail("Error entering text '" + text + "' into '"+ controlDescription + "'", exception);
        }
    }
}