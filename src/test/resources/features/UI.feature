@UI
Feature: UI tests

  @e2e
  Scenario: Create a new user
    Given User navigates to the login page
    When User clicks on 'Sign Up Button'
    Then User is on the 'Add User' page
    And User completes the following fields on the page
      | Fields        | Values            |
      | Email         | [GENERATED_EMAIL] |
      | Password      | [PASSWORD]        |
      | firstName     | [FIRST_NAME]      |
      | lastName      | [LAST_NAME]       |
      | submit Button |                   |
    Then User is on the 'Contact List' page

  @e2e
  Scenario: Log in with the newly created user
    Given User navigates to the login page
    And User completes the following fields on the page
      | Fields    | Values            |
      | Username  | [GENERATED_EMAIL] |
      | Password  | [PASSWORD]        |
    When User clicks on 'submit Button'
    Then User is on the 'Contact List' page
    Then The table 'contactTable ' not includes the following records
      | Name | Birthdate | Email | Phone | Address | City, State/Province, Postal Code | Country |
      |      |           |       |       |         |                                   |         |
    When User clicks on 'add New Contact Button'
    Then User is on the 'Add Contact' page
    And User completes the following fields on the page
      | Fields          | Values                     |
      | firstName       | [FIRST_NAME]               |
      | lastName        | [LAST_NAME]                |
      | birthdate       | 2000-05-20                 |
      | email           | [GENERATED_EMAIL]          |
      | phone           | [PHONE_NUMBER]             |
      | street1         | [GENERATED_STREET]        |
      | street2         | [GENERATED_BUILDINGNUMBER] |
      | city            | [GENERATED_CITY]           |
      | stateOrProvince | [GENERATED_STATE]           |
      | postalCode      | [GENERATED_POSTCODE]       |
      | country         | [GENERATED_COUNTRY]        |
      | submit Button   |                            |
    Then User is on the 'Contact List' page
    Then The table 'contactTable' includes the following records
      | Birthdate  | Email             | Phone          | Country             |
      | 2000-05-20 | [GENERATED_EMAIL] | [PHONE_NUMBER] | [GENERATED_COUNTRY] |
    When User clicks on 'contact Details Button'
    Then User is on the 'Contact Details' page
    Then The following fields should be displayed with values
      | Fields        | Values             |
      | firstName       | [FIRST_NAME]               |
      | lastName        | [LAST_NAME]                |
      | birthdate       | 2000-05-20                 |
      | email           | [GENERATED_EMAIL]          |
      | phone           | [PHONE_NUMBER]             |
      | street1         | [GENERATED_STREET]        |
      | street2         | [GENERATED_BUILDINGNUMBER] |
      | city            | [GENERATED_CITY]           |
      | stateOrProvince | [GENERATED_STATE]           |
      | postalCode      | [GENERATED_POSTCODE]       |
      | country         | [GENERATED_COUNTRY]        |


  Scenario: Log in with the existing user
    Given User navigates to the login page
    And User completes the following fields on the page
      | Fields    | Values            |
      | Username  | api041224145058@clrmail.com |
      | Password  | Test1234        |
    When User clicks on 'submit Button'
    Then User is on the 'Contact List' page
    Then The table 'contactTable' includes the following records
      | Birthdate  | Email                          | Phone       | Country      |
      | 2000-05-20 | portal101224190514@clrmail.com | 34809789312 | Bedfordshire |
      | 2000-05-20 | portal041224185302@clrmail.com | 90918606820 | Bedfordshire |
      | 2000-05-20 | portal051224093907@clrmail.com | 58846419214 | Cumberland   |
      | 2000-05-20 | portal051224094552@clrmail.com | 41704582739 | Cornwall     |
      | 2000-05-20 | portal101224180015@clrmail.com | 18781946489 | Cumberland   |
      | 2000-05-20 | portal101224182549@clrmail.com | 89339816665 | Bedfordshire |
      | 2000-05-20 | portal101224132554@clrmail.com | 51399627513 | Bedfordshire |
      | 2000-05-20 | portal101224132111@clrmail.com | 38540834517 | Cumbria      |
      | 2000-05-20 | portal051224104102@clrmail.com | 12885748946 | Bedfordshire |
      | 2000-05-20 | portal101224173710@clrmail.com | 19492569347 | Cumbria      |
      | 2000-05-20 | portal101224125557@clrmail.com | 75072012631 | Cornwall     |
      | 2000-05-20 | portal101224125245@clrmail.com | 33857499086 | Cumbria      |
      | 2000-05-20 | portal061224114559@clrmail.com | 51218554277 | Bedfordshire |
      | 2000-05-20 | portal051224182638@clrmail.com | 32418609317 | Bedfordshire |
      | 2000-05-20 | portal101224165708@clrmail.com | 32808566751 | Cornwall     |
      | 2000-05-20 | portal051224182514@clrmail.com | 71771247913 | Cumberland   |
      | 2000-05-20 | portal101224125809@clrmail.com | 44118214663 | Bedfordshire |
      | 2000-05-20 | portal041224184300@clrmail.com | 25548153833 | Cornwall     |
      | 2000-05-20 | portal051224093604@clrmail.com | 22270172054 | Bedfordshire |
      | 2000-05-20 | portal051224093737@clrmail.com | 90641243660 | Cleveland    |
      | 2000-05-20 | portal101224182137@clrmail.com | 49341264040 | Bedfordshire |
      | 2000-05-20 | portal041224183237@clrmail.com | 31178325224 | Bedfordshire |


  Scenario Outline: Log in with invalid or empty user email
    Given User navigates to the login page
    Then User is on the 'Portal Login' page
    And User completes the following fields on the page
      | Fields   | Values          |
      | Username | <Invalid Email> |
      | Password | Test1234        |
    When User clicks on 'submit Button'
    Then Error Message is displayed with value: 'Incorrect username or password'
    Examples:
      | Invalid Email             |
      | -blank-                   |
      | userstestownerclrmail#com |
      | userstestownerclrmail.com |







