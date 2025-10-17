package stepdefinitions.Balsam;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import org.fluentlenium.core.annotation.*;
import pages.Balsam.CartPage;
import pages.Balsam.ProductPage;
import pages.CommonPage;
import stepdefinitions.CommonStepDef;
import org.junit.Assert;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import java.util.List;
import java.util.Map;

public class ProductStepDef {

    @Steps
    CommonStepDef commonStepDef; //
    @Steps
    ProductPage productPage; // selctor
    @Page
    CartPage cartPage;
    @Page
    CommonPage commonPage;

    public List<String> actualProductDetails;
    String height, shape, lights, setup;

    @And("I Customize and Add to Cart the item using the following:")
    public void CustomizeAddToProduct(DataTable dataTable) {

        List<Map<String, String>> dataTableList = dataTable.asMaps(String.class, String.class);

        commonStepDef.testStep("Customize and Add to Cart");

        commonStepDef.clickElementIfExist(commonPage.BTN_CLOSE_DIALOG());

        for (Map<String, String> e : dataTableList) {
            height = e.get("Height");
            shape = e.get("Shape");
            lights = e.get("Lights");
            setup = e.get("Setup");

            commonStepDef.clickElementIfExist(productPage.BTN_PRODUCT_OPTION(height));
            commonStepDef.clickElementIfExist(productPage.BTN_PRODUCT_OPTION(shape));
            commonStepDef.clickElementIfExist(productPage.BTN_PRODUCT_OPTION(lights));
            commonStepDef.waitForPageInSecond(2000);
            commonStepDef.clickTextIfExist("No Thanks");
            commonStepDef.clickElementIfExist(productPage.LBL_TOTAL());
            commonStepDef.clickElementIfExist(productPage.BTN_PRODUCT_OPTION(setup));

        }

        actualProductDetails = commonStepDef.getTextListOfElementsIfExist(
                productPage.LBL_PRODUCT_NAME(),
                productPage.LBL_HEIGHT_VALUE(),
                productPage.LBL_SHAPE_VALUE(),
                productPage.LBL_LIGHTS_VALUE(),
                productPage.LBL_SETUP_VALUE(),
                productPage.LBL_TOTAL());

        commonStepDef.clickElement(productPage.BTN_ADD_TO_CART());
        commonStepDef.waitForPageInSecond(1500);
    }

    @Then("I Validate And Verify the added item from Dialog")
    public void validateAndVerifyAddedProductDetails() {

        commonStepDef.testStep("Validate and Verify Added Product Details");
        commonStepDef.clickTextIfExist("No Thanks");

        for (String currActualList : actualProductDetails) {
            //it will verify the actual details from Product Details Section is equal to the dialog
            commonStepDef.verifyVisibilityofElement(productPage.LBL_ADD_TO_CART_DIALOG(removeUncessaryCharacter(currActualList)));
        }

    }

    @When("I Click the View Cart Button")
    public void clickViewCartButton() {
        commonStepDef.testStep("Click the View Cart Button");
        commonStepDef.clickElement(productPage.BTN_VIEW_CART());
        commonStepDef.waitForPageInSecond(2000);
    }

    @Then("I Validate And Verify the added item from Cart Page")
    public void validateAndverifyAddedItem() {
        commonStepDef.testStep("Validate And Verify the added item from Cart Page");
        commonStepDef.clickTextIfExist("No Thanks");

        String getCartItemDetails = removeUncessaryCharacter(commonStepDef.getTextElement(cartPage.LBL_CART_ITEM_DETAILS()));   //getText [Product,Size,Shape,Lights,Setup]
        String getPrice = removeUncessaryCharacter(commonStepDef.getTextElement(cartPage.LBL_CART_ITEM_PRICE()));               //getPrice from GUI since it separated
        getCartItemDetails = String.format("%s %s", getCartItemDetails, getPrice);                                              //join the price since the UI element is separated

        List<String> _expectedProductDetails = actualProductDetails; //this is from the productList during validation of the added cart item
        for (String _expected : _expectedProductDetails) {

            String _currActualList = removeUncessaryCharacter(_expected);
            commonStepDef.testStep(String.format("expectedCartDetails : %s |  actualProductDetails : %s", _expected, getCartItemDetails));
            Assert.assertTrue(getCartItemDetails.contains(_currActualList));
            assertThat(getCartItemDetails)
                    .as("Expecting to find item '%s' in the list.", _currActualList)
                    .contains(_currActualList);
        }
    }

    public String removeUncessaryCharacter(String value) {
        if (value == null) return "";
        return value
                .replaceAll("\\s*®|\\s*Trees|[^a-zA-Z0-9+ $,]", "")
                .replace('\n', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }

    public Boolean isPalindrome(String str) {
        String normalizedStr = str.toLowerCase();
        String reversedStr = new StringBuilder(normalizedStr).reverse().toString();
        return normalizedStr.equals(reversedStr);
    }

    public void reverse() {
        String original = "Hello, World!";
        StringBuilder reversed = new StringBuilder(original);
        reversed.reverse();
        commonStepDef.testStep(String.format("Original: {%s} | Reverse : {%s} ", original, reversed));
    }
}
