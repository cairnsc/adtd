package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.html.Form;

public interface FormRetrieveRequest {
    /**
     * Prepare a request to retrieve the page containing the form to be tested. By default the request method is GET.
     * @return Request.
     */
    Request prepare();

    /**
     * Get form retrieved in the request.
     * @return Form.
     * @throws Exception The form is not available.
     */
    Form getForm() throws Exception;
}
