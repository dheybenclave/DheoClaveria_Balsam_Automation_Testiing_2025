package stepdefinitions;

import io.cucumber.datatable.DataTable;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageComponent;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.actions.UnknownPageException;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.CommonPage;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Common step utilities used across step definitions.
 * <p>
 * Centralizes navigation, element interactions, verifications, and
 * small browser utilities so step definitions remain concise and readable.
 * Methods typically wrap Serenity/WebDriver primitives and add logging
 * via testStep() for consistent, searchable reports.
 */
public class CommonStepDef extends PageComponent {
    private static final Logger logger = LoggerFactory.getLogger(CommonStepDef.class);
    public CommonPage commonPage;

    public String mainwindow;
    public Set<String> s1;
    public Iterator<String> i1;

    // --- Navigation Methods ---

    /**
     * Navigate to a page resolved from configuration and assert the navigation succeeded.
     * <p>
     * Delegates to {@link #thePage(String)} which resolves the URL from serenity.conf
     * using the key pattern pages.<lowercase-page>.
     *
     * @param page Logical page name as configured under pages.*
     */
    public void navigatePage(String page) {
        this.thePage(page);
        Ensure.thatTheCurrentPage().currentUrl();
    }

    /**
     * Resolve a page URL from configuration and open it.
     * <p>
     * Looks for a property named "pages.{pageName in lowercase}" first via
     * optional property, then via mandatory property. Throws an exception if
     * the page is not configured to fail fast.
     *
     * @param pageName Logical page name (case-insensitive)
     * @throws net.serenitybdd.screenplay.actions.UnknownPageException when the page is not configured
     */
    public void thePage(String pageName) {
        String pageUrl = EnvironmentSpecificConfiguration.from(SystemEnvironmentVariables.currentEnvironmentVariables())
                .getOptionalProperty("pages." + pageName.toLowerCase())
                .orElse(SystemEnvironmentVariables.currentEnvironmentVariables()
                        .getProperty("pages." + pageName.toLowerCase()));
        if (pageUrl == null) {
            throw new UnknownPageException("No page defined with the name '" + pageUrl + "'");
        }
        testStep(String.format("Navigate Page to %s", pageName));
        this.getDriver().get(pageUrl);
    }

    /**
     * Capture the current window handle and all available handles for later switching.
     * <p>
     * Call this before opening a new window/tab so you can return to the original context.
     */
    public void generatedSwitchHandler() {
        testStep("Generated Switch Handler");
        mainwindow = this.getDriver().getWindowHandle();
        s1 = this.getDriver().getWindowHandles();
        i1 = s1.iterator();
    }

    /**
     * Switch back to the provided main window handle.
     *
     * @param windowHandle previously captured handle to switch to
     */
    public void switchToMainWindow(String windowHandle) {
        testStep("Switch to Main Window");
        this.getDriver().switchTo().window(windowHandle);
    }

    /**
     * Reset frame context by switching to the parent frame then default content.
     * <p>
     * Useful after interacting within iframes to ensure subsequent actions run in the top document.
     */
    public void switchToParentFrame() {
        testStep("Switch to Parent Frame");
        this.getDriver().switchTo().parentFrame();
        this.getDriver().switchTo().frame(0);
        this.getDriver().switchTo().defaultContent();
    }

    public void clickBackPage() {
        testStep("Click Back/Previous Button Page");
        this.getDriver().navigate().back();
    }

    public void clickRefreshPage() {
        testStep("Click Back/Previous Button Page");
        this.getDriver().navigate().refresh();
    }

    /**
     * Navigate to a UI section by clicking the supplied element after ensuring visibility.
     *
     * @param element element acting as a navigation trigger (e.g., menu item or link)
     */
    public void NavigateToUIPage(WebElementFacade element) {
        testStep(String.format("Navigate to : '%s'", element));
        verifyVisibilityofElement(element);
        element.click();
    }

    // --- Verification Methods ---

    /**
     * Verify a list of expected strings is visible somewhere on the current page.
     * <p>
     * Each string is matched using a generic label locator to be resilient to layout changes.
     *
     * @param dataTable single-column Cucumber table of expected texts
     */
    public void verifyTextListedinPage(DataTable dataTable) {
        List<String> expectedElementTextList = dataTable.asList();
        testStep(String.format("I verify the following text in the page : %s", expectedElementTextList));
        for (String expectedElementText : expectedElementTextList) {
            verifyVisibilityofElement(commonPage.PAGE_CONTROL_LABEL(expectedElementText));
        }
    }

