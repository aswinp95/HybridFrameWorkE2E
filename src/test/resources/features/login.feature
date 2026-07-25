Feature: SauceDemo Login Functionality

Scenario Outline: Login with multiple user types

Given User navigate to the application url
When User confirm the login page
And User should enter username "<username>" password "<password>" and proceed to login
Then User should see result "<expectedResult>"

Examples:
      | username         | password     | expectedResult                                        |
      | standard_user    | secret_sauce | Products                                              |
      | locked_out_user  | secret_sauce | Epic sadface: Sorry, this user has been locked out.   |
      | problem_user     | secret_sauce | Products                                              |