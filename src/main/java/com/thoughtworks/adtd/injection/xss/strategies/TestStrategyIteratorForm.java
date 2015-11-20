package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.injection.xss.XssPayload;

import java.util.NoSuchElementException;

public class TestStrategyIteratorForm implements TestStrategyIterator {
    private final Form form;
    private int currentFieldIdx;
    private int currentPayloadIdx;

    public TestStrategyIteratorForm(Form form) {
        this.form = form;
        currentFieldIdx = 0;
        currentPayloadIdx = 0;
    }

    public boolean hasNext() {
        return (currentFieldIdx < form.countFormFields());
    }

    public TestStrategy next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        XssPayload payload = new XssPayload(XssPayload.PAYLOAD_LIST[currentPayloadIdx]);
        FormData formData = new FormData(form);
        currentPayloadIdx++;
        if (currentPayloadIdx == XssPayload.PAYLOAD_LIST.length) {
            currentFieldIdx++;
            currentPayloadIdx = 0;
        }
        return new TestStrategyFormField(form, formData, currentFieldIdx, payload);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int count() {
        return form.countFormFields() * XssPayload.PAYLOAD_LIST.length;
    }
}
