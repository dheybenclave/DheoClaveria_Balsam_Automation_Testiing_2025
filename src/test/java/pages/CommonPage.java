package pages;

import net.serenitybdd.core.pages.PageComponent;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.steps.UIInteractions;

public class CommonPage extends PageComponent {

    public WebElementFacade BTN_CLOSE_DIALOG() {return $("//*[@id='bannerCloseButton' or @aria-label='Close drawer']");}
    public WebElementFacade LBL_FIELD(String name) {return $("//*[contains(text(),'"+name+"') and not(@type='application/json')]");}
    public WebElementFacade LBL_FIELD_WITH_PARENT_SELECTOR(String parentSelector, String name) {return $( parentSelector+"//*[contains(text(),'"+name+"')]");}
    public WebElementFacade PAGE_CONTROL_LABEL(String labelText) { return $(String.format("//*[contains(text(),'%s')]", labelText));
    }


}
