package com.qa.tests.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{
	private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);
	
	private int reCount = 0;
	private static final int MAX_RETRY_COUNT =2;
	
	@Override
	public boolean retry(ITestResult result) {
	    if (reCount < MAX_RETRY_COUNT) {
	        reCount++;
	        log.warn("Retrying test '{}' — attempt {} of {}", result.getName(), reCount, MAX_RETRY_COUNT);
	        return true;
	    }
	    return false;
	}

}
