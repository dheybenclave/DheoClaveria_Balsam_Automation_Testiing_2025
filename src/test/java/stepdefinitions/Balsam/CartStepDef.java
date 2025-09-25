package stepdefinitions.Balsam;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import org.fluentlenium.core.annotation.Page;
import pages.Balsam.CartPage;
import stepdefinitions.CommonStepDef;

public class CartStepDef {

    @Steps
    CommonStepDef commonStepDef;
    @Steps
    ProductStepDef productStepDef;
    @Page
    CartPage cartPage;

    @Then("I Validate the Cart Icon should have {} value")
    public void validateCartItemNumber(Integer count) {
        // Validate the Cart Icon item count
        commonStepDef.testStep("Validate the Cart Icon should have " + count + " value");
        commonStepDef.verifyVisibilityofElement(cartPage.IMG_REMOVE_CART_COUNT());
    }

    @When("I Click the Remove Button")
    public void clickRemoveButton() {
        // Click on the Remove Button for the cart item
        commonStepDef.testStep("Click Remove Button");
        commonStepDef.clickElement(cartPage.BTN_REMOVE_CART_ITEM());
    }

    @Then("I Validate and Verify that the added item is removed from the Cart Page")
    public void validateAndVerifyRemovedItemfromCart() {
        // Validate that the item has been removed from the cart
        commonStepDef.testStep("Validate and Verify that removed item from the Cart Page");
        commonStepDef.verifyVisibilityofElement(cartPage.LBL_REMOVE_CART_ITEM_MESSAGE());
        commonStepDef.verifyVisibilityofElement(cartPage.LBL_REMOVE_CART_ITEM_UNDO());
    }
}
