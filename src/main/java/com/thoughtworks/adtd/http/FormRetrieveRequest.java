package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.responseProcessors.FormResponseProcessor;
import com.thoughtworks.adtd.html.responseProcessors.HtmlResponseProcessor;
import org.jsoup.nodes.Document;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;

public class FormRetrieveRequest implements RequestExecutor {
    private final String formAction;
    private Request request;
    private HtmlResponseProcessor htmlResponseProcessor;
    private FormResponseProcessor formResponseProcessor;

    public FormRetrieveRequest(String formAction) {
        this.formAction = formAction;
    }

    /**
     * Prepare a request to retrieve the page containing the form to be tested. By default the request method is GET.
     * @return Request.
     * @throws Exception
     */
    public Request prepare() throws Exception {
        if (request != null) {
            throw new IllegalStateException("A request has already been prepared");
        }

        htmlResponseProcessor = new HtmlResponseProcessor();
        formResponseProcessor = new FormResponseProcessor(htmlResponseProcessor, formAction);
        request = new RequestImpl(this)
                .method("GET")
                .processWith(htmlResponseProcessor)
                .processWith(formResponseProcessor);

        return request;
    }

    /**
     * Get the document retrieved in the request.
     * @return Document.
     * @throws Exception
     */
    public Document getDocument() throws Exception {
        return htmlResponseProcessor.getDocument();
    }

    /**
     * Get form retrieved in the request.
     * @return Form.
     * @throws Exception
     */
    public Form getForm() throws Exception {
        return formResponseProcessor.getForm();
    }

    public Response execute(WebProxy proxy) throws Exception {
        request.expectIfUnset(status().is(HttpStatus.OK));
        return proxy.execute(request);
    }
}
