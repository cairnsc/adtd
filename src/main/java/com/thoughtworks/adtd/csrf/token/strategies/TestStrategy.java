package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.html.FormData;

public interface TestStrategy {

    void mutateFormData(FormData formData);

}
