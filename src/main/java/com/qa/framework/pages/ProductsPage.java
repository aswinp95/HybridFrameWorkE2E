package com.qa.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductsPage extends BasePage{
	private static final Logger log = LogManager.getLogger(ProductsPage.class);
	
	@FindBy (className = "title")
	private WebElement titleName;

	public ProductsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getTitle() {
		String title = titleName.getText();
		log.info("Products page title retrieved: {}", title);
		return title;
	}
	

}
