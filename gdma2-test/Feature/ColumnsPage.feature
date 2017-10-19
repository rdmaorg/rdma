Feature: Columns page checkout

  Scenario: Verifying existence of each element on the Columns Page
    Given User is on the Columns Page
    Then All elements are visible on the Columns Page

  Scenario: Verifying text displayed of each element on the Columns Page
    Given User is on the Columns Page
    Then Correct text is displayed for each element on the Columns Page
##
##Missing test cases: Dropdown Display and Store; Special field; Display/Insert/Update/Null
