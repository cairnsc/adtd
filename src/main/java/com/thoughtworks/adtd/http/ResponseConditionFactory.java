package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.http.responseConditions.ResponseBodyConditionFactory;
import com.thoughtworks.adtd.http.responseConditions.ResponseStatusConditionFactory;

/**
 * Convenience methods to create {@link ResponseCondition}s for use when using the fluent interface.
 */
public class ResponseConditionFactory {
    /**
     * Get a {@link ResponseStatusConditionFactory}.
     * @return ResponseStatusConditionFactory.
     */
    public static ResponseStatusConditionFactory status() {
        return new ResponseStatusConditionFactory();
    }

    public static ResponseBodyConditionFactory body() {
        return new ResponseBodyConditionFactory();
    }
}
