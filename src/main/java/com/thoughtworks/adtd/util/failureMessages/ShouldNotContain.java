package com.thoughtworks.adtd.util.failureMessages;

import org.apache.commons.lang3.StringUtils;

public class ShouldNotContain {
    public static final String MESSAGE = "Expected <%s> not to contain%n  <%s>";
    public static final String MESSAGE_WITH_CONTEXT = MESSAGE + "%nError context:%n  <%s>";

    public static String shouldNotContain(String subject, String contains) {
        return String.format(MESSAGE, subject, contains);
    }

    public static String shouldNotContain(String subject, String contains, String context) {
        if (StringUtils.isBlank(contains)) {
            return shouldNotContain(subject, contains);
        }
        return String.format(MESSAGE_WITH_CONTEXT, subject, contains, context);
    }

}
