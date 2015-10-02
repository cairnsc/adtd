package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.html.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;
import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelectorWithAttribute;

public class FormRetrieveRequestImpl implements FormRetrieveRequest, RequestExecutor {
    private final String formAction;
    private Request request;
    private Form form;

    public FormRetrieveRequestImpl(String formAction) {
        this.formAction = formAction;
    }

    public Request prepare() {
        if (request != null) {
            throw new IllegalStateException("A request has already been prepared");
        }

        request = new RequestImpl(this)
                .method("GET");

        return request;
    }

    public Form getForm() throws Exception {
        if (!requestIsComplete()) {
            throw new IllegalStateException("A request must first be prepared and executed");
        }
        return form;
    }

    public Response execute(WebProxy proxy) throws Exception {
        request.expectIfUnset(status().is(HttpStatus.OK));
        return proxy.execute(request);
    }

    public void process(Request request, Response response) throws Exception {
        // REVISIT: check response content type to process result appropriately
        String body = response.getBody();
        Document doc = Jsoup.parse(body);
        form = FormElementImpl.getFormFromDocument(doc, formAction);
//        validateForm(form);
//        validateTokenElement(form);
    }

    // REVISIT: CSRF test is a outside scope of basic retrieve request
    private void validateForm(Form form) throws Exception {
//        String methodValue = form.getMethod();
//        if (!methodValue.equals("POST")) {
//            // REVISIT: we need to add an explanation of why + how to mitigate
//            throw new ElementTypeException("input", "name", tokenInputName, "method", "POST", methodValue);
//        }
    }

    private void validateTokenElement(Form form) throws Exception {
//        Element element = form.selectOne(elementSelectorWithAttribute("input", "name", tokenInputName));
//
//        String tokenValue = element.attr("value");
//        if (StringUtils.isBlank(tokenValue)) {
//            throw new ElementAttributeException("input", "name", tokenInputName, "has no value");
//        }
//
//        String typeValue = element.attr("type");
//        if (!typeValue.equals("hidden")) {
//            throw new ElementTypeException("input", "name", tokenInputName, "type", "hidden", typeValue);
//        }
    }

    private boolean requestIsComplete() {
        return (request != null && form != null);
    }
}
