Feature: API - Local Authority
  This tests the local authority's endpoints

  Scenario: local authorities
    Given I have 2 local authorities
    When I fetch local authorities via the API
    Then the response included 2 results
    And the response contains local authorities

  Scenario: getting a local authority
    Given I have an local authority with the following details
      | id | code   | name    |
      |  1 | code_1 | Hackney |
    When I fetch the local authority "1"
    Then the response includes a local authority
    And the local authority's "code" is "code_1"
    And the establishment's "name" is "Hackney"

