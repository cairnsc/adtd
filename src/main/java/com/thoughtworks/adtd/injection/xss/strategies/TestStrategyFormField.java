package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestInfo;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.injection.xss.XssPayload;

public class TestStrategyFormField implements TestStrategy {
    private final RequestInfo requestInfo;
    private final int paramIndex;
    private final XssPayload xssPayload;

    public TestStrategyFormField(RequestInfo requestInfo, int paramIndex, XssPayload payload) {
        this.requestInfo = requestInfo;
        this.paramIndex = paramIndex;
        this.xssPayload = payload;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    public XssPayload getXssPayload() {
        return xssPayload;
    }

    public Request createRequest(RequestExecutor requestExecutor) throws Exception {
        Request request = requestInfo.createRequest(requestExecutor, null);
        request.getParams().setParam(paramIndex, xssPayload.getPayload());
        return request;
    }

    public boolean matches(String content) {
        return xssPayload.matches(content);
    }
}
