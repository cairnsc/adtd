package com.thoughtworks.adtd.http;

import org.apache.commons.lang3.StringEscapeUtils;

public class ElementAttributeException extends Exception {

    public ElementAttributeException(String element, String attrIdentifier, String attrIdentifierValue, String reason) {
        super("Element " + element + " with " + attrIdentifier + "=\"" + StringEscapeUtils.escapeJava(attrIdentifierValue) + "\" " + reason);
    }

}
