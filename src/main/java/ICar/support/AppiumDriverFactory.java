package ICar.support;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
//import junit.framework.Assert;

public class AppiumDriverFactory {
	private static Logger logger = LoggerFactory.getLogger(AppiumDriverFactory.class);
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	private static String platform;
	private static String platformName;
	private static String platformNameWeb;
	private static List<String> udidList;
	private static Map<String, Boolean> deviceBuffer;
	private static Map<String, AppiumDriver<MobileElement>> driverBuffer;
	private static final int SECONDS = 60;
	private static final int TOTAL_TIMES;
	static String xpathSpinner = "//UIAApplication[1]/UIAWindow[1]/UIAActivityIndicator[1]";
	public static ExpectedCondition<Boolean> appPageLoad;
	private static By allSpinners = By.cssSelector(xpathSpinner);
	public static int maxPageLoadWait = 90;


	//private static CommandPrompt commandPrompt = new CommandPrompt();

	static {
		platformName = (System.getProperty("platformName")!=null)?System.getProperty("platformName") : configProperty.hasProperty("platformName") ? configProperty.getProperty("platformName") : "platform-not-specified";
		try {
			platformNameWeb = (System.getProperty("platformNameWeb")!=null)?System.getProperty("platformNameWeb") : configProperty.hasProperty("platformNameWeb") ? configProperty.getProperty("platformNameWeb") : "platform-not-specified";
		} catch (Exception e) {
			// TODO: handle exception
		}
		TOTAL_TIMES = configProperty.hasProperty("DevicePollingCount") ? Integer.valueOf(configProperty.getProperty("DevicePollingCount")) : 3;
		driverBuffer = new HashMap<String, AppiumDriver<MobileElement>>();
		deviceBuffer = new HashMap<String, Boolean>();

		udidList = new ArrayList<String>();
		if (configProperty.hasProperty("udidList")) {
			udidList.addAll(Arrays.asList(configProperty.getProperty("udidList").split(",")));
		}
		if (configProperty.hasProperty("udid") && !udidList.contains(configProperty.getProperty("udid"))) {
			udidList.add(configProperty.getProperty("udid"));
		}
		if (configProperty.hasProperty("udid2") && !udidList.contains(configProperty.getProperty("udid2"))) {
			udidList.add(configProperty.getProperty("udid2"));
		}

		udidList.forEach(udid -> {
			if (StringUtils.isNotBlank(udid)) {
				deviceBuffer.put(udid, true);
				driverBuffer.put(udid, null);
			}
		});

		appPageLoad = new ExpectedCondition<Boolean>() {
			public final Boolean apply(final WebDriver driver) {
				List<WebElement> spinners = driver.findElements(allSpinners);
				for (WebElement spinner : spinners) {
					try {
						if (spinner.isDisplayed()) {
							return false;
						}
					} catch (NoSuchElementException ex) {
						ex.printStackTrace();
					}
				}
				// To wait click events to trigger
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
				}
				return true;
			}
		};
	}

	/**
	 * To get a new instance of app driver using default parameters
	 * 
	 * @return driver
	 */
	public static AppiumDriver<MobileElement> get() {
		Throwable t = new Throwable();
		String testName;
		if (configProperty.getProperty("runMobileWeb").equals("true")) {
			testName = new Exception().getStackTrace()[2].getMethodName();
		}else {
			testName=t.getStackTrace()[1].getMethodName();
		}
		return get(testName, null);
	}

	/**
	 * To get a new instance of app driver using a particular udid/devicename
	 * from config.properties file (say udid2/devicename2)
	 * 
	 * @return driver
	 */
	public static AppiumDriver<MobileElement> getAnotherDevice() {
		if (configProperty.hasProperty("udid")) {
			return get(null,configProperty.getProperty("udid"));
		} else {
			throw new RuntimeException("udid is not mentioned in config.properties file");
		}
	}

	/**
	 * To get a new instance of app driver using a particular udid
	 * 
	 * @return driver
	 */
