package ICar.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;


/**
 * Wrapper for Selenium WebDriver actions which will be performed on browser
 * 
 * Wrappers are provided with exception handling which throws Skip Exception on
 * occurrence of NoSuchElementException
 * 
 */
public class AppActions {

	/**
	 * Type the text in the text field
	 * 
	 * @param txt
	 *            : WebElement of the Text Field
	 * @param txtToType
	 *            : Text to type [String]
	 * @param driver
	 *            : WebDriver Instances
	 * @param elementDescription
	 *            : Description about the WebElement
	 * @throws Exception
	 */
	public static void typeOnTextField(MobileElement txt, String txtToType,
			AppiumDriver<MobileElement> driver, String elementDescription) throws Exception {

		if (!AppUtils.waitForElement(driver, txt))
			throw new Exception(elementDescription
					+ " field not found in page!!");

		try {
			txt.clear();
			txt.click();
			txt.sendKeys(txtToType);
		} catch (NoSuchElementException e) {
			throw new Exception(elementDescription
					+ " field not found in page!!");
		}
		hideKeyboard(driver);
	}// typeOnTextField


	/**
	 * To click on the element
	 * @param btn
	 * @param driver
	 * @param elementDescription
	 * @throws Exception
	 */
	public static void clickOnElement(MobileElement btn, AppiumDriver<MobileElement> driver,
			String elementDescription) throws Exception {
		if (!AppUtils.waitForElement(driver, btn))
			throw new Exception(elementDescription + " not found in page!!");

		try {
			btn.click();
		} catch (NoSuchElementException e) {
			throw new Exception(elementDescription + " not found in page!!");
		}

	}// clickOnElement

	/**
	 * To tap on the middle of the element
	 * @param element
	 * @param driver
	 * @param Description
	 * @throws Exception
	 */
	public static void tapOnElement(MobileElement element, AppiumDriver<MobileElement> driver, String Description) throws Exception{
		if(!AppUtils.waitForElement(driver, element))
			throw new Exception(Description + " not found in page!!");
		try {
			int x = element.getLocation().getX() + (element.getSize().width/2);
			int y = element.getLocation().getY()+(element.getSize().height/2);
			System.out.println(x);
			System.out.println(y);

			TouchAction touchAction = new TouchAction(driver);
			
			touchAction
			.tap(TapOptions.tapOptions()
			.withElement(ElementOption.element(element)))
			.release()
			.perform();


		} catch (NoSuchElementException e) {
			throw new Exception(Description + " not found in page!!");
		}
	}//tapOnElement

	/**
	 * To tap on the middle of the element by Touch Actions with coordinates
	 * @param element
	 * @param driver
	 * @param Description
	 * @throws Exception
	 */
	public static void tapOnElementByTouchActionWithCoordinates(MobileElement element, AppiumDriver<MobileElement> driver, String Description) throws Exception{
		if(!AppUtils.waitForElement(driver, element))
			throw new Exception(Description + " not found in page!!");
		try {
			int x = element.getLocation().getX() + (element.getSize().width/2);
			int y = element.getLocation().getY()+(element.getSize().height/2);
			TouchAction touchAction = new TouchAction(driver);
			
			touchAction
			.tap(PointOption.point(x, y))
			.perform();


		} catch (NoSuchElementException e) {
			throw new Exception(Description + " not found in page!!");
		}
	}//tapOnElementByTouchActionWithCoordinates
	/**
	 * To long press an element
	 * @param element
	 * @param driver
	 * @param duration
	 * @param Description
	 * @throws Exception
	 */
	public static void longPressElement(MobileElement element, AppiumDriver<MobileElement> driver, int duration, String Description) throws Exception {

		if(!AppUtils.waitForElement(driver, element))
			throw new Exception(Description + " not found in page!!");
		try {
			TouchAction action = new TouchAction(driver);
			int x = element.getLocation().getX() + (element.getSize().width/2);
			int y = element.getLocation().getY()+(element.getSize().height/2);
			action
			.longPress(PointOption.point(x,y))
			.release()
			.perform();
			
		} catch (NoSuchElementException e) {
			throw new Exception(Description + " not found in page!!");
		}
	}

