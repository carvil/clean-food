Feature: API - Establishments
  This tests the establishment's endpoints

  Scenario: establishments
    Given I have 2 establishments
    When I fetch all establishments via the API
    Then the response included 2 results
    And the response contains establishments

  Scenario: getting an establishment
    Given I have an establishment with the following details
      | fhrs_id | name         | postcode |
      | ABC1234 | Restaurant X | E2 8RS   |
    When I fetch the estalishment "ABC1234"
    Then the response includes an establishment
    And the establishment's "postcode" is "E2 8RS"
    And the establishment's "name" is "Restaurant X"
