package com.thoughtworks.adtd.html;

import com.thoughtworks.adtd.http.ElementAttributeException;
import com.thoughtworks.adtd.http.ElementCountException;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelectorWithAttribute;

public class FormElementImpl implements Form {

    private final FormElement formElement;
    private String method;

    public static FormElementImpl getFormFromDocument(Document doc, String formAction) throws Exception {
        Elements formElements = doc.select(elementSelectorWithAttribute("form", "action", formAction));
        if (formElements.size() != 1) {
            throw new ElementCountException("form", 1, formElements.size(), "action", formAction);
        }
        return new FormElementImpl((FormElement) formElements.first());
    }

    public FormElementImpl(FormElement formElement) {
        this.formElement = formElement;
    }

    public Elements select(String cssQuery) {
        return formElement.select(cssQuery);
    }

    public Elements selectExact(String cssQuery, int numElements) throws Exception {
        Elements elements = formElement.select(cssQuery);
        if (elements.size() != numElements) {
            throw new ElementCountException(cssQuery, numElements, elements.size());
        }
        return elements;
    }

    public Element selectOne(String cssQuery) throws Exception {
        return selectExact(cssQuery, 1).first();
    }

    public String getMethod() throws Exception {
        if (method == null) {
            String methodValue = formElement.attr("method");

            if (!StringUtils.isBlank(methodValue)) {
                String sanitizedMethod = StringUtils.strip(methodValue).toUpperCase();

                if (!(sanitizedMethod.equals("GET") || sanitizedMethod.equals("POST"))) {
                    // should we be strict or should we default to get?
                    throw new ElementAttributeException("form", "method", methodValue, "is not a valid method");
                }

                method = sanitizedMethod;
            } else {
                method = "GET";
            }
        }
        return method;
    }



}

