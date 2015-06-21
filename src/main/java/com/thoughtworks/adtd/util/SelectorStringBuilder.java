package com.thoughtworks.adtd.util;

import org.apache.commons.lang3.StringEscapeUtils;

public class SelectorStringBuilder {

    public static String elementSelector(String element) {
        return element;
    }

    public static String elementSelectorWithAttribute(String element, String attribute, String attributeValue) {
        StringBuilder sb = new StringBuilder();
        sb.append(element);
        sb.append("[");
        sb.append(attribute);
        sb.append("=\"");
        sb.append(StringEscapeUtils.escapeJava(attributeValue));
        sb.append("\"]");
        return sb.toString();
    }

}
