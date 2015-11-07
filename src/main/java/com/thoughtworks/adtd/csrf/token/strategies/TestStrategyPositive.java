package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.html.FormData;

public class TestStrategyPositive implements TestStrategy {
    private final String tokenInputName;

    public TestStrategyPositive(String tokenInputName) {
        this.tokenInputName = tokenInputName;
    }

    public String getTokenInputName() {
        return tokenInputName;
    }

    public void mutateFormData(FormData formData) {
    }

}
