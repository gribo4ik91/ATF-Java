@SQL
Feature:  SQl Tests

#Contact
  @UI5
  Scenario: The newly created user present in DB
    Given Details from "Contact" table
      | Fields        | Values                     |
      | id            | [USER_ID]                  |
      | firstName     | [FIRST_NAME]               |
      | lastName      | [LAST_NAME]                |
      | birthdate     | 2000-05-20                 |
      | email         | [GENERATED_EMAIL_API]      |
      | phone         | [PHONE_NUMBER]             |
      | street1       | [GENERATED_STREET]         |
      | street2       | [GENERATED_BUILDINGNUMBER] |
      | city          | [GENERATED_CITY]           |
      | stateProvince | [GENERATED_STATE]          |
      | postalCode    | [GENERATED_POSTCODE]       |
      | country       | [GENERATED_COUNTRY]        |
    Then User should get 1 users
    Then All the pieces of information from "Contact" are the same


#User
  @UI2
  Scenario: Select all users
    Given User query for all users in "Accounts" table
    Then User should get 2 users

  @UI5
  Scenario: The newly created user present in DB
    Given Details from "Accounts" table
      | Fields    | Values                |
      | firstName | [FIRST_NAME]          |
      | lastName  | [LAST_NAME]           |
      | email     | [GENERATED_EMAIL_API] |
      | Password  | [PASSWORD]            |
    Then User should get 1 users
    Then All the pieces of information from "Accounts" are the same