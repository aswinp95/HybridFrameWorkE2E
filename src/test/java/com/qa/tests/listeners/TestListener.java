package com.qa.tests.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.WebDriver;
import com.qa.framework.driver.DriverFactory;
import com.qa.framework.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        log.warn("Test failed: {}. Capturing screenshot.", result.getName());

        WebDriver driver = DriverFactory.getDriver();
        ScreenshotUtil.attachScreenshot(driver, result.getName());
    }
}