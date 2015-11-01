package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.responseProcessors.FormResponseProcessor;
import com.thoughtworks.adtd.html.responseProcessors.FormResponseProcessorImpl;
import com.thoughtworks.adtd.html.responseProcessors.HtmlResponseProcessor;
import com.thoughtworks.adtd.html.responseProcessors.HtmlResponseProcessorImpl;
import org.jsoup.nodes.Document;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;

public class FormRetrieveRequestImpl implements FormRetrieveRequest, RequestExecutor {
    private final String formAction;
    private Request request;
    private HtmlResponseProcessor htmlResponseProcessor;
    private FormResponseProcessor formResponseProcessor;

    public FormRetrieveRequestImpl(String formAction) {
        this.formAction = formAction;
    }

    public Request prepare() throws Exception {
        if (request != null) {
            throw new IllegalStateException("A request has already been prepared");
        }

        htmlResponseProcessor = new HtmlResponseProcessorImpl();
        formResponseProcessor = new FormResponseProcessorImpl(htmlResponseProcessor, formAction);
        request = new RequestImpl(this)
                .method("GET")
                .processWith(htmlResponseProcessor)
                .processWith(formResponseProcessor);

        return request;
    }

    public Document getDocument() throws Exception {
        return htmlResponseProcessor.getDocument();
    }

    public Form getForm() throws Exception {
        return formResponseProcessor.getForm();
    }

    public Response execute(WebProxy proxy) throws Exception {
        request.expectIfUnset(status().is(HttpStatus.OK));
        return proxy.execute(request);
    }

//    public void process(Request request, Response response) throws Exception {
//        // REVISIT: check response content type to process result appropriately
//        String body = response.getBody();
//        Document doc = Jsoup.parse(body);
//        form = FormElementImpl.getFormFromDocument(doc, formAction);
////        validateForm(form);
////        validateTokenElement(form);
//    }

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

//    private boolean requestIsComplete() {
//        return (request != null && form != null);
//    }
}
