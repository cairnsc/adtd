package com.thoughtworks.adtd.injection.xss;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XssPatternTest {
    @Test
    public void shouldMatchPattern() {
        String testPattern = "<script>adtd();</script>";
        XssPattern xssPattern = new XssPattern(testPattern);
        String content = "<html><body>" + testPattern + "</body></html>";

        boolean matches = xssPattern.matches(content);

        assertThat(matches).isTrue();
    }

    @Test
    public void shouldMatchPatternCaseInsensitive() {
        String testPattern = "<script>adtd();</script>";
        XssPattern xssPattern = new XssPattern(testPattern);
        String content = "<html><body>" + testPattern.toUpperCase() + "</body></html>";

        boolean matches = xssPattern.matches(content);

        assertThat(matches).isTrue();
    }
}