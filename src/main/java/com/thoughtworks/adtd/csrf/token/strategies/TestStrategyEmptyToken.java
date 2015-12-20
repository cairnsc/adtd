package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestParameters;

public class TestStrategyEmptyToken implements TestStrategy {
    private final int paramIndex;

    public TestStrategyEmptyToken(int paramIndex) {
        this.paramIndex = paramIndex;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    public void mutateRequest(Request request) {
        RequestParameters requestParams = request.getParams();
        requestParams.setParam(paramIndex, "");
    }
}