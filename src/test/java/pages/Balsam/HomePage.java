package pages.Balsam;

import net.serenitybdd.core.pages.WebElementFacade;
import pages.CommonPage;

public class HomePage extends CommonPage {

    static String PARENT_HEADER = "//header[contains(@class,'fixed-marquee-header')]";
    static String PARENT_CART_GRID = "//div[@data-testid='results-grid']";

    public WebElementFacade TXT_SEARCH() {return $(PARENT_HEADER + "//input[@id='constructor-search-input']");}
    public WebElementFacade LBL_CART_ITEM(String cartItemIndex) {return $( "("+ PARENT_CART_GRID + "//div[contains(@class,'listingContainer_product-listing-item-wrapper')])["+cartItemIndex+"]");}

}
