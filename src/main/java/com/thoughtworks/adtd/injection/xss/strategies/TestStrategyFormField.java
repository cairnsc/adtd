package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.injection.xss.XssPayload;

public class TestStrategyFormField implements TestStrategy {
    private final Form form;
    private final FormData formData;
    private final int formFieldIdx;
    private final XssPayload xssPayload;

    public TestStrategyFormField(Form form, FormData formData, int formFieldIdx, XssPayload payload) {
        this.form = form;
        this.formData = formData;
        this.formFieldIdx = formFieldIdx;
        this.xssPayload = payload;
    }

    public Form getForm() {
        return form;
    }

    public FormData getFormData() {
        return formData;
    }

    public int getFormFieldIdx() {
        return formFieldIdx;
    }

    public XssPayload getPayload() {
        return xssPayload;
    }

    public Request createRequest(RequestExecutor requestExecutor) throws Exception {
        Request request = form.createRequest(requestExecutor);
        formData.setFormField(formFieldIdx, xssPayload.getPayload());
        formData.setRequestParams(request);
        formData.setImmutable();
        return request;
    }

    public boolean matches(String content) {
        return xssPayload.matches(content);
    }
}
