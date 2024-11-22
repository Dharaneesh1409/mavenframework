package ICar.support;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * 
 * ExtentReports Generator (Works with @Listeners(EmailReport.class))
 *
 */
public class ExtentReporter {

	private static ExtentReports extentReport = null;
	private static ExtentSparkReporter sparkReporter = null;
	private static ConcurrentHashMap<Integer, ExtentTest> tests = new ConcurrentHashMap<Integer, ExtentTest>();
	  private static File configFile = new File(System.getProperty("user.dir") + File.separator + "ReportConfig.xml");
	    private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	private static boolean isReportClosed = false;

	private static File getExtentReportConfigFile() {
		File file = null;
		try {
			URL resourceReportConfig = ExtentReporter.class.getClassLoader()
					.getResource(File.separatorChar + "reports" + File.separatorChar + "spark-config.json");

			file = new File(resourceReportConfig.toURI());
		} catch (Exception e) {

		}
		return file;
	}

	/**
	 * To form a unique test name in the format "PackageName.ClassName#MethodName"
	 * 
	 * @param iTestResult
	 * @return String - test name
	 */
	private static String getTestName(ITestResult iTestResult) {
		// String testClassName = iTestResult.getTestClass().getRealClass().getName();
		String testMethodName = iTestResult.getName();
		// return testClassName + "#" + testMethodName;
		return testMethodName;
	}

	/**
	 * To convert milliseconds to Date object
	 * 
	 * @param millis
	 * @return Date
	 */
	private static Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * To set run status for interrupted tests
	 * 
	 * @param iTestResult
	 * @param extentTest
	 */
	private static void setInterruptedTestStatus(ITestResult iTestResult, ExtentTest extentTest) {
		if (!(extentTest.getStatus().equals(Status.PASS))) {
			return;
		}
		switch (iTestResult.getStatus()) {
		case 2:
			if (iTestResult.getThrowable() == null)
				extentTest.log(Status.FAIL, "<font color=\"red\">Test Failed</font>");
			else
				extentTest.log(Status.FAIL, "<div class=\"stacktrace\">"
						+ ExceptionUtils.getStackTrace(iTestResult.getThrowable()) + "</div>");
			break;
		case 3:
			if (iTestResult.getThrowable() == null)
				extentTest.log(Status.SKIP, "<font color=\"orange\">Test Skipped</font>");
			else
				extentTest.log(Status.SKIP, "<div class=\"stacktrace\">"
						+ ExceptionUtils.getStackTrace(iTestResult.getThrowable()) + "</div>");
			break;
		}
	}

	/**
	 * Returns an ExtentReports instance if already exists. Creates new and returns
	 * otherwise.
	 * 
	 * @param iTestResult
	 * @return {@link ExtentReports} - Extent report instance
	 */
	public static synchronized ExtentReports getReportInstance(ITestResult iTestResult) {
		if (extentReport == null) {
			extentReport = new ExtentReports();

			try {
				sparkReporter = new ExtentSparkReporter(getReportPath(iTestResult));
				sparkReporter.loadJSONConfig(getExtentReportConfigFile());
				extentReport.attachReporter(sparkReporter);
			} catch (Exception e) {
				System.out.println("Error creating ExtentReport instance- " + e.getLocalizedMessage());
			}

		}
		return extentReport;
	}

	private static String getReportPath(ITestResult iTestResult) {
		String reportFilePath = new File(iTestResult.getTestContext().getOutputDirectory()).getParent() + File.separator
				+ "ExtentReport.html";
		return reportFilePath;
	}

