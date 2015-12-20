package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.http.RequestInfo;
import com.thoughtworks.adtd.http.RequestParameter;
import com.thoughtworks.adtd.http.RequestParameters;
import com.thoughtworks.adtd.injection.xss.XssPayload;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.thoughtworks.adtd.testutil.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestStrategyIteratorFormTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private TestStrategyIteratorForm iterator;
    private RequestInfo requestInfoMock;
    private RequestParameters requestParameters;

    @Before
    public void setUp() {
        requestInfoMock = mock(RequestInfo.class);
        requestParameters = new RequestParameters();
        requestParameters.param("a", "A");
        requestParameters.param("b", "B");
        when(requestInfoMock.getRequestParameters()).thenReturn(requestParameters);
        iterator = new TestStrategyIteratorForm(requestInfoMock);
    }

    @Test
    public void shouldCreateTestStrategy() {
        TestStrategyFormField next = (TestStrategyFormField)iterator.next();
        assertThat(next.getParamIndex()).isEqualTo(0);
    }

    @Test
    public void shouldExhaustIterator() {
        int count = iterator.count();
        assertThat(count).isEqualTo(XssPayload.PAYLOAD_LIST.length * requestParameters.size());

        int idx;
        for (idx = 0; idx < count; idx++) {
            TestStrategyFormField next = (TestStrategyFormField)iterator.next();
            assertThat(next).isNotNull();
            assertThat(next.getParamIndex()).isEqualTo((idx + 1) / XssPayload.PAYLOAD_LIST.length);
        }

        assertThat(idx).isEqualTo(count);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void shouldThrowExceptionOnNextWhenIteratorExhausted() {
        TestStrategyIteratorBasic iterator = new TestStrategyIteratorBasic();
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