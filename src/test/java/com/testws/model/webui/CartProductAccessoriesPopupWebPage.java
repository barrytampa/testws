package com.testws.model.webui;

import com.testws.library.webui.AbstractWebPage;
import com.testws.library.webui.ChromeWebBrowser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Popup that may appear if an item is selected to be added to the cart, to allow user choosing accessory options */
public class CartProductAccessoriesPopupWebPage extends AbstractWebPage {
    @FindBy(xpath = "//button[@aria-label='Cancel']")
    public WebElement cancelButton;

    @FindBy(xpath = "//button[contains(text(), 'Add To Cart')]")
    private WebElement addToCartButton;

    @FindBy(xpath = "//select[@name='accessories']")
    private WebElement productAccessoryDropdownWebElement;

    public CartProductAccessoriesPopupWebPage(ChromeWebBrowser chromeWebBrowser) {
        super(chromeWebBrowser);
    }

    /** Return true if the popup (the select control on it) is displayed within given time limit
     *
     * @param timeLimitInSeconds        time limit for waiting to see if web element is displayed
     * @return
     */
    public boolean isVisible(int timeLimitInSeconds) {
        return webElementIsDisplayed(productAccessoryDropdownWebElement, timeLimitInSeconds);
    }

    /** Cancel the popup by pressing the cancel button */
    public void cancel() throws Exception {
        clickWebElement("Cart Accessories Popup 'Cancel' Button", cancelButton);
    }

    /** Accept the dialog by pressing the Add to Cart button */
    public void accept() throws Exception {
        clickWebElement("Cart Accessories Popup 'Add to Cart' Button", addToCartButton);
    }

    public void selectAccessoryAtIndex(int itemIndex) throws Exception {
        testLogger.logTestStep("Select Product Accessory Dropdown item at index " + itemIndex);

        try {
            Select productAccessoryDropdownSelectControl = new Select(productAccessoryDropdownWebElement);

            productAccessoryDropdownSelectControl.selectByIndex(itemIndex);

            String selectedItemText = productAccessoryDropdownSelectControl.getFirstSelectedOption().getText();

            testLogger.log("(Item '" + selectedItemText + "' was selected in Product Accessory dropdown");
        } catch (Exception exception) {
            testLogger.fail("Failed to select Product Accessory Dropdown item at index " + itemIndex, exception);
        }
    }
}