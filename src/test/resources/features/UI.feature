@UI
Feature: UI tests

  Scenario: Create a new user
    Given User navigates to the login page
    Then User is on the 'Portal Login' page
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


  Scenario: Log in with the existing user
    Given User navigates to the login page
    And User completes the following fields on the page
      | Fields    | Values            |
      | Username  | api041224145058@clrmail.com |
      | Password  | Test1234        |
    When User clicks on 'submit Button'
    Then User is on the 'Contact List' page


  Scenario: Log in with the newly created user
    Given User navigates to the login page
    When User completes the following fields on the page
      | Fields    | Values            |
      | Username  | [GENERATED_EMAIL] |
      | Password  | [PASSWORD]        |
    And User clicks on 'submit Button'
    Then User is on the 'Contact List' page


  Scenario: Check that the contact table is empty for newly created user
    Given User navigates to the login page
    When User completes the following fields on the page
      | Fields    | Values            |
      | Username  | test200325155510@clrmail.com |
      | Password  | [PASSWORD]        |
    Then User clicks on 'submit Button' and is redirected to the 'Contact List' page
    Then The table 'contactTable ' not includes the following records
      | Birthdate  | Email                          | Phone       | Country      |
      | 2000-05-20 | portal101224190514@clrmail.com | 34809789312 | Bedfordshire |



  Scenario: Check possibility to add a new contact for newly created user
    Given User navigates to the login page
    When User completes the following fields on the page
      | Fields    | Values            |
      | Username  | [GENERATED_EMAIL] |
      | Password  | [PASSWORD]        |
    Then User clicks on 'submit Button' and is redirected to the 'Contact List' page
    Then User clicks on 'add New Contact Button' and is redirected to the 'Add Contact' page
    When User completes the following fields on the page
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
    And The table 'contactTable' includes the following records
      | Birthdate  | Email             | Phone          | Country             |
      | 2000-05-20 | [GENERATED_EMAIL] | [PHONE_NUMBER] | [GENERATED_COUNTRY] |


  Scenario: Log in with the newly created user and add check Contact Details page
    Given User navigates to the login page
    When User completes the following fields on the page
      | Fields    | Values            |
      | Username  | [GENERATED_EMAIL] |
      | Password  | [PASSWORD]        |
    Then User clicks on 'submit Button' and is redirected to the 'Contact List' page
    When User clicks on 'contact Details Button'
    Then User is on the 'Contact Details' page
    And The following fields should be displayed with values
      | Fields          | Values                     |
      | firstName       | [FIRST_NAME]               |
      | lastName        | [LAST_NAME]                |
      | birthdate       | 2000-05-20                 |
      | email           | [GENERATED_EMAIL]          |
      | phone           | [PHONE_NUMBER]             |
      | street1         | [GENERATED_STREET]         |
      | street2         | [GENERATED_BUILDINGNUMBER] |
      | city            | [GENERATED_CITY]           |
      | stateOrProvince | [GENERATED_STATE]          |
      | postalCode      | [GENERATED_POSTCODE]       |
      | country         | [GENERATED_COUNTRY]        |



  @e2e @ignore
  Scenario: Log in with the newly created user and add a new contact
    Given User navigates to the login page
#    Then User is on the 'Portal Login' page
    When User completes the following fields on the page
      | Fields    | Values            |
      | Username  | [GENERATED_EMAIL] |
      | Password  | [PASSWORD]        |
    And User clicks on 'submit Button'
#    Then User is on the 'Contact List' page
    And The table 'contactTable ' not includes the following records
      | Name | Birthdate | Email | Phone | Address | City, State/Province, Postal Code | Country |
      |      |           |       |       |         |                                   |         |
    When User clicks on 'add New Contact Button'
    Then User is on the 'Add Contact' page
    When User completes the following fields on the page
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
    And The table 'contactTable' includes the following records
      | Birthdate  | Email             | Phone          | Country             |
      | 2000-05-20 | [GENERATED_EMAIL] | [PHONE_NUMBER] | [GENERATED_COUNTRY] |
    When User clicks on 'contact Details Button'
    Then User is on the 'Contact Details' page
    And The following fields should be displayed with values
      | Fields          | Values                     |
      | firstName       | [FIRST_NAME]               |
      | lastName        | [LAST_NAME]                |
      | birthdate       | 2000-05-20                 |
      | email           | [GENERATED_EMAIL]          |
      | phone           | [PHONE_NUMBER]             |
      | street1         | [GENERATED_STREET]         |
      | street2         | [GENERATED_BUILDINGNUMBER] |
      | city            | [GENERATED_CITY]           |
      | stateOrProvince | [GENERATED_STATE]          |
      | postalCode      | [GENERATED_POSTCODE]       |
      | country         | [GENERATED_COUNTRY]        |



  Scenario: Check that the contact table is displayed with the following information for existing user
    Given User navigates to the login page
    And User completes the following fields on the page
      | Fields   | Values                      |
      | Username | api041224145058@clrmail.com |
      | Password | Test1234                    |
    When User clicks on 'submit Button' and is redirected to the 'Contact List' page
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


  Scenario Outline: Log in with invalid or empty user email '<Invalid Email>'
    Given User navigates to the login page
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


  Scenario Outline: Verify that adding a contact without mandatory fields <Id> results in an error message
    Given User navigates to the login page
    And User completes the following fields on the page
      | Fields   | Values                      |
      | Username | 20250213153133a@clrmail.com |
      | Password | Test1234                    |
    Then User clicks on 'submit Button' and is redirected to the 'Contact List' page
    Then User clicks on 'add New Contact Button' and is redirected to the 'Add Contact' page
    When User completes the following fields on the page
      | Fields          | Values                     |
      | firstName       | <Invalid first name>       |
      | lastName        | <Invalid last name>        |
      | birthdate       | 2000-05-20                 |
      | email           | [GENERATED_EMAIL]          |
      | phone           | [PHONE_NUMBER]             |
      | street1         | [GENERATED_STREET]         |
      | street2         | [GENERATED_BUILDINGNUMBER] |
      | city            | [GENERATED_CITY]           |
      | stateOrProvince | [GENERATED_STATE]          |
      | postalCode      | [GENERATED_POSTCODE]       |
      | country         | [GENERATED_COUNTRY]        |
    And User clicks on 'submit Button'
    Then Error Message is displayed with value: '<Message>'
    Examples:
      | Id                        | Invalid first name | Invalid last name | Message                                                                                                     |
      | both fields are blank     | -blank-            | -blank-           | Contact validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required. |
      | last name field is blank  | usersFirstName     | -blank-           | Contact validation failed: lastName: Path `lastName` is required.                                           |
      | first name field is blank | -blank-            | usersLastName     | Contact validation failed: firstName: Path `firstName` is required.                                         |

