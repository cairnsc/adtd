package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.http.responseConditions.ResponseStatusConditionFactory;

public class ResponseConditionFactory {

    public static ResponseStatusConditionFactory status() {
        return new ResponseStatusConditionFactory();
    }

}
