package com.thoughtworks.adtd.http.responseConditions;

import com.thoughtworks.adtd.http.HttpStatus;

public class ResponseStatusConditionFactory {

    public ResponseStatusCondition is(HttpStatus status) {
        return new ResponseStatusCondition(status);
    }

    public ResponseStatusCondition is(int status) {
        return new ResponseStatusCondition(status);
    }

}
