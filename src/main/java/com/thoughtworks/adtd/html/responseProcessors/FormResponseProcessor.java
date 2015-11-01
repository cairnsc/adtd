package com.thoughtworks.adtd.html.responseProcessors;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.http.ResponseProcessor;

public interface FormResponseProcessor extends ResponseProcessor {
    /**
     * Get the form action.
     * @return Form action.
     */
    String getAction();

    /**
     * Get the parsed form.
     * @return Form.
     */
    Form getForm() throws Exception;
}
