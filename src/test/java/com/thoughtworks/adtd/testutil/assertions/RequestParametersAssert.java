package com.thoughtworks.adtd.testutil.assertions;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormField;
import com.thoughtworks.adtd.http.RequestParameter;
import com.thoughtworks.adtd.http.RequestParameters;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;

public class RequestParametersAssert extends AbstractAssert<RequestParametersAssert, RequestParameters> {
    Failures failures = Failures.instance();

    protected RequestParametersAssert(RequestParameters actual) {
        super(actual, RequestParametersAssert.class);
    }

    public RequestParametersAssert isDerivedFrom(Form form) {
        return isDerivedFrom(form.getFormFields());
    }

    public RequestParametersAssert isDerivedFrom(List<FormField> formFields) {
        isNotNull();
        ArrayList<RequestParameter> params = newArrayList(actual.getParams().values());
        compareSizes(params, formFields);

        for (int idx = 0; idx < params.size(); idx++) {
            RequestParameter param = params.get(idx);
            FormField formField = formFields.get(idx);
            compareParams(param, formField, idx);
        }
        return this;

    }

    private void compareSizes(List<RequestParameter> params, List<FormField> formFields) {
        if (params.size() != formFields.size()) {
            throw failures.failure(String.format(
                    "RequestParameters and Form should have same number of elements but RequestParameters size was:%n" +
                            "  <%s>%n" +
                            "while FormData size was:%n" +
                            "  <%s>%n",
                    params.size(), formFields.size()
            ));
        }
    }

    private void compareParams(RequestParameter param, FormField formField, int idx) {
        if (!StringUtils.equals(param.getName(), formField.getName()) ||
                param.getValues().size() != 1 ||
                !StringUtils.equals(param.getValues().get(0), formField.getValue()) ||
                !param.getProperties().equals(formField.getProperties())) {
            throw failures.failure(info, elementsDifferAtIndex(param, formField, idx));
        }
    }
}
