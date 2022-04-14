package com.testws.model.webui;

import com.testws.core.TestLogger;
import com.testws.driver.ChromeBrowserDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Search results grid control, found on the Portal's main page
 * Contains common methods to page through the grid and get search results descriptions for validation
 * */
public class SearchResultsGrid {
    private static final String GRID_PAGE_LIST_ITEM_PARTIAL_CLASS_STRING =        "rc-pagination-item rc-pagination-item-";
    private static final String GRID_NEXT_PAGE_BUTTON_XPATH_STRING =              "//li[contains(@class,'rc-pagination-next')]//a";

    private static final String SEARCH_RESULT_DETAIL_LINKS_XPATH =                "//a[@data-testid='itemDescription']";

    private final ChromeBrowserDriver chromeBrowserDriver;

    public SearchResultsGrid(MainPortalPage mainPortalPage) {
        this.chromeBrowserDriver = mainPortalPage.chromeBrowserDriver;
    }

    /** Get the search page count from the paging controls at the bottom of the grid */
    public int getSearchPageCount(boolean failIfNoPagesFound) throws Exception {
        try {
            // Get the list item control used to page to the last page
            WebElement lastGridPageButton = chromeBrowserDriver.findWebElement(By.xpath("//li[contains(@class,'" + GRID_PAGE_LIST_ITEM_PARTIAL_CLASS_STRING + "')][last()]"), false);

            // Parse the last page number from the last page string
            return Integer.parseInt(lastGridPageButton.getText().replace(GRID_PAGE_LIST_ITEM_PARTIAL_CLASS_STRING, ""));
        } catch (Exception exception) {
            if (failIfNoPagesFound) {
                chromeBrowserDriver.getTestLogger().fail("Control for getting page count of " + this.getClass().getSimpleName() + " was not found -- no grid pages found", exception);
            }
        }

        return 0;
    }

    /** Page to the first grid page by clicking the page 1 button */
    public void pageToFirstPage() throws Exception {
        WebElement firstGridPageButton;
        
        try {
            // Get the list item control used to page to the last page
            firstGridPageButton = chromeBrowserDriver.findWebElement(By.xpath("//li[contains(@class,'" + GRID_PAGE_LIST_ITEM_PARTIAL_CLASS_STRING + "1')]"), false);

        } catch (Exception e) {
            throw new Exception("Control for going to the first page of " + this.getClass().getSimpleName() + " was not found -- no grid pages found");
        }
        
        chromeBrowserDriver.clickWebElement("First Grid Page button", firstGridPageButton);
    }

    /** Page to the next grid page by clicking the '>' Next Page button */
    public void pageToNextPage() throws Exception {
        WebElement nextGridPageButton;

        try {
            // Get the list item control used to page to the last page
            nextGridPageButton = chromeBrowserDriver.findWebElement(By.xpath(GRID_NEXT_PAGE_BUTTON_XPATH_STRING), false);

        } catch (Exception e) {
            throw new Exception("Control for going to the first page of " + this.getClass().getSimpleName() + " was not found -- no grid pages found");
        }

        chromeBrowserDriver.clickWebElement("Next Grid Page button", nextGridPageButton);
    }

    /** Get the search page result descriptions from the currently visible search page */
    public List<String> getCurrentPageSearchResultsItemDescriptions(boolean failIfNoItemsFound) throws Exception {
        TestLogger testLogger = chromeBrowserDriver.getTestLogger();

        List<String> itemDescriptions = new ArrayList<>();

        String itemDescription;

        List<WebElement> itemDescriptionLinks = chromeBrowserDriver.findWebElementList(By.xpath(SEARCH_RESULT_DETAIL_LINKS_XPATH));

        if (failIfNoItemsFound && itemDescriptionLinks.size() == 0) {
            chromeBrowserDriver.getTestLogger().fail("No Search items found in " + this.getClass().getSimpleName() + "", null);
        }

        int itemOrdinal = 1;

        for (WebElement link : itemDescriptionLinks) {
            itemDescription = link.getText();

            if (itemDescription == null || "".equals(itemDescription)) {
                throw new Exception("Empty Search Item Description found in " + this.getClass().getSimpleName() + " - item descriptions should not be empty");
            }

            itemDescriptions.add(itemDescription);

            testLogger.log("Item " + itemOrdinal + ": " + itemDescription);

            itemOrdinal++;
        }

        return itemDescriptions;
    }

    /** Get the search results descriptions from all the grid pages, by paging the grid and combining results from all pages */
    public List<String> getAllPagesSearchResultsItemDescriptions(boolean failIfNotItemsFound) throws Exception {
        int resultsPageCount = getSearchPageCount(failIfNotItemsFound);

        TestLogger testLogger = chromeBrowserDriver.getTestLogger();

        testLogger.log(resultsPageCount + " page(s) of Search results data found");

        List<String> allItemDescriptions = new ArrayList<>();
        List<String> currentPageItemDescriptions;

        for (int pageIndex = 0; pageIndex < resultsPageCount; pageIndex++) {
            if (pageIndex == 0) {
                pageToFirstPage();
            } else {
                pageToNextPage();
            }

            testLogger.log("Search Results Descriptions for Page " + (pageIndex + 1));

            currentPageItemDescriptions = getCurrentPageSearchResultsItemDescriptions(failIfNotItemsFound);

            allItemDescriptions.addAll(currentPageItemDescriptions);
        }

        return allItemDescriptions;
    }

    /** At the item at the specified search results index to the cart
     * @param itemIndex     Index of the item to select. NOTE: If a NEGATIVE item index is passed, use position from the END (-1 being the last item, -2 second to last, etc.)
     */
    public void addItemToCart(int itemIndex) throws Exception {
        TestLogger testLogger = chromeBrowserDriver.getTestLogger();

        List<WebElement> addItemButtons = chromeBrowserDriver.findWebElementList(By.xpath("//input[@data-testid = 'itemAddCart']"));

        int itemIndexToUse = itemIndex < 0 ? addItemButtons.size() + itemIndex : itemIndex;

        if (itemIndexToUse < 0 || itemIndexToUse >= addItemButtons.size()) {
            testLogger.fail("Item at search results index " + itemIndex + " was not found - there were " + addItemButtons.size() + " search results", null);
        }

        chromeBrowserDriver.clickWebElement("'Add to Cart' button for Search results item at index " + itemIndexToUse, addItemButtons.get(itemIndexToUse));
    }
}