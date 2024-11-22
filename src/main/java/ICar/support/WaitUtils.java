package ICar.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.xml.XmlTest;


public class WaitUtils {
    public static ExpectedCondition<Boolean> documentLoad;
    public static ExpectedCondition<Boolean> framesLoad;
    public static ExpectedCondition<Boolean> imagesLoad;
    public static ExpectedCondition<Boolean> spinnerLoad;
    public static int maxPageLoadWait = 60;
    public static int maxElementWait = 30;
  //*[@id="brandBand_2"]/div/di
  //  static String cssSpinner = "//";
    public ElementLayer elementLayer;
	HashMap<String, String> getPageObjects;
 	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();

	
    private static By allSpinners = By.cssSelector("div[class='slds-spinner slds-spinner_brand slds-spinner_small']>div:nth-child(2)r");

    static {
        documentLoad = new ExpectedCondition<Boolean>() {
            public final Boolean apply(final WebDriver driver) {
                final JavascriptExecutor js = (JavascriptExecutor) driver;
                boolean docReadyState = false;
                try {
                    docReadyState = (Boolean) js.executeScript("return (function() { if (document.readyState != 'complete') {  return false; } if (window.jQuery != null && window.jQuery != undefined && window.jQuery.active) { return false;} if (window.jQuery != null && window.jQuery != undefined && window.jQuery.ajax != null && window.jQuery.ajax != undefined && window.jQuery.ajax.active) {return false;}  if (window.angular != null && angular.element(document).injector() != null && angular.element(document).injector().get('$http').pendingRequests.length) return false; return true;})();");
                } catch (WebDriverException e) {
                    docReadyState = true;
                }
                return docReadyState;
            }
        };

        imagesLoad = new ExpectedCondition<Boolean>() {
            public final Boolean apply(final WebDriver driver) {
                boolean docReadyState = true;
                try {
                    JavascriptExecutor js;
                    List<WebElement> images = driver.findElements(By.cssSelector("img[src]"));
                    for (int i = 0; i < images.size(); i++) {
                        try {
                            js = (JavascriptExecutor) driver;
                            docReadyState = docReadyState && (Boolean) js.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", images.get(i));
                            if (!docReadyState) {
                                break;
                            }
                        } catch (StaleElementReferenceException e) {
                            images = driver.findElements(By.cssSelector("img[src]"));
                            i--;
                            continue;
                        } catch (WebDriverException e) {

                            // setting the true value if any exception arise
                            // Ex:: inside frame or switching to new windows or
                            // switching to new frames
                            docReadyState = true;
                        }
                    }
                } catch (WebDriverException e) {
                    docReadyState = true;
                }
                return docReadyState;
            }
        };

        framesLoad = new ExpectedCondition<Boolean>() {
            public final Boolean apply(final WebDriver driver) {
                boolean docReadyState = true;
                try {
                    JavascriptExecutor js;
                    List<WebElement> frames = driver.findElements(By.cssSelector("iframe[style*='hidden']"));
                    for (WebElement frame : frames) {
                        try {
                            driver.switchTo().defaultContent();
                            driver.switchTo().frame(frame);
                            js = (JavascriptExecutor) driver;
                            docReadyState = docReadyState && (Boolean) js.executeScript("return (document.readyState==\"complete\")");
                            driver.switchTo().defaultContent();
                            if (!docReadyState) {
                                break;
                            }
                        } catch (WebDriverException e) {
                            docReadyState = true;
                        }
                    }
                } catch (WebDriverException e) {
                    docReadyState = true;
                } finally {
                    driver.switchTo().defaultContent();
                }
                return docReadyState;
            }
        };

        spinnerLoad = new ExpectedCondition<Boolean>() {
            public final Boolean apply(final WebDriver driver) {
                //List<WebElement> spinners = driver.findElements(allSpinners);
              //  for (WebElement spinner : spinners) {
            	WebElement spinner = driver.findElement(allSpinners);
                    try {
                        if (spinner.isDisplayed()) {
                            return false;
                        }
                    } catch (NoSuchElementException ex) {
                        ex.printStackTrace();
                    }
                //}
                /*// To wait click events to trigger
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                spinners = driver.findElements(allSpinners);
                for (WebElement spinner : spinners) {
                    try {
                        if (spinner.isDisplayed()) {
                            return false;
                        }
                    } catch (NoSuchElementException ex) {
                        ex.printStackTrace();
                    }
                }*/
                return true;
            }
        };

    }
    
