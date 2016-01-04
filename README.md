# adtd
adtd is a framework for Attack-Driven Test Development.

The framework strives to simplify the task of verifying threat mitigations in code during development. The tests it
includes today are intended for use in high-level tests such as integration tests.

## Goals
The goal of adtd is to provide a straightforward way of verifying security threat mitigations are operating correctly.
This includes mitigations against common problems such as those found in the OWASP Top 10 along with other important
security controls.

Creation of the framework was motivated by the lack of a simplified means of testing these concerns on projects that
make heavy use of automated testing.

## Getting started
In order to use adtd, you must create a class that implements the WebProxy interface to execute requests generated in
tests, and another class that implements the Response interface so the tests can process the result of the request.
Together the two interfaces provide an adapter for adtd to access your web application through your web framework and
test framework of choice.

Adapters to common frameworks will be created over time. See [Adapters](#adapters) for a list of adapters.

## Structure
Suites of tests are categorized by the type of threat mitigation (for example, Cross-Site Scripting) and bundled into
individual packages. Each test suite contains an test orchestrator, whose responsibility is to execute the tests in the
suite.

### Test Execution
Test execution always follows the same simple pattern:

- Get the next test from the test orchestrator
- Prepare a Request by invoking the prepare method in the test
- Modify the Request as needed
- Invoke the execute method in the test to execute the test
- Assert the test succeeded by invoking the assertResponse method in the test 

## Preparing tests
In order to execute tests, information about how to access the resource being tested is needed. The RequestInfo class
holds request parameters and data for that purpose. It is passed to tests, which use it to create requests that contain
attack payloads before executing the test.

RequestInfo can be created in two ways: through automated resource reconnaissance or manually. Since resources often
evolve over time, automated means are preferable since it reduces the risk of forgetting to test a request parameter. 

### Reconnaissance by HTML form inspection
The FormRetrieveRequest class requests a resource containing a form to produce a Form object that encapsulates
information about how to submit a form to a web application. The Form can in turn be used to create a RequestInfo
object that can be used to test the resource.

```java
private RequestInfo retrieveForm(WebProxy webProxy) throws Exception {
  // create a FormRetrieveRequest to retrieve a form with an action of "/form"
  FormRetrieveRequest formRetrieveRequest = new FormRetrieveRequest("/form");

  // prepare a Request to retrieve "/" using the GET method
  Request request = formRetrieveRequest.prepare()
          .method("GET")
          .uri("/");

  // execute the request
  request.execute(webProxy);

  // return RequestInfo created from the retrieved Form
  return formRetrieveRequest.getForm().getRequestInfo();
}
```

## Cross-Site Scripting (XSS) Tests

### Testing for Reflected XSS
When data received in a request will be reflected back to the requester in the response, you should test for reflected
XSS.

To test a resource for reflected XSS, provide an instance of TestStrategyIteratorRequestInfo to XssTestOrchestrator to
orchestrate tests. This will result in the orchestrator iterating through all parameters of a RequestInfo, testing XSS
payloads against each.

### Testing for Persistent XSS
If data received in a request is persisted to a data store and later sent in a separate response, you should test for
persistent XSS.

To test a resource for persistent XSS, provide an instance of TestStrategyIteratorInjected to XssTestOrchestrator to
orchestrate tests. This requires the use of a stub of the data store that request data is persisted to, to inject XSS
payloads when a response is being generated. The orchestrator iterates through each XSS payload when using this
iterator. 

## Cross-Site Request Forgery (CSRF) Tests

### Testing the Synchronizer Token Pattern
If CSRF mitigation using the Synchronizer Token Pattern is used, you should verify it is functioning correctly.

To verify CSRF mitigation, use CsrfTokenTestOrchestrator to orchestrate tests. The orchestrator iterates through several
tests to transmit invalid CSRF tokens, and one test to verify the positive (valid CSRF token) case. 

## HTTP Response Splitting Tests
When data received in a request will be reflected back to the requester in the response, you should test for HTTP
Response Splitting.

To test for response splitting, use HttpResponseSplittingTestOrchestrator to orchestrate tests. The orchestrator
iterates through all headers of a RequestInfo, sending a dangerous payload for each.

Support for the persistent case will be added in a coming release.

## Adapters
Adapters for common web and test frameworks will be listed here as they are created. Interested in creating an adapter?
See the [Contributing](#contributing) section below.

* [Spring Framework](https://github.com/cairnsc/adtd-springframework)

## Contributing
Interested in contributing a test or an adapter? Have an idea for how to improve test orchestration or to make the 
framework better in general? Send a message!

We'll accept pull requests soon and put a project backlog online to help avoid duplication of efforts.