	/**
	 * To start and return a new extent test instance with given test case
	 * description. Returns the test instance if the test has already been started
	 * 
	 * @param description - test case description
	 * @return {@link ExtentTest} - ExtentTest Instance
	 */
	public static ExtentTest createTest(ExtentReports extent, String description) {
		ExtentTest test = null;
		ITestResult iTestResult = Reporter.getCurrentTestResult();
		String testName = iTestResult != null ? getTestName(iTestResult) : Thread.currentThread().getName();
		Integer hashCode = iTestResult != null ? iTestResult.hashCode() : Thread.currentThread().hashCode();

		if (tests.containsKey(hashCode)) {
			test = tests.get(hashCode);
			if (description != null && !description.isEmpty()) {
				// test.setDescription(description);
			}
		} else {
			if (iTestResult == null || !iTestResult.getMethod().isTest()) {
				test = extent.createTest(testName, description);
				tests.put(hashCode, test);
			} else {
				test = extent.createTest(testName, description).assignCategory(iTestResult.getMethod().getGroups())
						.assignCategory(iTestResult.getTestClass().getRealClass().getName());
				tests.put(hashCode, test);
			}
		}

		/*-
		else {
			if (iTestResult == null || !iTestResult.getMethod().isTest()) {
				test = extent.createTest(testName, description);
			} else {
				test = extent.createTest(testName, description).assignCategory(iTestResult.getMethod().getGroups())
						.assignCategory(iTestResult.getTestClass().getRealClass().getName());
				tests.put(hashCode, test);
			}
		} */

		// System.out.println("Test created hashcode-" + testName + "," + hashCode);

		return test;
	}

	/**
	 * Returns the test instance if the test has already been started. Else creates
	 * a new test with empty description
	 * 
	 * @return {@link ExtentTest} - ExtentTest Instance
	 */
	private static ExtentTest getTest() {
		ExtentTest test = null;
		ITestResult iTestResult = Reporter.getCurrentTestResult();
		String testName = iTestResult != null ? getTestName(iTestResult) : Thread.currentThread().getName();
		Integer hashCode = iTestResult != null ? iTestResult.hashCode() : Thread.currentThread().hashCode();

		if (tests.containsKey(hashCode)) {
			test = tests.get(hashCode);
		} else {
			// System.out.println(testName + " test entry not found");
			// tests.entrySet().stream().forEach(e -> {
			// System.out.println("hashcode-" + e.getKey());
			// });
		}
		return test;
		// return createTest(getReportInstance(Reporter.getCurrentTestResult()), "");
	}

	/**
	 * To start a test with given test case info
	 * 
	 * @param testCaseInfo
	 */
	public static void testCaseInfo(String testCaseInfo) {

		createTest(getReportInstance(Reporter.getCurrentTestResult()),
				"<strong><font size = \"4\" color = \"#000000\">" + testCaseInfo + "</font></strong>");

		// getTest().createNode("<strong><font size = \"4\" color = \"#000080\">" +
		// testCaseInfo + "</font></strong>");
	}

	/**
	 * To log the given message to the reporter at INFO level
	 * 
	 * @param message
	 */
	public static void info(String message) {
		getTest().log(Status.INFO, message);
	}

	/**
	 * To log the given message to the reporter at DEBUG level
	 * 
	 * @param event
	 */
	public static void debug(String event) {
		getTest().log(Status.INFO, event);
	}

	/**
	 * To log the given message to the reporter at PASS level
	 * 
	 * @param passMessage
	 */
	public static void pass(String passMessage) {
		getTest().log(Status.PASS, "<font color=\"green\">" + passMessage + "</font>");
	}

	/**
	 * To log the given message to the reporter at FAIL level
	 * 
	 * @param failMessage
	 */
	public static void fail(String failMessage) {
		getTest().log(Status.FAIL, "<font color=\"red\">" + failMessage + "</font>");
	}

	/**
	 * To log the given message to the reporter at SKIP level
	 * 
	 * @param message
	 */
	public static void skip(String message) {
		getTest().log(Status.SKIP, "<font color=\"orange\">" + message + "</font>");
	}

	/**
	 * To print the stack trace of the given error/exception
	 * 
	 * @param t
	 */
	public static void logStackTrace(Throwable t) {
		if (t instanceof SkipException) {
			getTest().log(Status.SKIP, "<div class=\"stacktrace\">" + ExceptionUtils.getStackTrace(t) + "</div>");
		} else {
			getTest().log(Status.FAIL, "<div class=\"stacktrace\">" + ExceptionUtils.getStackTrace(t) + "</div>");
		}
	}

	/**
	 * To add attributes to a extent test instance
	 * 
	 * @param attribs
	 */
	public static void addAttribute(String... attribs) {
		getTest().assignAuthor(attribs);
	}

