package com.testws.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Data Provider for Search Data -- normally this would come from a file or database, but is hard-coded here for demo purposes */
public class SearchDataProvider {

    /** Get sets of search data as a Map: the key for each item is the search text to use, the Value of each item is expected partial
     * text that should be present in all the search result details.
     */
    public static Map<String, String> getExpectedSearchResultTextBySearchInputText(TestEnvironmentType testEnvironmentType) throws Exception {
        Map<String, String> searchDataMap = new HashMap<>();

        switch (testEnvironmentType) {
            case TEST_ENVIRONMENT_PRODUCTION:
                searchDataMap.put("steel work table", "Table");     // <---used for code test
                searchDataMap.put("Steel Work Table", "Table");     // Add more data here to get coverage of different permutations
                break;

            default:
                throw new Exception("Can't get search data for test environment '" + testEnvironmentType + "' - it has not been defined in SearchDataProvider");
        }

        if (searchDataMap.size() == 0) {
            throw new Exception("No search data entries were provided for test environment '" + testEnvironmentType + "' - in SearchDataProvider");
        }

        return searchDataMap;
    }
}
