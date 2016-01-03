package com.thoughtworks.adtd.injection.http;

import com.thoughtworks.adtd.http.RequestInfo;
import com.thoughtworks.adtd.http.RequestParameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpResponseSplittingTestOrchestratorTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private RequestInfo requestInfoMock;
    public HttpResponseSplittingTestOrchestrator orchestrator;
    private RequestParameters requestParametersMock;

    @Before
    public void setUp() {
        requestInfoMock = mock(RequestInfo.class);
        requestParametersMock = mock(RequestParameters.class);
        when(requestInfoMock.getRequestParameters()).thenReturn(requestParametersMock);
        orchestrator = new HttpResponseSplittingTestOrchestrator(requestInfoMock);
    }

    @Test
    public void shouldThrowExceptionOnRemove() {
        expectedException.expect(UnsupportedOperationException.class);

        orchestrator.remove();
    }

    @Test
    public void shouldGetCountFromForm() {
        int requestParametersCount = 123;
        when(requestParametersMock.size()).thenReturn(requestParametersCount);

        int count = orchestrator.count();

        assertThat(count).isEqualTo(requestParametersCount);
    }

    @Test
    public void shouldExhaustIterator() {
        int requestParametersCount = 2;
        when(requestParametersMock.size()).thenReturn(requestParametersCount);
        int count = orchestrator.count();
        assertThat(count).isEqualTo(requestParametersCount);

        int idx;
        for (idx = 0; idx < count; idx++) {
            HttpResponseSplittingTest next = orchestrator.next();
            assertThat(next).isNotNull();
        }

        assertThat(idx).isEqualTo(count);
        assertThat(orchestrator.hasNext()).isFalse();
    }
}