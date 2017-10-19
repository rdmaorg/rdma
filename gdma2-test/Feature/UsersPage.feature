Feature: Users page checkout

  Scenario: Verifying existence of each element on the Users Page
    Given User is on the Users Page
    Then All elements are visible on the Users Page

  Scenario: Verifying text displayed of each element on the Users Page
    Given User is on the Users Page
    Then Correct text is displayed for each element on the Users Page

  ##Test Data: User1.properties
  Scenario: Insert new user - Admin user
    Given User is on the Users Page
    When User press Insert User button
    And Populate all fields on the Insert new User form
    And User set admin button to Yes
    And Press Save button on the Insert New User form
    And Press Save User button to confirm the save
    Then New user is created and it is displayed on the users list page

  Scenario: Admin user rights
    Given Log in as admin user
    Then Admin user is able to see configuration button

  ##Test Data: User2.properties
  Scenario: Insert new user - No Admin user
    Given User is on the Users Page
    When User press Insert User button
    And Populate all fields on the Insert new form for No Admin User
    And Press Save button on the Insert New User form
    And Press Save User button to confirm the save
    Then New user is created and it is displayed on the users list page

  Scenario: NoAdmin user rights
    Given Log in as NoAdmin user
    Then No Admin user doesnt see configuration button

  Scenario: Insert new user Fail - without populating any field on the form
    Given User is on the Users Page
    When User press Insert User button
    And User doesnt populate any field on the Insert new user form
    And Press Save button on the Insert New User form
    Then Create new user action faild and correct warning message is displayed

  ##Test Preparation: Set valid ID of the user which you want to delete
  Scenario: Delete existing users
    Given User is on the Users Page
    When User choose a user from the list and press Delete button
    And User confirm delete button
    Then User is successfully removed from the page

  ##Test Preparation: Set valid ID of the user which you want to edit
  Scenario: Edit user details
    Given User is on the Users Page
    When User choose a user from the list and press Edit button
    And Change the user details
    And Press Submit button
    And Press Save User details button
    Then User details are successfully edit
