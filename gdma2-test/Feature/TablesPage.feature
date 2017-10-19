Feature: Tables page checkout

  Scenario: Verifying existence of each element on the Tables Page
    Given User is on the Tables Page
    Then All elements are visible on the Tables Page

  Scenario: Verifying text displayed of each element on the Tables Page
    Given User is on the Tables Page
    Then Correct text is displayed for each element on the Tables Page

  Scenario: Edit Alias
    Given User is on the Tables Page
    When User press Edit Alias button
    And User edit alias field
    And Press submit button on the edit Alias page
    And Confirm save alias action by clicking on the Yes button
    Then Table alias is successfully edit

  ##Step definition-Deactivate table: Before test execution set valid Table ID of the table which you want to delete
  Scenario: Deactivate table - need to be done from database
    Given User is on the Tables Page
    When Choose one active table from the list
    And Connect to DB and deactivate the table
    Then Verify that table is successfully deactivated
