package com.thoughtworks.adtd.util;

import java.util.List;
import java.util.Map;

public interface MultiValueMap<TKey, TValue> extends Map<TKey, List<TValue>> {
    void add(TKey key, TValue value);
}
