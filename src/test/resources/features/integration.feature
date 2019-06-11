@CleanStubby
Feature: Integration
  This feature is to test all steps

  Scenario: Validate the steps for feature toggle
    Given the feature DELETE_INTEGRATION is ENABLE
    And A have a mock http_delete_successful for dependency integration
    And I have a request with body http_delete_body_request
    When I make a DELETE to /test
    Then I expect to receive a 200 status
    Given the feature DELETE_INTEGRATION is DISABLE
    And I have a request with body http_delete_body_request
    When I make a DELETE to /test
    Then I expect to receive a 400 status
    Given the features toggle with status
      | name               | status |
      | DELETE_INTEGRATION | ENABLE |
    And I have a request with body http_delete_body_request
    When I make a DELETE to /test
    Then I expect to receive a 200 status
    Given the features toggle with status
      | name               | status  |
      | DELETE_INTEGRATION | DISABLE |
    And I have a request with body http_delete_body_request
    When I make a DELETE to /test
    Then I expect to receive a 400 status

  Scenario: Validate a default steps without request
    Given A have a mock http_get_successful for dependency integration
    When I make a GET to /test
    Then I expect to receive a 200 status
    And I expect mock http_get_successful for dependency integration to have been called 1 times
    And I expect http_get_body_response as response

  Scenario: Validate a default steps with request body
    Given A have a mock http_post_successful for dependency integration
    And I have a request with body http_post_body_request
    When I make a POST to /test
    Then I expect to receive a 200 status with body http_post_body_response

  Scenario: Validate a default steps with request defined
    Given A have a mock http_post_successful for dependency integration
    When I make a request defined in http_post_defined_body_request
    Then I expect to receive a 200 status