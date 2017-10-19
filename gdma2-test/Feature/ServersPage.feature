Feature: Servers page checkout

  Scenario: Verifying existence of each element on the Servers Page
    Given User is on the Servers Page
    Then All elements are visible on the Servers Page

  Scenario: Verifying text displayed of each element on the Servers Page
    Given User is on the Servers Page
    Then Correct text is displayed for each element on the Servers Page

  ##Test Data: ActiveServer.properties
  Scenario: Insert new active server - success
    Given User is on the Servers Page
    When User press Insert Server button
    And Populate all fields on the insert new server form
    And Press Save button on the insert new server form
    And Confirm the save action by clicking on the save server button
    Then New server is successfully created and it is displayed on the server list page

  ##Test Data: InactiveServer.properties
  Scenario: Insert new inactive server - success
    Given User is on the Servers Page
    When User press Insert Server button
    And Populate again all fields on the insert new server form
    And User set Active field to be False
    And Press Save button on the insert new server form
    And Confirm the save action by clicking on the save server button
    Then New server is successfully created and it is displayed on the server list page

  Scenario: Insert new server - fail
    Given User is on the Servers Page
    When User press Insert Server button
    And User doesnt populate any field on the Insert New Server form
    And Press Save button on the insert new server form
    Then Create new server action faild and correct warning message is displayed

  Scenario: Cancel save action
    Given User is on the Servers Page
    When User press Insert Server button
    And Populate all fields on the insert new server form with new test data
    And Press Save button on the insert new server form
    And Press Cancel button on the Insert new server page to cancel the save action
    Then Save action is successfully canceled and server is not created

  ##Test Preparation: Set valid server id
  Scenario: Edit server details
    Given User is on the Servers Page
    When User choose one server from the list and select edit button
    And User edit server name
    And Press Save button on the insert new server form
    And Confirm the save action by clicking on the save server button
    Then Server details are successfully edit and new changes are visible on the list

  Scenario: Check the values for Connection type dropdown menu
    Given User is on the Servers Page
    When User press Insert Server button
    Then Verify that valid number of connection types are displayed within Connection types dropdown menu

  ##Test Preparation: Set valid server id (no need to change)
  Scenario: Deactivate server
    Given User is on the Servers Page
    When User choose one active server and grant full access on one table within this server
    And User select edit button
    And User select active button
    And Press Save button on the insert new server form
    And Confirm the save action by clicking on the save server button
    Then Server is successfully deactivate and users who has access on this server cannot see them in data module

  ##Step definition-Delete server: Before test execution set valid Server ID of the server which you want to delete
  Scenario: Delete server - need to be done from database
    And Connect to DB and deactivate the server
    Then Verify that server is successfully deleted
