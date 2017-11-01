Feature: Login Action

  Scenario: Verifying existence of each element on the LogIn Page
    Given User is on the LogIn Page
    Then All elements are visible on the LogIn Page

  Scenario: Verifying text displayed of each element on the LogIn Page
    Given User is on the LogIn Page
    Then Correct text is displayed for each element on the LogIn Page

  Scenario: Successful Login
    Given User is on the LogIn Page
    When User enters valid username and password
    And User press Login button
    Then User is successfully logged in

  Scenario: Login Fail - invalid credenitails
    Given User is on the LogIn Page
    When User enters invalid username and password
    And User press Login button
    Then Login failed and correct warning message is displayed

  Scenario: Login Fail - without populating any field on the form
    Given User is on the LogIn Page
    When User doesnt populate any field on the form
    And User press Login button
    Then Login failed and correct warning message 2 is displayed

  Scenario: Clear function
    Given User is on the LogIn Page
    When User enters valid username and password
    And User press Clear button
    Then User credentials are successfully deleted
