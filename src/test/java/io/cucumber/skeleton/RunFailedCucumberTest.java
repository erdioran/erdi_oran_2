package io.cucumber.skeleton;

import base.DriverManager;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "@target/failedrerun.txt",
        glue = "io.cucumber.skeleton",
        monochrome = true,
        plugin = {"summary", "pretty",
                "html:target/cucumber-reports-retry.html",
                "json:target/cucumber-retry.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
public class RunFailedCucumberTest {

    private static final Logger LOGGER = LogManager.getLogger(RunFailedCucumberTest.class);

    @AfterClass
    public static void afterSuite() {
        try {
            if (DriverManager.isDriverActive()) {
                DriverManager.quitDriver();
                LOGGER.info("Retry test suite completed, all drivers closed");
            }
        } catch (Exception e) {
            LOGGER.warn("Error during retry suite cleanup: {}", e.getMessage());
        }
    }
}
