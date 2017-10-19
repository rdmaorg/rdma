Feature: Home page checkout

  Scenario: Verifying existence of each element on the Home Page
    Given User is on the Home Page
    Then All elements are visible on the Home Page

  Scenario: Verifying text displayed of each element on the Home Page
    Given User is on the Home Page
    Then Correct text is displayed for each element on the Home Page
