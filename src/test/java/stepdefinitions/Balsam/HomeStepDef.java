package stepdefinitions.Balsam;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import org.fluentlenium.core.annotation.Page;
import pages.Balsam.HomePage;
import pages.Balsam.ProductPage;
import pages.CommonPage;
import stepdefinitions.CommonStepDef;
import utils.ExcelReader;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import static java.lang.String.format;

public class HomeStepDef {

    @Steps
    CommonStepDef commonStepDef;
    @Steps
    ProductStepDef productStepDef;
    @Page
    HomePage homePage;
    @Page
    ProductPage productPage;
    @Page
    CommonPage commonPage;

    @Given("{} Navigate to {} using {}")
    public void navigateToPage(String actor, String page, String role) {
        // Navigate to the specified page using the given role
        commonStepDef.testStep(format("NavigateToPage : Page '%s' | Role '%s'", page, role));
        commonStepDef.navigatePage(page);
        commonStepDef.zoomInOutPage(80);
        commonStepDef.waitForPageInSecond(2000);
        commonStepDef.clickElementIfExist(commonPage.BTN_CLOSE_DIALOG());
        commonStepDef.waitForPageInSecond(1000);
    }

    @When("I Search the {}")
    public void searchProducts(String searchValue) {
        // Search for the specified product
        commonStepDef.testStep("Search Products in Page");
        commonStepDef.enterText(homePage.TXT_SEARCH(), searchValue, 2000);
        commonStepDef.waitForPageInSecond(2000);
        commonStepDef.verifyTextInPage("Search result for:");
        commonStepDef.waitForPageInSecond(2000);
    }

    @When("I Select the item {} from the search result")
    public void selectProductItemsFromSearchResults(String cartItemIndex) {
        // Select the specified item from the search results
        commonStepDef.testStep("select Product Items From Search Results");
        commonStepDef.clickElement(homePage.LBL_CART_ITEM(cartItemIndex));
        commonStepDef.waitForPageInSecond(2000);
    }

    @Then("I Test the Excel Reader for using {} fileName")
    public void testExternalFile(String fileName) {

        ExcelReader reader = new ExcelReader();
        try {
            String exeFileName = fileName;
            String exeSheetName = "UserList";
            String role = "Test_Engineer";

            String filePath = System.getProperty("user.dir") + "/src/test/resources/testData/" + exeFileName + ".xlsx";
            Map<String, String> credentials = reader.getUsernameAndPasswordByRole(filePath, exeSheetName, role);
            String _username = credentials.get("username");
            String _password = credentials.get("password");

            commonStepDef.testStep(String.format("Get Username and Password using Role in '%s' - file  | %s - workSheet | %s - role", exeFileName, exeSheetName, role));
            commonStepDef.testStep(String.format("Get Username : '%s'", _username));
            commonStepDef.testStep(String.format("Get Password : ", _password));

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
