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
        features = "@target/failedrerun.txt",
        glue = "io.cucumber.skeleton",
        monochrome = true,
        plugin = {"summary", "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "rerun:target/failedrerun.txt"}
)

public class RunnerFailed extends AbstractTestNGCucumberTests {


    private static final Logger LOGGER = LogManager.getLogger(RunnerFailed.class);

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @BeforeMethod
    public void startBrowser() {
        try {
            DriverManager.launchBrowser(ConfigManager.getBrowser());
            LOGGER.info("Browser started successfully for failed test re-run: {}", ConfigManager.getBrowser());
        } catch (Exception e) {
            LOGGER.error("Failed to start browser for re-run: {}", e.getMessage());
            throw new RuntimeException("Browser initialization failed for re-run", e);
        }
    }

    @AfterMethod
    public void closeBrowser(ITestResult result) {
        try {
            if (DriverManager.isDriverActive()) {
                DriverManager.quitDriver();
                LOGGER.info("Browser closed successfully after re-run");
            }
        } catch (Exception e) {
            LOGGER.warn("Error while closing browser after re-run: {}", e.getMessage());
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        try {
            if (DriverManager.isDriverActive()) {
                DriverManager.quitDriver();
                LOGGER.info("Failed test re-run suite completed, all drivers closed");
            }
        } catch (Exception e) {
            LOGGER.warn("Error during failed test suite cleanup: {}", e.getMessage());
        }
    }

    @After(order = 1)
    public void afterScenario(Scenario scenario) {
        try {
            if (DriverManager.isDriverActive()) {
                // Always take screenshot for failed test re-runs since these are already failed scenarios
                if (scenario.isFailed() || ConfigManager.getBooleanValue("capture.all.screens", true)) {
                    TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
                    byte[] screenshot = ts.getScreenshotAs(BYTES);
                    scenario.attach(screenshot, "image/png", "RERUN_" + scenario.getName());
                    LOGGER.info("Screenshot captured for failed test re-run: {}", scenario.getName());
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to capture screenshot for re-run scenario {}: {}", scenario.getName(), e.getMessage());
        }
    }

}
