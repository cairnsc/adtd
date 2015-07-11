package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.Request;

/**
 * Test to validate CSRF protection using the synchronizer token pattern.
 *
 * Tests:
 *  1. Positive case: valid token submitted
 *  2. Negative case: missing token
 *  3. Negative case: invalid token
 *
 * TODO: iterator with strategies (single request or request per test)
 *
 * Read about the synchronizer token pattern at https://www.owasp.org/index.php/Cross-Site_Request_Forgery_%28CSRF%29_Prevention_Cheat_Sheet#General_Recommendation:_Synchronizer_Token_Pattern
 *
 * To execute CSRF tests:
 *  1. Prepare form retrieval query and execute.
 *  2. Set form inputs.
 *  3. Prepare submit query and execute.
 *  4. Assert response.
 */
public interface CsrfTokenTest {

    /**
     * Prepare a request to retrieve the page containing the form to be tested. By default the request method is GET.
     * @param formAction Form action attribute.
     * @param tokenInputName Name of the input tag containing the CSRF token.
     * @return Request.
     */
    Request prepareRetrieve(String formAction, String tokenInputName);

    /**
     * Get the form data that will be used in the submit request. Can be modified until the request is executed.
     * @return Form data.
     * @throws Exception The form data is not available.
     */
    FormData getFormData() throws Exception;

    /**
     * Prepare a request to submit
     * @return
     * @throws Exception
     */
    Request prepareSubmit() throws Exception;

    void assertResponse() throws Exception;

}