//	public static AppiumDriver<MobileElement> get(String testName, String udid) {
//		//    udid ="UiAutomator2";
//		//		if(udid==null) {
//		//			udid="108AE17A-3D28-45B2-97F1-78F5E08659FB";
//		//		}
//
//		AppiumDriver<MobileElement> driver = null;
//		try {
//
//			if(configProperty.getProperty("runMobileApp").equals("true") && configProperty.getProperty("runMobileWeb").equals("false")) {
//				driver = getAppiumDriver(testName, udid);	
//			}else {
//				driver = getAppiumWebDriver(testName, udid);
//			}
//
//
//		} catch (Exception e) {
//			logger.error("Could not create a driver session. Trying again...");
//			driver = getAppiumDriver(testName, udid);
//		} finally {
//			// Update start time of the tests once free slot is assigned - Just
//			// for reporting purpose
//			try {
//				Field f = Reporter.getCurrentTestResult().getClass().getDeclaredField("m_startMillis");
//				f.setAccessible(true);
//				f.setLong(Reporter.getCurrentTestResult(), Calendar.getInstance().getTime().getTime());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return driver;
//	}
	
	public static AppiumDriver<MobileElement> get(String testName, String udid) {

		AppiumDriver<MobileElement> driver = null;
		try {
			
			if(configProperty.getProperty("runMobileApp").equals("true") && configProperty.getProperty("runMobileWeb").equals("false")) {
				driver = getAppiumDriver(testName, udid);	
			}else {
				driver = getAppiumWebDriverForLambdaTest(testName, udid);
			}
			
			
		} catch (Exception e) {
			logger.error("Could not create a driver session. Trying again...");
			driver = getAppiumDriver(testName, udid);
		} finally {
			// Update start time of the tests once free slot is assigned - Just
			// for reporting purpose
			try {
				Field f = Reporter.getCurrentTestResult().getClass().getDeclaredField("m_startMillis");
				f.setAccessible(true);
				f.setLong(Reporter.getCurrentTestResult(), Calendar.getInstance().getTime().getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return driver;
	}

	/**
	 * To set desired capabilities using configured parameters
	 * 
	 * @param udid
	 *            - udid to get a particular device/ blank or null to get any
	 *            available device
	 * @return capabilities
	 */
	private static DesiredCapabilities getDesiredCapabilities(String testName, String udid) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		String appPath = null;
		capabilities.setCapability("platformName", System.getProperty("platformName")!=null ? System.getProperty("platformName") :configProperty.getProperty("platformName"));
		capabilities.setCapability("platformVersion", System.getProperty("platformVersion")!=null ? System.getProperty("platformVersion") : configProperty.getProperty("platformVersion"));
		capabilities.setCapability("name", testName);
		if(AppUtils.getMobileAppRunPlatfrom().equalsIgnoreCase("Android")){
			capabilities.setCapability("autoGrantPermissions", "true");
		}
		if(AppUtils.getMobileAppRunPlatfrom().equalsIgnoreCase("iOS")){
			capabilities.setCapability("waitForQuiescence", "true");
			capabilities.setCapability("unicodeKeyboard", true);
		}
		capabilities.setCapability("appWaitActivity","SplashActivity, SplashActivity,OtherActivity, *, *.SplashActivity");
		//		capabilities.setCapability("automationName", configProperty.getProperty("automationName"));

		if(System.getProperty("runSauceLabFromLocal")!=null ? System.getProperty("runSauceLabFromLocal").equals("true"):configProperty.getProperty("runSauceLabFromLocal").equalsIgnoreCase("true")){
			capabilities.setCapability("app", System.getProperty("apppathforsaucelab")!=null ? System.getProperty("apppathforsaucelab") : configProperty.getProperty("apppathforsaucelab"));
			capabilities.setCapability("deviceName", System.getProperty("sauceDeviceName")!=null ? System.getProperty("sauceDeviceName") :configProperty.getProperty("sauceDeviceName"));
			capabilities.setCapability("appiumVersion", System.getProperty("appiumVersion")!=null ? System.getProperty("appiumVersion") : configProperty.getProperty("appiumVersion"));
			capabilities.setCapability("tunnelIdentifier", "Muthu");
			Log.event("Launching app in sauce labs");
		}else{
			if (StringUtils.isNotBlank(udid)) {
				if (isDeviceAvailable(udid)) {
					capabilities.setCapability("udid", udid);
					capabilities.setCapability("deviceName", udid);

				} else {
					throw new RuntimeException(udid + " not available after waiting for " + TOTAL_TIMES + " minutes");
				}
			} else {
				udid = getAvailableDeviceUDID();
				if (StringUtils.isBlank(udid)) {
					throw new RuntimeException("No device available after waiting for " + TOTAL_TIMES + " minutes");
				}
				capabilities.setCapability("udid", udid);
				capabilities.setCapability("deviceName", udid);
			}
			logger.info("Device udid : " + udid);

			if(configProperty.getProperty("platformName").equalsIgnoreCase("win")) {
				capabilities.setCapability("platformVersion", udid);
			}

			if ("mobileweb".equalsIgnoreCase(configProperty.getProperty("appType"))) {
				capabilities.setCapability("app", configProperty.getProperty("browser"));
			} else if ("Win".equalsIgnoreCase(platform)) {
				capabilities.setCapability("app", configProperty.getProperty("WinAppPackage"));
			} else if (configProperty.hasProperty("appPathiOS")) {
				capabilities.setCapability("app", configProperty.getProperty("appPathiOS"));
			} else {
				appPath = getAppAbsoultePath();
				capabilities.setCapability("app", appPath);
			}    	
		}

		return capabilities;
	}

	/**
	 * Method to get absolute path of app (ipa or app / apk)
	 * 
	 * @return absolute path of app
	 */
	private static String getAppAbsoultePath() {
		File classpathRoot = new File(System.getProperty("user.dir"));
		logger.info("App path should be:" + classpathRoot + "/app");
		File appDir = new File(classpathRoot, "/app");
		String fileName = "";
		File folder = new File("app");
		File[] listOfFiles = folder.listFiles();;
		for (File listFile : listOfFiles) {
			String fileExtension = FilenameUtils.getExtension(listFile.getAbsolutePath());
			if (null != platformName && platformName.equalsIgnoreCase("iOS") && (fileExtension.equals("ipa") || fileExtension.equals("app"))) {
				fileName = listFile.getName();
				break;
			} else if (null != platformName && platformName.trim().equalsIgnoreCase("Android") && fileExtension.equals("apk")) {
				fileName = listFile.getName();
				break;
			}
		}
		if (null != fileName && !fileName.isEmpty()) {
			File app = new File(appDir, fileName);
			String appName = app.getAbsolutePath();
			return appName;
		}
		return null;
	}

	/**
	 * To wait for given time until a random device becomes available
	 * 
	 * @return udid of the device when it is available
	 */
	private static String getAvailableDeviceUDID() {
		String udid = null;
		int maxTry = 0;
		while (udid == null && maxTry++ < TOTAL_TIMES) {
			clearStaleDriverSessions();
			synchronized (deviceBuffer) {
				for (String deviceudid : deviceBuffer.keySet()) {
					if (deviceBuffer.get(deviceudid)) {
						udid = deviceudid;
						deviceBuffer.put(deviceudid, false);
						break;
					}
				}
			}
			if (udid == null) {
				try {
					logger.info(Reporter.getCurrentTestResult().getName() + " says -> No devices available now. Waiting for 1 minute...");
					TimeUnit.SECONDS.sleep(SECONDS);
				} catch (InterruptedException e) {
					logger.error("Unable to get udid: " + e.getMessage());
				}
			}
		}
		return udid;
	}

	/**
	 * To wait for given time until the device with given udid become available
	 * 
	 * @param udid
	 *            - udid of the device
	 * @return true when the device is available
	 */
	private static boolean isDeviceAvailable(String udid) {
		boolean isFree = false;
		int maxTry = 0;
		while (!isFree && maxTry++ < TOTAL_TIMES) {
			clearStaleDriverSessions();
			synchronized (deviceBuffer) {
				if (deviceBuffer.get(udid)) {
					deviceBuffer.put(udid, false);
					isFree = true;
				}
			}
			if (!isFree) {
				try {
					logger.info(Reporter.getCurrentTestResult().getName() + " says -> " + udid + " is not available now. Waiting for 1 minute...");
					TimeUnit.SECONDS.sleep(SECONDS);
				} catch (InterruptedException e) {
					logger.error("Unable to get udid: " + e.getMessage());
				}
			}
		}
		return isFree;
	}

	/**
	 * To quit the appium driver and update the device buffer and driver buffer
	 * 
	 * @param driver
	 */
	public static void quitDriver(AppiumDriver<MobileElement> driver) {
		if (driver != null) {
			String udid = "";
			if(null != driver.getCapabilities().getCapability("udid")) {
				udid = driver.getCapabilities().getCapability("udid").toString();
				synchronized (deviceBuffer) {
					driver.quit();
					deviceBuffer.put(udid, true);
					driverBuffer.put(udid, null);
				}
			} else {
				udid = driver.getCapabilities().getCapability("platformVersion").toString();
				synchronized (deviceBuffer) {
					driver.quit();
					deviceBuffer.put(udid, true);
					driverBuffer.put(udid, null);
				}
			}
			driver.quit();
		} else {
			logger.error("Driver is null");
		}
	}

	/**
	 * To create an Appium Driver session with given capabilities.
	 * 
	 * @param udid
	 *            - udid to get a particular device/ blank or null to get any
	 *            available device
	 * @return AppiumDriver instance
	 * @throws MalformedURLException 
	 */

	private static AppiumDriver<MobileElement> getAppiumDriver(String testName, String udid) {
		AppiumDriver<MobileElement> driver = null;

		String sauceUserName = null;
		String sauceAuthKey = null;

		DesiredCapabilities capabilities = new DesiredCapabilities();
		if(System.getProperty("runSauceLabFromLocal")!=null ? System.getProperty("runSauceLabFromLocal").equals("true"):configProperty.getProperty("runSauceLabFromLocal").equalsIgnoreCase("true")){

			if(System.getProperty("runRealDevice")!=null ? System.getProperty("runRealDevice").equals("true"):configProperty.getProperty("runRealDevice").equalsIgnoreCase("true")){
				capabilities.setCapability("testobject_api_key",  System.getProperty("testobject_api_key")!=null ? System.getProperty("testobject_api_key") :configProperty.getProperty("testobject_api_key"));
				capabilities.setCapability("platformVersion", System.getProperty("platformVersion")!=null ? System.getProperty("platformVersion") :configProperty.getProperty("platformVersion"));
				capabilities.setCapability("platformName",System.getProperty("platformName")!=null ? System.getProperty("platformName") :configProperty.getProperty("platformName"));
				//				capabilities.setCapability("deviceName", "Huawei P30"); // Optional
				capabilities.setCapability("phoneOnly", System.getProperty("phoneOnly")!=null ? System.getProperty("phoneOnly") :configProperty.getProperty("phoneOnly"));
				capabilities.setCapability("privateDevicesOnly", System.getProperty("privateDevicesOnly")!=null ? System.getProperty("privateDevicesOnly") :configProperty.getProperty("privateDevicesOnly"));
				capabilities.setCapability("testobject_app_id", System.getProperty("testobject_app_id")!=null ? System.getProperty("testobject_app_id") :configProperty.getProperty("testobject_app_id"));
				capabilities.setCapability("tunnelIdentifier", System.getProperty("tunnelIdentifierMobile")!=null ? System.getProperty("tunnelIdentifierMobile") :configProperty.getProperty("tunnelIdentifierMobile"));
				//tunnelIdentifier
				//				URL EU_endpoint = new URL("https://eu1.appium.testobject.com/wd/hub");
				URL US_endpoint = null;
				try {
					US_endpoint = new URL("https://us1.appium.testobject.com/wd/hub");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(platformName.toLowerCase().equals("android")){
					driver = new AndroidDriver<MobileElement>(US_endpoint, capabilities);					
				}else if(platformName.toLowerCase().equals("ios")){
					driver = new IOSDriver<MobileElement>(US_endpoint, capabilities);
				}else {
					Log.fail("Invalid platformName. We are supporting only iOS and Android");
				}

				try {
					sauceUserName = configProperty.hasProperty("sauceUserName")
							? configProperty.getProperty("sauceUserName") : null;
					sauceAuthKey = configProperty.hasProperty("sauceAuthKey") ? configProperty.getProperty("sauceAuthKey")
							: null;
					String saucelabsSessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
					String sauceLink = "http://saucelabs.com/jobs/" + saucelabsSessionId + "?auth=" + newHMACMD5Digest(
							sauceUserName + ":" + sauceAuthKey, saucelabsSessionId);
					logger.debug("Saucelab link for " + testName + ":: " + sauceLink);
					Log.addSauceJobUrlToReport(driver, sauceLink);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else {

				String appiumURL = "https://" + configProperty.getProperty("sauceUserName") + ":" + configProperty.getProperty("sauceAuthKey") + "@ondemand.saucelabs.com:443/wd/hub";
				try {
					//				udid="UiAutomator2";
					capabilities = getDesiredCapabilities(testName, udid);
					Log.event(capabilities+"");
					Log.event("Appiuum URl-" + appiumURL);
					if(platformName.toLowerCase().equals("android")){
						driver = new AndroidDriver<MobileElement>(new URL(appiumURL), capabilities);
					}else if(platformName.toLowerCase().equals("ios")){
						driver = new IOSDriver<MobileElement>(new URL(appiumURL), capabilities);
					}else {
						Log.fail("Invalid platformName. We are supporting only iOS and Android");
					}
					try {
						sauceUserName = configProperty.hasProperty("sauceUserName")
								? configProperty.getProperty("sauceUserName") : null;
						sauceAuthKey = configProperty.hasProperty("sauceAuthKey") ? configProperty.getProperty("sauceAuthKey")
								: null;
						String saucelabsSessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
						String sauceLink = "http://saucelabs.com/jobs/" + saucelabsSessionId + "?auth=" + newHMACMD5Digest(
								sauceUserName + ":" + sauceAuthKey, saucelabsSessionId);
						logger.debug("Saucelab link for " + testName + ":: " + sauceLink);
						Log.addSauceJobUrlToReport(driver, sauceLink);
					} catch (Exception e) {
						// TODO: handle exception
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (System.getProperty("appium.screenshots.dir") != null) {
			try {
				logger.info("Initialize the Appium driver for AWS");
				driver = new IOSDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			logger.info("Initialize the Appium driver for local");
			try {
				String appiumURL = "http://" + configProperty.getProperty("host") + ":" + configProperty.getProperty("port") + "/wd/hub";
				capabilities = getDesiredCapabilities(testName, udid);
				udid = capabilities.getCapability("udid").toString();

				switch (platformName.toLowerCase()) {
				case "android":
					driver = new AndroidDriver<MobileElement>(new URL(appiumURL), capabilities);
					break;
				case "ios":
					driver = new IOSDriver<MobileElement>(new URL(appiumURL), capabilities);
					break;
				case "win":
					appiumURL = "http://" + udid + ":" + configProperty.getProperty("port");
					driver = new IOSDriver<MobileElement>(new URL(appiumURL), capabilities);
					break;
				default:
					logger.error("Unable to load platform property. Platform is set to " + platform);
					break;
				}

				synchronized (deviceBuffer) {
					deviceBuffer.put(udid, false);
					driverBuffer.put(udid, driver);
				}
			} catch (Exception e) {
				synchronized (deviceBuffer) {
					if (capabilities.getCapability("udid") != null && deviceBuffer.containsKey(capabilities.getCapability("udid").toString())) {
						deviceBuffer.put(capabilities.getCapability("udid").toString(), true);
						driverBuffer.put(capabilities.getCapability("udid").toString(), null);
					}
				}
				logger.error("Unable to create driver session with given URL and DesiredCapabilities : " + e.getMessage());
				throw new RuntimeException("Unable to create driver session with given URL and DesiredCapabilities : " + e.getMessage());
			}
			String platformName = capabilities.getCapability("platformName").toString();
			String platformVersion = capabilities.getCapability("platformVersion").toString();

			Log.addExecutionEnvironmentToReport(driver, platformName+"_"+platformVersion);
		}
		return driver;
	}

	private static AppiumDriver<MobileElement> getAppiumWebDriver(String testName, String udid) {
		AppiumDriver<MobileElement> driver = null;

		String sauceUserName = null;
		String sauceAuthKey = null;

		DesiredCapabilities capabilities = new DesiredCapabilities();
		if(System.getProperty("runSauceLabFromLocal")!=null ? System.getProperty("runSauceLabFromLocal").equals("true"):configProperty.getProperty("runSauceLabFromLocal").equalsIgnoreCase("true")){
			String appiumURL = "https://" + configProperty.getProperty("sauceUserName") + ":" + configProperty.getProperty("sauceAuthKey") + "@ondemand.saucelabs.com:443/wd/hub";
			try {
				capabilities.setCapability("name", testName);
				capabilities.setCapability("appiumVersion", System.getProperty("appiumVersionWeb")!=null ? System.getProperty("appiumVersionWeb") :configProperty.getProperty("appiumVersionWeb"));
				capabilities.setCapability("deviceName",System.getProperty("deviceNameWeb")!=null ? System.getProperty("deviceNameWeb") :configProperty.getProperty("deviceNameWeb"));
				capabilities.setCapability("browserName", System.getProperty("browserNameWeb")!=null ? System.getProperty("browserNameWeb") :configProperty.getProperty("browserNameWeb"));
				capabilities.setCapability("platformVersion", System.getProperty("platformVersionWeb")!=null ? System.getProperty("platformVersionWeb") :configProperty.getProperty("platformVersionWeb"));
				capabilities.setCapability("platformName",System.getProperty("platformNameWeb")!=null ? System.getProperty("platformNameWeb") :configProperty.getProperty("platformNameWeb"));  
				System.out.println(configProperty.getProperty("platformNameWeb"));
				if(configProperty.getProperty("platformNameWeb").equalsIgnoreCase("Android")) {
					ChromeOptions chromeOptions = new ChromeOptions();
					chromeOptions.setExperimentalOption("w3c", false);
					capabilities.merge(chromeOptions);
				}
				Log.message(capabilities+"");
				Log.message("Appiuum URl-" + appiumURL);
				if(platformNameWeb.toLowerCase().equals("android")){
					driver = new AndroidDriver<MobileElement>(new URL(appiumURL), capabilities);
				}else if(platformNameWeb.toLowerCase().equals("ios")){
					driver = new IOSDriver<MobileElement>(new URL(appiumURL), capabilities);
				}
				try {
					sauceUserName = configProperty.hasProperty("sauceUserName")
							? configProperty.getProperty("sauceUserName") : null;
					sauceAuthKey = configProperty.hasProperty("sauceAuthKey") ? configProperty.getProperty("sauceAuthKey")
							: null;
					String saucelabsSessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
					String sauceLink = "https://saucelabs.com/jobs/" + saucelabsSessionId + "?auth=" + newHMACMD5Digest(
							sauceUserName + ":" + sauceAuthKey, saucelabsSessionId);
					logger.debug("Saucelab link for " + testName + ":: " + sauceLink);
					Log.addSauceJobUrlToReport(driver, sauceLink);
				} catch (Exception e) {
					// TODO: handle exception
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {

			//			String platformName = capabilities.getCapability("platformName").toString();
			//			String platformVersion = capabilities.getCapability("platformVersion").toString();
			//			
			//			Log.addExecutionEnvironmentToReport(driver, platformName+"_"+platformVersion);
			try {
				Field f = Reporter.getCurrentTestResult().getClass().getDeclaredField("m_startMillis");
				f.setAccessible(true);
				f.setLong(Reporter.getCurrentTestResult(), Calendar.getInstance().getTime().getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.fail("Unable to create mobile driver session with given URL and DesiredCapabilities. Please try with Saucelabs.");
		}
		return driver;
	}
	
	private static AppiumDriver<MobileElement> getAppiumWebDriverForLambdaTest(String testName, String udid) {
		AppiumDriver<MobileElement> driver = null;

		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		if(System.getProperty("runLambdaTestFromLocal")!=null ? System.getProperty("runLambdaTestFromLocal").equals("true"):configProperty.getProperty("runLambdaTestFromLocal").equalsIgnoreCase("true"))
		{
			
		}
			String appiumURL = "https://" + configProperty.getProperty("lambdaUserName") + ":" + configProperty.getProperty("lambdaAuthKey") + "@hub.lambdatest.com/wd/hub";
			String browserName =Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browserName");
		    String browserVersion = configProperty.hasProperty("browserVersion")
		                ? configProperty.getProperty("browserVersion")
		                        : null;
		       capabilities.setCapability("name", testName);
		       capabilities.setCapability("platformName", configProperty.getProperty("DevicePlatFormName"));
		       capabilities.setCapability("deviceName", configProperty.getProperty("DeviceName"));
		       capabilities.setCapability("platformVersion", configProperty.getProperty("DevicePlatFormVersion"));
		       capabilities.setCapability("browserName",configProperty.getProperty("DeviceBrowserName"));
		       capabilities.setCapability("version",configProperty.getProperty("DeviceBrowserVersion"));
		       capabilities.setCapability("visual", configProperty.getProperty("Visual"));
		       capabilities.setCapability("network", configProperty.getProperty("network"));
		       capabilities.setCapability("video", configProperty.getProperty("video"));
		       capabilities.setCapability("console", configProperty.getProperty("console"));
		       if(configProperty.getProperty("DevicePlatFormName").equalsIgnoreCase("android")) {
		       ChromeOptions chromeOptions = new ChromeOptions();
			   chromeOptions.setExperimentalOption("w3c", false);
			   capabilities.merge(chromeOptions);  
		       }
		       
			
	            try {
	                driver = new AppiumDriver(new URL(appiumURL),capabilities);
	   //             driver.manage().window().maximize();
	            }
	            catch(Exception e) {
	                e.getMessage();
	            }
	            String lambdaSessionId = (((RemoteWebDriver) driver).getSessionId()).toString();        
	            String lambdaLink ="https://automation.lambdatest.com/logs/?sessionID="+lambdaSessionId;
	            logger.debug("LambdaTest link for " + testName + ":: " + lambdaLink);
	            Log.addExecutionEnvironmentToReport(driver, "Chrome"+"_"+platformName);
	            Log.addLambdaJobUrlToReport(driver, lambdaLink);
	            return driver;
		}

	/**
	 * To clear the stale driver sessions and to free the udid of the devices in
	 * the driverBuffer and the deviceBuffer respectively
	 */
	private static void clearStaleDriverSessions() {
		synchronized (deviceBuffer) {
			for (String udid : deviceBuffer.keySet()) {
				try {
					AppiumDriver<MobileElement> driver = driverBuffer.get(udid);
					if (driver != null) {
						driver.getSessionDetails().toString();
					}
				} catch (Exception e) {
					deviceBuffer.put(udid, true);
					driverBuffer.put(udid, null);
				}
			}
		}
	}
	
	



	/**
	 * To get the active platform name. Returns platformName value.
	 * 
	 * @param platformName
	 *                    - platformName of the device
	 */
	public static String getPlatformName(){
		return platformName;
	}

	/**
	 * generates an md5 HMAC digest based on the provided key and message.
	 * 
	 * @param keyString
	 *            Secret key
	 * @param msg
	 *            The message to be authenticated
	 * @return the digest
	 */
	public static String newHMACMD5Digest(String keyString, String msg) {
		String sEncodedString = null;
		try {
			SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacMD5");
			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(key);

			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

			StringBuffer hash = new StringBuffer();

			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			sEncodedString = hash.toString();
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		}
		return sEncodedString;
	}

}