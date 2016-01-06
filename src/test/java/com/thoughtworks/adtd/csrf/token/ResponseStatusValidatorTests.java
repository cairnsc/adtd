package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseStatusValidatorTests {
    public static final int STATUS_POSITIVE = 200;
    public static final int STATUS_NEGATIVE = 400;
    private ResponseStatusValidator validator;
    private CsrfTokenTest testMock;
    private Request requestMock;
    private Response responseMock;

    @Before
    public void setUp() {
        validator = new ResponseStatusValidator(STATUS_POSITIVE, STATUS_NEGATIVE);
        testMock = mock(CsrfTokenTest.class);
        requestMock = mock(Request.class);
        responseMock = mock(Response.class);
    }

    @Test
    public void shouldReturnTrueForPositiveTestWhenStatusCodeMatches() {
        when(testMock.isPositiveTest()).thenReturn(true);
        when(responseMock.getStatus()).thenReturn(STATUS_POSITIVE);

        boolean result = validator.validate(testMock, requestMock, responseMock);

        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnTrueForNegativeTestWhenStatusCodeMatches() {
        when(testMock.isPositiveTest()).thenReturn(false);
        when(responseMock.getStatus()).thenReturn(STATUS_NEGATIVE);

        boolean result = validator.validate(testMock, requestMock, responseMock);

        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseForPositiveTestWhenStatusCodeDoesNotMatch() {
        when(testMock.isPositiveTest()).thenReturn(true);
        when(responseMock.getStatus()).thenReturn(STATUS_NEGATIVE + 1);

        boolean result = validator.validate(testMock, requestMock, responseMock);

        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnFalseForNegativeTestWhenStatusCodeDoesNotMatch() {
        when(testMock.isPositiveTest()).thenReturn(false);
        when(responseMock.getStatus()).thenReturn(STATUS_NEGATIVE + 1);

        boolean result = validator.validate(testMock, requestMock, responseMock);

        assertThat(result).isFalse();
    }
}