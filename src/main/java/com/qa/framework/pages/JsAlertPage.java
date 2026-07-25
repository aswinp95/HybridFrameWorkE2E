package com.qa.framework.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JsAlertPage extends BasePage{
	
	private static final Logger log = LogManager.getLogger(JsAlertPage.class);
	
	@FindBy (xpath = "//h1[contains(text(),'Javascript Alert Box Demo')]")
	private WebElement pageHeadLine;

	@FindBy (xpath = "//p[contains(text(), 'JavaScript Alerts')]/button")
	private WebElement jsAlerts;
	
	@FindBy (xpath = "//p[contains(text(), 'Confirm box:')]/button")
	private WebElement confirmBox;
	
	@FindBy (id = "confirm-demo")
	private WebElement confirmBoxText;
	
	@FindBy (xpath = "//p[contains(text(), 'Prompt box:')]/button")
	private WebElement promptBox;
	
	@FindBy (id = "prompt-demo")
	private WebElement promptBoxText;
	
	public JsAlertPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String toGetPageHeadLine() {
		  
		  String pageHeading = pageHeadLine.getText();
		  log.info("On JSAlert Page to get the page heading : ", pageHeading);
	      return pageHeading;
	}
	
	public String triggerJsAlert() {
		safeClick(jsAlerts);
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		String alertMsg = alert.getText();
		alert.accept();
		log.info("On alert page clicked JS Alert: {} ", alertMsg);
		return alertMsg;
	}
	
	public String triggerConfirmBox() {
		safeClick(confirmBox);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().accept();
		String confirmResultText = confirmBoxText.getText();
		log.info("On alert page clicked ok for confirmBox alert: {} ", confirmResultText);
		return confirmResultText;
		
	}
	

	public String triggerPromptBox(String inputName) {
		safeClick(promptBox);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		alert.sendKeys(inputName);
		alert.accept();
		log.info("On alert page enter my name for promptBox alert: {} ", inputName);
		return promptBoxText.getText();
	}

	
}
