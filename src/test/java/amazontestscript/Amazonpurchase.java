package amazontestscript;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ICar.support.DataProviderUtils;
import ICar.support.EnvironmentPropertiesReader;
import ICar.support.Log;
import ICar.support.TestDataExtractor;
import ICar.support.WebDriverFactory;

public class Amazonpurchase {
	
	
	EnvironmentPropertiesReader environmentPropertiesReader;

	String webSite, SalseForcewebSite, environment;
	private String workbookName;

	String packageName = this.getClass().getPackage().getName();

	// String workbookName = "testdata\\data\\" + packageName + ".xls";

	String className = this.getClass().getSimpleName();

	private String sheetName = className;

	EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	public static int maxElementWait = 3;

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {

		SalseForcewebSite = (System.getProperty("AmazonwebSite") != null ? System.getProperty("AmazonwebSite")
				: context.getCurrentXmlTest().getParameter("AmazonwebSite"));
		environment = (System.getProperty("environment") != null ? System.getProperty("environment")
				: context.getCurrentXmlTest().getParameter("environment"));
		if (environment.equalsIgnoreCase("stage")) {
			workbookName = "testdata\\data_stage\\" + packageName + ".xls";
		}

	}

	@Test(enabled = true, description = "Verify the Home page", dataProviderClass = DataProviderUtils.class, dataProvider = "parallelTestDataProvider")
	public void TC01(String browser) throws Exception {

// getting the input values from excel sheet to hash map
		HashMap<String, String> testData = TestDataExtractor.initTestData(workbookName, sheetName);

// Get the web driver instance
		final WebDriver driver = WebDriverFactory.get();

// inputing the description to extended report
		Log.testCaseInfo(testData);

		try {
			
			
			
			Log.testCaseResult();
		} catch (Exception e) {
			Log.exception(e, driver);
		} // catch
		finally {
			Log.endTestCase();
			driver.quit();
		} // finally

	}// TC01

}
