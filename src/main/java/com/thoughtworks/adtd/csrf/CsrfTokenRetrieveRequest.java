package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.html.ElementAttributeException;
import com.thoughtworks.adtd.html.ElementTypeException;
import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormElementImpl;
import com.thoughtworks.adtd.http.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;
import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelectorWithAttribute;

public class CsrfTokenRetrieveRequest implements RequestExecutor {

    private final CsrfTokenTestImpl testOrchestrator;
    private final String formAction;
    private final String tokenInputName;
    private Request request;
    private Form form;

    public CsrfTokenRetrieveRequest(CsrfTokenTestImpl testOrchestrator, String formAction, String tokenInputName) {
        this.testOrchestrator = testOrchestrator;
        this.formAction = formAction;
        this.tokenInputName = tokenInputName;
    }

    public void prepareRequest() {
        request = new RequestImpl(this)
                .method("GET");
    }

    public Request getRequest() {
        return request;
    }

    public Form getForm() {
        return form;
    }

    public Response execute(WebProxy proxy) throws Exception {
        request.expectIfUnset(status().is(HttpStatus.OK));
        return proxy.execute(request);
    }

    public void process(Request request, Response response) throws Exception {
        // REVISIT: check response content type and choose an appropriate strategy to process result
        String body = response.getBody();
        Document doc = Jsoup.parse(body);
        form = FormElementImpl.getFormFromDocument(doc, formAction);
        validateForm(form);
        validateTokenElement(form);
        testOrchestrator.notifyRequestComplete();
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
