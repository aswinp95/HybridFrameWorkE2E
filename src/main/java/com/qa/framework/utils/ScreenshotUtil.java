package com.qa.framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;

public class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    public static void attachScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
        byte[] screenshotBytes = screenshotDriver.getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(screenshotName, new ByteArrayInputStream(screenshotBytes));
    }
}