    public static boolean waitForPageLoads(final WebDriver driver, int maxWait) {
       // long startTime = StopWatch.startTime();
        WebDriverWait wait = new WebDriverWait(driver, maxWait);
        try {

            wait.until(WebDriverFactory.documentLoad);
            String title = driver.getTitle().toLowerCase();
            String url = driver.getCurrentUrl().toLowerCase();
            Log.event("Page URL:: " + url);

            if ("the page cannot be found".equalsIgnoreCase(title) || title.contains("is not available") || url.contains("/error/") || url.toLowerCase().contains("/errorpage/")) {
                //Assert.fail("Site is down. [Title: " + title + ", URL:" + url + "]");
            	Log.event("Site is down. [Title: " + title + ", URL:" + url + "]");
                return false;
            }
        } catch (TimeoutException e) {
            driver.navigate().refresh();
            wait.until(WebDriverFactory.documentLoad);
        }
        Log.event("Page Load Wait");
		return true;

    } // waitForPageLoad

    /**
     * To wait till spinner load
     * 
     * @param driver
     *            -
     */
    public static void waitForSpinner(final WebDriver driver) {
        long startTime = StopWatch.startTime();
    	XmlTest xmlParameters = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		String Environment = (xmlParameters.getParameter("Environment") != null) ? (xmlParameters.getParameter("Environment")) : "";
        try {
        	 new WebDriverWait(driver, 15).until(spinnerLoad);
        	 if(Environment.equalsIgnoreCase("saucelab")){
        		if(waitForPageLoads(driver, 7)){
        			Log.event("Page loaded successfully", StopWatch.elapsedTime(startTime));
        		}
        		else{
        			Assert.fail("Site is down.");
        		}
        	 }
        } catch (Exception ex) {
            Log.event("Catched spinner load exception due to " + ex.getMessage());
        }
        Log.event("Spinner Load Wait");
    }
    
    /*public static void waitForSpinner(final WebDriver driver) {
        long startTime = StopWatch.startTime();
        try {
        	//driver.manage().logs();
            (new WebDriverWait(driver, 15).pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage("Spinners/page not loading")).until(spinnerLoad);
            Thread.sleep(100);
            (new WebDriverWait(driver, 30).pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage("Page document is not loading")).until(documentLoad);
            (new WebDriverWait(driver, 30).pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage("Page images is not loaded completely")).until(imagesLoad);
            
        } catch (Exception ex) {
            Log.event("Catched spinner load exception");
        }
        Log.event("Spinner Load Wait: (Sync)", StopWatch.elapsedTime(startTime));

    }*/

    
    public static void waitForSpinner(final WebDriver driver, int waittime) {
        long startTime = StopWatch.startTime();
        try {
               new WebDriverWait(driver, waittime).until(spinnerLoad);
        } catch (Exception ex) {
            Log.event("Catched spinner load exception due to " + ex.getMessage());
        }
        Log.event("Spinner Load Wait: (Sync)", StopWatch.elapsedTime(startTime));

    }


  /*  public static void closeAlert(WebDriver driver) { 
    	try {
    	if(ValidationUtils.isElementPresent(driver.findElement(By.cssSelector("div[name='alert_modal'] button[class='Modal__header__close']"))))
		driver.findElement(By.cssSelector("div[name='alert_modal'] button[class='Modal__header__close']")).click();
	} catch(Exception e) {
		 e.printStackTrace();
	}
    }*/

