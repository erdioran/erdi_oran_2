package base;


import base.DriverManager;
import utils.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static utils.ElementManager.returnElement;
import static utils.Helper.sleepInSeconds;


public class AutomationMethods {


    private static final Logger LOGGER = LogManager.getLogger(AutomationMethods.class);

    public static void pageLoad(String url) {
        DriverManager.getDriver().get(url);
    }

    public static String getBase64Screenshot() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }


    public static void enterText(String element, String textToEnter) throws Exception {
        WebElement webElement = findObject(returnElement(element));
        webElement.clear();
        webElement.sendKeys(textToEnter);
    }

    public static void enterText(By by, String textToEnter) throws Exception {
        WebElement webElement = findObject(by);
        webElement.clear();
        webElement.sendKeys(textToEnter);
    }


    public static String getText(String element) throws Exception {
        return findObject(returnElement(element)).getText().trim();
    }

    public static void clear(String element) throws Exception {
        By by = returnElement(element);
        WebElement input = DriverManager.getDriver().findElement(by);

        input.clear();

        if (!input.getAttribute("value").isEmpty()) {
            input.sendKeys(Keys.CONTROL + "a");
            input.sendKeys(Keys.DELETE);
        }

        if (!input.getAttribute("value").isEmpty()) {
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
            js.executeScript("arguments[0].value = '';", input);
        }
    }

    public static String getText(By by) throws Exception {
        return findObject(by).getText().trim();
    }

    public static WebElement findObject(By by) throws Exception {
        FluentWait<WebDriver> wait = getFluentWait();
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public static List<WebElement> waitAllElement(By selector) {
        FluentWait<WebDriver> wait = getFluentWait();
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(selector));
    }


    public static void click(String element) throws Exception {
        waitForIntervalsAndClick(returnElement(element), 1, ConfigManager.getExplicitWaitTime());
    }


    public static void click(By by) throws Exception {
        waitForIntervalsAndClick(by, 1, ConfigManager.getExplicitWaitTime());
    }

    public static void clickPath(By element) throws Exception {
        waitForIntervalsAndClick(element, 1, ConfigManager.getExplicitWaitTime());
    }


    public static void waitForIntervalsAndClick(By by, int interval, int maxWait) throws Exception {
        boolean elementExists = false;
        int counter = 0;
        while (counter <= maxWait) {
            try {
                DriverManager.getDriver().findElement(by).click();
                elementExists = true;
                counter = maxWait + 1;
            } catch (Exception e) {
                LOGGER.info("Web element [{}] | Click attempt: [{}]", by.toString(), counter);
                sleepInSeconds(interval);
                counter++;
                elementExists = false;
            }
        }
        if (!elementExists) {
            DriverManager.getDriver().findElement(by).click();
        }
    }


    public static FluentWait<WebDriver> getFluentWait(int intervalInSeconds, int maxWaitTimeInSeconds) {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(intervalInSeconds))
                .pollingEvery(Duration.ofSeconds(maxWaitTimeInSeconds))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(ElementNotInteractableException.class);
    }

    public static FluentWait<WebDriver> getFluentWait() {
        return getFluentWait(1, ConfigManager.getExplicitWaitTime());
    }

    public static void switchToTab() {
        String main = DriverManager.getDriver().getWindowHandle();
        for (String windowHandle : DriverManager.getDriver().getWindowHandles()) {
            if (!main.contentEquals(windowHandle)) {
                DriverManager.getDriver().switchTo().window(windowHandle);
                LOGGER.info("Tab switch");
                break;
            }
        }
    }

    public static int getTabCount() {
        return DriverManager.getDriver().getWindowHandles().size();
    }

    public static String getTabUrl() {
        return DriverManager.getDriver().getCurrentUrl();
    }


    public static boolean elementVisibiltyWithSize(String element) throws Exception {
        By by = returnElement(element);
        return DriverManager.getDriver().findElements(by).size() > 0;
    }

    public static boolean elementVisibiltyWithSize(By by) throws Exception {
        return DriverManager.getDriver().findElements(by).size() > 0;
    }


    public static boolean checkStringContains(String text, String value) {
        return text.contains(value);
    }

    public static int getListSize(String element) throws Exception {
        return DriverManager.getDriver().findElements(returnElement(element)).size();
    }

    public static int getListSize(By by) throws Exception {
        return DriverManager.getDriver().findElements(by).size();
    }

    public static int getRandomNumberBetween(int start, int end) {
        return (int) (Math.random() * end) + start;
    }

    public static void pageScroollDownToTargetElement(By by) {
        DriverManager.getDriver().findElement(by).sendKeys(Keys.ARROW_DOWN);
        LOGGER.info("Page scroll down");
    }

    public static void pageScroollUpToTargetElement(By by) {
        DriverManager.getDriver().findElement(by).sendKeys(Keys.ARROW_UP);
        LOGGER.info("Page scroll up");
    }

    public static void scrollToElementCenter(By by) {
        WebElement element = DriverManager.getDriver().findElement(by);

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        int windowHeight = DriverManager.getDriver().manage().window().getSize().getHeight();
        int elementPositionY = element.getLocation().getY();
        int elementHeight = element.getSize().getHeight();

        int scrollToY = elementPositionY - (windowHeight / 2) + (elementHeight / 2);
        js.executeScript("window.scrollTo(0, " + scrollToY + ")");
    }


    public static String getElementBackgroundColor(By by) {
        String buttonColor = DriverManager.getDriver().findElement(by).getCssValue("background-color");
        return buttonColor;
    }

    public static String getElementFontColor(By by) {
        String buttonColor = DriverManager.getDriver().findElement(by).getCssValue("color");
        return buttonColor;
    }

    public static void backPage() {
        DriverManager.getDriver().navigate().back();
    }

    public static void refreshPage() {
        DriverManager.getDriver().navigate().refresh();
    }


    public static String getImageUrl(By by) {
        String stockStatus = DriverManager.getDriver().findElement(by).getAttribute("src");
        return stockStatus;
    }


    public static String getValue(By by) {
        String value = DriverManager.getDriver().findElement(by).getAttribute("value");
        return value;
    }

    public static String getClassValue(By by) {
        String value = DriverManager.getDriver().findElement(by).getAttribute("class");
        return value;
    }

    public static String getPlaceholderValue(By by) {
        String value = DriverManager.getDriver().findElement(by).getAttribute("placeholder");
        return value;
    }

    public static String getDisableValue(String element) throws Exception {
        String value = DriverManager.getDriver().findElement(returnElement(element)).getAttribute("aria-valuenow");
        return value;
    }

    public static boolean getImageDisplayStatus(By by) {
        WebElement img = DriverManager.getDriver().findElement(by);
        Boolean display = (Boolean) ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("return arguments[0].complete "
                        + "&& typeof arguments[0].naturalWidth != \"undefined\" "
                        + "&& arguments[0].naturalWidth > 0", img);
        return display != null && display;
    }

    public static String getDataToogleValue(By by) {
        String value = DriverManager.getDriver().findElement(by).getAttribute("data-toggle");
        return value;
    }


    public static void keyboardEnter() {
        Actions actions = new Actions(DriverManager.getDriver());
        actions.sendKeys(Keys.ENTER).perform();
    }

    public static void scrollPageToTop() {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("window.scrollTo(0, 0)");
    }

    public static void switchDriverIframe(By by) {
        DriverManager.getDriver().switchTo().frame(DriverManager.getDriver().findElement(by));
    }

    public static void switchDriverDefault() {
        DriverManager.getDriver().switchTo().defaultContent();
    }


    public static void closeTab() {
        DriverManager.getDriver().close();
    }


    public static void closeTabOtherThanTheMainTab() {
        String mainTab = DriverManager.getDriver().getWindowHandle();
        Set<String> allTabs = DriverManager.getDriver().getWindowHandles();

        for (String tab : allTabs) {
            if (!tab.equals(mainTab)) {
                DriverManager.getDriver().switchTo().window(tab);
                DriverManager.getDriver().close();
            }
        }

        DriverManager.getDriver().switchTo().window(mainTab);
    }

    public static boolean waitForElement(String element, int timeoutInSeconds) {
        int attempts = 0;
        while (attempts < timeoutInSeconds) {
            try {
                if (elementVisibiltyWithSize(element)) {
                    return true;
                }
            } catch (NoSuchElementException e) {
                sleepInSeconds(1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            attempts++;
        }
        return false;
    }

    public static int getProductCount() throws Exception {
        By productCards = returnElement("URUN_LIST_PHOTO_LIST");
        return DriverManager.getDriver().findElements(productCards).size();
    }

    public static int getRandomProductNumber() throws Exception {
        int productCount = getProductCount();
        if (productCount == 0) {
            throw new RuntimeException("No products found on the page!");
        }
        return getRandomNumberBetween(1, productCount);
    }

    public static void selectProductByNumber(int productNumber) throws Exception {
        int productCount = getProductCount();
        if (productNumber < 1 || productNumber > productCount) {
            throw new RuntimeException("Invalid product number: " + productNumber + ". Total products: " + productCount);
        }

        By productXpath = By.xpath("(//div[contains(@class,'m-productCard__photo')])[" + productNumber + "]");
        click(productXpath);
        LOGGER.info("Product selected: " + productNumber + "/" + productCount);
    }

    public static String getProductBrand() throws Exception {
        return getText(returnElement("URUN_MARKA"));
    }

    public static String getProductDetail() throws Exception {
        return getText(returnElement("URUN_DETAY"));
    }

    public static String getProductPrice() throws Exception {
        return getText(returnElement("URUN_FIYAT"));
    }

    public static void writeProductInfoToFile() throws Exception {
        String brand = getProductBrand();
        String detail = getProductDetail();
        String price = getProductPrice();
        
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        
        String productInfo = "=== ÜRÜN BİLGİLERİ ===\n" +
                           "Marka: " + brand + "\n" +
                           "Detay: " + detail + "\n" +
                           "Fiyat: " + price + "\n" +
                           "Tarih: " + timestamp + "\n" +
                           "========================\n\n";
        
        utils.FileManager.writeToFile(productInfo);
    }

    public static int getSizeCount() throws Exception {
        By sizeElements = returnElement("URUN_BEDEN_LIST");
        return DriverManager.getDriver().findElements(sizeElements).size();
    }

    public static int getClickableSizeCount() throws Exception {
        // Try multiple selectors for size elements
        String[] sizeSelectors = {
            "//span[@class='m-variation__item' and not(contains(@class, '-disabled'))]",
            "//span[contains(@class, 'm-variation__item') and not(contains(@class, 'disabled'))]",
            "//div[contains(@class, 'size') and not(contains(@class, 'disabled'))]",
            "//button[contains(@class, 'size') and not(@disabled)]",
            "//a[contains(@class, 'size') and not(contains(@class, 'disabled'))]"
        };
        
        for (String selector : sizeSelectors) {
            By sizeBy = By.xpath(selector);
            int count = DriverManager.getDriver().findElements(sizeBy).size();
            if (count > 0) {
                LOGGER.info("Found {} clickable sizes using selector: {}", count, selector);
                return count;
            }
        }
        
        // Fallback: try to find any size elements and check if they're clickable
        By fallbackSelector = returnElement("URUN_BEDEN_LIST");
        List<WebElement> allSizes = DriverManager.getDriver().findElements(fallbackSelector);
        int clickableCount = 0;
        for (WebElement size : allSizes) {
            if (size.isEnabled() && size.isDisplayed()) {
                clickableCount++;
            }
        }
        
        LOGGER.info("Found {} clickable sizes using fallback method", clickableCount);
        return clickableCount;
    }

    public static void selectClickableSizeByNumber(int sizeNumber) throws Exception {
        int clickableSizeCount = getClickableSizeCount();
        if (clickableSizeCount == 0) {
            LOGGER.warn("No sizes found, skipping size selection");
            return;
        }
        
        if (sizeNumber < 1 || sizeNumber > clickableSizeCount) {
            throw new RuntimeException("Invalid clickable size number: " + sizeNumber + ". Total clickable sizes: " + clickableSizeCount);
        }
        
        // Try multiple selectors for clicking sizes
        String[] sizeSelectors = {
            "(//span[@class='m-variation__item' and not(contains(@class, '-disabled'))])[" + sizeNumber + "]",
            "(//span[contains(@class, 'm-variation__item') and not(contains(@class, 'disabled'))])[" + sizeNumber + "]",
            "(//div[contains(@class, 'size') and not(contains(@class, 'disabled'))])[" + sizeNumber + "]",
            "(//button[contains(@class, 'size') and not(@disabled)])[" + sizeNumber + "]",
            "(//a[contains(@class, 'size') and not(contains(@class, 'disabled'))])[" + sizeNumber + "]"
        };
        
        boolean sizeSelected = false;
        for (String selector : sizeSelectors) {
            try {
                By sizeBy = By.xpath(selector);
                List<WebElement> elements = DriverManager.getDriver().findElements(sizeBy);
                if (!elements.isEmpty()) {
                    elements.get(0).click();
                    LOGGER.info("Clickable size selected: {} / {} using selector: {}", sizeNumber, clickableSizeCount, selector);
                    sizeSelected = true;
                    break;
                }
            } catch (Exception e) {
                LOGGER.debug("Failed to select size with selector: {}, error: {}", selector, e.getMessage());
            }
        }
        
        // Fallback: try using the element from JSON
        if (!sizeSelected) {
            try {
                By fallbackSelector = returnElement("URUN_BEDEN_LIST");
                List<WebElement> allSizes = DriverManager.getDriver().findElements(fallbackSelector);
                int clickableIndex = 0;
                for (WebElement size : allSizes) {
                    if (size.isEnabled() && size.isDisplayed()) {
                        clickableIndex++;
                        if (clickableIndex == sizeNumber) {
                            size.click();
                            LOGGER.info("Size selected using fallback method: {} / {}", sizeNumber, clickableSizeCount);
                            sizeSelected = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("Fallback size selection failed: {}", e.getMessage());
            }
        }
        
        if (!sizeSelected) {
            LOGGER.warn("Could not select size, continuing without size selection");
        }
    }

    public static int getRandomClickableSizeNumber() throws Exception {
        int clickableSizeCount = getClickableSizeCount();
        if (clickableSizeCount == 0) {
            LOGGER.warn("No clickable sizes found on the page, will skip size selection");
            return 0;
        }
        return getRandomNumberBetween(1, clickableSizeCount);
    }

    public static void selectRandomSize() throws Exception {
        int randomNumber = getRandomClickableSizeNumber();
        if (randomNumber > 0) {
            selectClickableSizeByNumber(randomNumber);
        } else {
            LOGGER.info("Skipping size selection as no sizes are available");
        }
    }

    public static void selectSizeByNumber(int sizeNumber) throws Exception {
        selectClickableSizeByNumber(sizeNumber);
    }

    public static int getRandomSizeNumber() throws Exception {
        return getRandomClickableSizeNumber();
    }

    private static String savedProductPrice = "";

    public static void saveProductPrice() throws Exception {
        savedProductPrice = getProductPrice();
        LOGGER.info("Product price saved: " + savedProductPrice);
    }

    public static String getSavedProductPrice() {
        return savedProductPrice;
    }

    public static String getCartPrice() throws Exception {
        return getText(returnElement("SEPET_FIYAT"));
    }

    public static boolean comparePrices() throws Exception {
        String cartPrice = getCartPrice();
        LOGGER.info("Saved price: " + savedProductPrice);
        LOGGER.info("Cart price: " + cartPrice);
        
        String normalizedSavedPrice = normalizePriceFormat(savedProductPrice);
        String normalizedCartPrice = normalizePriceFormat(cartPrice);
        
        LOGGER.info("Normalized saved price: " + normalizedSavedPrice);
        LOGGER.info("Normalized cart price: " + normalizedCartPrice);
        
        boolean isEqual = normalizedSavedPrice.equals(normalizedCartPrice);
        LOGGER.info("Price comparison: " + (isEqual ? "EQUAL" : "DIFFERENT"));
        
        return isEqual;
    }
    
    private static String normalizePriceFormat(String price) {
        if (price == null || price.trim().isEmpty()) {
            return "";
        }
        
        String cleanPrice = price.replaceAll("[^0-9,.]", "").trim();
        
        if (cleanPrice.endsWith(",00")) {
            cleanPrice = cleanPrice.substring(0, cleanPrice.length() - 3);
        }
        
        if (cleanPrice.endsWith(".00")) {
            cleanPrice = cleanPrice.substring(0, cleanPrice.length() - 3);
        }
        
        return cleanPrice;
    }

    public static void verifyPricesMatch() throws Exception {
        if (!comparePrices()) {
            throw new RuntimeException("Prices do not match! Product price: " + savedProductPrice + ", Cart price: " + getCartPrice());
        }
        LOGGER.info("Price verification successful - Prices match");
    }

    public static String getAriaLabel(String element) throws Exception {
        WebElement webElement = findObject(returnElement(element));
        return webElement.getAttribute("aria-label");
    }

    public static String getAriaLabel(By by) throws Exception {
        WebElement webElement = findObject(by);
        return webElement.getAttribute("aria-label");
    }

}
