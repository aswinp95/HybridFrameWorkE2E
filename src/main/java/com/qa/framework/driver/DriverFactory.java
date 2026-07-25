package com.qa.framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

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
 *     implementation (Chrome/Firefox/Edge) to instantiate based on a runtime
 *     parameter, so calling code never uses `new ChromeDriver()` directly.
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    // ThreadLocal<WebDriver>: each thread that calls .set() gets its OWN copy,
    // invisible to other threads, even though they're all calling the same
    // static field. This is the crux of thread-safe parallel execution.
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    // Private constructor: prevents `new DriverFactory()` from outside this class,
    // reinforcing that this class is meant to be used only via its static methods.
    private DriverFactory() {
    }

    /**
     * Factory method: creates a browser instance based on the given browser name.
     * Called once per test method (per thread) from a TestNG @BeforeMethod or
     * a Cucumber @Before hook.
     */
    public static void createDriver(String browser) {
        log.info("Creating WebDriver instance for browser: {}", browser);

        WebDriver driver;

        // Java 14+ enhanced switch expression — cleaner than old-style switch-case
        // with fall-through risk.
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

        driverThreadLocal.set(driver);
        log.info("WebDriver instance bound to thread: {}", Thread.currentThread().getId());
    }

    /**
     * Every page/test calls THIS to get the driver — never a static shared field.
     * Because it reads from ThreadLocal, each thread transparently gets its own
     * instance despite calling the exact same method.
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver not initialized for thread: " + Thread.currentThread().getId()
                            + ". Did you forget to call createDriver() first?");
        }
        return driver;
    }

    /**
     * Cleanup — MUST be called after every test/scenario. If you skip this,
     * ThreadLocal still holds a reference to the WebDriver even after the thread
     * returns to the pool for reuse on the next test — classic memory leak source.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            log.info("Quitting WebDriver for thread: {}", Thread.currentThread().getId());
            driver.quit();
            driverThreadLocal.remove(); // critical: detach reference, not just null it
        }
    }
}