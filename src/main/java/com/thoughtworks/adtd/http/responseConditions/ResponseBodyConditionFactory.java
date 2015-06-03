package com.thoughtworks.adtd.http.responseConditions;

import com.thoughtworks.adtd.http.responseConditions.body.HasContent;

public class ResponseBodyConditionFactory {

    public HasContent isNotNullOrEmpty() {
        return new HasContent(true);
    }

    public HasContent isNullOrEmpty() {
        return new HasContent(false);
    }

}
