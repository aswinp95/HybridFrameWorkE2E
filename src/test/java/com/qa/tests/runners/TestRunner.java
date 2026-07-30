package com.qa.tests.runners;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

	@CucumberOptions(
			features = "src/test/resources/features",
			glue = {"com.qa.tests.stepdefs", "com.qa.tests.hooks"},
			plugin = {"pretty", "html:target/cucumber-reports/cucumber.html"}, dryRun = false,
			monochrome = true)

	public class TestRunner extends AbstractTestNGCucumberTests {
	    @Override
	    @DataProvider(parallel = false)
	    public Object[][] scenarios() {
	        return super.scenarios();
	}
	    
	}    

