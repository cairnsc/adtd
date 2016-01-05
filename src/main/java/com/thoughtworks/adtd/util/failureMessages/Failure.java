package com.thoughtworks.adtd.util.failureMessages;

import org.apache.commons.lang3.StringUtils;

public class Failure {
    public static final String MESSAGE = "Failure: <%s>";
    public static final String MESSAGE_WITH_CONTEXT = MESSAGE + "%nError context:%n  <%s>";

    public static String failure(String message) {
        return String.format(MESSAGE, message);
    }

    public static String failure(String message, String context) {
        if (StringUtils.isBlank(context)) {
            return failure(message);
        }
        return String.format(MESSAGE_WITH_CONTEXT, message, context);
    }
}
