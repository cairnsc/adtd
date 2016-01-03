package com.thoughtworks.adtd.util.failureMessages;

import org.apache.commons.lang3.StringUtils;

public class ShouldBeEmpty {
    public static final String MESSAGE = "Expected <%s> to not be empty, but it was";
    public static final String MESSAGE_WITH_CONTEXT = MESSAGE + "%nError context:%n  <%s>";

    public static String shouldBeEmpty(String subject) {
        return String.format(MESSAGE, subject);
    }

    public static String shouldBeEmpty(String subject, String context) {
        if (StringUtils.isBlank(context)) {
            return shouldBeEmpty(subject);
        }
        return String.format(MESSAGE_WITH_CONTEXT, subject, context);
    }
}
