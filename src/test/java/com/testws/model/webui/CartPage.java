package com.testws.model.webui;

import com.testws.driver.ChromeBrowserDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * The Product Cart page */
public class CartPage {
    public static final String ITEM_DESCRIPTION_XPATH = "//div[contains(@class, 'cartItem ag-item gtm-product-auto')]//span[contains(@class, 'itemDescription')]//a";

    ChromeBrowserDriver chromeBrowserDriver;

    private final EmptyCartConfirmationPopupPage emptyCartConfirmationPopupPage;

    public EmptyCartConfirmationPopupPage getEmptyCartConfirmationPopupPage() {     return emptyCartConfirmationPopupPage; }

    @FindBy(xpath = "//a[contains(@class, 'emptyCartButton')]")
    public WebElement emptyCartButton;

    public CartPage(ChromeBrowserDriver chromeBrowserDriver) {
        this.chromeBrowserDriver = chromeBrowserDriver;

        chromeBrowserDriver.initPageFactoryElements(this);

        emptyCartConfirmationPopupPage =        new EmptyCartConfirmationPopupPage(chromeBrowserDriver);
    }

    /**
     * Get a list of all the Item descriptions for the items in the cart
     */
    public List<String> getItemDescriptions() {
        List<String> itemDescriptions = new ArrayList<>();

        List<WebElement> itemDescriptionWebElements = chromeBrowserDriver.findWebElementList(By.xpath(ITEM_DESCRIPTION_XPATH));

        for (WebElement itemDescriptionWebElement : itemDescriptionWebElements) {
            itemDescriptions.add(itemDescriptionWebElement.getText());
        }

        return itemDescriptions;
    }

    /**
     * Empty the cart by clicking the button to empty it, and verify there are no items in the cart
     */
    public void emptyCart() throws Exception {
        chromeBrowserDriver.clickWebElement("'Empty Cart' Button in the Cart's Header", emptyCartButton);

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

        chromeBrowserDriver.getTestLogger().getValidation().validateEquals(cartItemsCount, 0, "Cart Item Count");
    }
}