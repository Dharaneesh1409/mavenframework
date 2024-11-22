package ICar.support;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.manager.SeleniumManager;

import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.xml.XmlTest;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

/**
 * WebdriverFactory class used to get a web driver instance, depends on the user
 * requirement as driverHost, driverPort and browserName we adding the
 * desiredCapabilities and other static action initialized here and some methods
 * used to retrieve the Hub and node information. It also consists page wait
 * load for images/frames/document
 */

public class WebDriverFactory {

	
	private static Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	private static MobileEmulationUserAgentConfiguration mobEmuUA = new MobileEmulationUserAgentConfiguration();

	static String driverHost;
	static String driverPort;
	static String browserName;
	static String deviceName;
	static URL hubURL;
	static Proxy zapProxy = new Proxy();

	static DesiredCapabilities ieCapabilities1 = new DesiredCapabilities();
	static DesiredCapabilities firefoxCapabilities1 = new DesiredCapabilities();
	static DesiredCapabilities chromeCapabilities1 = new DesiredCapabilities();
	static DesiredCapabilities safariCapabilities1 = new DesiredCapabilities();
	static DesiredCapabilities edgeCapabilities1 = new DesiredCapabilities();
	static ChromeOptions opt = new ChromeOptions();
	public static ExpectedCondition<Boolean> documentLoad;
	public static ExpectedCondition<Boolean> framesLoad;
	public static ExpectedCondition<Boolean> imagesLoad;
	public static int maxPageLoadWait = 90;
	public static int maxWindowWait = 90;

