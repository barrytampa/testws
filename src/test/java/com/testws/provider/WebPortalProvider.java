package com.testws.provider;

import com.testws.core.TestEnvironmentType;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Data Provider for the web portal, for the URL and other properties as needed that might vary with the test environment */
public class WebPortalProvider {
    public static String getMainPortalURL(TestEnvironmentType testEnvironmentType) throws Exception {
        switch (testEnvironmentType) {
            case TEST_ENVIRONMENT_PRODUCTION:
                return "https://webstaurantstore.com";

            default:
                throw new Exception("Can't get main portal URL for test environment '" + testEnvironmentType + "' - URL has not been defined in WebPortalProvider.getMainPortalURL()");
        }
    }
}