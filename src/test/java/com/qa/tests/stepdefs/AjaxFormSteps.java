package com.qa.tests.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.driver.DriverFactory;
import com.qa.framework.pages.AjaxFormPage;
import com.qa.framework.pages.PlaygroundHomePage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AjaxFormSteps {
	
	private static final Logger log = LogManager.getLogger(AjaxFormSteps.class);
	
	private WebDriver driver;
	private PlaygroundHomePage playgroundHomePage;
	private AjaxFormPage ajaxFormPage;
	
	@Given("User should navigate to playground page")
	public void user_should_navigate_to_playground_page() {
	   driver = DriverFactory.getDriver();
	   driver.get(ConfigReader.getPlaygroundUrl());
	}
	@When("User click the Ajax form link and navigate to Ajax form page")
	public void user_click_the_ajax_form_link_and_navigate_to_ajax_form_page() {
	   playgroundHomePage = new PlaygroundHomePage(driver);
	   ajaxFormPage = playgroundHomePage.goToAjaxFormPage();
	}
	@And("User should enter {string} and {string} in the required field")
	public void user_should_enter_and_in_the_required_field(String textName, String textMessage ) {
	    ajaxFormPage.enterName(textName);
	    ajaxFormPage.enterMessage(textMessage);
	}
	@And("User should click submit button")
	public void user_should_click_submit_button() {
	    ajaxFormPage.clickSubmit();
	}
	@Then("User should verify once the form submit with {string} expected result")
	public void user_should_verify_once_the_form_submit_with_expected_result(String confirmMessage) {
	    Assert.assertEquals(ajaxFormPage.getConfirmationMessage(), confirmMessage);
	}

}
