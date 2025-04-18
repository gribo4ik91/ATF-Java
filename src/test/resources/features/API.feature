@API
Feature:  API Tests
#  Contact
  Scenario: Add a new contact successfully
    When User adds a contact with the following details:
      | Fields        | Values                     |
      | id            | [USER_ID]                  |
      | firstName     | [FIRST_NAME]               |
      | lastName      | [LAST_NAME]                |
      | birthdate     | 2000-05-20                 |
      | email         | [GENERATED_EMAIL_API]          |
      | phone         | [PHONE_NUMBER]             |
      | street1       | [GENERATED_STREET]         |
      | street2       | [GENERATED_BUILDINGNUMBER] |
      | city          | [GENERATED_CITY]           |
      | stateProvince | [GENERATED_STATE]          |
      | postalCode    | [GENERATED_POSTCODE]       |
      | country       | [GENERATED_COUNTRY]        |
    Then the response status code should be '201'



  Scenario: A new contact present in the list of contacts
  When New contact is created with the following details:
    | Fields        | Values                     |
    | id            | [USER_ID]                  |
    | firstName     | [FIRST_NAME]               |
    | lastName      | [LAST_NAME]                |
    | birthdate     | 2000-05-20                 |
    | email         | [GENERATED_EMAIL_API]          |
    | phone         | [PHONE_NUMBER]             |
    | street1       | [GENERATED_STREET]         |
    | street2       | [GENERATED_BUILDINGNUMBER] |
    | city          | [GENERATED_CITY]           |
    | stateProvince | [GENERATED_STATE]          |
    | postalCode    | [GENERATED_POSTCODE]       |
    | country       | [GENERATED_COUNTRY]        |
    And the contact should appear in the contact list
    Then the response status code should be '200'
    Then I should see my contact in the list








#User
  Scenario: Add new user successfully
    When Add a new user with the following details:
      | Fields        | Values                     |
      | firstName     | [FIRST_NAME]               |
      | lastName      | [LAST_NAME]                |
      | email         | [GENERATED_EMAIL_API]          |
      | Password      | [PASSWORD]        |
    Then the response status code should be '201'



  Scenario: Login and logout with a new user successfully
    When User is logged in as the new user
    And New user can log out
    Then the response status code should be '200'

  @UI6
  Scenario: Login and delete  new user successfully
    When New user is created with the following details:
      | Fields    | Values               |
      | firstName | [FIRST_NAME]         |
      | lastName  | [LAST_NAME]          |
      | email     | [GENERATED_EMAIL_UI] |
      | Password  | [PASSWORD]           |
    And The user can be deleted
    Then the response status code should be '200'








