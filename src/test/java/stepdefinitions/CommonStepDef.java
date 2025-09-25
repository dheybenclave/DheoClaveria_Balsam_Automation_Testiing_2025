package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
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

import java.security.Key;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CommonStepDef extends PageComponent {
    public static Logger logger = LoggerFactory.getLogger(CommonStepDef.class);
    private int CLIENT_CODE_STACK_INDEX;
    public CommonPage commonPage;

    public String mainwindow;
    public Set<String> s1;
    public Iterator<String> i1;

    // --- Navigation Methods ---
    public void navigatePage(String page) {
        this.thePage(page);
        Ensure.thatTheCurrentPage().currentUrl();
    }

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

    public void generatedSwitchHandler() {
        testStep("Generated Switch Handler");
        mainwindow = this.getDriver().getWindowHandle();
        s1 = this.getDriver().getWindowHandles();
        i1 = s1.iterator();
    }

    public void switchToMainWindow(String windowHandle) {
        testStep("Switch to Main Window");
        this.getDriver().switchTo().window(windowHandle);
    }

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

    public void NavigateToUIPage(WebElementFacade element) {
        testStep(String.format("Navigate to : '%s'", element));
        verifyVisibilityofElement(element);
        element.click();
    }

    // --- Verification Methods ---
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

    public void verifyTextInPage(String... textList) {
        for (String currText : textList) {
            testStep(String.format("verify the text in the page :%s", (Object) textList));
            verifyVisibilityofElement(commonPage.LBL_FIELD(currText));
        }
    }

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

    public void clickElementIfExist(WebElementFacade element) {
        testStep(String.format("Click for Element if Exist '%s'", element));
        if (element.isVisible() && element.isPresent()) {
            verifyVisibilityofElement(element);
            waitABit(2000);
            // clickOn(element);
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

    public void CollapaseMenu(WebElementFacade element, String identifierValue) {
        testStep(String.format("Collapse Menu : '%s'", element));
        identifierValue = identifierValue.length() > 0 ? identifierValue : "collapsed";
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
    public void enterText(WebElementFacade element, String value, int waitForMilliSec) {
        testStep(String.format("Enter Text '%s' with Value %s", element, value));
        verifyVisibilityofElement(element);
        element.typeAndEnter(value);
        waitABit(waitForMilliSec);
    }

    public void setAttibute(WebElementFacade element, String attName, String attValue, int waitForMilliSec) {
        testStep(String.format("Set Attribute  '%s' | Name : '%s' | Value : '%s'", element, attName, attValue));
        element.shouldBePresent();
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attName, attValue);
        waitABit(waitForMilliSec);
    }

    public void setInputValue(WebElementFacade element, String value, int waitForMilliSec) {
        testStep(String.format("Set Value '%s' with Value %s", element, value));
        this.setAttibute(element, "value", value, waitForMilliSec);
        waitABit(waitForMilliSec);
    }

    // --- Utility Methods ---

    public List<String> getTextListOfElements(WebElementFacade... elements) {
        List<String> textList = new java.util.ArrayList<>();
        for (WebElementFacade element : elements) {
            testStep(String.format("Get Text Attribute of Element : '%s' | Value : '%s'", element, element.getText()));
            verifyVisibilityofElement(element);
            textList.add(element.getText());
        }
        return textList;
    }

    public List<String> getTextListOfElementsIfExist(WebElementFacade... elements) {
        List<String> textList = new java.util.ArrayList<>();
        for (WebElementFacade element : elements) {

            if (element.isVisible() && element.isPresent()) {
                testStep(String.format("Get Text Attribute of Element : '%s' | Value : '%s'", element,element.getText()));
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

    public void waitForPageInSecond(int timeInMilliseconds) {
        testStep(String.format("Wait For Page In Second(s) %s", timeInMilliseconds));
        waitABit(timeInMilliseconds);
    }

    public void AcceptAllCookiesPage() {
        testStep("AccepAllCookiesPage");
        clickTextWithParentSelectorIfExist("//div[@id='onetrust-button-group-parent']", "Accept all");
    }

    public void focusElement(WebElementFacade element) {
        evaluateJavascript("arguments[0].focus();", element);
    }

    public void zoomInOutPage(int percent) {
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        js.executeScript("document.body.style.zoom='" + percent + "%'");
    }

    public void scrollToElement(WebElementFacade elemeent) {
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", elemeent.getElement());

    }

    public void pressKey(WebElementFacade elemeent, Keys key) {
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", elemeent.getElement());

    }

    public void testStep(String message) {
        logger.info("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), message);
    }

}
