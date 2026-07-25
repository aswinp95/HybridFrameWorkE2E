package com.qa.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage{
	
	private static final Logger log = LogManager.getLogger(LoginPage.class);
	
	@FindBy (id = "user-name")
	private WebElement usernameField;
	
	@FindBy (id = "password")
	private WebElement passwordField;
	
	@FindBy (id = "login-button")
	private WebElement loginButton;
	
	@FindBy (css = "[data-test='error']")
	private WebElement errorMessage;

	public LoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void enterUsername(String username) {
		usernameField.sendKeys(username);
	}
	
	public void enterPassword(String password) {
		passwordField.sendKeys(password);
	}
	
	public void clickLogin() {
		loginButton.click();
		log.info("Login Button Clicked");
	}
	
	public String getErrorMessage() {
		return errorMessage.getText();
	}
	
	public ProductsPage login(String username, String password) {
		
		enterUsername(username);
		enterPassword(password);
		clickLogin();
		return new ProductsPage(driver);
		
	}
	
	
	
}
