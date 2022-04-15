package com.testws.model.webui;

import com.testws.driver.ChromeBrowserDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Popup that may appear if an item is selected to be added to the cart, to allow user choosing accessory options */
public class EmptyCartConfirmationPopupPage {
    @FindBy(xpath = "//button[contains(text(),'Empty Cart')]")
    public WebElement acceptButton;

    @FindBy(xpath = "//button[contains(text(),'Close')]")
    public WebElement cancelButton;

    private final ChromeBrowserDriver chromeBrowserDriver;

    public EmptyCartConfirmationPopupPage(ChromeBrowserDriver chromeBrowserDriver) {
        this.chromeBrowserDriver = chromeBrowserDriver;

        chromeBrowserDriver.initPageFactoryElements(this);
    }

    /** Cancel the popup by pressing the cancel button */
    public void cancel() throws Exception {
        chromeBrowserDriver.clickWebElement("'Close' Button in the Empty Cart Confirmation Popup", cancelButton);
    }

    /** Accept the dialog by pressing the Add to Cart button */
    public void accept() throws Exception {
        chromeBrowserDriver.clickWebElement("'Empty Cart' Button in the Empty Cart Confirmation Popup", acceptButton);
    }
}