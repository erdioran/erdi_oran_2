package io.cucumber.skeleton;


import base.DriverManager;
import io.cucumber.junit.platform.engine.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.jupiter.api.AfterAll;


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("io.cucumber.features")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "summary,pretty,html:target/cucumber-reports.html,json:target/cucumber.json,com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "io.cucumber.skeleton")
@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_ENABLED_PROPERTY_NAME, value = "false")

public class RunCucumberTest {


    private static final Logger LOGGER = LogManager.getLogger(RunCucumberTest.class);

    @AfterAll
    public static void afterSuite() {
        try {
            if (DriverManager.isDriverActive()) {
                DriverManager.quitDriver();
                LogManager.getLogger(RunCucumberTest.class).info("Test suite completed, all drivers closed");
            }
        } catch (Exception e) {
            LogManager.getLogger(RunCucumberTest.class).warn("Error during suite cleanup: {}", e.getMessage());
        }
    }

}
