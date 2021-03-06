package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestParameter;
import com.thoughtworks.adtd.http.RequestParameters;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Test strategy for a request with an invalid CSRF token. Generates a random alphanumeric value of the same length as
 * the CSRF token.
 */
public class TestStrategyInvalidToken implements TestStrategy {
    public static final String STRATEGY_NAME = "Invalid CSRF token (random alphanumeric value of same length)";
    private final int paramIndex;

    public TestStrategyInvalidToken(int paramIndex) {
        this.paramIndex = paramIndex;
    }

    public String getStrategyName() {
        return STRATEGY_NAME;
    }

    public boolean isPositiveTest() {
        return false;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    public void mutateRequest(Request request) throws AssertionFailureException {
        RequestParameters requestParams = request.getParams();
        RequestParameter param = requestParams.getParam(paramIndex);
        String value = param.getValues().get(0);
        String newValue = RandomStringUtils.randomAlphanumeric(value.length());
        requestParams.setParam(paramIndex, newValue);
    }
}
