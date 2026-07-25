package com.qa.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PlaygroundHomePage extends BasePage {
	
	@FindBy (xpath = "//a[text()='Ajax Form Submit']")
	private WebElement ajaxFormLink;
	
	@FindBy (xpath = "//a[text()='Javascript Alerts']")
	private WebElement jsAlertLink;

	public PlaygroundHomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public AjaxFormPage goToAjaxFormPage() {
		safeClick(ajaxFormLink);
		return new AjaxFormPage(driver);
	}
	
	public JsAlertPage goToJsAlertPage() {
		safeClick(jsAlertLink);
		return new JsAlertPage(driver);
	}

}
