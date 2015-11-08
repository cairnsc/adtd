package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.injection.xss.XssPattern;

public class TestStrategyFormField implements TestStrategy {
    private final Form form;
    private final FormData formData;
    private final String formField;
    private final XssPattern xssPattern;

    public TestStrategyFormField(Form form, FormData formData, String formField, XssPattern xssPattern) {
        this.form = form;
        this.formData = formData;
        this.formField = formField;
        this.xssPattern = xssPattern;
    }

    public XssPattern getXssPattern() {
        return xssPattern;
    }

    public Request createRequest(RequestExecutor requestExecutor) throws Exception {
        Request request = form.createRequest(requestExecutor);
        formData.setFormField(formField, xssPattern.getInjectionString());
        formData.setRequestParams(request);
        formData.setImmutable();
        return request;
    }

    public boolean matches(String content) {
        return xssPattern.matches(content);
    }
}
