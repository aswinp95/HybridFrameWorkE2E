package com.qa.tests.stepdefs;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.driver.DriverFactory;
import com.qa.framework.pages.LoginPage;
import com.qa.framework.pages.ProductsPage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {
	
	private static final Logger log = LogManager.getLogger(LoginSteps.class);
	private WebDriver driver;
	private LoginPage loginPage;
	private ProductsPage productsPage;
	
	@Given("User navigate to the application url")
	public void user_navigate_to_the_application_url() {
		driver = DriverFactory.getDriver();
		driver.get(ConfigReader.getBaseUrl());
		log.info("Navigated to: {}", ConfigReader.getBaseUrl());
	}
	
	@When("User confirm the login page")
	public void user_confirm_the_login_page() {
		log.info("On login page: {}", driver.getCurrentUrl());
		loginPage = new LoginPage(driver);
	}
	
	@And("User should enter username {string} password {string} and proceed to login")
	public void user_should_enter_username_password_and_proceed_to_login(String username, String password) {
		productsPage =loginPage.login(username, password);
	    log.info("Login attempted with username: {}", username);
	}
	
	@Then("User should see result {string}")
	public void user_should_see_result(String expectedResult) {
	    if (expectedResult.equals("Products")) {
	    	Assert.assertEquals(productsPage.getTitle(), expectedResult);
	    } else {
	        Assert.assertEquals(loginPage.getErrorMessage(), expectedResult);
	    }
	}

}
