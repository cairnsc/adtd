package com.thoughtworks.adtd.html;

import org.apache.commons.lang3.StringEscapeUtils;

public class ElementTypeException extends Exception {

    public ElementTypeException(String element, String attrIdentifier, String attrIdentifierValue, String attrError, String attrExpected, String attrActual) {
        super("Element " + element + " with " + attrIdentifier + "=\"" + StringEscapeUtils.escapeJava(attrIdentifierValue) + "\" " + "has " + attrError + "=\"" + attrActual + "\", expected \"" + attrExpected + "\"");
    }

}
