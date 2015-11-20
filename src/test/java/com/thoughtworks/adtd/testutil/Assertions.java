package com.thoughtworks.adtd.testutil;

import com.thoughtworks.adtd.html.FormData;

public class Assertions extends org.assertj.core.api.Assertions {
    public static FormDataAssert assertThat(FormData formData) {
        return new FormDataAssert(formData);
    }
}
