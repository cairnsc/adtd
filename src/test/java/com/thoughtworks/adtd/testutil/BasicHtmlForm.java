package com.thoughtworks.adtd.testutil;

public interface BasicHtmlForm {
    String FORM_ACTION = "test";
    String FORM_METHOD = "post";
    String HIDDEN_TOKEN_NAME = "testToken";
    String HIDDEN_TOKEN_VALUE = "xyz";
    String HTML = "<html><body>" +
            "<form action=\"" + FORM_ACTION + "\" method=\"" + FORM_METHOD + "\">" +
            "<input type=\"hidden\" name=\"" + HIDDEN_TOKEN_NAME + "\" value=\"" + HIDDEN_TOKEN_VALUE + "\">" +
            "</form>" +
            "</body></html>";
}
