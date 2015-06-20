package com.thoughtworks.adtd.util;

import org.junit.Test;

import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelectorWithAttribute;
import static org.assertj.core.api.Assertions.assertThat;

public class SelectorStringBuilderTests {

    @Test
    public void shouldBuildElementWithAttributeSelector() {
        String element = "a";
        String attribute = "b";
        String attributeValue = "c\"";

        String selector = elementSelectorWithAttribute(element, attribute, attributeValue);

        assertThat(selector).isEqualTo("a[b=\"c\\\"\"]");
    }

}
