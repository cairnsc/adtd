package com.thoughtworks.adtd.injection.xss;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XssPayloadTests {
    @Test
    public void shouldMatchPattern() {
        String testPattern = "<script>adtd();</script>";
        XssPayload xssPayload = new XssPayload(testPattern);
        String content = "<html><body>" + testPattern + "</body></html>";

        boolean matches = xssPayload.matches(content);

        assertThat(matches).isTrue();
    }

    @Test
    public void shouldMatchPatternCaseInsensitive() {
        String testPattern = "<script>adtd();</script>";
        XssPayload xssPayload = new XssPayload(testPattern);
        String content = "<html><body>" + testPattern.toUpperCase() + "</body></html>";

        boolean matches = xssPayload.matches(content);

        assertThat(matches).isTrue();
    }
}