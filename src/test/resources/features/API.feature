@API
Feature:  API Tests
#  Contact
  Scenario: Add a new contact successfully
    When User adds a contact with the following details:
      | Fields        | Values                     |
      | id            | [USER_ID]                  |
      | firstName     | [FIRST_NAME]               |
      | lastName      | [LAST_NAME]                |
      | birthdate     | 1970-01-01                 |
      | email         | [GENERATED_EMAIL]          |
      | phone         | [PHONE_NUMBER]             |
      | street1       | [GENERATED_STREET]         |
      | street2       | [GENERATED_BUILDINGNUMBER] |
      | city          | [GENERATED_CITY]           |
      | stateProvince | [GENERATED_STATE]          |
      | postalCode    | [GENERATED_POSTCODE]     |
      | country       | [GENERATED_COUNTRY]        |
    Then the response status code should be '201'
    And the contact should appear in the contact list
    Then the response status code should be '200'
    Then I should see my contact in the list


#User
  Scenario: Add new user successfully
    When Add a new user with the following details:
      | Fields        | Values                     |
      | firstName     | [FIRST_NAME]               |
      | lastName      | [LAST_NAME]                |
      | email         | [GENERATED_EMAIL]          |
      | Password      | [PASSWORD]        |
    Then the response status code should be '201'
#    And A new user can log in
#    Then the response status code should be '200'


  Scenario: Login and logout with a new user successfully
    When User is logged in as the new user
#    Then the response status code should be '200'
    And New user can log out
    Then the response status code should be '200'


  Scenario: Login and delete  new user successfully
    When User is logged in as the new user
#    Then the response status code should be '200'
    And Delete a new user
    Then the response status code should be '200'


