package com.thoughtworks.adtd.http;

import java.util.List;

public interface RequestParameter {
    String getName();
    List<String> getValues();
}