	/**
	 * To end an extent test instance
	 */
	public static void endTest(String message) {
		getTest().log(Status.INFO, "<font color=\"green\">" + message + "</font>");
	}

	/**
	 * To change the test run status to SKIP (to be used with retry analyzer)
	 * 
	 * @param result
	 * 
	 *               public static void setTestStatusAsSkip(ITestResult result) {
	 *               try { ExtentTest test = tests.get(result.hashCode());
	 *               test.getTest().getLogList().forEach(log -> { if
	 *               (log.getLogStatus() == Status.ERROR || log.getLogStatus() ==
	 *               Status.FAIL || log.getLogStatus() == Status.FATAL) {
	 *               log.setLogStatus(Status.SKIP); } });
	 *               test.getTest().setStatus(Status.SKIP); } catch (Exception e) {
	 *               e.printStackTrace(); } }
	 */

	/**
	 * To flush and close the report instance
	 * 
	 * @param allTestCaseResults
	 * @param outdir
	 */
	public static void closeReport(List<ITestResult> allTestCaseResults, String outdir) {
		if (isReportClosed) {
			return;
		}
		if (extentReport == null && allTestCaseResults.size() > 0) {
			getReportInstance(allTestCaseResults.get(0));
		} else if (extentReport == null && allTestCaseResults.size() == 0) {

		}
		if (extentReport != null) {
			String testName = null;
			ExtentTest extentTest = null;
			Integer hashCode = 0;
			for (ITestResult iTestResult : allTestCaseResults) {
				testName = getTestName(iTestResult);
				hashCode = iTestResult.hashCode();
				if (!tests.containsKey(hashCode)) {
					extentTest = extentReport.createTest(testName, iTestResult.getMethod().getDescription() == null ? ""
							: iTestResult.getMethod().getDescription());

					extentTest.assignCategory(iTestResult.getMethod().getGroups());
					List<String> output = Reporter.getOutput(iTestResult);
					for (String step : output) {
						if (step.contains("test-message")) {
							extentTest.log(Status.INFO, step);
						} else {
							extentTest.log(Status.INFO, step);
						}
					}
					setInterruptedTestStatus(iTestResult, extentTest);

					tests.put(hashCode, extentTest);
				} else {
					extentTest = tests.get(hashCode);
					// if (extentTest.getEndedTime() == null) {
					// setInterruptedTestStatus(iTestResult, extentTest);
					// extentTest.setEndedTime(getTime(iTestResult.getEndMillis()));
				}
			}
		}
		for (ExtentTest eTest : tests.values()) {
			// extentReport.endTest(eTest);
		}
		extentReport.flush();
		isReportClosed = true;
		
		
		
		String browser = System.getProperty("browserName") != null ? System.getProperty("browserName")
				: configProperty.getProperty("browserName").toLowerCase().trim();
	
        File reportFolder = new File("test-reports/TestReports_"+browser);
		File reportSourceFile = new File("test-output/ExtentReport.html");
		File reportScreenshotFile = new File("test-output/ScreenShot");
		File reportSparkFile = new File("test-output/spark");
		
		
		try {
			reportFolder=  FileUtils.createReportFolder(reportFolder);
		} catch (IOException e2) {
			e2.printStackTrace();
			
		}
		
		String reportDestFolder = reportFolder + File.separator +browser+"_TestReport.html";
		File reportDestFile = new File(reportDestFolder);
		
		String screenshotFolder = reportFolder + File.separator + "ScreenShot";
		File screenshotDestFolder =  new File(screenshotFolder);
		
		String sparkFolder = reportFolder + File.separator + "spark";
		File sparkDestFolder =  new File(sparkFolder);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {			
			e1.printStackTrace();
		}

		try {
		    FileUtils.copyFile(reportSourceFile, reportDestFile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		
		try {
		    FileUtils.copyFolder(reportScreenshotFile, screenshotDestFolder);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		try {
		    FileUtils.copyFolder(reportSparkFile, sparkDestFolder);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		
		// extentReport.close();
	}
	
	
	
}
