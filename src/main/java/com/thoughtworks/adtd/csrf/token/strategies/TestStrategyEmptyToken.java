package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.html.FormData;

public class TestStrategyEmptyToken implements TestStrategy {
    private final String tokenInputName;

    public TestStrategyEmptyToken(String tokenInputName) {
        this.tokenInputName = tokenInputName;
    }

    public String getTokenInputName() {
        return tokenInputName;
    }

    public void mutateFormData(FormData formData) {
        formData.setFormField(tokenInputName, "");
    }

}
