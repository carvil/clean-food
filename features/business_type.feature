Feature: API - Business Type
  This tests the business type's endpoints

  Scenario: business types
    Given I have 2 business types
    When I fetch business types via the API
    Then the response included 2 results
    And the response contains business types

  Scenario: getting a business type
    Given I have an business type with the following details
      | id | name           |
      |  1 | pub/restaurant |
    When I fetch the Business Type "1"
    Then the response includes a Business Type
    And the establishment's "name" is "pub/restaurant"
