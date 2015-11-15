package com.thoughtworks.adtd.injection.http;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;

import java.util.Iterator;

public class HttpResponseSplittingTestOrchestrator implements Iterator<HttpResponseSplittingTest> {
    private final Form form;
    private int currentIdx;

    public HttpResponseSplittingTestOrchestrator(Form form) {
        this.form = form;
        currentIdx = 0;
    }

    public boolean hasNext() {
        return (currentIdx < form.countFormFields());
    }

    public HttpResponseSplittingTest next() {
        FormData formData = new FormData(form);
        HttpResponseSplittingTest test = new HttpResponseSplittingTest(form, formData, currentIdx);
        currentIdx++;
        return test;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int count() {
        return form.countFormFields();
    }
}
