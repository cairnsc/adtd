package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.csrf.token.strategies.TestStrategy;
import com.thoughtworks.adtd.csrf.token.strategies.TestStrategyEmptyToken;
import com.thoughtworks.adtd.csrf.token.strategies.TestStrategyInvalidToken;
import com.thoughtworks.adtd.csrf.token.strategies.TestStrategyPositive;
import com.thoughtworks.adtd.http.RequestInfo;
import com.thoughtworks.adtd.http.RequestParameter;
import com.thoughtworks.adtd.http.RequestParameters;
import com.thoughtworks.adtd.http.ResponseValidator;
import com.thoughtworks.adtd.util.AssertionFailureException;
import com.thoughtworks.adtd.util.failureMessages.ShouldHaveNumElements;
import com.thoughtworks.adtd.util.failureMessages.ShouldNotBeEmpty;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Tests:
 *  1. Positive case: valid token submitted
 *  2. Negative case: missing token
 *  3. Negative case: invalid token
 */
public class CsrfTokenTestOrchestrator implements Iterator<CsrfTokenTest> {
    private final String tokenInputName;
    private final ResponseValidator validator;
    private List<CsrfTokenTest> testList;
    private final Iterator<CsrfTokenTest> iterator;

    public CsrfTokenTestOrchestrator(RequestInfo requestInfo, ResponseValidator validator, String tokenInputName) throws Exception {
        requestInfo.setImmutable();
        this.tokenInputName = tokenInputName;
        this.validator = validator;
        createTestList(requestInfo, tokenInputName, validator);
        iterator = testList.iterator();
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public CsrfTokenTest next() {
        return iterator.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int count() {
        return testList.size();
    }

    private void createTestList(RequestInfo requestInfo, String tokenInputName, ResponseValidator validator) throws Exception {
        Integer paramIndex = getParamIndex(requestInfo, tokenInputName);
        checkParamValue(requestInfo.getRequestParameters(), paramIndex);
        testList = newArrayList();
        addTest(requestInfo, validator, new TestStrategyPositive(paramIndex));
        addTest(requestInfo, validator, new TestStrategyEmptyToken(paramIndex));
        addTest(requestInfo, validator, new TestStrategyInvalidToken(paramIndex));
    }

    private void addTest(RequestInfo requestInfo, ResponseValidator validator, TestStrategy testStrategy) {
        testList.add(new CsrfTokenTest(testStrategy, requestInfo, validator));
    }

    private Integer getParamIndex(RequestInfo requestInfo, String tokenInputName) throws AssertionFailureException {
        List<Integer> paramIndices = requestInfo.getRequestParameters().paramIndexOf(tokenInputName);
        if (paramIndices.size() != 1) {
            throw new AssertionFailureException(ShouldHaveNumElements.expectedNumElements(
                "CSRF token named \"" +  tokenInputName + "\"", 1, paramIndices.size()
            ));
        }
        return paramIndices.get(0);
    }

    private void checkParamValue(RequestParameters requestParameters, Integer paramIndex) throws AssertionFailureException {
        RequestParameter param = requestParameters.getParam(paramIndex);
        List<String> values = param.getValues();
        if (values.size() != 1) {
            throw new AssertionFailureException(ShouldHaveNumElements.expectedNumElements(
                    "CSRF token at request parameter index " + paramIndex, 1, values.size()
            ));
        }

        String value = values.get(0);
        if (StringUtils.isBlank(value)) {
            throw new AssertionFailureException(ShouldNotBeEmpty.expectedValue(
                    "CSRF token at request parameter index " + paramIndex
            ));
        }
    }
}
