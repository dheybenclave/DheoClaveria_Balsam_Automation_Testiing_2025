package stepdefinitions.Balsam;

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
        //Write a program that takes an input array and prints the frequency of the elements in an array.
        //I/P -> a[12] = {1, 2, 3, 1, 2, 3, 4, 5, 6, 6, 7, 7};
        // O/P -> {1=2, 2=2, 3=2, 4=1, 5=1, 6=2, 7=2}

        int[] a = {1, 2, 3, 1, 2, 3, 4, 5, 6, 6, 7, 7};
        Map<Integer, Integer> frequencyMap = new java.util.HashMap<>();

        for (int element : a) {
            int counter = frequencyMap.getOrDefault(element, 0);
            frequencyMap.put(element, counter + 1);
        }

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        
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

    @Then("I Test the Excel Reader for using {} fileName in {} sheetName using {} role")
    public void testExternalFile(String fileName, String sheetName, String role) {

        ExcelReader reader = new ExcelReader();
        try {

            String filePath = System.getProperty("user.dir") + "/src/test/resources/testData/" + fileName + ".xlsx";
            Map<String, String> credentials = reader.getUsernameAndPasswordByRole(filePath, sheetName, role);
            String _username = credentials.get("username");
            String _password = credentials.get("password");

            commonStepDef.testStep(String.format("Get Username and Password using Role in '%s' - file  | %s - workSheet | %s - role", fileName, sheetName, role));
            commonStepDef.testStep(String.format("Get Username : '%s'", _username));
            commonStepDef.testStep(String.format("Get Password : '%s'", _password));

        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }

    }
}
