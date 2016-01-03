package com.thoughtworks.adtd.http;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.thoughtworks.adtd.html.FormField;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.thoughtworks.adtd.testutil.assertions.Assertions.assertThat;

public class RequestParametersTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private RequestParameters requestParameters;

    @Before
    public void setUp() {
        requestParameters = new RequestParameters();
    }

    @Test
    public void shouldCreateWithFormFields() {
        List<FormField> formFields = newArrayList();
        formFields.add(new FormField("a", "A"));
        formFields.add(new FormField("Z", "Z"));

        requestParameters = new RequestParameters(formFields);

        assertThat(requestParameters).isDerivedFrom(formFields);
    }

    @Test
    public void shouldMakeImmutable() {
        assertThat(requestParameters.isMutable()).isTrue();

        requestParameters.setImmutable();

        assertThat(requestParameters.isMutable()).isFalse();
    }

    @Test
    public void shouldAddParam() {
        String paramName = "A";
        String paramValue = "B";

        requestParameters.param(paramName, paramValue);

        Multimap<String, RequestParameter> params = requestParameters.getParams();
        assertThat(params.size()).isEqualTo(requestParameters.size()).isEqualTo(1);
        Collection<RequestParameter> param = params.get(paramName);
        assertThat(param).containsExactly(new RequestParameterImpl(paramName, paramValue));
    }

    @Test
    public void shouldAddMultipleParams() {
        String paramNames[] = { "A", "B" };
        String paramValues1[] = { "1", "2" };
        String paramValue2 = "3";

        requestParameters.param(paramNames[0], paramValues1);
        requestParameters.param(paramNames[1], paramValue2);

        assertThat(requestParameters.size()).isEqualTo(2);
        Multimap<String, RequestParameter> params = requestParameters.getParams();
        RequestParameter params1 = Iterables.getOnlyElement(params.get(paramNames[0]));
        assertThat(params1.getValues()).containsExactly(paramValues1);
        RequestParameter params2 = Iterables.getOnlyElement(params.get(paramNames[1]));
        assertThat(params2.getValues()).containsExactly(paramValue2);
    }

    @Test
    public void shouldGetParamAtIndex() {
        requestParameters.param("A", "b", "c", "d");
        requestParameters.param("e", "f");
        String paramName = "g";
        String paramValue = "h";
        requestParameters.param(paramName, paramValue);

        RequestParameter param = requestParameters.getParam(2);

        assertThat(param.getName()).isEqualTo(paramName);
        assertThat(param.getValues()).containsExactly(paramValue);
    }

    @Test
    public void shouldThrowExceptionInParamWhenRequestParametersIsImmutable() throws Exception {
        requestParameters.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("RequestParameters is immutable");

        requestParameters.param("A", "B");
    }

    @Test
    public void shouldAddParamInSetParamByName() {
        String paramName = "A";
        String paramValue = "B";
        requestParameters.setParam("x", "X");
        requestParameters.setParam("y", "Y");

        requestParameters.setParam(paramName, paramValue);

        List<RequestParameter> params = requestParameters.getParam(paramName);
        assertThat(params).containsExactly(new RequestParameterImpl(paramName, paramValue));
        List<Integer> paramIndexOf = requestParameters.paramIndexOf(paramName);
        assertThat(paramIndexOf).containsExactly(2);
    }

    @Test
    public void shouldReplaceParamInSetParamByName() {
        String paramName = "A";
        requestParameters.param(paramName, "B", "C");

        String replacementValue = "Z";
        requestParameters.setParam(paramName, replacementValue);

        List<RequestParameter> params = requestParameters.getParam(paramName);
        assertThat(params).containsExactly(new RequestParameterImpl(paramName, replacementValue));
        List<Integer> paramIndexOf = requestParameters.paramIndexOf(paramName);
        assertThat(paramIndexOf).containsExactly(0);
    }

    @Test
    public void shoudlReplaceParamAndRemoveRemainderWithSameNameInSetParamByName() {
        String paramName = "A";
        requestParameters.param(paramName, "B", "C");
        String paramNameExtra1 = "x";
        requestParameters.setParam(paramNameExtra1, "X", "Y");
        requestParameters.param(paramName, "D");
        String paramNameExtra2 = "y";
        requestParameters.setParam(paramNameExtra2, "Y");

        String replacementValue = "Z";
        requestParameters.setParam(paramName, replacementValue);

        List<RequestParameter> params = requestParameters.getParam(paramName);
        assertThat(params).containsExactly(new RequestParameterImpl(paramName, replacementValue));
        List<Integer> paramIndexOf = requestParameters.paramIndexOf(paramName);
        assertThat(paramIndexOf).containsExactly(0);
        assertThat(requestParameters.getParam(paramNameExtra1)).isNotEmpty();
        assertThat(requestParameters.getParam(paramNameExtra2)).isNotEmpty();
    }

    @Test
    public void shouldThrowExceptionInSetParamByNameWhenRequestParametersIsImmutable() throws Exception {
        requestParameters.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("RequestParameters is immutable");

        requestParameters.setParam("A", "B");
    }

    @Test
    public void shouldSetParamAtIndex() {
        String paramName = "A";
        String paramValue = "B";
        requestParameters.param(paramName, paramValue);

        String replacementValue = "C";
        requestParameters.setParam(0, replacementValue);

        List<RequestParameter> params = requestParameters.getParam(paramName);
        assertThat(params).hasSize(1);
        RequestParameter requestParameter = params.get(0);
        assertThat(requestParameter.getValues()).containsExactly(replacementValue);
    }

    @Test
    public void shouldThrowExceptionInSetParamByIndexWhenRequestParametersIsImmutable() throws Exception {
        requestParameters.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("RequestParameters is immutable");

        requestParameters.setParam(0, "B");
    }

    @Test
    public void shouldGetParamIndexOf() {
        requestParameters.param("A", "A", "B", "C");
        requestParameters.param("B", "D");
        requestParameters.param("C", "B");
        requestParameters.param("B", "D");
        requestParameters.param("D", "C");

        List<Integer> indexes = requestParameters.paramIndexOf("B");

        assertThat(indexes).hasSize(2);
        assertThat(indexes).containsExactly(1, 3);
    }
}