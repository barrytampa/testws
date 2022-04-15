package com.testws.model.webui;

import com.testws.library.webui.AbstractWebPage;
import com.testws.library.webui.ChromeWebBrowser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Popup that may appear if an item is selected to be added to the cart, to allow user choosing accessory options */
public class EmptyCartConfirmationPopupWebPage extends AbstractWebPage {
    @FindBy(xpath = "//button[contains(text(),'Empty Cart')]")
    public WebElement acceptButton;

    @FindBy(xpath = "//button[contains(text(),'Close')]")
    public WebElement cancelButton;

    public EmptyCartConfirmationPopupWebPage(ChromeWebBrowser chromeWebBrowser) {
        super(chromeWebBrowser);
    }

    /** Accept the dialog by pressing the Add to Cart button */
    public void accept() throws Exception {
        clickWebElement("'Empty Cart' Button in the Empty Cart Confirmation Popup", acceptButton);
    }

    /** Cancel the popup by pressing the cancel button */
    public void cancel() throws Exception {
        clickWebElement("'Close' Button in the Empty Cart Confirmation Popup", cancelButton);
    }
}