Feature: Connection Types page checkout

  Scenario: Verifying existence of each element on the Connection Type Page
    Given User is on the Connection Type Page
    Then All elements are visible on the Connection Type Page

  Scenario: Verifying text displayed of each element on the Connection Type Page
    Given User is on the Connection Type Page
    Then Correct text is displayed for each element on the Connection Type Page

  Scenario: Insert new connection type - success
    Given User is on the Connection Type Page
    When User press Insert Connection button
    And Populate all fields on the form
    And Press Save button on the Insert New Connection form
    And Press Save Connection type button to confirm the save
    Then New connection type is created and it is displayed on the connection types list page
  
  Scenario: Insert new connection type - fail
    Given User is on the Connection Type Page
    When User press Insert Connection button
    And User doesnt populate any field on the Insert New Connection form
    And Press Save button on the Insert New Connection form
    Then Create new connection action faild and correct warning message is displayed

  Scenario: Cancel save action
    Given User is on the Connection Type Page
    When User press Insert Connection button
    And Populate all fields on the form with new test data
    And Press Save button on the Insert New Connection form
    And Press Cancel Connection type button to cancel the save action
    Then Save action is successfully canceled and connection type is not created

  ##Test Preparation: Set valid ID of the connection type which you want to delete
  Scenario: Delete connection type
    Given User is on the Connection Type Page
    When User choose one connection type from the list and press Delete button
    And Press Delete Connection type button to confirm the delete action
    Then Connection type is successfully deleted and it is not displayed on the list
##
##Missing edit functionality - cannot locate edit element
