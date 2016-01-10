package com.thoughtworks.adtd.html.failureMessages;

public class ShouldUseHttpMethod {
    public static final String MESSAGE = "Expected %s to use the HTTP method <%s> but it was <%s>";
    public static final String MESSAGE_WITH_EXPLANATION = MESSAGE + "%nExplanation: <%s>";

    public static String shouldUseHttpMethod(String subject, String expectedMethod, String actualMethod) {
        return String.format(MESSAGE, subject, expectedMethod, actualMethod);
    }

    public static String shouldUseHttpMethod(String subject, String expectedMethod, String actualMethod, String explanation) {
        return String.format(MESSAGE_WITH_EXPLANATION, subject, expectedMethod, actualMethod, explanation);
    }
}
