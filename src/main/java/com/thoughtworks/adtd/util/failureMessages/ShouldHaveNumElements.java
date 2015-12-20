package com.thoughtworks.adtd.util.failureMessages;

public class ShouldHaveNumElements {
    public static String expectedNumElements(String message, int expected, int actual) {
        return message + ": expected " + expected + " elements, actual " + actual + " elements";
    }
}
