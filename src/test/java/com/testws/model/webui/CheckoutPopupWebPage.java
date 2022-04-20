package com.testws.model.webui;

import com.testws.library.webui.AbstractWebPage;
import com.testws.library.webui.ChromeWebBrowser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Popup that appears after adding an item to the cart, to ask the user if they want to view cart of checkout
 * The popup vanishes by itself after a short time
 * */
public class CheckoutPopupWebPage extends AbstractWebPage {
    private static final String POPUP_XPATH =               "//div[@id='watnotif-wrapper']";
    private static final String VIEW_CART_BUTTON_XPATH =    POPUP_XPATH + "//a[contains(text(),'View Cart')]";
    private static final String CHECKOUT_BUTTON_XPATH =     POPUP_XPATH + "//a[contains(text(),'Checkout')]";

    @FindBy(xpath = VIEW_CART_BUTTON_XPATH)
    private WebElement viewCartLink;

    @FindBy(xpath = CHECKOUT_BUTTON_XPATH)
    private WebElement checkoutButton;

    public CheckoutPopupWebPage(ChromeWebBrowser chromeWebBrowser) {
        super(chromeWebBrowser);
    }

    // Return true if the view Cart Link is visible within one second
    public boolean isVisible() {
        try {
            return webElementIsDisplayed(viewCartLink, 1);
        } catch (Exception exception) {
            return false;
        }
    }

    /** Accept the dialog by pressing the Add to Cart button */
    public void viewCart() throws Exception {
        clickWebElement("'View Cart' Button in the View Cart Popup", viewCartLink);
    }

    /** Cancel the popup by pressing the cancel button */
    public void checkout() throws Exception {
        clickWebElement("'Checkout' Button in the View Cart Popup", checkoutButton);
    }
}