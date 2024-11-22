package ICar.support;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class AppUtils {
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	public static int maxElementWait = 5;

	/**
	 * To wait for the specific element on the page
	 * 
	 * @param driver
	 *            : Webdriver
	 * @param element
	 *            : Webelement to wait for
	 */
	public static boolean waitForElement(AppiumDriver<MobileElement> driver, MobileElement element) {
		return waitForElement(driver,element,maxElementWait);
	}

	/**
	 * To wait for the specific element on the page
	 * 
	 * @param driver
	 *            : Webdriver
	 * @param element
	 *            : Webelement to wait for
	 */
	public static boolean waitForElement(AppiumDriver<MobileElement> driver, MobileElement element, int timeout) {
		boolean statusOfElementToBeReturned = false;
		try{
			try {
				if(element.isDisplayed()) {
					statusOfElementToBeReturned = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
				WebDriverWait wait = new WebDriverWait(driver,timeout);
				wait.until(ExpectedConditions.visibilityOf(element));
				statusOfElementToBeReturned = true;
			}
		} catch(Exception e){
			statusOfElementToBeReturned = false;
		}
		return statusOfElementToBeReturned;
	}
	
	/**
	 * Wait until the element getting disappear
	 * @param driver
	 * @param element
	 */
	public static void waitForElementInvisible(AppiumDriver<MobileElement> driver, MobileElement element) {
		FluentWait<WebDriver> wait = new WebDriverWait(driver, 60).pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(StaleElementReferenceException.class).withMessage("Page Load Timed Out");
		wait.until(new Function<WebDriver,Boolean>() { 
			@Override
			public Boolean apply(WebDriver arg0) {
				return !element.isDisplayed();
			}
		});
	}

	/**
	 * Wait for the loading spinner
	 * @param driver
	 * @param element
	 */
	public static void waitForSpinner(AppiumDriver<MobileElement> driver, MobileElement element) {
		try {
			waitForElementInvisible(driver, element);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * To get the Run Platform (Android / iOS)
	 * @return
	 */
	public static String getMobileAppRunPlatfrom(){
		String platformName=null;
		if(Boolean.valueOf(System.getProperty("runMobileApp")).equals(true) ? true : configProperty.hasProperty("runMobileApp") && configProperty.getProperty("runMobileApp").equalsIgnoreCase("true")){
			platformName = System.getProperty("platformName")!=null ? System.getProperty("platformName") : configProperty.getProperty("platformName");
		}
		return platformName;
	}

}

