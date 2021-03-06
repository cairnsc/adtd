package com.thoughtworks.adtd.util.failureMessages;

import org.apache.commons.lang3.StringUtils;

public class ShouldNotBeEmpty {
    public static final String MESSAGE = "Expected <%s> to be empty, but it was not";
    public static final String MESSAGE_WITH_CONTEXT = MESSAGE + "%nError context:%n  <%s>";

    public static String shouldNotBeEmpty(String subject) {
        return String.format(MESSAGE, subject);
    }

    public static String shouldNotBeEmpty(String subject, String context) {
        if (StringUtils.isBlank(context)) {
            return shouldNotBeEmpty(subject);
        }
        return String.format(MESSAGE_WITH_CONTEXT, subject, context);
    }
}
