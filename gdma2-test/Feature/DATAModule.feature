Feature: Data module page checkout

  Scenario: Verifying existence of each element on the Data module Page
    Given User is on the Data module Page
    Then All elements are visible on the Data module Page

  Scenario: Verifying text displayed of each element on the Data module Page
    Given User is on the Data module Page
    Then Correct text is displayed for each element on the Data module Page

  Scenario: Insert and Delete new record
    Given User set full access rights for Classic Models - Mobile_Phone table
    And User navigates to the Classic Models - Mobile_Phone table in DATA module
    And User press New button
    And User populate all fields on the create new entry form
    When Select Create button
    Then New entry is successfully saved
    And User successfully removed new record from database

  Scenario: Edit record details - single row
    Given User set full access rights for Classic Models - Mobile_Phone table
    And User navigates to the Classic Models - Mobile_Phone table in DATA module
    And User press New button
    And User populate all fields on the create new entry form
    And Select Create button
    And User press Edit button for this new record
    And User edit record details
    When Select Update button
    Then Details are successfully edit

  ##Test Data:NewEntry2_DataModule.properties file
  Scenario: Edit record details - multiple row
    Given User set full access rights for Classic Models - Mobile_Phone table
    And User navigates to the Classic Models - Mobile_Phone table in DATA module
    And User press New button
    And User populate all fields on the create new entry form
    And Select Create button
    And User create one more record
    And User press Edit button for these two new records
    And User edit record details for these two new items
    When Select Update button
    Then Details for both items are successfully edit

  Scenario: Select all functionality
    Given User is on the Data module Page
    When User select Select ALL button
    Then All items are selected

  Scenario: Deselect all functionality
    Given User is on the Data module Page
    When User select Deselect ALL button
    Then All items are now deselected

  ##Download file location:"C:\\GDMA Automation testing"
  Scenario: Download functionality
    When User navigates to Data Module Page and select Download button
    Then File is successfully downloaded

  ##Upload file location:C:\\GDMA\\Upload.csv
  Scenario: Upload functionality
    When User navigates to the ClassicModels - Books table in Data module Page
    And User select Upload button
    Then File is successfully uploaded
