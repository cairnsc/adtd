package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.responseProcessors.CsrfFormTokenProcessor;
import com.thoughtworks.adtd.html.responseProcessors.FormResponseProcessor;
import com.thoughtworks.adtd.html.responseProcessors.HtmlResponseProcessor;
import org.jsoup.nodes.Document;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;

/**
 * A request to perform reconnaissance by inspecting a HTML form. Produces a {@link com.thoughtworks.adtd.html.Form},
 * which can be used to create a {@link com.thoughtworks.adtd.http.RequestInfo}.
 */
public class FormRetrieveRequest implements RequestExecutor {
    private final String formAction;
    private Request request;
    private HtmlResponseProcessor htmlResponseProcessor;
    private FormResponseProcessor formResponseProcessor;
    private String csrfTokenInputName;

    /**
     * Instantiate a FormRetrieveRequest.
     * @param formAction Form action attribute to identify the form in the HTML document.
     */
    public FormRetrieveRequest(String formAction) {
        this.formAction = formAction;
    }

    /**
     * Indicate an input parameter in the form is a CSRF token. Must be invoked before the Request is prepared.
     * @param tokenInputName Name of the input parameter containing of the CSRF token.
     * @return This FormRetrieveRequest.
     */
    public FormRetrieveRequest withCsrfToken(String tokenInputName) {
        checkMutability();
        this.csrfTokenInputName = tokenInputName;
        return this;
    }

    /**
     * Prepare a request to retrieve the page containing the form to be tested. By default the request method is GET.
     * @return Request.
     * @throws Exception
     */
    public Request prepare() throws Exception {
        checkMutability();
        htmlResponseProcessor = new HtmlResponseProcessor();
        formResponseProcessor = new FormResponseProcessor(htmlResponseProcessor, formAction);
        request = new RequestImpl(this, getRequestContext())
                .method("GET")
                .processWith(htmlResponseProcessor)
                .processWith(formResponseProcessor);

        if (csrfTokenInputName != null) {
            CsrfFormTokenProcessor csrfFormTokenProcessor = new CsrfFormTokenProcessor(formResponseProcessor, csrfTokenInputName);
            request.processWith(csrfFormTokenProcessor);
        }
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
     * Get information about the form retrieved in the request.
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

    public void process(Request request, Response response) throws Exception {
    }

    private String getRequestContext() {
        return String.format("FormRetrieveRequest for a form with action=<%s>", formAction);
    }

    private void checkMutability() {
        if (request != null) {
            throw new IllegalStateException("A request has already been prepared");
        }
    }
}
