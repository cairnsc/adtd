package com.thoughtworks.adtd.util.failureMessages;

import org.apache.commons.lang3.StringUtils;

public class ShouldHaveValue {
    public static final String MESSAGE = "Expected <%s> to be equal to:%n  <%s>%nbut was:%n  <%s>";
    public static final String MESSAGE_WITH_CONTEXT = MESSAGE + "%nError context:%n  <%s>";

    public static String shouldHaveValue(String subject, String expected, String actual) {
        return String.format(MESSAGE, subject, expected, actual);
    }

    public static String shouldHaveValue(String subject, String expected, String actual, String context) {
        if (StringUtils.isBlank(context)) {
            return shouldHaveValue(subject, expected, actual);
        }
        return String.format(MESSAGE_WITH_CONTEXT, subject, expected, actual, context);
    }

    public static String shouldHaveValue(String subject, int statusCode, int status) {
        return shouldHaveValue(subject, Integer.toString(statusCode), Integer.toString(status));
    }

    public static String shouldHaveValue(String subject, int statusCode, int status, String context) {
        return shouldHaveValue(subject, Integer.toString(statusCode), Integer.toString(status), context);
    }
}
