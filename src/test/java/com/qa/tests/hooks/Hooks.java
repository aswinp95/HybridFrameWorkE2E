package com.qa.tests.hooks;
import io.cucumber.java.Scenario;
import com.qa.framework.utils.ScreenshotUtil;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.driver.DriverFactory;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
	private static final Logger log = LogManager.getLogger(Hooks.class);
	
	@Before
	public void setup() {
        // This must run BEFORE any @Given/@When/@Then step, since LoginSteps
        // calls DriverFactory.getDriver() expecting a driver to already exist.
		DriverFactory.createDriver(ConfigReader.getBrowser());
		WebDriver driver = DriverFactory.getDriver();
		log.info("Driver initialized for browser:{}", ConfigReader.getBrowser());
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
		
	}
	
	@After
	public void tearDown(Scenario scenario) {
	    if (scenario.isFailed()) {
	        WebDriver driver = DriverFactory.getDriver();
	        ScreenshotUtil.attachScreenshot(driver, scenario.getName());
	        log.warn("Scenario failed: {}. Screenshot captured.", scenario.getName());
	    }
	    DriverFactory.quitDriver();
	    log.info("Driver Quit for thread: {}", Thread.currentThread().threadId());
	}

}
