package com.thoughtworks.adtd.http.responseConditions;

import com.thoughtworks.adtd.http.ResponseCondition;
import com.thoughtworks.adtd.http.responseConditions.body.HasContent;

/**
 * Convenience methods to create {@link ResponseCondition}s for the response body.
 */
public class ResponseBodyConditionFactory {
    public HasContent isNotNullOrEmpty() {
        return new HasContent(true);
    }
    public HasContent isNullOrEmpty() {
        return new HasContent(false);
    }
}
