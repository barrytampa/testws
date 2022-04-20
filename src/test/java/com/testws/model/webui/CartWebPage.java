package com.testws.model.webui;

import com.testws.library.webui.AbstractWebPage;
import com.testws.library.webui.ChromeWebBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * The Product Cart page */
public class CartWebPage extends AbstractWebPage {

    // Control locators
    private static final String EMPTY_CART_BUTTON_XPATH =       "//a[contains(@class, 'emptyCartButton')]";
    private static final String ITEM_DESCRIPTIONS_XPATH =       "//div[contains(@class, 'cartItem ag-item gtm-product-auto')]//span[contains(@class, 'itemDescription')]//a";

    private final EmptyCartConfirmationPopupWebPage emptyCartConfirmationPopupPage;

    public EmptyCartConfirmationPopupWebPage getEmptyCartConfirmationPopupPage() {     return emptyCartConfirmationPopupPage; }

    @FindBy(xpath = EMPTY_CART_BUTTON_XPATH)
    private WebElement emptyCartButton;

    public CartWebPage(ChromeWebBrowser chromeWebBrowser) {
        super(chromeWebBrowser);

        emptyCartConfirmationPopupPage =        new EmptyCartConfirmationPopupWebPage(chromeWebBrowser);
    }

    /**
     * Get a list of all the Item descriptions for the items in the cart
     */
    public List<String> getItemDescriptions() {
        List<String> itemDescriptions = new ArrayList<>();

        List<WebElement> itemDescriptionWebElements = findWebElementList(By.xpath(ITEM_DESCRIPTIONS_XPATH));

        for (WebElement itemDescriptionWebElement : itemDescriptionWebElements) {
            itemDescriptions.add(itemDescriptionWebElement.getText());
        }

        return itemDescriptions;
    }

    /**
     * Empty the cart by clicking the button to empty it, and verify there are no items in the cart
     */
    public void emptyCart() throws Exception {
        clickWebElement("'Empty Cart' Button in the Cart's Header", emptyCartButton);

        emptyCartConfirmationPopupPage.accept();

        int cartItemsCount = 0;

        Timer timer = new Timer(60000, null);

        // Wait up to 60 seconds for cart count to go to zero
        while (timer.isRunning()) {
            cartItemsCount = getItemDescriptions().size();

            if (cartItemsCount == 0) {
                break;
            }

            Thread.sleep(100);
        }

        validation.validateEquals(cartItemsCount, 0, "Cart Item Count");
    }
}