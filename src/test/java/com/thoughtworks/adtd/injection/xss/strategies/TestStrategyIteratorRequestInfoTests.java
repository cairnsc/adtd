package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.html.RequestParameterProperty;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.http.RequestInfo;
import com.thoughtworks.adtd.http.RequestParameter;
import com.thoughtworks.adtd.http.RequestParameters;
import com.thoughtworks.adtd.injection.xss.XssPayload;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.thoughtworks.adtd.testutil.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestStrategyIteratorRequestInfoTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private TestStrategyIteratorRequestInfo iterator;
    private RequestInfo requestInfoMock;
    private RequestParameters requestParameters;

    @Test
    public void shouldCreateTestStrategy() {
        createIteratorWithSimpleRequestInfo();
        TestStrategyRequestParam next = (TestStrategyRequestParam)iterator.next();

        assertThat(next.getParamIndex()).isEqualTo(0);
    }

    @Test
    public void shouldExhaustIterator() {
        createIteratorWithSimpleRequestInfo();
        int count = XssPayload.PAYLOAD_LIST.length * requestParameters.size();

        int idx;
        for (idx = 0; idx < count; idx++) {
            TestStrategyRequestParam next = (TestStrategyRequestParam)iterator.next();
            assertThat(next).isNotNull();
            assertThat(next.getParamIndex()).isEqualTo(idx / XssPayload.PAYLOAD_LIST.length);
            assertThat(next.getXssPayload().getPayload()).isEqualTo(XssPayload.PAYLOAD_LIST[idx % XssPayload.PAYLOAD_LIST.length]);
        }

        assertThat(idx).isEqualTo(count);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void shouldSkipImmutableFields() throws Exception {
        requestInfoMock = mock(RequestInfo.class);
        requestParameters = new RequestParameters();
        requestParameters.param("a", Collections.singletonList("A"), EnumSet.of(RequestParameterProperty.REQUEST_PARAMETER_IGNORE));
        requestParameters.param("b", "B");
        requestParameters.param("c", Collections.singletonList("C"), EnumSet.of(RequestParameterProperty.REQUEST_PARAMETER_IGNORE));
        requestParameters.param("D", "d");
        when(requestInfoMock.getRequestParameters()).thenReturn(requestParameters);
        iterator = new TestStrategyIteratorRequestInfo(requestInfoMock);
        int paramIndexes[] = { 0, 2 };

        while (iterator.hasNext()) {
            TestStrategyRequestParam next = (TestStrategyRequestParam)iterator.next();
            assertThat(next.getParamIndex()).isNotIn(paramIndexes);
        }
    }

    @Test
    public void shouldThrowExceptionOnNextWhenIteratorExhausted() {
        createIteratorWithSimpleRequestInfo();
        while (iterator.hasNext()) {
            iterator.next();
        }
        expectedException.expect(NoSuchElementException.class);

        iterator.next();
    }

    @Test
    public void shouldThrowExceptionOnRemove() {
        createIteratorWithSimpleRequestInfo();
        expectedException.expect(UnsupportedOperationException.class);

        iterator.remove();
    }

    private void createIteratorWithSimpleRequestInfo() {
        requestInfoMock = mock(RequestInfo.class);
        requestParameters = new RequestParameters();
        requestParameters.param("a", "A");
        requestParameters.param("b", "B");
        when(requestInfoMock.getRequestParameters()).thenReturn(requestParameters);
        iterator = new TestStrategyIteratorRequestInfo(requestInfoMock);
    }
}