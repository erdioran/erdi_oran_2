package io.cucumber.skeleton;

import base.DriverManager;
import utils.ConfigManager;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;

import static org.openqa.selenium.OutputType.BYTES;

public class CucumberHooks {

    private static final Logger LOGGER = LogManager.getLogger(CucumberHooks.class);

    @Before
    public void setUp() {
        try {
            DriverManager.launchBrowser(ConfigManager.getBrowser());
            LOGGER.info("Browser started successfully: {}", ConfigManager.getBrowser());
        } catch (Exception e) {
            LOGGER.error("Failed to start browser: {}", e.getMessage());
            throw new RuntimeException("Browser initialization failed", e);
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (DriverManager.isDriverActive()) {
                if (scenario.isFailed() || ConfigManager.getBooleanValue("capture.all.screens", false)) {
                    TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
                    byte[] screenshot = ts.getScreenshotAs(BYTES);
                    scenario.attach(screenshot, "image/png", scenario.getName());
                    LOGGER.info("Screenshot captured for scenario: {}", scenario.getName());
                }
                
                DriverManager.quitDriver();
                LOGGER.info("Browser closed successfully");
            }
        } catch (Exception e) {
            LOGGER.warn("Error during teardown: {}", e.getMessage());
        }
    }
}
