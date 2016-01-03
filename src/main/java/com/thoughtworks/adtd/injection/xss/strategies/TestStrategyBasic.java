package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.http.RequestImpl;
import com.thoughtworks.adtd.injection.xss.XssPayload;

public class TestStrategyBasic implements TestStrategy {
    private final XssPayload xssPayload;

    public TestStrategyBasic(XssPayload xssPayload) {
        this.xssPayload = xssPayload;
    }

    public XssPayload getXssPayload() {
        return xssPayload;
    }

    public Request createRequest(RequestExecutor requestExecutor) throws Exception {
        return new RequestImpl(requestExecutor, "");
    }

    public boolean matches(String content) {
        return xssPayload.matches(content);
    }
}
