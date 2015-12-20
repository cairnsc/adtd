package com.thoughtworks.adtd.testutil.assertions;

import com.thoughtworks.adtd.http.RequestParameters;

public class Assertions extends org.assertj.core.api.Assertions {
    public static RequestParametersAssert assertThat(RequestParameters requestParameters) {
        return new RequestParametersAssert(requestParameters);
    }
}
