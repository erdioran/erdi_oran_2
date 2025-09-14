package base;

import utils.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class DriverManager {
    private static final Logger LOGGER = LogManager.getLogger(DriverManager.class);
    private static final InheritableThreadLocal<WebDriver> WEB_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        return WEB_DRIVER_THREAD_LOCAL.get();
    }

    public static void setDriver(WebDriver driver) {
        WEB_DRIVER_THREAD_LOCAL.set(driver);
    }

    public static void quitDriver() {
        try {
            WebDriver driver = WEB_DRIVER_THREAD_LOCAL.get();
            if (driver != null) {
                driver.quit();
                WEB_DRIVER_THREAD_LOCAL.remove();
                LOGGER.info("WebDriver quit successfully");
            }
        } catch (Exception e) {
            LOGGER.warn("Error while quitting WebDriver: {}", e.getMessage());
        }
    }


    public static void launchBrowser(String browser) {
        try {
            WebDriver driver = createDriver(browser.toLowerCase());
            configureDriver(driver);
            setDriver(driver);
            LOGGER.info("Successfully launched {} browser", browser);
        } catch (Exception e) {
            LOGGER.error("Failed to launch {} browser: {}", browser, e.getMessage());
            throw new RuntimeException("Browser launch failed: " + e.getMessage(), e);
        }
    }

    private static WebDriver createDriver(String browser) {
        switch (browser) {
            case "chrome":
                return createChromeDriver();
            case "firefox":
                return createFirefoxDriver();
            case "edge":
                return createEdgeDriver();
            default:
                throw new IllegalArgumentException(
                    String.format("Unsupported browser: %s. Supported browsers: chrome, firefox, edge", browser));
        }
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        // Basic Chrome arguments
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        if (ConfigManager.isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        options.addArguments("--disable-popup-blocking");
        
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (ConfigManager.isHeadless()) {
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-gpu");
        
        if (ConfigManager.isHeadless()) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
        }
        
        return new EdgeDriver(options);
    }

    private static void configureDriver(WebDriver driver) {

        if (!ConfigManager.isHeadless()) {
            driver.manage().window().maximize();
        }
        
        int implicitWait = ConfigManager.getIntValue("implicit.wait.time", 10);
        int pageLoadTimeout = ConfigManager.getIntValue("page.load.time.out", 60);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
    }

    public static boolean isDriverActive() {
        try {
            WebDriver driver = getDriver();
            return driver != null && driver.getWindowHandles().size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static void refreshDriver() {
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.navigate().refresh();
            LOGGER.info("Page refreshed");
        }
    }

}
