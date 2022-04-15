package com.testws.model.webui;

import com.testws.library.webui.AbstractWebPage;
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
public class SearchResultsWebGrid extends AbstractWebPage {
    private static final String GRID_PAGE_LIST_ITEM_PARTIAL_CLASS_STRING =          "rc-pagination-item rc-pagination-item-";
    private static final String GRID_FIRST_PAGE_BUTTON_XPATH_STRING =               "//li[contains(@class,'" + GRID_PAGE_LIST_ITEM_PARTIAL_CLASS_STRING + "1')]";
    private static final String GRID_LAST_PAGE_BUTTON_XPATH_STRING =                "//li[contains(@class,'" + GRID_PAGE_LIST_ITEM_PARTIAL_CLASS_STRING + "')][last()]";
    private static final String GRID_NEXT_PAGE_BUTTON_XPATH_STRING =                "//li[contains(@class,'rc-pagination-next')]//a";

    private static final String SEARCH_RESULT_DETAIL_LINKS_XPATH =                  "//a[@data-testid='itemDescription']";

    private static final String ADD_ITEM_TO_CART_BY_ORDINAL_FORMAT_STRING =         "//div[@id='ProductBoxContainer'][%s]//input[@data-testid='itemAddCart']";

    public SearchResultsWebGrid(MainPortalWebPage mainPortalPage) {
        super(mainPortalPage.getChromeBrowserDriver());
    }

    /** Get the search page count from the paging controls at the bottom of the grid */
    public int getSearchPageCount(boolean failIfNoPagesFound) throws Exception {
        try {
            // Get the list item control used to page to the last page
            WebElement lastGridPageButton = findWebElement(By.xpath(GRID_LAST_PAGE_BUTTON_XPATH_STRING), false);

            // Parse the last page number from the last page string
            return Integer.parseInt(lastGridPageButton.getText().replace(GRID_PAGE_LIST_ITEM_PARTIAL_CLASS_STRING, ""));
        } catch (Exception exception) {
            if (failIfNoPagesFound) {
                testLogger.fail("Control for getting page count of " + this.getClass().getSimpleName() + " was not found -- no grid pages found", exception);
            }
        }

        return 0;
    }

    /** Page to the first grid page by clicking the page 1 button */
    public void pageToFirstPage() throws Exception {
        WebElement firstGridPageButton;
        
        try {
            // Get the list item control used to page to the first page
            firstGridPageButton = findWebElement(By.xpath(GRID_FIRST_PAGE_BUTTON_XPATH_STRING), false);

        } catch (Exception e) {
            throw new Exception("Control for going to the first page of " + this.getClass().getSimpleName() + " was not found -- no grid pages found");
        }
        
        clickWebElement("First Grid Page button", firstGridPageButton);
    }

    /** Page to the next grid page by clicking the '>' Next Page button */
    public void pageToNextPage() throws Exception {
        WebElement nextGridPageButton;

        try {
            // Get the list item control used to page to the next page
            nextGridPageButton = findWebElement(By.xpath(GRID_NEXT_PAGE_BUTTON_XPATH_STRING), false);

            clickWebElement("Next Grid Page button", nextGridPageButton);

        } catch (Exception exception) {
            testLogger.fail("Error going to the next page of " + this.getClass().getSimpleName() + " may not have been found", exception);
        }
    }

    /** Return the count of items on the current page (the number of item description controls found) */
    public int getCurrentPageItemCount() {
        return findWebElementList(By.xpath(SEARCH_RESULT_DETAIL_LINKS_XPATH)).size();
    }

    /** Get the search page result descriptions from the currently visible search page */
    public List<String> getCurrentPageSearchResultsItemDescriptions(boolean failIfNoItemsFound) throws Exception {
        List<String> itemDescriptions = new ArrayList<>();

        String itemDescription;

        List<WebElement> itemDescriptionLinks = findWebElementList(By.xpath(SEARCH_RESULT_DETAIL_LINKS_XPATH));

        if (failIfNoItemsFound && itemDescriptionLinks.size() == 0) {
            testLogger.fail("No Search items found in " + this.getClass().getSimpleName() + "", null);
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

    /** Add the item at the specified index to the cart
     * @param currentPageItemIndex      Index of the item to select relative to the items on the current page only (not all pages)
     */
    public void addItemToCart(int currentPageItemIndex) throws Exception {
        // Convert the current page index to ordinal (plus one) as xpath uses ordinals
        WebElement addItemButton = findWebElement(By.xpath(String.format(ADD_ITEM_TO_CART_BY_ORDINAL_FORMAT_STRING, currentPageItemIndex + 1)), true);

        clickWebElement("'Add to Cart' button for Search results item at current page item index " + currentPageItemIndex, addItemButton);
    }
}