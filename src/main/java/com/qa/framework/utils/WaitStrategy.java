package com.qa.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface WaitStrategy {
    WebElement waitForElement(WebDriver driver, By locator);
}