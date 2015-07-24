package com.thoughtworks.adtd.html;

import com.thoughtworks.adtd.http.Request;

import java.util.List;

/**
 * Data to submit in a form. Permits fields with duplicate names. Attempts to retain order of fields.
 */
public interface FormData {

    List<FormFieldData> getFormFields();
    boolean isMutable();
    void setImmutable();
    FormFieldData getFormField(String name);
    void setFormField(String name, String value);
    void addFormField(String name, String value);
    void setRequestParams(Request request);

}
