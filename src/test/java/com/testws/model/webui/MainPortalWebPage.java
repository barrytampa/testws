package com.testws.model.webui;

import com.testws.library.webui.AbstractWebPage;
import com.testws.library.webui.ChromeWebBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Main Page for webstaurant web site portal
 */
public class MainPortalWebPage extends AbstractWebPage {
    // Control Locator strings
    private static final String VIEW_CART_LINK_XPATH =              "//a[@data-testid='cart-nav-link']";
    private static final String SEARCH_BUTTON_XPATH =               "//button[@value='Search']";
    private static final String SEARCH_INPUTBOX_XPATH =             "//input[@id='searchval']";
    private static final String CLEAR_SEARCH_BUTTON_XPATH =         "//button[@aria-label='Clear Search']";

    public WebElement searchInputBox;

    @FindBy(xpath = SEARCH_BUTTON_XPATH)
    public WebElement performSearchButton;

    @FindBy(xpath = VIEW_CART_LINK_XPATH)
    public WebElement viewCartLink;

    private final SearchResultsWebGrid searchResultsWebGrid;
    private final CartWebPage cartPage;
    private final CartProductAccessoriesPopupWebPage cartProductAccessoriesPopupPage;
    private final CheckoutPopupWebPage checkoutPopupPage;

    public SearchResultsWebGrid getSearchResultsGrid() {                                   return searchResultsWebGrid; }
    public CartWebPage getCartPage() {                                                  return cartPage; }
    public CartProductAccessoriesPopupWebPage getCartProductAccessoriesPopupPage() {    return cartProductAccessoriesPopupPage; }
    public CheckoutPopupWebPage getCheckoutPopupPage() {                                return checkoutPopupPage; }

    public MainPortalWebPage(ChromeWebBrowser chromeWebBrowser) {
        super(chromeWebBrowser);

        searchResultsWebGrid =                     new SearchResultsWebGrid(this);
        cartPage =                              new CartWebPage(chromeWebBrowser);
        cartProductAccessoriesPopupPage =       new CartProductAccessoriesPopupWebPage(chromeWebBrowser);
        checkoutPopupPage =                     new CheckoutPopupWebPage(chromeWebBrowser);
    }

    /** Open the Main portal page by navigating to the URL for this test environment given by the Web portal Data provider */
    public void open(String url) throws Exception {
        chromeWebBrowser.gotoUrl(url);
    }

    /** Perform a search by typing the given search text into the search input box, and clicking the search button
     * If the 'clear search' button is on the page (meaning a previous search has been done) then click it to clear the previous search
     */
    public void performSearch(String searchText) throws Exception {
        WebElement clearSearchButton = findWebElement(By.xpath(CLEAR_SEARCH_BUTTON_XPATH), false);

        if (clearSearchButton != null && clearSearchButton.isDisplayed()) {
            clickWebElement("Clear Search Button", clearSearchButton);
        }

        searchInputBox = findWebElement(By.xpath(SEARCH_INPUTBOX_XPATH), true);

        sendKeysToWebElement(searchText, "Search Input Box", searchInputBox);

        clickWebElement("Search Button", performSearchButton);
    }

    /** View the cart by clicking the link to open the cart */
    public void viewCart() throws Exception {

        // If the checkout popup page is visible, use the View Cart button on it, otherwise just use the View Cart link in the header
        if (checkoutPopupPage.isVisible()) {
            checkoutPopupPage.viewCart();
        } else {
            clickWebElement("View Cart Link on Main Portal Page", viewCartLink);
        }
    }
}