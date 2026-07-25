package com.qa.framework.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AjaxFormPage extends BasePage {
	
	private static final Logger log = LogManager.getLogger(AjaxFormPage.class);
	
	@FindBy (xpath = "//h1[text()='Form Submit Demo']")
	private WebElement pageTitle;
	
	@FindBy (id = "title")
	private WebElement nameField;
	
	@FindBy (id="description")
	private WebElement messageField;
	
	@FindBy (id = "btn-submit")
	private WebElement submitButton;
	
	@FindBy (id = "submit-control")
	private WebElement confirmationMessage;

	public AjaxFormPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public boolean isPageLoaded() {
		return pageTitle.isDisplayed();
	}
	
	public  void enterName(String textName) {
		nameField.sendKeys(textName);
	}
	
	public void enterMessage(String textMessage) {
		messageField.sendKeys(textMessage);
	}
	
	public void clickSubmit() {
		safeClick(submitButton);
	}
	
//	public String getConfirmationMessage() {
//		String confirmMessage = confirmationMessage.getText();
//		log.info("On AjaxForm page the confirmation message is: {}",confirmMessage);
//		return confirmMessage;
//	}
	
	public String getConfirmationMessage() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.textToBePresentInElement(
	            confirmationMessage,
	            "Form submitted Successfully!"
	    ));

	    String confirmMessage = confirmationMessage.getText();
	    log.info("On AjaxForm page the confirmation message is: {}", confirmMessage);
	    return confirmMessage;
	}
	
	public String submitForm(String textName, String textMessage) {
		enterName(textName);
		enterMessage(textMessage);
		clickSubmit();
		return getConfirmationMessage();
	}

}
