package com.thoughtworks.adtd.xss;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XssTestImplTests {

    @Test
    public void shouldReturnTestString() {
        String testPattern = "<script>";
        XssTestImpl test = new XssTestImpl(testPattern);

        assertThat(test.getTestPattern()).isEqualTo(testPattern);
    }

    @Test
    public void shouldMatchPattern() {
        String testPattern = "<script>adtd();</script>";
        XssTestImpl test = new XssTestImpl(testPattern);

        String content = "<html><body>" + testPattern + "</body></html>";
        boolean matches = test.matches(content);

        assertThat(matches).isTrue();
    }

    @Test
    public void shouldMatchPatternCaseInsensitive() {
        String testPattern = "<script>adtd();</script>";
        XssTestImpl test = new XssTestImpl(testPattern);

        String content = "<html><body>" + testPattern.toUpperCase() + "</body></html>";
        boolean matches = test.matches(content);

        assertThat(matches).isTrue();
    }

}
