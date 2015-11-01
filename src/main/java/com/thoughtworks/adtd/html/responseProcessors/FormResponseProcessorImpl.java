package com.thoughtworks.adtd.html.responseProcessors;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormElementImpl;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import org.jsoup.nodes.Document;

public class FormResponseProcessorImpl implements FormResponseProcessor {
    private final HtmlResponseProcessor htmlResponseProcessor;
    private final String formAction;
    private Response response;
    private Form form;

    public FormResponseProcessorImpl(HtmlResponseProcessor htmlResponseProcessor, String formAction) {
        this.htmlResponseProcessor = htmlResponseProcessor;
        this.formAction = formAction;
    }

    public String getAction() {
        return formAction;
    }

    public Form getForm() throws Exception {
        if (!responseProcessed()) {
            throw new IllegalStateException("A response has not yet been processed");
        }
        return form;
    }

    public void prepare(Request request) {
    }

    public void process(Request request, Response response) throws Exception {
        this.response = response;
        Document document = htmlResponseProcessor.getDocument();
        form = FormElementImpl.getFormFromDocument(document, formAction);
    }

    private boolean responseProcessed() {
        return response != null;
    }
}
