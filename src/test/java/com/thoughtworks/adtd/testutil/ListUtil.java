package com.thoughtworks.adtd.testutil;

import java.util.List;

public class ListUtil {
    public static boolean containsType(List<?> list, Class type) {
        return (indexOfType(list, type) != -1);
    }

    public static int indexOfType(List<?> list, Class type) {
        for (int idx = 0; idx < list.size(); idx++) {
            if (type.isInstance(list.get(idx))) {
                return idx;
            }
        }
        return -1;
    }

    public static <T> T getByType(List<T> list, Class type) {
        return list.get(indexOfType(list, type));
    }
}
