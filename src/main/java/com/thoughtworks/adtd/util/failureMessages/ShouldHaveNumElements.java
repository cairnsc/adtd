package com.thoughtworks.adtd.util.failureMessages;

import org.apache.commons.lang3.StringUtils;

public class ShouldHaveNumElements {
    public static final String MESSAGE = "Expected <%s> to have <%s> elements but it has <%s>";
    public static final String MESSAGE_WITH_CONTEXT = MESSAGE + "%nError context:%n  <%s>";

    public static String shouldHaveNumElements(String subject, int expected, int actual) {
        return String.format(MESSAGE, subject, expected, actual);
    }

    public static String shouldHaveValue(String subject, int expected, int actual, String context) {
        if (StringUtils.isBlank(context)) {
            return shouldHaveNumElements(subject, expected, actual);
        }
        return String.format(MESSAGE_WITH_CONTEXT, subject, expected, actual, context);
    }
}
