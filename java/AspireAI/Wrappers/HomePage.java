package AspireAI.Wrappers;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.How;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import AspireAI.CustomFactories.AjaxElementLocatorFactory;
import AspireAI.CustomFactories.PageFactory;
import AspireAI.CustomFactories.IFindBy;
import AspireAI.CustomFactories.CustomData;

import org.zelle.support.AppActions;
import org.zelle.support.AppUtils;
import org.zelle.support.BrowserActions;
import org.zelle.support.Utils;
import org.testng.Assert;
import org.zelle.support.ElementLayer;
import org.zelle.support.Log;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;

public class HomePage extends LoadableComponent<HomePage> {

	WebDriver driver;
	String elementIdentifier;
	String elementObject;
	private boolean isPageLoaded;
	public ElementLayer elementLayer;
	private String appURL;
	HashMap<String, String> getPageObjects;
 	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();

	@IFindBy(how = How.ID, using = "environment", AI = true)
	public WebElement selEnvironment;
	
	@IFindBy(how = How.ID, using = "launchURL", AI = true)
	public WebElement fldEnvironmentUrl;
	
	@IFindBy(how = How.NAME, using = "fiId", AI = true)
	public WebElement selFiId;
	
	@IFindBy(how = How.ID, using = "pid", AI = true)
	public WebElement inpPartnerId;
	
	@IFindBy(how = How.NAME, using = "activity", AI = true)
	public WebElement selActivity;
	
	@IFindBy(how = How.NAME, using = "let", AI = true)
	public WebElement selLegalEntityIdType;
	
	@IFindBy(how = How.ID, using = "lei", AI = true)
	public WebElement inpLegalEntityId1;
	
	@IFindBy(how = How.NAME, using = "acc[0][typ]", AI = true)
	public WebElement selAccountType;
	
	@IFindBy(how = How.NAME, using = "acc[0][id]", AI = true)
	public WebElement inpAccountID;
	
	@IFindBy(how = How.NAME, using = "tkn[0][typ]", AI = true)
	public WebElement selTokenType;
	
	@IFindBy(how = How.NAME, using = "tkn[0][id]", AI = true)
	public WebElement inpTokenID;
	
	@IFindBy(how = How.CSS, using = "button.btn.btn-primary.mr-1", AI = true)
	public WebElement btnSubmit;
	
	@IFindBy(how = How.CSS, using = "button.btn.btn-secondary.ml-1", AI = true)
	public WebElement btnReset;
	
	

	public HomePage(){
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, Utils.maxElementWait);
		PageFactory.initElements(finder, this);
	}
	public HomePage(WebDriver driver, String url) {
        appURL = url;
        this.driver = driver;
        ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, Utils.maxElementWait);
        PageFactory.initElements(finder, this);
    }// GetStartedPage

	public HomePage(WebDriver driver) throws Exception{
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, Utils.maxElementWait);
		PageFactory.initElements(finder, this);
       CustomData.pageclassName = this.getClass().getName();
		elementLayer = new ElementLayer(driver);
	}

	@Override
	protected void load() {
		isPageLoaded = true;
		driver.get(appURL);
		Utils.waitForPageLoad(driver);
	}// load

	@Override
	protected void isLoaded() {
		if (!isPageLoaded) {
			Assert.fail();
		}

		if (isPageLoaded && !(Utils.waitForElement(driver, selFiId))) {
			Log.fail("Page did not open up. Site might be down.", driver);
		}
		elementLayer = new ElementLayer(driver);
	}// isLoaded
	
	/**
	 * Select from Environment Dropdown
	 * @throws Exception
	 */
	public void selectEnvFromDropdown(String envName) throws Exception {
		WebElement el=selEnvironment;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.selectDropDownValue(driver, el, envName);			
		
	}
	/**
	 * Get Environment URL
	 * @throws Exception
	 */
	public void getEnvironmentUrl() throws Exception {
		WebElement el=fldEnvironmentUrl;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.getText(driver, fldEnvironmentUrl, "fldEnvironmentUrl");			
		
	}
	

	
	
	/**
	 * Select from FI ID Dropdown
	 * @throws Exception
	 */
	public void selectFIIDfromDropdown(String FI) throws Exception {
		WebElement el=selFiId;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.selectDropDownValue(driver, el, FI);			
		
	}
	
	/**
	 * Select from Activity Dropdown
	 * @throws Exception
	 */
	public void selectfromActivityDropdown(String activity) throws Exception {
		WebElement el=selActivity;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.selectDropDownValue(driver, el, activity);			
		
	}
	
	/**
	 * Select from Legal Entity ID Type Dropdown
	 * @throws Exception
	 */
	public void selectLegalEntityIDTypefromDropdown(String legalEntityIDType) throws Exception {
		WebElement el=selLegalEntityIdType;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.selectDropDownValue(driver, el, legalEntityIDType);			
		
	}
	
	/**
	 * Select from AccountType Dropdown
	 * @throws Exception
	 */
	public void selectAccountTypefromDropdown(String accountType) throws Exception {
		WebElement el=selAccountType;
		BrowserActions.scrollToViewElement(el, driver);
		Select accountTypeDropDown= new Select(el);
		accountTypeDropDown.selectByVisibleText(accountType);
		}
	
	/**
	 * Select from Token Type Dropdown
	 * @throws Exception
	 */
	public void selectTokenTypefromDropdown(String tokenType) throws Exception {
		WebElement el=selTokenType;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.selectDropDownValue(driver, el, tokenType);			
		
	}
	
	/**
	 * Enter PartnerID 
	 * @throws Exception
	 */
	public void typeToEnterPartnerID(String PartnerID) throws Exception {
		WebElement el=selTokenType;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.typeOnTextField(el, PartnerID, driver, PartnerID);	
		
	}
	
	/**
	 * Enter Token ID 
	 * @throws Exception
	 */
	public void typeToEnterTokenID(String TokenID) throws Exception {
		WebElement el=inpTokenID;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.typeOnTextField(el, TokenID, driver, "PartnerID");	
		
	}
	
	/**
	 * Enter Legal Entity ID 
	 * @throws Exception
	 */
	public void typeToEnterLegalEntityID(String LegalEntityID) throws Exception {
		WebElement el=inpLegalEntityId1;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.typeOnTextField(el, LegalEntityID, driver, "Legal Entity ID");	
		
	}
	
	/**
	 * Enter Account Number 
	 * @throws Exception
	 */
	public void typeToEnterAccountNumber(String  accountNumber) throws Exception {
		WebElement el=inpAccountID;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.typeOnTextField(el, accountNumber, driver, "PartnerID");	
		
	}
	
	/**
	 * Click on Reset button  
	 * @throws Exception
	 */
	public void clickResetButton() throws Exception {
		WebElement el=btnReset;
		BrowserActions.scrollToViewElement(el, driver);
		BrowserActions.clickOnElement(el, driver, "btnReset");
		
	}
	
	
	
}
