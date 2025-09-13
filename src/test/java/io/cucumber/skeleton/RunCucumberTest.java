package io.cucumber.skeleton;


import base.DriverManager;
import utils.ConfigManager;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;

import static org.openqa.selenium.OutputType.BYTES;


@CucumberOptions(
        publish = false,
        features = "src/test/resources/io.cucumber.features",
        glue = "io.cucumber.skeleton",
        monochrome = true,
        plugin = {"summary", "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "rerun:target/failedrerun.txt"}
)

public class RunCucumberTest extends AbstractTestNGCucumberTests {


    private static final Logger LOGGER = LogManager.getLogger(RunCucumberTest.class);

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @BeforeMethod
    public void startBrowser() {
        try {
            DriverManager.launchBrowser(ConfigManager.getBrowser());
            LOGGER.info("Browser started successfully: {}", ConfigManager.getBrowser());
        } catch (Exception e) {
            LOGGER.error("Failed to start browser: {}", e.getMessage());
            throw new RuntimeException("Browser initialization failed", e);
        }
    }


    @AfterMethod
    public void closeBrowser(ITestResult result) {
        try {
            if (DriverManager.isDriverActive()) {
                DriverManager.quitDriver();
                LOGGER.info("Browser closed successfully");
            }
        } catch (Exception e) {
            LOGGER.warn("Error while closing browser: {}", e.getMessage());
        }
    }


    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        try {
            if (DriverManager.isDriverActive()) {
                DriverManager.quitDriver();
                LOGGER.info("Test suite completed, all drivers closed");
            }
        } catch (Exception e) {
            LOGGER.warn("Error during suite cleanup: {}", e.getMessage());
        }
    }

    @After(order = 1)
    public void afterScenario(Scenario scenario) {
        try {
            if (DriverManager.isDriverActive()) {
                // Take screenshot for failed scenarios or if configured to capture all
                if (scenario.isFailed() || ConfigManager.getBooleanValue("capture.all.screens", false)) {
                    TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
                    byte[] screenshot = ts.getScreenshotAs(BYTES);
                    scenario.attach(screenshot, "image/png", scenario.getName());
                    LOGGER.info("Screenshot captured for scenario: {}", scenario.getName());
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to capture screenshot for scenario {}: {}", scenario.getName(), e.getMessage());
        }
    }

}
