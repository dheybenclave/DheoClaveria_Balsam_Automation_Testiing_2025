package pages.Balsam;

import net.serenitybdd.core.pages.WebElementFacade;
import pages.CommonPage;

public class ProductPage extends CommonPage {

    static String PARENT_PRODUCT = "//div[contains(@class,'productDetailContainer_matrix')]";
    static String PARENT_ADD_TO_CART_DIALOG = "//div[contains(@class,'add-to-cart-modal')]//div[contains(@class,'modal-content')]";

    public WebElementFacade LBL_PRODUCT_NAME() {return $(PARENT_PRODUCT + "//div[contains(@class,'productDetailNameRating_product')]/h1");}
    public WebElementFacade LBL_HEIGHT_VALUE() {return $(PARENT_PRODUCT + "//div[@id='product_filter_height']/following-sibling::div[contains(@class,'productDetailFilter_selected-value')]");}
    public WebElementFacade LBL_SHAPE_VALUE() {return $(PARENT_PRODUCT + "//div[@id='product_filter_shape']/following-sibling::div[contains(@class,'productDetailFilter_selected-value')]");}
    public WebElementFacade LBL_LIGHTS_VALUE() {return $(PARENT_PRODUCT + "//div[@id='product_filter_lights']/following-sibling::div[contains(@class,'productDetailFilter_selected-value')]");}
    public WebElementFacade LBL_SETUP_VALUE() {return $(PARENT_PRODUCT + "//div[@id='product_filter_setup']/following-sibling::div[contains(@class,'productDetailFilter_selected-value')]");}
    public WebElementFacade LBL_TOTAL() {return $(PARENT_PRODUCT + "//div[contains(@class,'product-detail-buybox')]//span[@class='product-price']");}
    public WebElementFacade BTN_ADD_TO_CART() {return $(PARENT_PRODUCT + "//button[@type='button' and text()='Add to Cart']");}
    public WebElementFacade BTN_PRODUCT_OPTION(String optionValue) {return $(PARENT_PRODUCT + "//div[contains(@class,'renderSelectBoxFilterItem_product-filter-item')]/following::span[text()='"+optionValue+"']/ancestor::span[last()]");}

    // ADD TO CART DIALOG
    public WebElementFacade LBL_ADD_TO_CART_DIALOG(String getLabel) {return $(PARENT_ADD_TO_CART_DIALOG + "//*[contains(text(),'"+getLabel+"')]");}
    public WebElementFacade BTN_VIEW_CART() {return $(PARENT_ADD_TO_CART_DIALOG + "//button[contains(@class, 'productAddToCartModal_btn-viewcart')]");}


}
