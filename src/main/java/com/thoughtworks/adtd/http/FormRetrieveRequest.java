package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.html.Form;
import org.jsoup.nodes.Document;

public interface FormRetrieveRequest {
    /**
     * Prepare a request to retrieve the page containing the form to be tested. By default the request method is GET.
     * @return Request.
     * @throws Exception
     */
    Request prepare() throws Exception;

    /**
     * Get the document retrieved in the request.
     * @return Document.
     * @throws Exception
     */
    Document getDocument() throws Exception;

    /**
     * Get form retrieved in the request.
     * @return Form.
     * @throws Exception
     */
    Form getForm() throws Exception;
}
