package com.thoughtworks.adtd.util.failureMessages;

public class ShouldNotBeEmpty {
    public static String expectedValue(String description) {
        return "Expected " + description + " to not be empty";
    }
}