    /**
     * To wait for the Element is present until its visibility is true
     * 
     * @param driver
     *            : Webdriver
     * @param seconds
     *            : no of millisecsonds to wait
     * @param elementToCheck
     *            : locator to be given as Webelement
     * @param msg
     *            : message to print in the log
     */
    @SuppressWarnings("deprecation")
	public static void waitForElementPresent(WebDriver driver, int seconds, WebElement elementTocheck, String msg) {
        (new WebDriverWait(driver, seconds).pollingEvery(200, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage(msg)).until(ExpectedConditions.visibilityOf(elementTocheck));

    }

    /**
     * To wait for the Element is present until its visibility is true
     * 
     * @param driver
     *            : Webdriver
     * @param elementToCheck
     *            : locator to be given as Webelement
     * @param msg
     *            : message to print in the log
     */
    @SuppressWarnings("deprecation")
	public static void waitForElementPresent(WebDriver driver, WebElement elementTocheck, String msg) {
        (new WebDriverWait(driver, 30).pollingEvery(200, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage(msg)).until(ExpectedConditions.visibilityOf(elementTocheck));

    }
    
	/**
	 * Finds the element by the given locatorType and locatorValue and waits for
	 * the element to be visible.
	 * 
	 * @param driver
	 *            : Webdriver
	 * @param locatorType
	 *            : how the element to be looked up (CSS or Xpath)
	 * @param locatorValue
	 *            : the address of the element
	 * @param timeOutInSec:
	 *            the maximum wait time in seconds
	 * @param msg
	 *            : timeout error message
	 */
	@SuppressWarnings("deprecation")
	public static void waitForElementPresent(WebDriver driver, String locatorType, String locatorValue, int timeOutInSec, String msg) {
		if (locatorType.equalsIgnoreCase("css")) {
			(new WebDriverWait(driver, timeOutInSec).pollingEvery(200, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage(msg))
							.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locatorValue)));
		} else if (locatorType.equalsIgnoreCase("xpath")) {
			(new WebDriverWait(driver, timeOutInSec).pollingEvery(200, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage(msg))
							.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorValue)));
		}
	}
	
	/**
	 * Finds the element by the given locatorType and locatorValue and waits for
	 * the element to be visible.
	 * 
	 * @param driver
	 *            : Webdriver
	 * @param locatorType
	 *            : how the element to be looked up (CSS or Xpath)
	 * @param locatorValue
	 *            : the address of the element
	 * @param timeOutInSec:
	 *            the maximum wait time in seconds
	 * @param msg
	 *            : timeout error message
	 */
	@SuppressWarnings("deprecation")
	public static void waitForElementAbsent(WebDriver driver, String locatorType, String locatorValue, int timeOutInSec, String msg) {
		if (locatorType.equalsIgnoreCase("css")) {
			(new WebDriverWait(driver, timeOutInSec).pollingEvery(200, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage(msg))
							.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locatorValue)));
		} else if (locatorType.equalsIgnoreCase("xpath")) {
			(new WebDriverWait(driver, timeOutInSec).pollingEvery(200, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage(msg))
							.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locatorValue)));
		}
	}

    /**
     * To wait for the Element is present until its invisibility is true
     * 
     * @param driver
     *            : Webdriver
     * @param seconds
     *            : no of millisecsonds to wait
     * @param locator
     *            : locator to be given as Webelement
     * @param msg
     *            : message to print in the log
     */
    @SuppressWarnings("deprecation")
	public static void waitForinvisiblityofElement(WebDriver driver, int seconds, String locator, String msg) {
        (new WebDriverWait(driver, seconds).pollingEvery(200, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage("Unable to locate the element")).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locator)));

    }

    /**
     * To wait for the Element is present until its invisibility is true
     * 
     * @param driver
     *            : Webdriver
     * @param seconds
     *            : no of millisecsonds to wait
     * @param locator
     *            : locator to be given as List element
     * @param msg
     *            : message to print in the log
     */
    @SuppressWarnings("deprecation")
	public static void waitForinvisiblityofListElement(WebDriver driver, int seconds, String locator, String msg) {
        List<WebElement> Elements_toFind = driver.findElements(By.cssSelector(locator));
        (new WebDriverWait(driver, 30).pollingEvery(200, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage(msg)).until((ExpectedConditions.invisibilityOfAllElements(Elements_toFind)));

    }

    /**
     * To wait for the Element is visible and clickable
     * 
     * @param driver
     *            : Webdriver
     * @param elementTocheck
     *            : locator to be given as Webelement
     * @param msg
     *            : message to print in the log
     */
    @SuppressWarnings("deprecation")
	public static void waitForelementToBeClickable(WebDriver driver, WebElement elementTocheck, String msg) {
        (new WebDriverWait(driver, 30).pollingEvery(200, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class, StaleElementReferenceException.class).withMessage(msg)).until(ExpectedConditions.elementToBeClickable(elementTocheck));
    }

    /**
     * WaitForPageLoad waits for the page load with default page load wait time
     * 
     * @param driver
     *            : Webdriver
     */
    public static void waitForPageLoad(final WebDriver driver) {
        waitForPageLoad(driver, WebDriverFactory.maxPageLoadWait);
    }

    /**
     * WaitForPageLoad waits for the page load with custom page load wait time
     * 
     * @param driver
     *            : Webdriver
     * @param maxWait
     *            : Max wait duration
     */
    public static void waitForPageLoad(final WebDriver driver, int maxWait) {
    // long startTime = StopWatch.startTime();
     WebDriverWait wait = new WebDriverWait(driver, maxWait);
     try {

         wait.until(WebDriverFactory.documentLoad);
         String title = driver.getTitle().toLowerCase();
         String url = driver.getCurrentUrl().toLowerCase();
         Log.event("Page URL:: " + url);

         if ("the page cannot be found".equalsIgnoreCase(title) || title.contains("is not available") || url.contains("/error/") || url.toLowerCase().contains("/errorpage/")) {
             Assert.fail("Site is down. [Title: " + title + ", URL:" + url + "]");
         }
     } catch (TimeoutException e) {
         driver.navigate().refresh();
         wait.until(WebDriverFactory.documentLoad);
     }
     Log.event("Page Load Wait ");

 } // waitForPageLoad

    /*static void waitForPageLoads(WebDriver wdriver) {
	    WebDriverWait wait = new WebDriverWait(wdriver, 60);

	    Predicate<WebDriver> pageLoaded = new Predicate<WebDriver>() {

	        public boolean apply(WebDriver input) {
	            return ((JavascriptExecutor) input).executeScript("return document.readyState").equals("complete");
	        }

	    };
	    wait.until(pageLoaded);
	}
*/
    /**
     * To wait for the specific element on the page
     * 
     * @param driver
     *            : Webdriver
     * @param element
     *            : Webelement to wait for
     * @return boolean - return true if element is present else return false
     */
    public static boolean waitForElement(WebDriver driver, WebElement element) {
        return waitForElement(driver, element, maxElementWait);
    }

    /**
     * To wait for the specific element on the page
     * 
     * @param driver
     *            : Webdriver
     * @param element
     *            : Webelement to wait for
     * @param maxWait
     *            : Max wait duration
     * @return boolean - return true if element is present else return false
     */
    public static boolean waitForElement(WebDriver driver, WebElement element, int maxWait) {
        boolean statusOfElementToBeReturned = false;
        long startTime = StopWatch.startTime();
        WebDriverWait wait = new WebDriverWait(driver, maxWait);
        try {
            WebElement waitElement = wait.until(ExpectedConditions.visibilityOf(element));
            if (waitElement.isDisplayed() && waitElement.isEnabled()) {
                statusOfElementToBeReturned = true;
                Log.event("Element is displayed:: " + element.toString());
            }
        } catch (Exception e) {
            statusOfElementToBeReturned = false;
            Log.event("Unable to find a element after " + StopWatch.elapsedTime(startTime) + " sec ==> " + element.toString());
        }
        return statusOfElementToBeReturned;
    }

    /**
     * To wait for the specific list elements on the page
     * 
     * @param driver
     *            -
     * @param elements
     *            - List elements to wait for to appear
     * @param maxWait
     *            - how long to wait for
     * @return boolean - return true if element is present else return false
     */
    public static boolean waitForListElement(WebDriver driver, List<WebElement> elements, int maxWait) {
        boolean statusOfElementToBeReturned = false;
        long startTime = StopWatch.startTime();
        WebDriverWait wait = new WebDriverWait(driver, maxWait);
        try {
            WebElement waitElement = (WebElement) wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            if (waitElement.isDisplayed() && waitElement.isEnabled()) {
                statusOfElementToBeReturned = true;
                Log.event("List Element is displayed :: " + elements.toString());
            }
        } catch (Exception ex) {
            statusOfElementToBeReturned = false;
            Log.event("Unable to find list element after " + StopWatch.elapsedTime(startTime) + " sec ==> " + elements.toString());
        }
        return statusOfElementToBeReturned;
    }

    /**
     * Wait until element disappears in the page
     * 
     * @param driver
     *            - driver instance
     * @param element
     *            - webelement to wait to have disaapear
     * @return true if element is not appearing in the page
     */
    public static boolean waitUntilElementDisappear(WebDriver driver, final WebElement element) {
        final boolean isNotDisplayed;

        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, WebDriverFactory.maxPageLoadWait);
        isNotDisplayed = wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                boolean isPresent = false;
                try {
                    if (element.isDisplayed()) {
                        isPresent = false;
                        Log.event("Element " + element.toString() + ", is still visible in page");
                    }
                } catch (Exception ex) {
                    isPresent = true;
                    Log.event("Element " + element.toString() + ", is not displayed in page ");
                    return isPresent;
                }
                return isPresent;
            }
        });
        return isNotDisplayed;
    }

    /**
     * To wait for the specific element which is in disabled state on the page
     * 
     * @param driver
     *            - current driver object
     * @param element
     *            - disabled web element
     * @param maxWait
     *            - duration of wait in seconds
     * @return boolean - return true if disabled element is present else return
     *         false
     */
    public static boolean waitForDisabledElement(WebDriver driver, WebElement element, int maxWait) {
        boolean statusOfElementToBeReturned = false;
        long startTime = StopWatch.startTime();
        WebDriverWait wait = new WebDriverWait(driver, maxWait);
        try {
            WebElement waitElement = wait.until(ExpectedConditions.visibilityOf(element));
            if (!waitElement.isEnabled()) {
                statusOfElementToBeReturned = true;
                Log.event("Element is displayed and disabled :: " + element.toString());
            }
        } catch (Exception ex) {
            statusOfElementToBeReturned = false;
            Log.event("Unable to find disabled element after " + StopWatch.elapsedTime(startTime) + " sec ==> " + element.toString());
        }
        return statusOfElementToBeReturned;
    }

}
