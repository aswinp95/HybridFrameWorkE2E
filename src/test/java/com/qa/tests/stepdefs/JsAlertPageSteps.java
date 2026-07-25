package com.qa.tests.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.driver.DriverFactory;
import com.qa.framework.pages.JsAlertPage;
import com.qa.framework.pages.PlaygroundHomePage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class JsAlertPageSteps {
	private static final Logger log = LogManager.getLogger(JsAlertPageSteps.class);
	private PlaygroundHomePage playgroundHomePage;
	private JsAlertPage jsAlertPage;
	private WebDriver driver;
	
    // holds the result of whichever trigger ran, so the next step can assert on it
    private String actualResult;
	
	@Given("User navigate to playground homepage")
	public void user_navigate_to_playground_homepage() {
	    driver = DriverFactory.getDriver();
	    driver.get(ConfigReader.getPlaygroundUrl());
	    
	}

	@When("User click JSAlerts link and user navigate to JSAlert Page")
	public void user_click_js_alerts_link_and_user_navigate_to_js_alert_page() {
		playgroundHomePage = new PlaygroundHomePage(driver);
		jsAlertPage = playgroundHomePage.goToJsAlertPage();
	   
	}

	@Then("User should verify the page headline {string}")
	public void user_should_verify_the_page_headline(String expectedHeading) {
	     Assert.assertEquals(jsAlertPage.toGetPageHeadLine(), expectedHeading);
	}

	@And("User click JavaScript Alerts button the alert should display")
	public void user_click_java_script_alerts_button_the_alert_should_display() {
		actualResult = jsAlertPage.triggerJsAlert();
	}

	@And("User verify the alert box text {string} and accept it")
	public void user_verify_the_alert_box_text_and_accept_it(String expectedText) {
		Assert.assertEquals(actualResult, expectedText);
	}

	@And("User click confirm box button the alert should display")
	public void user_click_confirm_box_button_the_alert_should_display() {
		actualResult = jsAlertPage.triggerConfirmBox();
	}

	@And("User accept the alert verify the alert box text {string}")
	public void user_accept_the_alert_verify_the_alert_box_text(String expectedText) {
		Assert.assertEquals(actualResult, expectedText);
	}

	@And("User click prompt box button the alert should display")
	public void user_click_prompt_box_button_the_alert_should_display() {
	    
	}

	@And("User enter the name {string} and accept it")
	public void user_enter_the_name_and_accept_it(String name) {
		actualResult = jsAlertPage.triggerPromptBox(name);
	}

	@And("User verify the prompt box text {string}")
	public void user_verify_the_prompt_box_text(String expectedText) {
		Assert.assertEquals(actualResult, expectedText);
	}


}
