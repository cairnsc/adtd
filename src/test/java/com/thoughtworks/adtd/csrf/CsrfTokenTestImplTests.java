package com.thoughtworks.adtd.csrf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CsrfTokenTestImplTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CsrfTokenTestImpl test;

    @Test
    public void shouldThrowExceptionIfPrepareRetrieveInvokedMoreThanOnce() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A retrieve request has already been created for this test");
        test = new CsrfTokenTestImpl();
        test.prepareRetrieve("test", "/test");

        test.prepareRetrieve("test", "/test");
    }

    @Test
    public void shouldSetFormData() {
    }

    @Test
    public void shouldThrowExceptionIfFormDataSetAfterSubmitRequestCreated() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to set form data after the submit request is created");
        test = new CsrfTokenTestImpl();
        test.prepareRetrieve("test", "/test");
        test.notifyRequestComplete();
        test.prepareSubmit();

        test.setFormData("test", "test");
    }

    @Test
    public void shouldThrowExceptionIfPrepareSubmitInvokedBeforeRetrieveRequestCreated() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A retrieve request must first be executed for this test");
        test = new CsrfTokenTestImpl();

        test.prepareSubmit();
    }

    @Test
    public void shouldThrowExceptionIfPrepareSubmitInvokedBeforeRetrieveRequestExecuted() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A retrieve request must first be executed for this test");
        test = new CsrfTokenTestImpl();
        test.prepareRetrieve("test", "/test");

        test.prepareSubmit();
    }

    @Test
    public void shouldThrowExceptionIfPrepareSubmitInvokedMoreThanOnce() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A submit request has already been created for this test");
        test = new CsrfTokenTestImpl();
        test.prepareRetrieve("test", "/test");
        test.notifyRequestComplete();
        test.prepareSubmit();

        test.prepareSubmit();
    }

}
