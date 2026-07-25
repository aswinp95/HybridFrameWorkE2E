Feature: To Verify the JSAlert Page

  Background:
    Given User navigate to playground homepage
    When User click JSAlerts link and user navigate to JSAlert Page
    Then User should verify the page headline "Javascript Alert Box Demo"

  Scenario: To verify the JavaScript Alerts
    And User click JavaScript Alerts button the alert should display
    And User verify the alert box text "I am an alert box!" and accept it

  Scenario: To verify the ConfirmBox Alert
    And User click confirm box button the alert should display
    And User accept the alert verify the alert box text "You pressed OK!"

  Scenario: To verify the PromptBox Alert
    And User click prompt box button the alert should display
    And User enter the name "Aswin" and accept it
    And User verify the prompt box text "You have entered 'Aswin' !"