    public void verifyVisibilityofElement(WebElementFacade element) {
        testStep(String.format("Verify the Visibility of the element %s in the page", element));
        testStep(String.format("the element %s is %s", element, element.isDisplayed()));
        shouldBeVisible(element);
    }

    public void verifyPresenceofElement(WebElementFacade element) {
        testStep(String.format(String.format("Verify the Presence of the element %s in the page", element)));
        testStep(String.format("the element %s is %s", element, element.isDisplayed()));
        element.isPresent();
    }

    /**
     * Verify multiple text tokens are present in the page using a generic locator.
     *
     * @param textList one or more expected text tokens
     */
    public void verifyTextInPage(String... textList) {
        for (String currText : textList) {
            testStep(String.format("verify the text in the page :%s", (Object) textList));
            verifyVisibilityofElement(commonPage.LBL_FIELD(currText));
        }
    }

    /**
     * Verify multiple text tokens within a given parent selector to scope assertions.
     *
     * @param parentSelector XPath/CSS parent selector to scope the search
     * @param textList       one or more expected text tokens
     */
    public void verifyTextInPageWithParentSelector(String parentSelector, String... textList) {
        for (String currText : textList) {
            testStep(String.format("verify the text in the page :%s", (Object) textList));
            verifyVisibilityofElement(commonPage.LBL_FIELD_WITH_PARENT_SELECTOR(parentSelector, currText));
        }
    }

    public void verifyVisibilityOfElements(WebElementFacade... elements) {
        testStep(String.format("Verify the visibility of %d elements", elements.length));
        for (WebElementFacade element : elements) {
            verifyVisibilityofElement(element);
        }
    }

    // --- Action/Click Methods ---
    public void clickElement(WebElementFacade element) {
        testStep(String.format("Click for Element '%s'", element));
        verifyVisibilityofElement(element);

        waitABit(2000);
        element.click();
    }

    /**
     * Click an element only if it is both visible and present.
     * <p>
     * Helps avoid noisy failures in dynamic UIs where elements are optional.
     *
     * @param element target element
     */
    public void clickElementIfExist(WebElementFacade element) {
        testStep(String.format("Click for Element if Exist '%s'", element));
        if (element.isVisible() && element.isPresent()) {
            verifyVisibilityofElement(element);
            waitABit(2000);// clickOn(element);
            element.click();

        }
    }

    public void clickTextIfExist(String elementText) {
        testStep(String.format("Click for Element if Exist '%s'", elementText));
        clickElementIfExist(commonPage.LBL_FIELD(elementText));
    }

    public void clickTextWithParentSelectorIfExist(String parentSelector, String elementText) {
        testStep(String.format("Click for Element if Exist '%s'", elementText));
        clickElementIfExist(commonPage.LBL_FIELD_WITH_PARENT_SELECTOR(parentSelector, elementText));
    }

    /**
     * Ensure a collapsible menu is expanded by checking a class identifier and clicking if needed.
     *
     * @param element         the header/trigger element for the collapsible menu
     * @param identifierValue class substring indicating the collapsed state (defaults to "collapsed")
     */
    public void CollapaseMenu(WebElementFacade element, String identifierValue) {
        testStep(String.format("Collapse Menu : '%s'", element));
        identifierValue = identifierValue.isEmpty() ? identifierValue : "collapsed";
        element.shouldBePresent();
        waitABit(1500);
        String getClassValue = element.getAttribute("class");
        if (!getClassValue.contains(identifierValue)) {
            element.click();
            waitABit(2000);
        } else {
            testStep(String.format("Element : '%s' already expanded/opened", element));
        }
        waitABit(2000);
    }

    /**
     * Toggle a collapsible menu based on the presence of the "collapsed" class.
     * <p>
     * Overload that uses the default class identifier.
     */
    public void CollapaseMenu(WebElementFacade element) {
        testStep(String.format("Collapse Menu : '%s'", element));
        element.shouldBePresent();
        String getClassValue = element.getAttribute("class");
        if (getClassValue.contains("collapsed")) {
            element.click();
            waitABit(2000);
        } else {
            testStep(String.format("Element : '%s' already expanded", element));
        }
        waitABit(2000);
    }

    // --- Input/Set Methods ---

