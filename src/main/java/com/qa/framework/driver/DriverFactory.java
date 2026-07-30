package com.qa.framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.UnexpectedAlertBehaviour;

import com.qa.framework.config.ConfigReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverFactory: the single gateway through which every test/page gets its WebDriver.
 *
 * Design patterns in play:
 *  1. Singleton-per-thread  -> achieved via ThreadLocal, NOT a classic static-instance
 *     singleton. A true classic Singleton (one instance for the whole JVM) would be
 *     actively WRONG here, because parallel TestNG threads would all fight over one
 *     browser session. ThreadLocal gives us "one instance per thread" which is the
 *     correct interpretation of Singleton in a multi-threaded test framework.
 *  2. Factory Pattern -> createDriver() decides WHICH concrete WebDriver
 *     implementation (Chrome/Firefox/Edge, LOCAL, REMOTE via BrowserStack, or DOCKER
 *     via a local Selenium Grid container) to instantiate based on runtime config,
 *     so calling code never uses `new ChromeDriver()` directly.
 *
 *  Execution modes, controlled by ConfigReader.getExecutionEnv():
 *   - "local"  -> real browser launched directly on this machine
 *   - "remote" -> RemoteWebDriver pointed at BrowserStack's cloud hub
 *   - "docker" -> RemoteWebDriver pointed at a local Selenium Grid container
 *                 (see docker-compose.yml). Currently Chrome-only, since the
 *                 container only registers a Chrome node — a Firefox/Edge node
 *                 would need its own service added to docker-compose.yml.
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private static final String DOCKER_GRID_URL = "http://localhost:4444/wd/hub";

    private DriverFactory() {
    }

    public static void createDriver(String browser) {
        log.info("Creating WebDriver instance for browser: {}", browser);

        WebDriver driver;
        String executionEnv = ConfigReader.getExecutionEnv();

        if (executionEnv.equalsIgnoreCase("remote")) {
            driver = createRemoteDriver(browser);
        } else if (executionEnv.equalsIgnoreCase("docker")) {
            driver = createDockerDriver(browser);
        } else {
            driver = switch (browser.toLowerCase()) {
                case "chrome" -> {
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--remote-allow-origins=*");
                    options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
                    yield new ChromeDriver(options);
                }
                case "firefox" -> {
                    WebDriverManager.firefoxdriver().setup();
                    yield new FirefoxDriver(new FirefoxOptions());
                }
                case "edge" -> {
                    WebDriverManager.edgedriver().setup();
                    yield new EdgeDriver(new EdgeOptions());
                }
                default -> throw new IllegalArgumentException(
                        "Unsupported browser: " + browser + ". Supported: chrome, firefox, edge");
            };
        }

        driverThreadLocal.set(driver);
        log.info("WebDriver instance bound to thread: {}", Thread.currentThread().getId());
    }

    /**
     * Builds a RemoteWebDriver pointed at BrowserStack's hub, using credentials
     * read from environment variables — NEVER hardcoded, NEVER committed to Git.
     */
    private static WebDriver createRemoteDriver(String browser) {
        String username = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

        if (username == null || accessKey == null) {
            throw new IllegalStateException(
                    "BROWSERSTACK_USERNAME or BROWSERSTACK_ACCESS_KEY environment variable "
                    + "is not set. Set them as Windows environment variables and restart your IDE.");
        }

        String hubUrl = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";

        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", browser);

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("os", "Windows");
        bstackOptions.put("osVersion", "11");
        bstackOptions.put("sessionName", "HybridFramework Test Run");
        bstackOptions.put("buildName", "HybridFramework Build 1");
        capabilities.setCapability("bstack:options", bstackOptions);

        try {
            log.info("Connecting to BrowserStack hub for browser: {}", browser);
            return new RemoteWebDriver(new URL(hubUrl), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid BrowserStack hub URL: " + hubUrl, e);
        }
    }

    /**
     * Builds a RemoteWebDriver pointed at the local Selenium Grid container
     * started via `docker-compose up -d`. No credentials needed — this is
     * purely local infrastructure, not a shared cloud service.
     */
    private static WebDriver createDockerDriver(String browser) {
        if (!browser.equalsIgnoreCase("chrome")) {
            throw new IllegalArgumentException(
                    "Docker execution currently only supports Chrome — the running "
                    + "container only registers a Chrome node. Requested browser: " + browser);
        }

        ChromeOptions options = new ChromeOptions();
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);

        try {
            log.info("Connecting to local Docker Selenium Grid at: {}", DOCKER_GRID_URL);
            return new RemoteWebDriver(new URL(DOCKER_GRID_URL), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Docker Grid URL: " + DOCKER_GRID_URL, e);
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver not initialized for thread: " + Thread.currentThread().getId()
                            + ". Did you forget to call createDriver() first?");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            log.info("Quitting WebDriver for thread: {}", Thread.currentThread().getId());
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}