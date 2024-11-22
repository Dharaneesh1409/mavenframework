package ICar.support;

import java.util.List;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({ "classpath:zapConfig.properties", "classpath:config.properties", "classpath:healenium.properties" })
public interface IPropertiesReader extends Config{

	// Test data properties
	String testDataSource();

	String excelName();

	String googleSheetID();

	String googleCredentialJson();

	String msaccessPath();

	String dbHost();

	int dbPort();

	String dbName();

	String dbUsername();

	String dbPassword();

//EXECUTION MODE

	Boolean runAWS();

	Boolean runSauceLabFromLocal();

	Boolean runBrowserStackFromLocal();

	Boolean runLambdaTestFromLocal();

//AWS CLOUD FORMATION 

	String stackName();

//Browser and Environment Details 

	Boolean runDesktop();

	@Key("browserName")
	String browserName();

	String browserVersion();

	String platform();

	Boolean headless();

	String resolution();

	Boolean BandwidthTest();

//*Run tests from local to Lambda test lab directly

	String lambdaUserName();

	String lambdaAuthKey();

	Boolean lambdaConnect();

	String tunnelIdentifier();

//*Run tests from local to sauce lab directly

	String sauceUserName();

	String sauceAuthKey();

	Boolean sauceConnect();

	// tunnelIdentifier//duplicate
	Boolean runSaucePerformance();

	String screenResolution();

	String seleniumVersion();

	String iedriverVersion();

	String chromedriverVersion();

	String maxTestDuration();

	String commandTimeout();

	String idleTimeout();

//*Run tests from local to Browser Stack lab directly

	String bsUserName();

	String bsAuthKey();

//User Agent (Mobile browser view)

	Boolean runUserAgentDeviceTest();

	String deviceName();

	String deviceOrientation();

//Android App details

	Boolean runMobileApp();

	String appiumVersion();

	String platformNameMobile();

	String platformVersion();

	// deviceName//duplicate
	String apppath();

	String appPathForCloudExecution();

	Boolean runRealDevice();

	List<String> udidList();

	String udid();

	String udid2();

	String host();

	Integer port();

	Integer DevicePollingCount();

	String appType();

	String version();

	String browser();

	String safariInitialUrl();

	Boolean useNewWDA();

	Boolean startIWDP();

	String WinAppPackage();

	String appPathiOS();

	String xcodeOrgId();

	String xcodeSigningId();

	String RealDeviceLogger();

	String XcodeConfigFile();

	Integer newCommandTimeout();

//Mobile Web - Appium Driver Saucelabs

	Boolean runMobileWeb();

	String platformNameWeb();

	String platformVersionWeb();

	String deviceNameWeb();

	String browserNameWeb();

	String appiumVersionWeb();

//Selenium webdriver specific timeouts in seconds

	Integer maxPageLoadWait();

	Integer minElementWait();

	Integer maxElementWait();

	Integer implicitWait();

	Integer maxWindowWait();

//Logging attributes

	Boolean isTakeScreenShot();

	Boolean fullPageScreenshot();

	Boolean embedSauceSession();

	Boolean writeResult();

	Boolean archiveTestResult();

	Boolean ReportBackUp();

	Boolean documentLoad();

	Boolean imageLoad();

	Boolean framesLoad();

//Retry

	Integer MaxRetryCount();

//ReadTestData From GoogleSheet

	String sheetId();

	String range();

// ZAP

	// Security Test- ZAP
	// @Key("ZAP_PROXY_ADDRESS")
	String ZAP_PROXY_ADDRESS();

	// @Key("ZAP_PROXY_PORT")
	int ZAP_PROXY_PORT();

	// @Key("ZAP_API_KEY")
	String ZAP_API_KEY();

	// @Key("title")
	String title();

	// @Key("template")
	String template();

	// @Key("description")
	String description();

	// @Key("reportfilename")
	String reportfilename();

	String projectName();

// Healenium

	String hlmProjectName();

	String hlmUrl();

	String hlmResultsEndpoint();

	String hlmClassToAvoid();

}
