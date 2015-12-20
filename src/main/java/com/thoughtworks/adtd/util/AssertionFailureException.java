package com.thoughtworks.adtd.util;

public class AssertionFailureException extends Exception {
    public AssertionFailureException(String message) {
        super(message);
    }

    public AssertionFailureException(Object expected, Object actual) {
        super("expected \"" + expected + "\", actual \"" + actual + "\"");
    }

    public AssertionFailureException(String message, Object expected, Object actual) {
        super(message + ": expected \"" + expected + "\", actual \"" + actual + "\"");
    }
}
