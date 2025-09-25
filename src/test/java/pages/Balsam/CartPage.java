package pages.Balsam;

import net.serenitybdd.core.pages.WebElementFacade;
import pages.CommonPage;

public class CartPage extends CommonPage {

    static String PARENT_CART_ITEM_LIST = "//div[contains(@class,'product-detail-list')]";
    public WebElementFacade LBL_CART_ITEM_PRICE() {return $(PARENT_CART_ITEM_LIST + "//*[contains(@class,'cartProductDetailItem_new_price')]");}
    public WebElementFacade LBL_CART_ITEM_DETAILS() {return $(PARENT_CART_ITEM_LIST + "//*[contains(@class,'cartProductDetailItem_product_details_container')]");}
    public WebElementFacade BTN_REMOVE_CART_ITEM() {return $(PARENT_CART_ITEM_LIST + "//button[contains(@class,'cartProductDetailItem_delete')]");}
    public WebElementFacade LBL_REMOVE_CART_ITEM_MESSAGE() {return $(PARENT_CART_ITEM_LIST + "//div[contains(@class,'cartProductDetailItem_product-name-wrapper')]");}
    public WebElementFacade LBL_REMOVE_CART_ITEM_UNDO() {return $(PARENT_CART_ITEM_LIST + "//a[contains(@class, 'cartProductDetailItem_undo')]");}
    public WebElementFacade IMG_REMOVE_CART_COUNT() {return $(  "//li[contains(@class,'headerIcons_cart-icon')]//span");}


}
