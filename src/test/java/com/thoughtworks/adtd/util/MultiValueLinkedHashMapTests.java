package com.thoughtworks.adtd.util;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiValueLinkedHashMapTests {

    private MultiValueLinkedHashMap<String, String> map;

    @Before
    public void setUp() {
        map = new MultiValueLinkedHashMap<String, String>();
    }

    @Test
    public void shouldBeEmptyAfterInitialization() {
        assertThat(map.size()).isZero();
    }

    @Test
    public void shouldAddElementForKey() {
        String key = "A";
        String value = "B";

        map.add(key, value);

        assertThat(map.size()).isEqualTo(1);
        List<String> list = map.get(key);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list).contains(value);
    }

    @Test
    public void shouldAddVariableArgumentsForKey() {
        String key = "A";
        String values[] = { "B", "C" };

        map.add(key, values[0], values[1]);

        assertThat(map.size()).isEqualTo(1);
        List<String> list = map.get(key);
        assertThat(list.size()).isEqualTo(2);
        assertThat(list).contains(values);
    }

    @Test
    public void shouldAddMultipleElementsForKeyAndRetainOrder() {
        String key = "A";
        String values[] = { "B", "C", "D" };

        map.add(key, values[0]);
        map.add(key, values[2]);
        map.add(key, values[1]);

        assertThat(map.size()).isEqualTo(1);
        List<String> list = map.get(key);
        assertThat(list.size()).isEqualTo(values.length);
        assertThat(list.get(0)).isEqualTo(values[0]);
        assertThat(list.get(1)).isEqualTo(values[2]);
        assertThat(list.get(2)).isEqualTo(values[1]);
    }

    @Test
    public void shouldContainKey() {
        String key = "A";
        String value = "B";

        map.add(key, value);

        assertThat(map.containsKey(key)).isTrue();
    }

    @Test
    public void shouldNotContainKey() {
        String key = "B";
        String value = "C";

        map.add(key, value);

        assertThat(map.containsKey("D")).isFalse();
    }

    @Test
    public void shouldContainValue() {
        String key = "A";
        String value = "B";

        map.add(key, value);

        List<String> list = new LinkedList<String>();
        list.add(value);
        assertThat(map.containsValue(list)).isTrue();
    }

    @Test
    public void shouldNotContainValue() {
        String key = "A";
        String value = "B";

        map.add(key, value);

        List<String> list = new LinkedList<String>();
        list.add("C");
        assertThat(map.containsValue(list)).isFalse();
    }

    @Test
    public void shouldPutValuesForKey() {
        String key = "A";
        String value = "B";
        map.add(key, value);
        String values[] = { "C" };

        map.put(key, Arrays.asList(values));

        assertThat(map.size()).isEqualTo(1);
        List<String> list = map.get(key);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list).contains(values);
    }

    @Test
    public void shouldRemoveKey() {
        String key = "A";
        String value = "B";
        map.add(key, value);

        List<String> list = map.remove(key);

        assertThat(map.size()).isZero();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list).contains(value);
    }

    @Test
    public void shouldCopyAllMappingsFromSourceMap() {
        String key = "A";
        String value = "B";
        map.add(key, value);
        MultiValueLinkedHashMap<String, String> src = new MultiValueLinkedHashMap<String, String>();
        String srcKeys[] = { key, "C" };
        String srcValues[] = { "D", "E" };
        src.add(srcKeys[0], srcValues[0]);
        src.add(srcKeys[1], srcValues[1]);

        map.putAll(src);

        assertThat(map.size()).isEqualTo(2);
        List<String> list1 = map.get(srcKeys[0]);
        assertThat(list1.size()).isEqualTo(1);
        assertThat(list1).contains(srcValues[0]);
        List<String> list2 = map.get(srcKeys[1]);
        assertThat(list2.size()).isEqualTo(1);
        assertThat(list2).contains(srcValues[1]);
    }

    @Test
    public void shouldClear() {
        String keys[] = { "A", "B" };
        String values[] = { "B", "C", "D" };
        map.add(keys[0], values[0]);
        map.add(keys[1], values[2]);
        map.add(keys[1], values[1]);

        map.clear();

        assertThat(map.isEmpty()).isTrue();
    }

    @Test
    public void shouldGetKeySet() {
        String keys[] = { "A", "B" };
        String values[] = { "B", "C", "D" };
        map.add(keys[0], values[0]);
        map.add(keys[1], values[2]);
        map.add(keys[1], values[1]);

        Set<String> keySet = map.keySet();

        assertThat(keySet.size()).isEqualTo(keys.length);
        assertThat(keySet).contains(keys);
    }

    @Test
    public void shouldGetValues() {
        String keys[] = { "A", "B" };
        String values[] = { "B", "C", "D" };
        map.add(keys[0], values[0]);
        map.add(keys[1], values[2]);
        map.add(keys[1], values[1]);

        LinkedList<List<String>> mapValues = new LinkedList<List<String>>(map.values());

        assertThat(mapValues.size()).isEqualTo(keys.length);
        assertThat(mapValues.get(0).size()).isEqualTo(1);
        assertThat(mapValues.get(0)).contains(values[0]);
        assertThat(mapValues.get(1).size()).isEqualTo(2);
        assertThat(mapValues.get(1)).contains(values[2]);
        assertThat(mapValues.get(1)).contains(values[1]);
    }

    @Test
    public void shouldGetEntrySet() {
        String key = "A";
        String values[] = { "B", "C" };
        map.add(key, values[0]);

        Set<Map.Entry<String, List<String>>> entrySet = map.entrySet();

        assertThat(entrySet.size()).isEqualTo(1);
        Map.Entry<String, List<String>> element = entrySet.iterator().next();
        assertThat(element.getKey()).isEqualTo(key);
        assertThat(element.getValue().size()).isEqualTo(1);
        map.add(key, values[1]);
        assertThat(element.getValue().size()).isEqualTo(2);
    }

    @Test
    public void shouldBeEqualWithIdenticalMaps() {
        String keys[] = { "A", "B" };
        String values[] = { "C", "D", "E" };
        map.add(keys[0], values[0]);
        map.add(keys[1], values[2]);
        map.add(keys[1], values[1]);
        MultiValueLinkedHashMap<String, String> cmpMap = new MultiValueLinkedHashMap<String, String>();
        cmpMap.add(keys[0], values[0]);
        cmpMap.add(keys[1], values[2]);
        cmpMap.add(keys[1], values[1]);

        assertThat(map.equals(cmpMap)).isTrue();
    }

    @Test
    public void shouldBeEqualWithDifferentMaps() {
        String keys[] = { "A", "B" };
        String values[] = { "C", "D", "E" };
        map.add(keys[0], values[0]);
        map.add(keys[1], values[2]);
        map.add(keys[1], values[1]);
        MultiValueLinkedHashMap<String, String> cmpMap = new MultiValueLinkedHashMap<String, String>();
        cmpMap.add(keys[0], values[0]);
        cmpMap.add(keys[1], values[1]);
        cmpMap.add(keys[1], values[2]);

        assertThat(map.equals(cmpMap)).isFalse();
    }

    @Test
    public void shouldHaveConsistentHash() {
        String keys[] = { "A", "B" };
        String values[] = { "C", "D", "E" };
        map.add(keys[0], values[0]);
        map.add(keys[1], values[2]);
        map.add(keys[1], values[1]);
        MultiValueLinkedHashMap<String, String> cmpMap = new MultiValueLinkedHashMap<String, String>();
        cmpMap.add(keys[0], values[0]);
        cmpMap.add(keys[1], values[2]);
        cmpMap.add(keys[1], values[1]);

        assertThat(map.hashCode()).isEqualTo(cmpMap.hashCode());
    }

}
