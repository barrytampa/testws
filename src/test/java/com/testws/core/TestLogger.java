package com.testws.core;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Copyright (c) 2022. Barry Ollikkala
 * Created by Barry Ollikkala on 4/13/2022.
 *
 * Very simple test logging class to support write test steps and simple log lines to a file, and test failures
 */
public class TestLogger {
    private Validation validation;

    int currentTestStepOrdinal = 1;

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public Validation getValidation() {
        return validation;
    }

    /**
     * Write a numbered test step to the test log, incrementing the test step ordinal
     * @param text      Text to write as test step to test log
     */
    public void logTestStep(String text) {
        writeLogLine(currentTestStepOrdinal + ". " + text);

        currentTestStepOrdinal++;
    }

    /**
     * Write a plain text string to the test log
     * @param text      Text to write to test log
     */
    public void log(String text) {
        writeLogLine("  " + text);
    }

    /** Write a failure to the test log, then throw an exception to end the test (normally we would capture a screenshot here too)
     *
     * @param text          Text to log as failure
     * @param exception     Exception to be logged
     */
    public void fail(String text, Exception exception) throws Exception {
        writeLogLine("*********");
        writeLogLine("TEST FAILURE: " + text);
        writeLogLine("*********");

        StringWriter sw = new StringWriter();

        if (exception == null) {
            exception = new Exception(text);
        }

        exception.printStackTrace(new PrintWriter(sw));

        String exceptionLogText = "EXCEPTION " + exception.getClass().getSimpleName() + ": " + exception.getMessage();

        log("");
        writeLogLine(exceptionLogText);
        writeLogLine(sw.toString());
        log("");

        throw exception;
    }

    /** Internal method to perform the actual writing to the test log - for demo purposes we will just write to the console
     *
     * @param text      Text to write to the Test Log
     */
    private void writeLogLine(String text) {
        System.out.println(text);
    }
}