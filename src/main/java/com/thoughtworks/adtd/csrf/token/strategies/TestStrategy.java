package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.html.FormData;

public interface TestStrategy {
    String getTokenInputName();
    void mutateFormData(FormData formData);
}
