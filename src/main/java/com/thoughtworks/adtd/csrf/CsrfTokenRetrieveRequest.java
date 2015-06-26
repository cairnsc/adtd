package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.http.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;
import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelectorWithAttribute;

public class CsrfTokenRetrieveRequest implements RequestExecutor {

    private final String formAction;
    private final String tokenInputName;
    private Request request;

    public CsrfTokenRetrieveRequest(String formAction, String tokenInputName) {
        this.formAction = formAction;
        this.tokenInputName = tokenInputName;
        request = new RequestImpl(this).method("GET");
    }

    public Response execute(WebProxy proxy) throws Exception {
        request.expectIfUnset(status().is(HttpStatus.OK));
        return proxy.execute(request);
    }

    public void process(Request request, Response response) throws Exception {
        // REVISIT: check response content type

        String body = response.getBody();
        Document doc = Jsoup.parse(body);

        FormElement formElement = getFormElement(doc);

        Elements elements = formElement.select("input[name=\"" + tokenInputName + "\"");
        if (elements.size() != 1) {
            throw new ElementCountException("input", 1, elements.size(), "name", tokenInputName);
        }

        Element inputElement = elements.first();
        String tokenValue = inputElement.attr("value");
        if (StringUtils.isBlank(tokenValue)) {
            throw new ElementAttributeException("input", "name", tokenInputName, "has no value");
        }

        String typeValue = inputElement.attr("type");
        if (!typeValue.equals("hidden")) {
            throw new ElementTypeException("input", "name", tokenInputName, "type", "hidden", typeValue);
        }

        String methodValue = formElement.attr("method").toUpperCase();
        if (!methodValue.equals("POST")) {
            throw new ElementTypeException("input", "name", tokenInputName, "method", "POST", methodValue);
        }
    }

    private FormElement getFormElement(Document doc) throws Exception {
        String selector = elementSelectorWithAttribute("form", "action", formAction);
        Elements formElements = doc.select(selector);
        if (formElements.size() != 1) {
            throw new ElementCountException("form", 1, formElements.size(), "action", formAction);
        }
        return (FormElement) formElements.first();
    }

    public Request getRequest() {
        return request;
    }

}
