package listener;


import base.DriverManager;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestListener implements ITestListener {

  private static final Logger LOGGER = LogManager.getLogger(TestListener.class);

  @Override
  public void onTestSuccess(ITestResult result) {
    LOGGER.info("Passed test method : [{}] in class [{}]", result.getMethod().getMethodName(), result.getTestClass().getName());
  }

  @Override
  public void onTestFailure(ITestResult result) {
    LOGGER.info("Failed test method : [{}] in class [{}]", result.getMethod().getMethodName(),
            result.getTestClass().getName());
    LOGGER.error(result.getThrowable());
    if (!result.wasRetried()) {
      if (DriverManager.getDriver() != null) {
        String base64Screenshot = ((TakesScreenshot) Objects.requireNonNull(DriverManager.getDriver()))
                .getScreenshotAs(OutputType.BASE64);
      }
    }
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    List<String> dependingTestList = result.getSkipCausedBy().stream().map(ITestNGMethod::getMethodName)
        .collect(Collectors.toList());
    LOGGER.info("Skipped test method : [{}] in class [{}]. Depending on test method {}", result.getMethod().getMethodName(),
        result.getTestClass().getName(), dependingTestList);
    if (result.wasRetried()) {
      LOGGER.debug("Test method skipped and being retried. Removing the node");
    } else {
      String testMethodNameOrDescription =
          StringUtils.isNotBlank(result.getMethod().getDescription()) ? result.getMethod().getDescription()
              : result.getMethod().getMethodName();
    }
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
  }

  @Override
  public void onStart(ITestContext context) {
    LOGGER.info("Executing test : [{}] from class [{}]", context.getCurrentXmlTest().getName(),
        context.getCurrentXmlTest().getXmlClasses().get(0).getName());

  }

  @Override
  public void onFinish(ITestContext context) {
  }
}
