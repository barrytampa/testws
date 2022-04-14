package com.testws.model.webui;

import com.testws.driver.ChromeBrowserDriver;
import com.testws.core.TestEnvironmentType;
import com.testws.provider.WebPortalProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Main Page for webstaurant web site portal
 */
public class MainPortalPage {
    protected final ChromeBrowserDriver chromeBrowserDriver;

    protected final TestEnvironmentType testEnvironmentType;

    private static final String SEARCH_INPUTBOX_XPATH =           "//input[@id='searchval']";
    private static final String CLEAR_SEARCH_BUTTON_XPATH =       "//button[@aria-label='Clear Search']";

    public WebElement searchInputBox;

    @FindBy(xpath = "//button[@value='Search']")
    public WebElement performSearchButton;

    @FindBy(xpath = "//a[@data-testid='cart-nav-link']")
    public WebElement viewCartLink;

    private final SearchResultsGrid searchResultsGrid;
    private final CartPage cartPage;
    private final CartProductAccessoriesPopupPage cartProductAccessoriesPopupPage;

    public SearchResultsGrid getSearchResultsGrid() {                               return searchResultsGrid; }
    public CartPage getCartPage() {                                                 return cartPage; }
    public CartProductAccessoriesPopupPage getCartProductAccessoriesPopupPage() {   return cartProductAccessoriesPopupPage; }

    public MainPortalPage(ChromeBrowserDriver chromeBrowserDriver, TestEnvironmentType testEnvironmentType) {
        this.chromeBrowserDriver = chromeBrowserDriver;

        chromeBrowserDriver.initPageFactoryElements(this);

        this.testEnvironmentType = testEnvironmentType;

        searchResultsGrid =                     new SearchResultsGrid(this);
        cartPage =                              new CartPage(chromeBrowserDriver);
        cartProductAccessoriesPopupPage =       new CartProductAccessoriesPopupPage(chromeBrowserDriver);
    }

    /** Open the Main portal page by navigating to the URL for this test environment given by the Web portal Data provider */
    public void open() throws Exception {
        chromeBrowserDriver.gotoUrl(WebPortalProvider.getMainPortalURL(testEnvironmentType));
    }

    /** Perform a search by typing the given search text into the search input box, and clicking the search button
     * If the 'clear search' button is on the page (meaning a previous search has been done) then click it to clear the previous search
     */
    public void performSearch(String searchText) throws Exception {
        WebElement clearSearchButton = chromeBrowserDriver.findWebElement(By.xpath(CLEAR_SEARCH_BUTTON_XPATH), false);

        if (clearSearchButton != null && clearSearchButton.isDisplayed()) {
            chromeBrowserDriver.clickWebElement("Clear Search Button", clearSearchButton);
        }

        searchInputBox = chromeBrowserDriver.findWebElement(By.xpath(SEARCH_INPUTBOX_XPATH), true);

        chromeBrowserDriver.sendKeysToWebElement(searchText, "Search Input Box", searchInputBox);

        chromeBrowserDriver.clickWebElement("Search Button", performSearchButton);
    }

    /** View the cart by clicking the link to open the cart */
    public void viewCart() throws Exception {
        chromeBrowserDriver.clickWebElement("View Cart Link on Main Portal Page", viewCartLink);
    }
}