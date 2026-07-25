Feature: Ajax form Submission

Scenario: To verify the Ajax form Submission

Given User should navigate to playground page
When User click the Ajax form link and navigate to Ajax form page
And User should enter "First Form" and "Hi! Welcome to Aswins's framework" in the required field
And User should click submit button
Then User should verify once the form submit with "Form submitted Successfully!" expected result
