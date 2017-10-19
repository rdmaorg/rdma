Feature: User access page checkout

  Scenario: Verifying existence of each element on the User access Page
    Given User is on the User Access Page
    Then All elements are visible on the User Access Page

  Scenario: Verifying text displayed of each element on the User Access Page
    Given User is on the User Access Page
    Then Correct text is displayed for each element on the User Access Page

  Scenario: Edit User access - set full access right
    Given User is on the User Access Page
    When User choose one user from the list and grant him full access rights
    Then Selected user has full access rights

  Scenario: Edit User access - set display access right
    Given User is on the User Access Page
    When User choose one user from the list and grant him only display  rights
    Then Selected user has display access rights

  Scenario: Edit User access - set update right
    Given User is on the User Access Page
    When User choose one user from the list and grant him only update rights
    Then Selected user has update access rights

  Scenario: Edit User access - set insert right
    Given User is on the User Access Page
    When User choose one user from the list and grant him only insert rights
    Then Selected user has insert access rights

  Scenario: Edit User access - set delete right
    Given User is on the User Access Page
    When User choose one user from the list and grant him only delete rights
    Then Selected user has delete access rights
