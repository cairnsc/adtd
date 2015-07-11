package com.thoughtworks.adtd.html;

import org.apache.commons.lang3.StringEscapeUtils;

public class ElementAttributeRequiredException extends Exception {

    public ElementAttributeRequiredException(String form, String attribute) {
        super("Element " + form + " is missing required attribute \"" + StringEscapeUtils.escapeJava(attribute) + "\"");
    }

}
