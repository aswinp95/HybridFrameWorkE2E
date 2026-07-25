package com.qa.framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    // variable that belongs to this class itself, not to any individual object
    // static means there is only ever one copy of this in memory, shared everywhere,
    // since we only want to load the file once and reuse it
    private static Properties properties;

    // static final = constant (assigned once, never changed). ALL_CAPS signals
    // to any reader that it never changes.
    private static final String ENV = System.getProperty("env", "qa"); // "qa" default if -Denv isn't passed
    private static final String CONFIG_PATH = "src/test/resources/config/config-" + ENV + ".properties";

    static {
        try {
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file at path: " + CONFIG_PATH
                    + ". Did you pass the correct -Denv flag?", e);
        }
    }

    public static String getBrowser() {
        return properties.getProperty("browser");
    }
    
    public static String getPlaygroundUrl() {
    	return properties.getProperty("playgroundUrl");
    }

    public static String getExecutionEnv() {
        return properties.getProperty("executionEnv");
    }

    public static String getBaseUrl() {
        return properties.getProperty("baseUrl");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicitWait"));
    }
}