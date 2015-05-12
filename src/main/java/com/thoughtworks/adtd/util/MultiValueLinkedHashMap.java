package com.thoughtworks.adtd.util;

import java.util.*;

public class MultiValueLinkedHashMap<TKey, TValue> implements MultiValueMap<TKey,TValue> {

    private final Map<TKey, List<TValue>> map;

    public MultiValueLinkedHashMap() {
        map = new LinkedHashMap<TKey, List<TValue>>();
    }

    public void add(TKey key, TValue value) {
        List<TValue> list = map.get(key);
        if (list == null) {
            list = new LinkedList<TValue>();
            map.put(key, list);
        }
        list.add(value);
    }

    public void add(TKey key, TValue... values) {
        List<TValue> list = map.get(key);
        if (list == null) {
            list = new LinkedList<TValue>();
            map.put(key, list);
        }
        Collections.addAll(list, values);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public List<TValue> get(Object key) {
        return map.get(key);
    }

    public List<TValue> put(TKey key, List<TValue> value) {
        return map.put(key, value);
    }

    public List<TValue> remove(Object key) {
        return map.remove(key);
    }

    public void putAll(Map<? extends TKey, ? extends List<TValue>> m) {
        map.putAll(m);
    }

    public void clear() {
        map.clear();
    }

    public Set<TKey> keySet() {
        return map.keySet();
    }

    public Collection<List<TValue>> values() {
        return map.values();
    }

    public Set<Entry<TKey, List<TValue>>> entrySet() {
        return map.entrySet();
    }

    public boolean equals(Object o) {
        return map.equals(o);
    }

    public int hashCode() {
        return map.hashCode();
    }
}
