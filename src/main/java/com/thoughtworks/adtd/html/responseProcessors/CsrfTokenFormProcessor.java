package com.thoughtworks.adtd.html.responseProcessors;

import com.thoughtworks.adtd.html.*;
import com.thoughtworks.adtd.html.failureMessages.ShouldNotHaveAttributeValue;
import com.thoughtworks.adtd.html.failureMessages.ShouldUseHttpMethod;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.http.ResponseProcessor;
import com.thoughtworks.adtd.util.AssertionFailureException;
import com.thoughtworks.adtd.util.failureMessages.ShouldHaveNumElements;
import com.thoughtworks.adtd.util.failureMessages.ShouldNotBeEmpty;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.util.List;

import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelectorWithAttribute;

/**
 * Processes a Form from a HtmlResponseProcessor for a CSRF token. Verifies the properties of the token and the form
 * that contains it.
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
        updateFormField(form);

    }

    private void validateForm(Form form) throws Exception {
        String methodValue = form.getMethod();
        if (!methodValue.equals("POST")) {
            throw new AssertionFailureException(ShouldUseHttpMethod.shouldUseHttpMethod(
                "form containing CSRF token", "POST", methodValue,
                "Using the GET method may lead to leaking the CSRF token in the URL"
            ));
        }
    }

    private void validateTokenElement(Form form) throws Exception {
        Element element = form.selectOne(elementSelectorWithAttribute("input", "name", tokenInputName));

        String tokenValue = element.attr("value");
        if (StringUtils.isBlank(tokenValue)) {
            throw new AssertionFailureException(ShouldNotBeEmpty.shouldNotBeEmpty("CSRF token input"));
        }

        String typeValue = element.attr("type");
        if (!typeValue.equals("hidden")) {
            throw new AssertionFailureException(ShouldNotHaveAttributeValue.shouldNotHaveAttribute(
                    "form input containing CSRF token", "type", "hidden", typeValue
            ));
        }
    }

    private void updateFormField(Form form) throws Exception {
        List<FormField> formFields = form.getFormField(tokenInputName);
        if (formFields.size() != 1) {
            throw new AssertionFailureException(ShouldHaveNumElements.shouldHaveNumElements(
                    "Form input containing CSRF token", 1, formFields.size()
            ));
        }

        formFields.get(0).property(RequestParameterProperty.REQUEST_PARAMETER_CSRF_TOKEN,
                RequestParameterProperty.REQUEST_PARAMETER_IGNORE);
    }
}
