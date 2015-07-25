package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.html.FormFieldData;
import org.apache.commons.lang3.RandomStringUtils;

public class TestStrategyInvalidToken implements TestStrategy {

    private final String tokenInputName;

    public TestStrategyInvalidToken(String tokenInputName) {
        this.tokenInputName = tokenInputName;
    }

    public void mutateFormData(FormData formData) {
        FormFieldData tokenField = formData.getFormField(tokenInputName);
        String newValue = RandomStringUtils.randomAlphanumeric(tokenField.getValue().length());
        formData.setFormField(tokenInputName, newValue);
    }

}
