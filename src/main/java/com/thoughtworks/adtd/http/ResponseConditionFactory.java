package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.http.responseConditions.ResponseBodyConditionFactory;
import com.thoughtworks.adtd.http.responseConditions.ResponseStatusConditionFactory;

public class ResponseConditionFactory {

    public static ResponseStatusConditionFactory status() {
        return new ResponseStatusConditionFactory();
    }

    public static ResponseBodyConditionFactory body() {
        return new ResponseBodyConditionFactory();
    }

}
