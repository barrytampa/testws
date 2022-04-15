package com.testws.library.webui;

import com.testws.core.TestLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Wrapper for Chrome Browser Driver class, support for common control methods with test logging
 */
public class ChromeWebBrowser {
    protected WebDriver webDriver;

    protected TestLogger testLogger;

    public TestLogger getTestLogger() {
        return testLogger;
    }

    /**
     * Opens and maximized the Chrome browser driver, setting up chromedriver using WebDriverManager, also attaching a TestLogger to the browser
     *
     * Note: Initializing and opening browser isn't done in the constructor in case multiple browsers are used for factory test cases, they must be started separately from object instantiation
     * @param testLogger            Test Logger to attach to the browser
     */
    public ChromeWebBrowser open(TestLogger testLogger) {
        this.testLogger = testLogger;

        WebDriverManager.chromedriver().setup();

        testLogger.logTestStep("Open Chrome Web Browser");

        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        return this;
    }

    /** Go to the given URL, logging a test step
     *
     * @param urlText       URL string to navigate to
     */
    public void gotoUrl(String urlText) throws Exception {
        testLogger.logTestStep("Go to URL '" + urlText + "'");

        try {
            webDriver.get(urlText);
        } catch (Exception exception) {
            testLogger.fail("Failure opening URL '" + urlText + "'", exception);
        }
    }

    /** Closes the Chrome browser */
    public void close() {
        testLogger.logTestStep("Close Chrome Browser");

        webDriver.quit();
    }
}