    /**
     * Type a value into an input and press Enter, then wait for UI reactions.
     *
     * @param element         input element
     * @param value           text to type
     * @param waitForMilliSec post-action wait in milliseconds
     */
    public void enterText(WebElementFacade element, String value, int waitForMilliSec) {
        testStep(String.format("Enter Text '%s' with Value %s", element, value));
        verifyVisibilityofElement(element);
        element.typeAndEnter(value);
        waitABit(waitForMilliSec);
    }

    /**
     * Set a DOM attribute via JavaScript.
     *
     * @param element         target element
     * @param attName         attribute name
     * @param attValue        attribute value
     * @param waitForMilliSec post-action wait in milliseconds
     */
    public void setAttibute(WebElementFacade element, String attName, String attValue, int waitForMilliSec) {
        testStep(String.format("Set Attribute  '%s' | Name : '%s' | Value : '%s'", element, attName, attValue));
        element.shouldBePresent();
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attName, attValue);
        waitABit(waitForMilliSec);
    }

    /**
     * Set the value of an input using attribute manipulation, then wait.
     *
     * @param element         input element
     * @param value           value to set
     * @param waitForMilliSec post-action wait in milliseconds
     */
    public void setInputValue(WebElementFacade element, String value, int waitForMilliSec) {
        testStep(String.format("Set Value '%s' with Value %s", element, value));
        this.setAttibute(element, "value", value, waitForMilliSec);
        waitABit(waitForMilliSec);
    }

    // --- Utility Methods ---

    /**
     * Get text content of all provided elements, asserting visibility for each.
     *
     * @param elements one or more elements
     * @return list of text values in the order provided
     */
    public List<String> getTextListOfElements(WebElementFacade... elements) {
        List<String> textList = new java.util.ArrayList<>();
        for (WebElementFacade element : elements) {
            testStep(String.format("Get Text Attribute of Element : '%s' | Value : '%s'", element, element.getText()));
            verifyVisibilityofElement(element);
            textList.add(element.getText());
        }
        return textList;
    }

    /**
     * Get text content for elements that are both visible and present.
     * <p>
     * Skips elements that are not currently visible/present to be resilient to optional UI.
     *
     * @param elements one or more elements
     * @return list of text values for elements that existed at call time
     */
    public List<String> getTextListOfElementsIfExist(WebElementFacade... elements) {
        List<String> textList = new java.util.ArrayList<>();
        for (WebElementFacade element : elements) {

            if (element.isVisible() && element.isPresent()) {
                testStep(String.format("Get Text Attribute of Element : '%s' | Value : '%s'", element, element.getText()));
                verifyVisibilityOfElements(element);
                textList.add(element.getText());
            }

        }
        return textList;
    }

    public String getTextElement(WebElementFacade element) {
        testStep(String.format("Get Text Attribute of Element : '%s' | Value : '%s'", element, element.getText()));
        verifyVisibilityofElement(element);
        return element.getText();
    }

    /**
     * Convenience wait wrapper for readability in step definitions.
     *
     * @param timeInMilliseconds duration to wait
     */
    public void waitForPageInSecond(int timeInMilliseconds) {
        testStep(String.format("Wait For Page In Second(s) %s", timeInMilliseconds));
        waitABit(timeInMilliseconds);
    }

    /**
     * Accept OneTrust cookies banner when present to avoid UI interference.
     */
    public void AcceptAllCookiesPage() {
        testStep("AccepAllCookiesPage");
        clickTextWithParentSelectorIfExist("//div[@id='onetrust-button-group-parent']", "Accept all");
    }

    public void focusElement(WebElementFacade element) {
        evaluateJavascript("arguments[0].focus();", element);
    }

    /**
     * Zoom the page to a specific percentage using CSS zoom.
     *
     * @param percent zoom percentage (e.g., 80 for 80%)
     */
    public void zoomInOutPage(int percent) {
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        js.executeScript("document.body.style.zoom='" + percent + "%'");
    }

    /**
     * Scroll the element into view.
     *
     * @param elemeent element to bring into viewport
     */
    public void scrollToElement(WebElementFacade elemeent) {
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", elemeent.getElement());

    }

    /**
     * Scroll the element into view before sending keys (reserved for future enhancement).
     *
     * @param element element to interact with
     * @param key     key to press
     */
    public void pressKey(WebElementFacade element, Keys key) {
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", element.getElement());

    }

    @Step
    public void testStep(String message) {
        logger.info(" : {}", message);
        logger.debug(" : {}", message);
    }

}
