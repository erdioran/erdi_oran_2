package io.cucumber.skeleton;


import base.AutomationMethods;
import base.DriverManager;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.junit.jupiter.api.Assertions;
import utils.ExcelManager;

import java.time.Duration;

import static utils.ElementManager.returnElement;
import static utils.Helper.*;
import static base.AutomationMethods.*;
import static utils.DataManager.getData;


public class StepDefinitionsGeneral {

    private static final Logger LOGGER = LogManager.getLogger(StepDefinitionsGeneral.class);

    int listSize;


    @And("Open home page")
    public void homePage() {

        pageLoad(getData("url.base"));
        LOGGER.info("user is on login page");
    }


    @And("Check {string} element is visible")
    public void elementVisible(String element) throws Exception {
        elementVisibiltyWithSize(element);
    }

    @And("Click {string}")
    public void clickStep(String element) throws Exception {
        click(element);
    }

    @And("Clear {string} textbox")
    public void clearTextbox(String element) throws Exception {
        clear(element);
    }


    @And("Press enter")
    public void pressEnter() throws Exception {
        keyboardEnter();
    }

    @And("Enter {string},{string} search term in {string}")
    public void enterExcellDataSearchTerm(String column, String cell, String element) throws Exception {
        String searchTerm = ExcelManager.getCellValue("TestData", Integer.parseInt(cell), Integer.parseInt(column));
        LOGGER.info("Search:" + searchTerm);
        AutomationMethods.enterText(returnElement(element), searchTerm);
    }

    @And("Save product information to file")
    public void saveProductInfo() throws Exception {
        AutomationMethods.writeProductInfoToFile();
    }

    @And("Select random size")
    public static void selectRandomSize() throws Exception {
        int randomNumber = getRandomSizeNumber();
        selectSizeByNumber(randomNumber);
    }

    @And("Save product price")
    public void saveProductPrice() throws Exception {
        AutomationMethods.saveProductPrice();
    }

    @And("Assert prices match")
    public void verifyPricesMatch() throws Exception {
        AutomationMethods.verifyPricesMatch();
    }

    @And("Assert quantity value is {string}")
    public void getQuantityValue(String value) throws Exception {
        String quantity = AutomationMethods.getAriaLabel("SEPET_ADET_BOX");
        LOGGER.info("Seçili miktar: " + quantity);
        Assertions.assertEquals(value + " adet", quantity);
    }

    @And("Wait {int} second")
    public void waitMinMin(int second) {
        sleepInSeconds(second);
    }

    @And("Wait {int}")
    public void wait(int second) {
        sleepInSeconds(second);
    }


    @And("Go to previous page")
    public void goToPreviousPage() {
        DriverManager.getDriver().navigate().back();
    }

    @And("Switch tab")
    public void switchTabTest() {
        switchToTab();
    }

    @And("Close tab")
    public void closeTabTest() {
        closeTab();
    }


    @And("Close tabs other than the main tab")
    public void closeTabsOtherThanTheMainTab() {
        closeTabOtherThanTheMainTab();
    }

    @And("Select random product")
    public void selectRandomProduct() throws Exception {
        int randomNumber = getRandomProductNumber();
        selectProductByNumber(randomNumber);
    }

    @And("Assert {string} element is visible")
    public void assertElementIsVisible(String element) throws Exception {
        boolean isVisible = waitForElementToBeVisible(element, 10);
        Assertions.assertTrue(isVisible, "Element '" + element + "' should be visible but was not found");
    }

    @And("Assert {string} element is not visible")
    public void assertElementIsNotVisible(String element) throws Exception {
        boolean isInvisible = waitForElementToBeInvisible(element, 10);
        Assertions.assertTrue(isInvisible, "Element '" + element + "' should not be visible but was found");

    }

    @And("Enter {string} text in {string}")
    public void enterTextInTextBox(String text, String element) throws Exception {
        enterText(element, text);
    }

    @And("Assert {string} element is {string} text")
    public void assertThatElementIsText(String element, String text) throws Exception {
        Assertions.assertEquals(getText(element), text);
    }

    @And("Assert {string} element is {string} disable value")
    public void assertThatElementDisableValue(String element, String text) throws Exception {
        Assertions.assertEquals(getDisableValue(element), text);
    }

    @And("Get list size {string}")
    public void getListSizeStep(String element) throws Exception {
        listSize = getListSize(element);
    }

    @And("Scroll to last record")
    public void scrollToPageEnd() throws Exception {
        scrollToElementCenter(By.xpath("(//tr[@class='ant-table-row ant-table-row-level-0'])[" + listSize + "]"));
    }

}
