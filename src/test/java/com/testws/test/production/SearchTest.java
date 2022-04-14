package com.testws.test.production;

import com.testws.core.TestLogger;
import com.testws.core.Validation;
import com.testws.driver.ChromeBrowserDriver;
import com.testws.model.webui.CartPage;
import com.testws.model.webui.CartProductAccessoriesPopupPage;
import com.testws.model.webui.MainPortalPage;
import com.testws.model.webui.SearchResultsGrid;
import com.testws.provider.SearchDataProvider;
import com.testws.core.TestEnvironmentType;
import org.testng.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Requested test case to use the web portal's search function, validate that all search results contain an expected string, add last search item to cart, and empty the cart */
public class SearchTest {
    // Define the test environment for this test (Production)
    private final TestEnvironmentType testEnvironmentType =       TestEnvironmentType.TEST_ENVIRONMENT_PRODUCTION;

    // Data Providers
    private SearchDataProvider searchDataProvider;

    // UI elements
    private ChromeBrowserDriver chromeBrowserDriver;
    private MainPortalPage mainPortalPage;

    // Logging and validation
    private final TestLogger testLogger =                         new TestLogger();
    private final Validation validation =                         new Validation(testLogger);

    /** Set up the test case by using the Production environment, initializing a Chrome web driver, and opening the Main Portal Web page */
    @BeforeMethod
    public void setUp() throws Exception {
        searchDataProvider =    new SearchDataProvider(TestEnvironmentType.TEST_ENVIRONMENT_PRODUCTION);

        chromeBrowserDriver =   new ChromeBrowserDriver().initialize(testLogger);

        mainPortalPage =        new MainPortalPage(chromeBrowserDriver, testEnvironmentType);

        mainPortalPage.open();
    }

    /** Clean up the test case by closing the web page and Chrome browser */
    @AfterMethod
    public void tearDown() {
        chromeBrowserDriver.quit();
    }

    /** Test Search by getting expected valid data for the test environment, performing the search, and validating expected result string in all the search results */
    @Test
    public void testMainPortalPageSearch() throws Exception {
        SearchResultsGrid searchResultsGrid =                               mainPortalPage.getSearchResultsGrid();
        CartProductAccessoriesPopupPage cartProductAccessoriesPopupPage =   mainPortalPage.getCartProductAccessoriesPopupPage();
        CartPage cartPage =                                                 mainPortalPage.getCartPage();

        // Get the search data from the search data provider
        Map<String, String> expectedSearchResultTextBySearchInputText = searchDataProvider.getExpectedSearchResultTextBySearchInputText();

        // Break out the search text and expected result, from the first entry only
        String searchText =                         (String)expectedSearchResultTextBySearchInputText.keySet().toArray()[0];
        String expectedSearchPartialText =          expectedSearchResultTextBySearchInputText.get(searchText);

        // Perform the search in the web UI's Main Page
        mainPortalPage.performSearch(searchText);

        // Get a list of all search descriptions from the Search results Grid on the main portal page (expect results are found for this test)
        // The method will perform the paging needed in the search results grid to get search results from all pages
        List<String> searchResultsDescriptions =    searchResultsGrid.getAllPagesSearchResultsItemDescriptions(true);

        // Validate the expected partial text is in all the search results descriptions
        // If one or more results don't match, the method will log ALL mismatching results across all the web pages before failing the test case
        validation.validatePartialTextInAllListItems(searchResultsDescriptions, expectedSearchPartialText, "Search Results Descriptions");

        // Note the item description of the last item in the all items list (also the last item on the current page)
        String lastSearchItemDescription = searchResultsDescriptions.get(searchResultsDescriptions.size() - 1);

        // Add the last item to the cart - need to get the current page item count to calculate its index on the current page
        searchResultsGrid.addItemToCart(searchResultsGrid.getCurrentPageItemCount() - 1);

        // If the product accessory popup appears, select the first option and click add to cart button
        if (cartProductAccessoriesPopupPage.isVisible()) {
            cartProductAccessoriesPopupPage.selectAccessoryAtIndex(1);

            cartProductAccessoriesPopupPage.accept();
        }

        // Navigate to the Cart
        mainPortalPage.viewCart();

        // get the current items in the cart
        List<String> cartItemDescriptions = cartPage.getItemDescriptions();

        // Verify the item we should have selected is now in the cart
        validation.validateListContainsItem(cartItemDescriptions, lastSearchItemDescription, "Cart Products");

        // Empty the cart and confirm it has no items left after emptying
        cartPage.emptyCart();
    }
}