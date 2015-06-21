package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.http.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;
import static com.thoughtworks.adtd.util.HtmlFormUtil.getFormElements;
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
        Element form = getFormElement(doc);
        Elements formElements = getFormElements(form);



    }

    private Element getFormElement(Document doc) throws Exception {
        String selector = elementSelectorWithAttribute("form", "action", formAction);
        Elements formElements = doc.select(selector);
        if (formElements.size() != 1) {
            throw new ElementCountException("form", 1, formElements.size(), "action", formAction);
        }
        return formElements.get(0);
    }

    public Request getRequest() {
        return request;
    }

}
