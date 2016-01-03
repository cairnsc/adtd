package com.thoughtworks.adtd.html;

import com.thoughtworks.adtd.http.RequestInfo;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelectorWithAttribute;

/**
 * Encapsulates a HTML form.
 */
public class Form {
    private final FormElement formElement;
    private ArrayList<FormField> formFields = newArrayList();
    private String method;
    private URI action;

    public static Form getFormFromDocument(Document doc, String formAction) throws Exception {
        Elements formElements = doc.select(elementSelectorWithAttribute("form", "action", formAction));
        if (formElements.size() != 1) {
            throw new ElementCountException("form", 1, formElements.size(), "action", formAction);
        }
        return new Form((FormElement) formElements.first());
    }

    public Form(FormElement formElement) {
        this.formElement = formElement;
        for (Connection.KeyVal keyVal : formElement.formData()) {
            formFields.add(new FormField(keyVal.key(), keyVal.value()));
        }
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

    public String getAction() throws Exception {
        if (action == null) {
            String actionValue = formElement.attr("action");

            if (StringUtils.isBlank(actionValue)) {
                throw new ElementAttributeRequiredException("form", "action");
            }

            action = new URI(actionValue);
        }
        return action.toString();
    }

    /**
     * Get a count of the number of form inputs in this form.
     * @return Number of form inputs in this form.
     */
    public int countFormFields() {
        return formFields.size();
    }

    /**
     * Get the fields in this form. Preserves order.
     * @return Form fields.
     */
    public List<FormField> getFormFields() {
        return Collections.unmodifiableList(formFields);
    }

    /**
     * Create a RequestInfo for this form.
     * @return RequestInfo for this form.
     * @throws Exception
     */
    public RequestInfo getRequestInfo() throws Exception {
        return new RequestInfo(this);
    }
}

