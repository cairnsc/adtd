package com.thoughtworks.adtd.html.failureMessages;

public class ShouldNotHaveAttributeValue {
    public static final String MESSAGE = "Expected <%s> attribute <%s> to have value:%n  <%s>%nbut was:%n  <%s>";

    public static String shouldNotHaveAttribute(String subject, String attribute, String expected, String actual) {
        return String.format(MESSAGE, subject, attribute, expected, actual);
    }
}
