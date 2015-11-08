package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.http.RequestImpl;
import com.thoughtworks.adtd.injection.xss.XssPattern;

public class TestStrategyBasic implements TestStrategy {
    private final XssPattern xssPattern;

    public TestStrategyBasic(XssPattern xssPattern) {
        this.xssPattern = xssPattern;
    }

    public XssPattern getXssPattern() {
        return xssPattern;
    }

    public Request createRequest(RequestExecutor requestExecutor) throws Exception {
        return new RequestImpl(requestExecutor);
    }

    public boolean matches(String content) {
        return xssPattern.matches(content);
    }
}
