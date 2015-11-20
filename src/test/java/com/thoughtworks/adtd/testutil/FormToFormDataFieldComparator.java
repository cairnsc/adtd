package com.thoughtworks.adtd.testutil;

import com.thoughtworks.adtd.html.FormFieldData;

import java.util.Comparator;

public class FormToFormDataFieldComparator implements Comparator<FormFieldData> {
    public int compare(FormFieldData c1, FormFieldData c2) {
        if (c1 == null || c2 == null) {
            if (c1 == null) {
                return 1;
            }
            return -1;
        }

        int rv1 = c1.getName().compareTo(c2.getName());
        if (rv1 != 0) {
            return rv1;
        }
        return c2.getValue().compareTo(c2.getValue());
    }
}