	static {
		try {
			documentLoad = new ExpectedCondition<Boolean>() {
				public final Boolean apply(final WebDriver driver) {
					final JavascriptExecutor js = (JavascriptExecutor) driver;
					boolean docReadyState = false;
					try {
						docReadyState = (Boolean) js.executeScript(
								"return (function() { if (document.readyState != 'complete') {  return false; } if (window.jQuery != null && window.jQuery != undefined && window.jQuery.active) { return false;} if (window.jQuery != null && window.jQuery != undefined && window.jQuery.ajax != null && window.jQuery.ajax != undefined && window.jQuery.ajax.active) {return false;}  if (window.angular != null && angular.element(document).injector() != null && angular.element(document).injector().get('$http').pendingRequests.length) return false; return true;})();");
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
								docReadyState = docReadyState && (Boolean) js.executeScript(
										"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
										images.get(i));
								if (!docReadyState) {
									break;
								}
							} catch (StaleElementReferenceException e) {
								images = driver.findElements(By.cssSelector("img[src]"));
								i--;
								continue;
							} catch (WebDriverException e) {

								// setting the true value if any exception arise
								// Ex:: inside frame or switching to new windows
								// or
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
								docReadyState = docReadyState
										&& (Boolean) js.executeScript("return (document.readyState==\"complete\")");
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

			XmlTest test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
			driverHost = System.getProperty("hubHost") != null ? System.getProperty("hubHost")
					: test.getParameter("deviceHost");
			driverPort = System.getProperty("hubPort") != null ? System.getProperty("hubPort")
					: test.getParameter("devicePort");

			maxPageLoadWait = configProperty.getProperty("maxPageLoadWait") != null
					? Integer.valueOf(configProperty.getProperty("maxPageLoadWait"))
					: maxPageLoadWait;

			opt.addArguments("--ignore-certificate-errors");
			opt.addArguments("--disable-bundled-ppapi-flash");
			opt.addArguments("--disable-extensions");
			opt.addArguments("--disable-web-security");
			opt.addArguments("--always-authorize-plugins");
			opt.addArguments("--allow-running-insecure-content");
			opt.addArguments("--test-type");
			opt.addArguments("--enable-npapi");
			chromeCapabilities1.setCapability(CapabilityType.TAKES_SCREENSHOT, true);

			try {
				hubURL = new URL("http://" + driverHost + ":" + driverPort + "/wd/hub");
			} catch (MalformedURLException e) {
				// e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to get instance of web driver using default parameters
	 * 
	 * @return browserName - Browser name
	 * @throws MalformedURLException
	 */
	public static WebDriver get() throws MalformedURLException {

		String browser = System.getProperty("browserName") != null ? System.getProperty("browserName")
				: configProperty.getProperty("browserName").toLowerCase().trim();
		String platform = System.getProperty("platformName") != null ? System.getProperty("platformName")
				: configProperty.getProperty("platform").toLowerCase().trim();

		// First priority for System Variable
		// Second priority for XML
		// Last priority for Config.properties

		browserName = (System.getProperty("browserName") != null ? System.getProperty("browserName")
				: !Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browserName")
						.toLowerCase().isEmpty()
								? Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
										.getParameter("browserName").toLowerCase()
								: browser + "_" + platform).toLowerCase();

		if (configProperty.getProperty("runMobileWeb").equals("true"))
			return AppiumDriverFactory.get();
		else
			return getDriver(browserName, null);
	}

	/**
	 * Method to get instance of web driver using browser details
	 * 
	 * @return driver - WebDriver
	 * @throws MalformedURLException
	 */
	public static WebDriver get(String browserSetup) throws MalformedURLException {
		return getDriver(browserSetup, null);
	}

	/**
	 * To generates an md5 HMAC digest based on the provided key and message.
	 * 
	 * @param keyString
	 *            - Secret key
	 * @param msg
	 *            - The message to be authenticated
	 * @return sEncodedString - the digest
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

	/**
	 * Get the test session Node IP address,port when executing through Grid
	 * 
	 * @param driver
	 *            : WebDriver
	 * @return nodeIP - Session ID
	 * @throws Exception
	 */
	public static final String getTestSessionNodeIP(final WebDriver driver) throws Exception {
		XmlTest test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		driverHost = System.getProperty("hubHost") != null ? System.getProperty("hubHost")
				: test.getParameter("deviceHost");
		driverPort = test.getParameter("devicePort");
		HttpHost host = new HttpHost(driverHost, Integer.parseInt(driverPort));
		HttpClient client = HttpClientBuilder.create().build();
		URL testSessionApi = new URL("http://" + driverHost + ":" + driverPort + "/grid/api/testsession?session="
				+ ((RemoteWebDriver) driver).getSessionId());
		BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST",
				testSessionApi.toExternalForm());
		HttpResponse response = client.execute(host, r);
		JSONObject object = new JSONObject(EntityUtils.toString(response.getEntity()));
		String nodeIP = object.getString("proxyId").toLowerCase();
		nodeIP = nodeIP.replace("http://", "");
		nodeIP = nodeIP.replaceAll(":[0-9]{1,5}", "").trim();
		return nodeIP;
	}

	/**
	 * Get the test session Hub IP address, port when executing through Grid
	 * 
	 * @param driver
	 *            - WebDriver
	 * @return nodeIP - Session ID
	 * @throws Exception
	 */
	public static final String getHubSession(final WebDriver driver) throws Exception {
		XmlTest test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		driverHost = System.getProperty("hubHost") != null ? System.getProperty("hubHost")
				: test.getParameter("deviceHost");
		driverPort = test.getParameter("devicePort");
		HttpHost host = new HttpHost(driverHost, Integer.parseInt(driverPort));
		HttpClient client = HttpClientBuilder.create().build();
		URL testSessionApi = new URL("http://" + driverHost + ":" + driverPort + "/grid/api/testsession?session="
				+ ((RemoteWebDriver) driver).getSessionId());
		BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST",
				testSessionApi.toExternalForm());
		HttpResponse response = client.execute(host, r);
		JSONObject object = new JSONObject(EntityUtils.toString(response.getEntity()));
		String nodeIP = object.getString("proxyId").toLowerCase();
		nodeIP = nodeIP.replace("http://", "");
		nodeIP = nodeIP.replaceAll(":[0-9]{1,5}", "").trim();
		return nodeIP;
	}

	/**
	 * To storing chrome mobile emulation configurations(width, height, pixelRatio)
	 * and returning the capabilities
	 * 
	 * <p>
	 * if required feasible result then set andriodWidth, androidHeight,
	 * androidPixelRatio, iosWidth,androidHeight and iosPixelRatio values in the
	 * config.propeties
	 *
	 * @param deviceName
	 *            - device name
	 * @param userAgent
	 *            - User agent
	 * @return chromeCapabilities
	 */
	public static DesiredCapabilities setChromeUserAgent(String deviceName, String userAgent) {
		Map<String, Object> deviceMetrics = new HashMap<String, Object>();
		Map<String, Object> mobileEmulation = new HashMap<String, Object>();

		int width = 0;
		int height = 0;
		Double pixRatio = null;

		width = Integer.valueOf(mobEmuUA.getDeviceWidth(deviceName));
		height = Integer.valueOf(mobEmuUA.getDeviceHeight(deviceName));
		pixRatio = Double.valueOf(mobEmuUA.getDevicePixelRatio(deviceName));

		deviceMetrics.put("width", width);
		deviceMetrics.put("height", height);
		deviceMetrics.put("pixelRatio", pixRatio);
		mobileEmulation.put("deviceMetrics", deviceMetrics);
		mobileEmulation.put("userAgent", userAgent);
		Log.event("mobileEmulation settings::==> " + mobileEmulation);
		opt.setExperimentalOption("mobileEmulation", mobileEmulation);
		chromeCapabilities1.setCapability(ChromeOptions.CAPABILITY, opt);
		return chromeCapabilities1;
	}

	/**
	 * To print the Har Summary details
	 * 
	 * @param har
	 */
	public static void printHarData(Har har) {
		HarLog log = har.getLog();
		List<HarEntry> harEntries = log.getEntries();
		Long totalSize = 0L;
		int callCount = 0;
		long totalTime = 0;
		for (HarEntry entry : harEntries) {
			callCount++;
			if (entry.getResponse() == null) {
				continue;
			}
			totalSize += entry.getResponse().getBodySize();
			totalTime += entry.getTime(TimeUnit.MILLISECONDS);
		}
		HarSummary summary = new HarSummary((double) totalSize / 1024, callCount, totalTime);
		Log.message("#################<b>PERF DATA</b>###################");
		Log.message("<br>");
		Log.message("Call count : " + summary.getCallCount());
		Log.message("Size : " + summary.getTotalPayloadSize() / 1024 + " MB");
		Log.message("Total load time : " + summary.getTotalLoadTime() / 1000 + " seconds");
	}

	/**
	 * To make sure sauce performance is enabled
	 * 
	 * @return
	 */
	public static boolean isSaucePerformanceEnabled() {
		if (System.getProperty("runSaucePerformance") != null
				&& System.getProperty("runSaucePerformance").trim().equalsIgnoreCase("true")
						? true
						: System.getenv("runSaucePerformance") != null
								&& System.getenv("runSaucePerformance").trim().equalsIgnoreCase("true") ? true
										: configProperty.hasProperty("runSaucePerformance") && configProperty
												.getProperty("runSaucePerformance").trim().equalsIgnoreCase("true")) {
			if ((System.getProperty("SELENIUM_DRIVER") != null
					&& StringUtils.containsIgnoreCase(System.getProperty("SELENIUM_DRIVER"), "chrome"))
					|| (System.getenv("SELENIUM_DRIVER") != null
							&& StringUtils.containsIgnoreCase(System.getenv("SELENIUM_DRIVER"), "chrome"))) {
				return true;
			}
		}
		return false;
	}

	/*
	 * generate sauce performance report
	 */
	public static String generateSaucePerformanceReport(final WebDriver driver) {
		String sauceLink = null, sauceUserName = null, sauceAuthKey = null;
		String saucelabsSessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
		sauceUserName = configProperty.hasProperty("sauceUserName") ? configProperty.getProperty("sauceUserName")
				: null;
		sauceAuthKey = configProperty.hasProperty("sauceAuthKey") ? configProperty.getProperty("sauceAuthKey") : null;
		sauceLink = "http://saucelabs.com/job-embed/" + saucelabsSessionId + ".js?auth="
				+ newHMACMD5Digest(sauceUserName + ":" + sauceAuthKey, saucelabsSessionId);
		return sauceLink;
	}

	/**
	 * Webdriver to get the web driver with browser name and platform and setting
	 * the desired capabilities for browsers
	 * 
	 * @param browserWithPlatform
	 *            - Browser With Platform
	 * @param proxy
	 *            - Proxy
	 * @return driver - WebDriver Instance
	 * @throws MalformedURLException
	 */
	public static WebDriver getDriver(String browserWithPlatform, Proxy proxy) throws MalformedURLException {
		String browser = null;
		String platform = null;
		String sauceUserName = null;
		String sauceAuthKey = null;
		WebDriver driver = null;
		String userAgent = null;
		long startTime = StopWatch.startTime();

		// Get invoking test name
		String callerMethodName = new Exception().getStackTrace()[2].getMethodName();
		Log.event("TestCaseID:: " + callerMethodName);

		// driver initialization part
		synchronized (System.class) {
			// From local to sauce lab for browser test
			if (configProperty.hasProperty("runSauceLabFromLocal")
					&& configProperty.getProperty("runSauceLabFromLocal").trim().equalsIgnoreCase("true")) {

				sauceUserName = configProperty.hasProperty("sauceUserName")
						? configProperty.getProperty("sauceUserName")
						: null;
				sauceAuthKey = configProperty.hasProperty("sauceAuthKey") ? configProperty.getProperty("sauceAuthKey")
						: null;

				driver = createSaucelabsWebDriver(callerMethodName, sauceUserName, sauceAuthKey);
				return driver;
			}
			if (configProperty.hasProperty("runLambdaTestFromLocal")
                    && configProperty.getProperty("runLambdaTestFromLocal").trim().equalsIgnoreCase("true")) {
           String     lambdaUserName = configProperty.hasProperty("lambdaUserName")
                        ? configProperty.getProperty("lambdaUserName")
                                : null;
            String    lambdaAuthKey = configProperty.hasProperty("lambdaAuthKey") ? configProperty.getProperty("lambdaAuthKey")
                        : null;
               driver = createlambdaTestWebDriver(callerMethodName, lambdaUserName, lambdaAuthKey);
                return driver;
            }
		}
		
		// To support local to local execution by grid configuration
		if (browserWithPlatform.contains("_")) {
			browser = browserWithPlatform.split("_")[0].toLowerCase().trim();
			platform = browserWithPlatform.split("_")[1].toUpperCase().trim();
			if (configProperty.hasProperty("runSauceLabFromLocal")
					&& configProperty.getProperty("runSauceLabFromLocal").trim().equalsIgnoreCase("false")) {
				platform = platform.split(" ")[0];
			}

		} else {
			platform = "ANY";
			browser = browserWithPlatform;
		}

		try {
			if ("chrome".equalsIgnoreCase(browser)) {
				ChromeOptions chromeOpt = new ChromeOptions();
				if (configProperty.hasProperty("runUserAgentDeviceTest")
						&& configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true")) {
					deviceName = configProperty.hasProperty("deviceName") ? configProperty.getProperty("deviceName")
							: null;
					userAgent = mobEmuUA.getUserAgent(deviceName);
					if (userAgent != null && deviceName != null) {
						driver = new RemoteWebDriver(hubURL, setChromeUserAgent(deviceName, userAgent));
					} else {
						logger.error(
								"Given user agent configuration not yet implemented (or) check the parameters(deviceName) value in config.properties: "
										+ deviceName);
					}
				} else {
//					chromeOpt.setPlatformName("Windows 11");
//					chromeOpt.setBrowserVersion("113");
					chromeOpt.addArguments("--remote-allow-origins=*");
					 driver = new ChromeDriver(chromeOpt);
					
				}
			} else if ("iexplorer".equalsIgnoreCase(browser)) {
				ieCapabilities1.setCapability("enablePersistentHover", false);
				ieCapabilities1.setCapability("ignoreZoomSetting", true);
				ieCapabilities1.setCapability("nativeEvents", false);
				ieCapabilities1.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				ieCapabilities1.setBrowserName("internet explorer");
				ieCapabilities1.setPlatform(Platform.fromString(platform));

				if (proxy != null)
					ieCapabilities1.setCapability(CapabilityType.PROXY, proxy);

				driver = new RemoteWebDriver(hubURL, ieCapabilities1);
			} else if (browser.toLowerCase().contains("edge")) {
				edgeCapabilities1.setPlatform(Platform.fromString(platform));
				edgeCapabilities1.setBrowserName("MicrosoftEdge");
				driver = new RemoteWebDriver(hubURL, edgeCapabilities1);
			} else if ("safari".contains(browser.split("\\&")[0])) {
				if (configProperty.hasProperty("runUserAgentDeviceTest")
						&& configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true")) {
					deviceName = configProperty.hasProperty("deviceName") ? configProperty.getProperty("deviceName")
							: null;
					userAgent = mobEmuUA.getUserAgent(deviceName);
					if (userAgent != null && deviceName != null) {
						driver = new RemoteWebDriver(hubURL, setChromeUserAgent(deviceName, userAgent));
					} else {
						logger.error(
								"Given user agent configuration not yet implemented (or) check the parameters(deviceName) value in config.properties: "
										+ deviceName);
					}
				} else {
					safariCapabilities1.setCapability("prerun",
							"https://gist.githubusercontent.com/saucyallison/3a73a4e0736e556c990d/raw/d26b0195d48b404628fc12342cb97f1fc5ff58ec/disable_fraud.sh");
					driver = new RemoteWebDriver(hubURL, safariCapabilities1);
				}
				// To run a ZAP TC's use Browser opt as zap
			} else if ("zap".equalsIgnoreCase(browser)) {
				Proxy zapChromeProxy = new Proxy();
				zapChromeProxy.setHttpProxy("localhost:8080");
				zapChromeProxy.setFtpProxy("localhost:8080");
				zapChromeProxy.setSslProxy("localhost:8080");
				chromeCapabilities1.setCapability(ChromeOptions.CAPABILITY, opt);
				//chromeOpt.addArguments("--remote-allow-origins=*");
				chromeCapabilities1.setCapability(CapabilityType.PROXY, zapChromeProxy);
				chromeCapabilities1.setPlatform(Platform.fromString(platform));
				driver = new RemoteWebDriver(hubURL, chromeCapabilities1);
			} else {
				synchronized (WebDriverFactory.class) {
					FirefoxOptions foptions = new FirefoxOptions();
					String headless = configProperty.getProperty("headless") != null
							? configProperty.getProperty("headless")
							: "false";

					if (!headless.equals("true") && !headless.equals("false"))
						headless = "false";

					foptions.setHeadless(Boolean.parseBoolean(headless));
					firefoxCapabilities1.setCapability("unexpectedAlertBehaviour", "ignore");
					firefoxCapabilities1.setPlatform(Platform.fromString(platform));
					firefoxCapabilities1.merge(foptions);
					driver = new RemoteWebDriver(hubURL, firefoxCapabilities1);
				}
			}
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(maxPageLoadWait, TimeUnit.SECONDS);
			Assert.assertNotNull(driver,
					"Driver did not intialize...\n Please check if hub is running / configuration settings are corect.");

		} catch (UnreachableBrowserException e) {
			e.printStackTrace();
			throw new SkipException("Hub is not started or down.");
		} catch (WebDriverException e) {

			try {
				if (driver != null) {
					driver.quit();
				}
			} catch (Exception e1) {
				e.printStackTrace();
			}

			if (e.getMessage().toLowerCase().contains("error forwarding the new session empty pool of vm for setup")) {
				throw new SkipException("Node is not started or down.");
			} else if (e.getMessage().toLowerCase()
					.contains("error forwarding the new session empty pool of vm for setup")
					|| e.getMessage().toLowerCase().contains("cannot get automation extension")
					|| e.getMessage().toLowerCase().contains("chrome not reachable")) {
				Log.message("&emsp;<b> --- Re-tried as browser crashed </b>");
				try {
					driver.quit();
				} catch (WebDriverException e1) {
					e.printStackTrace();
				}
				driver = get();
			} else {
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception encountered in getDriver Method." + e.getMessage().toString());
		} finally {
			// ************************************************************************************************************
			// * Update start time of the tests once free slot is assigned by
			// RemoteWebDriver - Just for reporting purpose
			// *************************************************************************************************************/
			try {
				Field f = Reporter.getCurrentTestResult().getClass().getDeclaredField("m_startMillis");
				f.setAccessible(true);
				f.setLong(Reporter.getCurrentTestResult(), Calendar.getInstance().getTime().getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		Log.event("Driver::initialize::Get", StopWatch.elapsedTime(startTime));
		try {
			browserWithPlatform = browserWithPlatform.split(" ")[0];
		} catch (Exception e) {
			// TODO: handle exception
		}
		Log.addExecutionEnvironmentToReport(driver, browserWithPlatform);
		return driver;
	}

	public static DesiredCapabilities getUserAgentDesiredCapabilities(DesiredCapabilities caps, String deviceName,
			String userAgent, String sauceConnect, String tunnelIdentifier) {

		Map<String, Object> deviceMetrics = new HashMap<String, Object>();
		Map<String, Object> mobileEmulation = new HashMap<String, Object>();

		int width = 0;
		int height = 0;
		Double pixRatio = null;

		width = Integer.valueOf(mobEmuUA.getDeviceWidth(deviceName));
		height = Integer.valueOf(mobEmuUA.getDeviceHeight(deviceName));
		pixRatio = Double.valueOf(mobEmuUA.getDevicePixelRatio(deviceName));

		deviceMetrics.put("width", width);
		deviceMetrics.put("height", height);
		deviceMetrics.put("pixelRatio", pixRatio);
		mobileEmulation.put("deviceMetrics", deviceMetrics);
		mobileEmulation.put("userAgent", userAgent);
		Log.event("mobileEmulation settings::==> " + mobileEmulation);

		ChromeOptions opt = new ChromeOptions();
		opt.setExperimentalOption("mobileEmulation", mobileEmulation);
		if(Boolean.parseBoolean(sauceConnect)) {
			opt.setCapability("tunnelIdentifier", tunnelIdentifier);
		}
		caps.setCapability(ChromeOptions.CAPABILITY, opt);
		return caps;
	}

	public static WebDriver createSaucelabsWebDriver(String testName, String sauceUserName, String sauceAuthKey)
			throws MalformedURLException {
		// right now we only support sauce labs

		WebDriver driver = null;
		String userAgent = null;

		DesiredCapabilities sauceDesiredCapabilities = new DesiredCapabilities();

		String screenResolution = configProperty.hasProperty("screenResolution")
				? configProperty.getProperty("screenResolution")
				: null;
		String seleniumVersion = configProperty.hasProperty("seleniumVersion")
				? configProperty.getProperty("seleniumVersion")
				: null;
		String iedriverVersion = configProperty.hasProperty("iedriverVersion")
				? configProperty.getProperty("iedriverVersion")
				: null;
		String chromedriverVersion = configProperty.hasProperty("chromedriverVersion")
				? configProperty.getProperty("chromedriverVersion")
				: null;
		String maxTestDuration = configProperty.hasProperty("maxTestDuration")
				? configProperty.getProperty("maxTestDuration")
				: null;
		String commandTimeout = configProperty.hasProperty("commandTimeout")
				? configProperty.getProperty("commandTimeout")
				: null;
		String idleTimeout = configProperty.hasProperty("idleTimeout") ? configProperty.getProperty("idleTimeout")
				: null;

		String browserName = configProperty.hasProperty("browserName") ? configProperty.getProperty("browserName")
				: null;

		String browserVersion = configProperty.hasProperty("browserVersion")
				? configProperty.getProperty("browserVersion")
				: null;

		String platformName = configProperty.hasProperty("platform") ? configProperty.getProperty("platform") : null;
		
		String sauceConnect = configProperty.hasProperty("sauceConnect") ? configProperty.getProperty("sauceConnect") : null;
		String tunnelIdentifier = configProperty.hasProperty("tunnelIdentifier") ? configProperty.getProperty("tunnelIdentifier") : "";
		
		MutableCapabilities sauceOptions = new MutableCapabilities();
		sauceOptions.setCapability("screenResolution", screenResolution);
		sauceOptions.setCapability("name", testName);
		sauceOptions.setCapability("seleniumVersion", seleniumVersion);
		sauceOptions.setCapability("iedriverVersion", iedriverVersion);
		sauceOptions.setCapability("chromedriverVersion", chromedriverVersion);
		sauceOptions.setCapability("maxDuration", maxTestDuration);
		sauceOptions.setCapability("commandTimeout", commandTimeout);
		sauceOptions.setCapability("idleTimeout", idleTimeout);
		sauceOptions.setCapability("recordScreenshots", true);
		sauceOptions.setCapability("recordVideo", true);
		sauceOptions.setCapability("videoUploadOnPass", true);
//		sauceOptions.setCapability("tunnelIdentifier", "4da3544cf3684feca84ecac76b6fbbe3");
		if (WebDriverFactory.isSaucePerformanceEnabled()) {
			sauceDesiredCapabilities.setCapability("extendedDebugging", true);
			sauceDesiredCapabilities.setCapability("capturePerformance", true);
		}

		if (browserName.toLowerCase().contains("chrome")) {
			ChromeOptions browserOptions = new ChromeOptions();
			browserOptions.setCapability("browserName", browserName);
			browserOptions.setCapability("browserVersion", browserVersion);
			browserOptions.setCapability("platformName", platformName);
			if(Boolean.parseBoolean(sauceConnect)) {
				browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
			}
			// browserOptions.setExperimentalOption("w3c", true);
			browserOptions.setCapability("sauce:options", sauceOptions);
			sauceDesiredCapabilities.setCapability(ChromeOptions.CAPABILITY, browserOptions);
		} else if (browserName.toLowerCase().contains("firefox")) {
			FirefoxOptions browserOptions = new FirefoxOptions();
			browserOptions.setCapability("browserName", browserName);
			browserOptions.setCapability("browserVersion", browserVersion);
			browserOptions.setCapability("platformName", platformName);
			if(Boolean.parseBoolean(sauceConnect)) {
				browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
			}
			browserOptions.setCapability("sauce:options", sauceOptions);
			sauceDesiredCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, browserOptions);
		} else if (browserName.toLowerCase().contains("iexplorer")) {
			InternetExplorerOptions browserOptions = new InternetExplorerOptions();
			browserOptions.setCapability("browserName", "Internet Explorer");
			browserOptions.setCapability("browserVersion", browserVersion);
			browserOptions.setCapability("platformName", platformName);
			if(Boolean.parseBoolean(sauceConnect)) {
				browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
			}
			browserOptions.setCapability("sauce:options", sauceOptions);
			sauceDesiredCapabilities.merge(browserOptions);
		} else if (browserName.toLowerCase().contains("edge")) {
			EdgeOptions browserOptions = new EdgeOptions();
			// browserOptions.setCapability("browserName", "MicrosoftEdge");
			browserOptions.setCapability("browserVersion", browserVersion);
			browserOptions.setCapability("platformName", platformName);
			browserOptions.setCapability("sauce:options", sauceOptions);
			if(Boolean.parseBoolean(sauceConnect)) {
				browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
			}
			sauceDesiredCapabilities.merge(browserOptions);
		} else if (browserName.toLowerCase().contains("safari")) {
			screenResolution = "1920x1440";
			sauceOptions.setCapability("screenResolution", screenResolution);
			SafariOptions browserOptions = new SafariOptions();
			browserOptions.setCapability("browserVersion", browserVersion);
			browserOptions.setCapability("platformName", platformName);
			if(Boolean.parseBoolean(sauceConnect)) {
				browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
			}
			browserOptions.setCapability("sauce:options", sauceOptions);
			sauceDesiredCapabilities.merge(browserOptions);
		}

		if (configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true")) {
			deviceName = configProperty.getProperty("deviceName") != null ? configProperty.getProperty("deviceName")
					: null;
			userAgent = mobEmuUA.getUserAgent(deviceName) != null ? mobEmuUA.getUserAgent(deviceName) : null;

			if (deviceName != null && userAgent != null) {
				driver = new RemoteWebDriver(
						new URL("http://" + sauceUserName + ":" + sauceAuthKey + "@ondemand.saucelabs.com:80/wd/hub"),
						getUserAgentDesiredCapabilities(sauceDesiredCapabilities, deviceName, userAgent, sauceConnect, tunnelIdentifier));

			} else {
				logger.error(
						"Invalid mobile emulation configuration, check theparameters(deviceName) value: " + deviceName);
			}
		} else {
			driver = new RemoteWebDriver(
					new URL("http://" + sauceUserName + ":" + sauceAuthKey + "@ondemand.saucelabs.com:80/wd/hub"),
					sauceDesiredCapabilities);
			driver.manage().window().maximize();
		}

		String saucelabsSessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
		String sauceLink = "http://saucelabs.com/jobs/" + saucelabsSessionId + "?auth=" + newHMACMD5Digest(
				System.getenv("SAUCE_USER_NAME") + ":" + System.getenv("SAUCE_API_KEY"), saucelabsSessionId);
		logger.debug("Saucelab link for " + testName + ":: " + sauceLink);
		Log.addExecutionEnvironmentToReport(driver, browserName+"_"+platformName);
		Log.addSauceJobUrlToReport(driver, sauceLink);
		// Log.addTestRunMachineInfo(driver, sauceLink);
		return driver;
	}
	
	public static WebDriver createlambdaTestWebDriver(String testName, String lambdaUserName, String lambdaAuthKey)
            throws MalformedURLException {
       WebDriver driver = null;
        String userAgent = null;
       DesiredCapabilities lambdaDesiredCapabilities = new DesiredCapabilities();
       String browserName =Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browserName");
       String browserVersion = configProperty.hasProperty("browserVersion")
                ? configProperty.getProperty("browserVersion")
                        : null;
        System.out.println(browserVersion);
        String platformName = configProperty.hasProperty("platform") ? configProperty.getProperty("platform") : null;
       String lambdaConnect = configProperty.hasProperty("lambdaConnect") ? configProperty.getProperty("lambdaConnect") : null;
        String tunnelIdentifier = configProperty.hasProperty("tunnelIdentifier") ? configProperty.getProperty("tunnelIdentifier") : "";
       if (WebDriverFactory.isSaucePerformanceEnabled()) {
            lambdaDesiredCapabilities.setCapability("extendedDebugging", true);
            lambdaDesiredCapabilities.setCapability("capturePerformance", true);
        }
       if (browserName.toLowerCase().contains("chrome")) {
            ChromeOptions browserOptions = new ChromeOptions();
            browserOptions.setCapability("browserName", browserName);
            browserOptions.setCapability("browserVersion", browserVersion);
            browserOptions.setCapability("platformName", platformName);
            browserOptions.setCapability("build", "LambdaTestSampleApp");
            browserOptions.setCapability("name", testName);
            browserOptions.setCapability("commandLog", true);
            browserOptions.setCapability("systemLog", true);
            if(Boolean.parseBoolean(lambdaConnect)) {
                browserOptions.setCapability("tunnel", true);
                browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);                
            }            
            lambdaDesiredCapabilities.setCapability(ChromeOptions.CAPABILITY, browserOptions);
       }
       if (browserName.toLowerCase().contains("safari")) {
    	   lambdaDesiredCapabilities.setCapability("browserName", browserName);
    	   lambdaDesiredCapabilities.setCapability("browserVersion", browserVersion);
    	   lambdaDesiredCapabilities.setCapability("platformName", platformName);
    	   lambdaDesiredCapabilities.setCapability("build", "LambdaTestSampleApp");
    	   lambdaDesiredCapabilities.setCapability("name", testName);
    	   lambdaDesiredCapabilities.setCapability("commandLog", true);
    	   lambdaDesiredCapabilities.setCapability("systemLog", true);
           if(Boolean.parseBoolean(lambdaConnect)) {
        	   lambdaDesiredCapabilities.setCapability("tunnel", true);
        	   lambdaDesiredCapabilities.setCapability("tunnelIdentifier", tunnelIdentifier);                
           }            
      } 
       else if (browserName.toLowerCase().contains("firefox")) {
            FirefoxOptions browserOptions = new FirefoxOptions();
            browserOptions.setCapability("browserName", browserName);
            browserOptions.setCapability("browserVersion", browserVersion);
            browserOptions.setCapability("platformName", platformName);
            if(Boolean.parseBoolean(lambdaConnect)) {
                browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
            }            
            lambdaDesiredCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, browserOptions);
        }
       if (configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true")) {
            deviceName = configProperty.getProperty("deviceName") != null ? configProperty.getProperty("deviceName")
                    : null;
            userAgent = mobEmuUA.getUserAgent(deviceName) != null ? mobEmuUA.getUserAgent(deviceName) : null;
           if (deviceName != null && userAgent != null) {
                driver = new RemoteWebDriver(
                        new URL("https://" + lambdaUserName + ":" + lambdaAuthKey + "@hub.lambdatest.com/wd/hub"),
                        getUserAgentDesiredCapabilities(lambdaDesiredCapabilities, deviceName, userAgent, lambdaConnect, tunnelIdentifier));
           } else {
                logger.error(
                        "Invalid mobile emulation configuration, check theparameters(deviceName) value: " + deviceName);
            }
        } else {
            try {
                driver = new RemoteWebDriver(
                        new URL("https://" + lambdaUserName + ":" + lambdaAuthKey + "@hub.lambdatest.com/wd/hub"),
                        lambdaDesiredCapabilities);
                driver.manage().window().maximize();
            }
            catch(Exception e) {
                e.getMessage();
            }
        }
       String lambdaSessionId = (((RemoteWebDriver) driver).getSessionId()).toString();        
       String lambdaLink ="https://automation.lambdatest.com/logs/?sessionID="+lambdaSessionId;
       logger.debug("LambdaTest link for " + testName + ":: " + lambdaLink);
        Log.addExecutionEnvironmentToReport(driver, browserName+"_"+platformName);
       Log.addLambdaJobUrlToReport(driver, lambdaLink);
        return driver;
    }

} // WebDriverFactory
