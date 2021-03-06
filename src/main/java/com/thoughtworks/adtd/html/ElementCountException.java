package com.thoughtworks.adtd.html;

import org.apache.commons.lang3.StringEscapeUtils;

public class ElementCountException extends Exception {

    public ElementCountException(String cssQuery, int countExpected, int countActual) {
        super("Expected " + countExpected + " elements matching \"" + cssQuery + "\", found " + countActual);
    }

    public ElementCountException(String element, int countExpected, int countActual, String attribute, String attributeValue) {
        super("Expected " + countExpected + " " + element + " elements with " + attribute + "=\"" + StringEscapeUtils.escapeJava(attributeValue) + "\", found " + countActual);
    }

}
