package com.qa.framework.pages;
import com.qa.framework.utils.WaitStrategy;
import com.qa.framework.utils.ExplicitWaitStrategy;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.framework.utils.ExplicitWaitStrategy;

public class BasePage {
//	protected access modifier — why not private or public? protected means
//	"visible to this class AND any subclass that extends it," but hidden
//	from unrelated classes. Since LoginPage will extend BasePage, it needs direct access to driver — but
//	random unrelated classes shouldn't be able to reach into it.

	protected WebDriver driver;
	protected WaitStrategy waitStrategy = new ExplicitWaitStrategy();

	public BasePage(WebDriver driver) {
		this.driver = driver;
	}
	
	protected WebElement waitFor(By locator) {
	    return waitStrategy.waitForElement(driver, locator);
	}

	protected void safeClick(WebElement element) {
		try {
			element.click();
		} catch (Exception e) {
			// Native click failed (likely intercepted by an overlay/ad).
	        // Fall back to a JS-driven click, which bypasses visual interception
	        // since it operates on the DOM directly rather than simulating a mouse event.
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);
		}
	}

}