package stepdefinitions.Balsam;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import org.fluentlenium.core.annotation.Page;
import pages.Balsam.CartPage;
import pages.Balsam.HomePage;
import pages.Balsam.ProductPage;
import pages.CommonPage;
import stepdefinitions.CommonStepDef;
import org.junit.Assert;
import utils.Utilities;

import java.util.List;
import java.util.Map;

public class ProductStepDef {

    @Steps
    CommonStepDef commonStepDef;
    @Steps
    ProductPage productPage;
    @Page
    CartPage cartPage;

    public List<String> actualProductDetails;
    public List<List<String>> expectedProductDetails;
    String height, shape, lights, setup;

    @And("I Customize and Add to Cart the item following:")
    public void CustomizeAddToProduct(DataTable dataTable) {

        List<Map<String, String>> dataTableList = dataTable.asMaps(String.class, String.class);
        expectedProductDetails = dataTable.asLists();

        commonStepDef.testStep("Customize and Add to Cart");

        for (Map<String, String> e : dataTableList) {
            height = e.get("Height");
            shape = e.get("Shape");
            lights = e.get("Lights");
            setup = e.get("Setup");

            commonStepDef.clickElementIfExist(productPage.BTN_PRODUCT_OPTION(height));
            commonStepDef.clickElementIfExist(productPage.BTN_PRODUCT_OPTION(shape));
            commonStepDef.clickElementIfExist(productPage.BTN_PRODUCT_OPTION(lights));
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
        for (String currActualList : actualProductDetails) {

            commonStepDef.testStep(String.format("currExpectedList %s | currActualList %s", expectedProductDetails.get(1), actualProductDetails.toString()));
            expectedProductDetails.get(1).contains(currActualList);

            String _currActualList = currActualList.replaceAll("\\s*®|\\s*Trees", "");
            commonStepDef.verifyVisibilityofElement(
                    productPage.LBL_ADD_TO_CART_DIALOG(_currActualList.replaceAll("[^a-zA-Z0-9+ $,]", "")));
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

        String getCartItemDetails = removeUncessaryCharacter(commonStepDef.getTextElement(cartPage.LBL_CART_ITEM_DETAILS()));   //getText [Product,Size,Shape,Lights,Setup]
        String getPrice = removeUncessaryCharacter(commonStepDef.getTextElement(cartPage.LBL_CART_ITEM_PRICE()));               //getPrice from GUI since it separated
        getCartItemDetails = String.join(" ", getCartItemDetails, getPrice);                                            //join the price since the UI element is separated

        List<String> _expectedProductDetails = actualProductDetails; //this is from the productList during validation of the added cart item
        for (String _expected : _expectedProductDetails) {

            String _currActualList = removeUncessaryCharacter(_expected);
            commonStepDef.testStep(String.format("expectedCartDetails : %s |  actualProductDetails.ToString: %s", _expected, getCartItemDetails));
            Assert.assertTrue(getCartItemDetails.contains(_currActualList));
        }
    }

    public String removeUncessaryCharacter(String value) {
        if (value == null) return "";
        String _v = value.replaceAll("\\s*®|\\s*Trees", "");
        _v = _v.replaceAll("[^a-zA-Z0-9+ $,]", "");
        _v = _v.replace("\n", " ");
        _v = _v.replaceAll("\\s+", " "); // collapse multiple spaces
        return _v.trim();
    }
}
