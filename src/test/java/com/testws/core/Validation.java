package com.testws.core;

import java.util.List;

/** Common test validation methods, which will log to the running test log
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 */
public class Validation {
    final TestLogger testLogger;

    public Validation(TestLogger testLogger) {
        this.testLogger = testLogger;

        testLogger.setValidation(this);
    }

    /** Validate two values are equal with full logging */
    public void validateEquals(Object actualValue, Object expectedValue, String itemLogDescription) throws Exception {
        String actualValueLogString =       actualValue == null     ? "" : actualValue.toString();
        String expectedValueLogString =     expectedValue == null   ? "" : expectedValue.toString();

        testLogger.logTestStep("Verify " + itemLogDescription + " is " + expectedValueLogString);

        boolean isEqual = actualValue == null ? expectedValue == null : actualValue.equals(expectedValue);

        if (!isEqual) {
            testLogger.fail(itemLogDescription + " was not '" + expectedValueLogString + "', it was '" + actualValueLogString + "'", null);
        }
    }

    /** Validate that every item in the given list contains the expected given substring, with full logging */
    public void validatePartialTextInAllListItems(List<String> list, String expectedPartialText, String listLogDescription) throws Exception {
        testLogger.logTestStep("Verify all items in " + listLogDescription + " list contain the partial text '" + expectedPartialText + "'");

        int listItemOrdinal = 1;
        int nonMatchingItemCount = 0;
        int totalItemCount = list.size();

        StringBuilder nonMatchingItemLogText = new StringBuilder();

        for (String listItemText : list) {
            if (!listItemText.contains(expectedPartialText)) {
                if (nonMatchingItemLogText.length() != 0) {
                    nonMatchingItemLogText.append("; ");
                }

                nonMatchingItemLogText.append("Item ").append(listItemOrdinal).append(": '").append(listItemText).append("'");
                nonMatchingItemCount++;
            }

            listItemOrdinal++;
        }

        if (nonMatchingItemLogText.length() > 0) {
            testLogger.fail(
                    nonMatchingItemCount + " items out of " + totalItemCount + " total in " + listLogDescription +
                            " did NOT contain expected partial text '" + expectedPartialText + "' - failures were: " + nonMatchingItemLogText,
                    null);
        } else {
            testLogger.log("All " + totalItemCount + " item(s) in " + listLogDescription + " were found to contain the sub-string '" + expectedPartialText + "' as expected");
        }
    }

    /** Validate that the list contains the given item, with full logging */
    public void validateListContainsItem(List<String> list, String item, String listLogDescription) throws Exception {
        testLogger.logTestStep("Verify " + listLogDescription + "' list contains the item '" + item);

        int indexOfItem = list.indexOf(item);

        if (indexOfItem == -1) {
            testLogger.fail(listLogDescription + " list did not contain the item '" + item + "'", null);
        } else {
            testLogger.log("Item '" + item + " was found at index " + indexOfItem + " in " + listLogDescription + " list");
        }
    }
}