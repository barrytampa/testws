package com.testws.core;

/**
 *  Copyright (c) 2022. Barry Ollikkala
 *  Created by Barry Ollikkala on 4/13/2022.
 *
 * Test Environment Types --- normally are passed from test suite files to the test case from the CI build and test suite config
 */
public enum TestEnvironmentType {
    TEST_ENVIRONMENT_TEST,
    TEST_ENVIRONMENT_STAGING,
    TEST_ENVIRONMENT_PRODUCTION
}