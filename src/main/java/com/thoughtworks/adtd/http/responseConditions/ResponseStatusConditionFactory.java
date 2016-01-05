package com.thoughtworks.adtd.http.responseConditions;

import com.thoughtworks.adtd.http.HttpStatus;
import com.thoughtworks.adtd.http.ResponseCondition;
import com.thoughtworks.adtd.http.responseConditions.status.HasStatusCode;

/**
 * Convenience methods to create {@link ResponseCondition}s for the response HTTP status code.
 */
public class ResponseStatusConditionFactory {
    public HasStatusCode is(HttpStatus status) {
        return new HasStatusCode(status);
    }
    public HasStatusCode is(int status) {
        return new HasStatusCode(status);
    }
}
