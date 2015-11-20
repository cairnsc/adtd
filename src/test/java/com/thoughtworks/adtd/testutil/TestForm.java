package com.thoughtworks.adtd.testutil;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormFieldData;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URI;
import java.util.List;

import static org.assertj.core.util.Lists.newArrayList;

public class TestForm implements Form {
    private String method;
    private URI action;
    private List<FormFieldData> formInputs;

    public TestForm() {
        formInputs = newArrayList();
    }

    public TestForm(String method) {
        this();
        this.method = method;
    }

    public TestForm(String method, URI action) {
        this(method);
        this.action = action;
    }

    public TestForm(String method, URI action, List<FormFieldData> formInputs) {
        this.method = method;
        this.action = action;
        this.formInputs = formInputs;
    }

    public Elements select(String cssQuery) {
        throw new NotImplementedException();
    }

    public Elements selectExact(String cssQuery, int numElements) throws Exception {
        throw new NotImplementedException();
    }

    public Element selectOne(String cssQuery) throws Exception {
        throw new NotImplementedException();
    }

    public String getMethod() throws Exception {
        return method;
    }

    public String getAction() throws Exception {
        return action.toString();
    }

    public int countFormFields() {
        return formInputs.size();
    }

    public List<FormFieldData> getFormFields() {
        return formInputs;
    }

    public void addFormField(String name, String value) {
        formInputs.add(new FormFieldData(null, name, value));
    }

    public Request createRequest(RequestExecutor requestExecutor) throws Exception {
        return null;
    }
}
