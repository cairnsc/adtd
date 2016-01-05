package com.thoughtworks.adtd.html.responseProcessors;

import com.thoughtworks.adtd.html.ElementAttributeException;
import com.thoughtworks.adtd.html.ElementTypeException;
import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.http.ResponseProcessor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelectorWithAttribute;

/**
 * Processes a Form from a HtmlResponseProcessor for a CSRF token. Verifies the properties of the token and the form
 * that contains it.
 *
 * TODO: mark the form field as a CSRF token when the capability to do so is added.
 */
public class CsrfTokenFormProcessor implements ResponseProcessor {
    private final FormResponseProcessor formResponseProcessor;
    private final String tokenInputName;

    public CsrfTokenFormProcessor(FormResponseProcessor formResponseProcessor, String tokenInputName) {
        this.formResponseProcessor = formResponseProcessor;
        this.tokenInputName = tokenInputName;
    }

    public void prepare(Request request) {
    }

    public void process(Request request, Response response) throws Exception {
        Form form = formResponseProcessor.getForm();
        validateForm(form);
        validateTokenElement(form);
    }

    private void validateForm(Form form) throws Exception {
        String methodValue = form.getMethod();
        if (!methodValue.equals("POST")) {
            // REVISIT: we need to add an explanation of why + how to mitigate
            throw new ElementTypeException("input", "name", tokenInputName, "method", "POST", methodValue);
        }
    }

    private void validateTokenElement(Form form) throws Exception {
        Element element = form.selectOne(elementSelectorWithAttribute("input", "name", tokenInputName));

        String tokenValue = element.attr("value");
        if (StringUtils.isBlank(tokenValue)) {
            throw new ElementAttributeException("input", "name", tokenInputName, "has no value");
        }

        String typeValue = element.attr("type");
        if (!typeValue.equals("hidden")) {
            throw new ElementTypeException("input", "name", tokenInputName, "type", "hidden", typeValue);
        }
    }
}