	/**
	 * To swipe up the element for visible
	 * @param driver
	 * @param element
	 */
	public static void swipeUpElementVisible(AppiumDriver<MobileElement> driver, MobileElement element){
		hideKeyboard(driver);
		try {
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOf(element));
			boolean status = true;
			if(status){
				Log.event("element is displayed");
				return;
			}
		} catch (Exception e) {
			again: for(int i=0;i<=5;i++){
				if(AppUtils.getMobileAppRunPlatfrom().equalsIgnoreCase("ios")) {
					hideKeyboard(driver);
					//driver.swipe(100,500,100,100,50);
					new TouchAction(driver).press(PointOption.point(100,500)).waitAction().moveTo(PointOption.point(100, 100)).release().perform();
				}
				if(AppUtils.getMobileAppRunPlatfrom().equalsIgnoreCase("android"))
				{
					Dimension size = driver.manage().window().getSize();
					int startX = (int)(size.width * 0.10);
					int endX = (int)(size.width * 0.80);
					int startY = size.height/2;
					new TouchAction(driver) .press(PointOption.point(startX,startY)).waitAction().moveTo(PointOption.point(endX, startY)).release().perform();
				}
				try {
					if(element.isDisplayed()){
						Log.event("element is displayed");
						break;
					}

				}
				catch (Exception e2) {
					Log.event("Element not available for this swipe");
					continue again;
				}
			} 
		}
	}//swipeUpElementVisible

	public static void swipeLittleDownOnFullPage(AppiumDriver<MobileElement> driver, MobileElement element){
		hideKeyboard(driver);
		try {
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOf(element));
			boolean status = true;
			if(status){
				Log.event("element is displayed");
				return;
			}
		} catch (Exception e) {
			again: for(int i=0;i<=5;i++){
				if(AppUtils.getMobileAppRunPlatfrom().equalsIgnoreCase("ios")) {
					hideKeyboard(driver);
					new TouchAction(driver) .press(PointOption.point(100,500)).waitAction().moveTo(PointOption.point(100, 100)).release().perform();
				}
				if(AppUtils.getMobileAppRunPlatfrom().equalsIgnoreCase("android"))
				{
					Dimension size = driver.manage().window().getSize();
					int starty = (int) (size.height * 0.80);
					int endy = (int) (size.height * 0.60);
					int startx = size.width / 2;
					new TouchAction(driver) .press(PointOption.point(startx,starty)).waitAction().moveTo(PointOption.point(startx, endy)).release().perform();
				}
				try {
					if(element.isDisplayed()){
						Log.event("element is displayed");
						break;
					}

				}
				catch (Exception e2) {
					Log.event("Element not available for this swipe");
					continue again;
				}
			} 
		}
	}//swipeUpElementVisible

	public static void scrollLittleDown(AppiumDriver<MobileElement> driver) {
		try {
			Dimension size = driver.manage().window().getSize();
			int starty = (int) (size.height * 0.80);
			int endy = (int) (size.height * 0.60);
			int startx = size.width / 2;
			new TouchAction(driver) .press(PointOption.point(startx,starty)).waitAction().moveTo(PointOption.point(startx, endy)).release().perform();

		} catch (Exception e) {

			Log.fail("Unable to scroll Screen");

		}

	}



	/**
	 * Swipe up the element to the top
	 * @param driver
	 * @param element
	 */
	public static void swipeUpElementToTop(AppiumDriver<MobileElement> driver, WebElement element){

		hideKeyboard(driver);
		try {
			int y = element.getLocation().getY();
			new TouchAction(driver) .press(PointOption.point(100,y)).waitAction().moveTo(PointOption.point(150, 150)).release().perform();
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			// TODO: handle exception
			swipeDownLittle(driver);
		}
	}//swipeUpElementToTop


	/**
	 * To select the native dropdown value
	 * @param driver
	 * @param element
	 * @param value
	 */
	public static void selectDropdownValueFromNativePicker(AppiumDriver<MobileElement> driver, String entry, String value){
		try {
			if(entry.contains("state")) {
				driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Select']")).click();;
			}else if(entry.contains("month")) {
				driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Select Month']")).click();
			}else if(entry.contains("year")) {
				driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Select Year']")).click();;
			}
			//                   clickOnElement(element, driver, "drodown");
			AppActions.nap(5);
			driver.findElements(By.xpath("//XCUIElementTypePickerWheel")).get(0).sendKeys(value);
			AppActions.nap(2);
			hideKeyboard(driver);
			AppActions.nap(5);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}//selectDropdownValueFromNativePicker

	/**
	 * To select Month and Year From Native picker
	 * @param element
	 * @param driver
	 * @param Description
	 * @throws Exception
	 */
	public static void selectMonthYearFromNativePicker(AppiumDriver<MobileElement> driver, String entry, String value){
		try {
			if(entry.contains("state")) {
				//driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Select']")).click();;
			}else if(entry.contains("month")) {
				driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Select Month']")).click();
			}else if(entry.contains("year")) {
				driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Select Year']")).click();;
			}
			//                   clickOnElement(element, driver, "drodown");
			AppActions.nap(5);
			driver.findElements(By.xpath("//XCUIElementTypePickerWheel")).get(0).sendKeys(value);
			AppActions.nap(2);
			hideKeyboard(driver);
			AppActions.nap(5);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}//selectDropdownValueFromNativePicker


	/**
	 * To select the normal dropdown value
	 * @param driver
	 * @param element
	 * @param value
	 * @throws Exception
	 */
	public static void selectDropdownValue(AppiumDriver<MobileElement> driver, MobileElement element, String value) throws Exception{
		int loop=1;
		while (!AppUtils.waitForElement(driver, (MobileElement)driver.findElement(By.id(value)))){
			scrollInsideTheElement(driver,element);
			if(loop==15){
				break;
			}
			loop++;

		}
		clickOnElement((MobileElement)driver.findElement(By.id(value)), driver, value);

	}


	/**
	 * To scroll inside the element
	 * @param driver
	 * @param element
	 */
	public static void scrollInsideTheElement(AppiumDriver<MobileElement> driver, MobileElement element){
		int x1 = element.getLocation().getX() + (element.getSize().width/2);
		int y1 = element.getLocation().getY();
		int x2 = x1;
		int y2 = (int) (element.getLocation().getY() + (element.getSize().height*0.75));
		//driver.swipe(x2, y2, x1, y1, 50);
		new TouchAction(driver).press(PointOption.point(x2,y2)).waitAction().moveTo(PointOption.point(x1, y1)).release().perform();
	}


	/**
	 * To swipe the display to little down
	 * @param driver
	 */
	public static void swipeDownLittle(AppiumDriver<MobileElement> driver){
		Dimension size=driver.manage().window().getSize();
		int y_start=(int)(size.height*0.45);
		int y_end=(int)(size.height*0.50);
		int x=size.width/2;
		//driver.swipe(x,y_start,x,y_end,50);
		new TouchAction(driver).press(PointOption.point(x,y_start)).waitAction().moveTo(PointOption.point(x, y_end)).release().perform();
	}//swipeDownLittle


	/**
	 * Scroll to view the element on top
	 * @param driver
	 * @param element
	 */

	public static void swipeUpByTouchAction(AppiumDriver<MobileElement> driver, MobileElement element){
		swipeUpElementVisible(driver, element);
		Dimension displaySize=driver.manage().window().getSize();
		int x1 = (int) (displaySize.getWidth()*0.5);
		int y1 = (int) (displaySize.getHeight()*0.1);
		int x2 = x1;
		int y2 = (element.getLocation().getY() + (element.getSize().width/2))-5;
		TouchAction action = new TouchAction(driver);
		//action.press(element, x2, y2).waitAction(100).moveTo(x1, y1).release().perform();
		action.press(PointOption.point(x2, y2)).waitAction().moveTo(PointOption.point(x1, y1)).release().perform();
		hideKeyboard(driver);
	}//swipeUpByTouchAction


	/**
	 * To get the location of the element
	 * @param driver
	 * @param element
	 * @return
	 */
	public static LinkedHashMap<String, String> getElementLocation(AppiumDriver<MobileElement> driver, MobileElement element){
		LinkedHashMap<String, String> location = new LinkedHashMap<>();

		int width = element.getSize().height;
		int height = element.getSize().width;
		int x = element.getLocation().getX();
		int y = element.getLocation().getY();
		location.put("width", ""+width);
		location.put("height", ""+height);
		location.put("x", ""+x);
		location.put("y", ""+y);

		return location;
	}//getElementLocation


	/**
	 *  To get the size (count) of the element
	 * @param xpath
	 * @param driver
	 * @return size
	 */
	public static int countElements(String xpath, AppiumDriver<MobileElement> driver) {
		return driver.findElements(By.xpath(xpath)).size();
	}//countElements

	/**
	 * Wrapper to get a text from the provided MobileElement
	 * 
	 * @param driver
	 *            : Appium Driver Instance
	 * @param fromWhichTxtShldExtract
	 *            : MobileElement from which text to be extract in String format
	 * @return: String - text from web element
	 * @param elementDescription
	 *            : Description about the MobileElement
	 * @throws Exception
	 */
	public static String getText(AppiumDriver<MobileElement> driver,
			MobileElement fromWhichTxtShldExtract, String elementDescription)
					throws Exception {
		String textFromHTMLAttribute = "";
		try {
			textFromHTMLAttribute = fromWhichTxtShldExtract.getText().trim();
			if (textFromHTMLAttribute.isEmpty())
				textFromHTMLAttribute = fromWhichTxtShldExtract
				.getAttribute("textContent");
		} catch (NoSuchElementException e) {
			throw new Exception(elementDescription + " not found in page!!");
		}
		return textFromHTMLAttribute;
	}// getText

	/**
	 * Wrapper to get a text from the provided MobileElement's Attribute
	 * 
	 * @param driver
	 *            : AppiumDriver Instance
	 * @param fromWhichTxtShldExtract
	 *            : MobileElement from which text to be extract in String format
	 * @param attributeName
	 *            : Attribute Name from which text should be extracted like
	 *            "style, class, value,..."
	 * @return: String - text from web element
	 * @param elementDescription
	 *            : Description about the MobileElement
	 * @throws Exception
	 */
	public static String getTextFromAttribute(AppiumDriver<MobileElement> driver,
			MobileElement fromWhichTxtShldExtract, String attributeName,
			String elementDescription) throws Exception {

		String textFromHTMLAttribute = "";

		try {
			textFromHTMLAttribute = fromWhichTxtShldExtract.getAttribute(
					attributeName).trim();
		} catch (NoSuchElementException e) {
			throw new Exception(elementDescription + " not found in page!!");
		}
		return textFromHTMLAttribute;
	}// getTextFromAttribute


	/**
	 * To wait for the given time
	 * @param time
	 */
	public static void nap(long time) {
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
		}
	}//nap

	/**
	 * To check whether locator string is xpath
	 * To convert from string to mobile element
	 * @param driver
	 * @param locator
	 * @return element
	 */
	public static MobileElement checkLocator(AppiumDriver<MobileElement> driver, String locator) {
		WebElement element = null;
		hideKeyboard(driver);
		if (locator.startsWith("//")) {
			element = (new WebDriverWait(driver, 10).pollingEvery(500,
					TimeUnit.MILLISECONDS).ignoring(
							NoSuchElementException.class,
							StaleElementReferenceException.class)
					.withMessage("Couldn't find " + locator))
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(locator)));
		} else {
			element = (new WebDriverWait(driver, 10).pollingEvery(500,
					TimeUnit.MILLISECONDS).ignoring(
							NoSuchElementException.class,
							StaleElementReferenceException.class)
					.withMessage("Couldn't find " + locator))
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.cssSelector(locator)));
		}
		return (MobileElement) element;
	}//checkLocator

	/**
	 * To hide the keyboard from the screen
	 * @param driver
	 */
	public static void hideKeyboard(AppiumDriver<MobileElement> driver){
		if(AppUtils.getMobileAppRunPlatfrom().equalsIgnoreCase("ios")){
			try {
				MobileElement btnDone = driver.findElement(By.id("Done"));
				if(btnDone.isDisplayed()){
					AppActions.clickOnElement(btnDone, driver, "Keyboard done button");
					return;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else{
			try {
				driver.hideKeyboard();
				return;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return;
	}//hideKeyboard

	/**
	 * To swipe the screen by JavaScript
	 * @param driver
	 * @param element
	 */
	public static void swipeByJavaScript(AppiumDriver<MobileElement> driver, MobileElement element){
		swipeUpElementVisible(driver, element);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Object> scrollObject = new HashMap<>();
		scrollObject.put("startX", 0.95);
		scrollObject.put("startY", 0.5);
		scrollObject.put("endX", 0.05);
		scrollObject.put("endY", 0.5);         
		scrollObject.put("direction", "up");
		scrollObject.put("duration", 1.8);
		js.executeScript("mobile: swipe", scrollObject);
	}//swipeByJavaScript


	/**
	 * To swipe the screen by JavaScript
	 * @param driver
	 * @param element
	 */
	public static void scrollByJavaScript(AppiumDriver<MobileElement> driver, MobileElement element){
		swipeUpElementVisible(driver, element);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Object> scrollObject = new HashMap<>();
		scrollObject.put("startX", 0.95);
		scrollObject.put("startY", 0.5);
		scrollObject.put("endX", 0.05);
		scrollObject.put("endY", 0.5);         
		scrollObject.put("direction", "up");
		scrollObject.put("duration", 1.8);
		js.executeScript("mobile: scroll", scrollObject);
	}//swipeByJavaScript

	/**
	 * Select drop down value and doesn't wait for spinner.
	 *
	 * @param elementLocator
	 *            the element locator
	 * @param valueToBeSelected
	 *            the value to be selected
	 * @throws Exception 
	 */
	public static void selectDropDownValue(AppiumDriver<MobileElement> driver,
			MobileElement dropDown, String valueToBeSelected) throws Exception {
		dropDown.click();
		boolean status=true;
		String temp = null;
		while (status){
			if(driver.findElement(By.id("numberpicker_input")).getAttribute("text").toLowerCase().equals(valueToBeSelected.toLowerCase())){
				driver.findElement(By.id("fragment_select_size_continue_button")).click();
				status=false;
				break;
			}else{
				MobileElement element1 = driver.findElement(By.id("numberpicker_input"));
				temp=element1.getAttribute("text");
				try {
					int leftX = element1.getLocation().getX();
					int rightX = leftX + element1.getSize().getWidth();
					int middleX = (rightX + leftX) / 2;
					int upperY = element1.getLocation().getY();
					int lowerY = upperY + element1.getSize().getHeight();
					int middleY = (upperY + lowerY) / 2;
					Double EndY = (middleY*1.6)/2;
					String endY = (EndY.toString()).split("\\.")[0];
					int endYint = Integer.parseInt(endY);
					//driver.swipe(middleX, middleY, middleX, endYint, 2500);
					new TouchAction(driver) .press(PointOption.point(middleX,middleY)).waitAction().moveTo(PointOption.point(middleX, endYint)).release().perform();
					if(driver.findElement(By.id("numberpicker_input")).getAttribute("text").equals(temp)){
						//driver.swipe(middleX, middleY, middleX, endYint, 2500);
						new TouchAction(driver) .press(PointOption.point(middleX,middleY)).waitAction().moveTo(PointOption.point(middleX, endYint)).release().perform();
					}
					if(driver.findElement(By.id("numberpicker_input")).getAttribute("text").equals(temp)){
						//driver.swipe(middleX, middleY, middleX, endYint, 2500);
						new TouchAction(driver) .press(PointOption.point(middleX,middleY)).waitAction().moveTo(PointOption.point(middleX, endYint)).release().perform();
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
			if(driver.findElement(By.id("numberpicker_input")).getAttribute("text").equals(temp)){
				Log.fail("Mentioned state is not available in the State dropdown",driver);
				break;
			}

		}
	}//selectDropDownValue



	//*******************************************************************************************//




	/**
	 * Scroll down action
	 */

	public static void scrolldown(AppiumDriver<MobileElement> driver){
		Dimension size=driver.manage().window().getSize();
		int y_start=(int)(size.height*0.80);
		int y_end=(int)(size.height*0.20);
		int x=size.width/2;
		//driver.swipe(x,y_start,x,y_end,4000);
		new TouchAction(driver) .press(PointOption.point(x,y_start)).waitAction().moveTo(PointOption.point(x, y_end)).release().perform();
	}

	/**
	 * Scroll to view the end
	 * @param driver
	 * @param element
	 */
	public static void scrollToViewElementToBottom(AppiumDriver<MobileElement> driver){
		Dimension size=driver.manage().window().getSize();
		int y_start=(int)(size.height*0.80);
		int y_end=(int)(size.height*0.20);
		int x=size.width/2;
		//driver.swipe(x,y_start,x,y_end,4000);
		new TouchAction(driver) .press(PointOption.point(x,y_start)).waitAction().moveTo(PointOption.point(x, y_end)).release().perform();
	}

	/**
	 * Scroll to view the end
	 * @param driver
	 * @param element
	 */
	public static void scrollToEnd(AppiumDriver<MobileElement> driver){
		Dimension size=driver.manage().window().getSize();
		int y_start=(int)(size.height*0.80);
		int y_end=(int)(size.height*0.20);
		int x=size.width/2;
		//driver.swipe(x,y_start,x,y_end,4000);
		new TouchAction(driver) .press(PointOption.point(x,y_start)).waitAction().moveTo(PointOption.point(x, y_end)).release().perform();

	}

}// AppActions


