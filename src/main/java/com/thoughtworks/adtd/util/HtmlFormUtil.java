package com.thoughtworks.adtd.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlFormUtil {

    public static String formElements[] = {
        "datalist",
        "input",
        "keygen",
        "output",
        "select",
        "textarea"
    };

    public static Elements getFormElements(Element formElement) {
        return formElement.select(StringUtils.join(formElements, ","));
    }

}
