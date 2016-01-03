package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.http.RequestInfo;
import com.thoughtworks.adtd.http.RequestParameters;
import com.thoughtworks.adtd.injection.xss.XssPayload;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static com.thoughtworks.adtd.testutil.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestStrategyIteratorRequestInfoTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private TestStrategyIteratorRequestInfo iterator;
    private RequestInfo requestInfoMock;
    private RequestParameters requestParameters;

    @Before
    public void setUp() {
        requestInfoMock = mock(RequestInfo.class);
        requestParameters = new RequestParameters();
        requestParameters.param("a", "A");
        requestParameters.param("b", "B");
        when(requestInfoMock.getRequestParameters()).thenReturn(requestParameters);
        iterator = new TestStrategyIteratorRequestInfo(requestInfoMock);
    }

    @Test
    public void shouldCreateTestStrategy() {
        TestStrategyRequestParam next = (TestStrategyRequestParam)iterator.next();
        assertThat(next.getParamIndex()).isEqualTo(0);
    }

    @Test
    public void shouldExhaustIterator() {
        int count = iterator.count();
        assertThat(count).isEqualTo(XssPayload.PAYLOAD_LIST.length * requestParameters.size());

        int idx;
        for (idx = 0; idx < count; idx++) {
            TestStrategyRequestParam next = (TestStrategyRequestParam)iterator.next();
            assertThat(next).isNotNull();
            assertThat(next.getParamIndex()).isEqualTo((idx + 1) / XssPayload.PAYLOAD_LIST.length);
        }

        assertThat(idx).isEqualTo(count);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void shouldThrowExceptionOnNextWhenIteratorExhausted() {
        TestStrategyIteratorInjected iterator = new TestStrategyIteratorInjected();
        while (iterator.hasNext()) {
            iterator.next();
        }
        expectedException.expect(NoSuchElementException.class);

        iterator.next();
    }

    @Test
    public void shouldThrowExceptionOnRemove() {
        expectedException.expect(UnsupportedOperationException.class);

        iterator.remove();
    }
}