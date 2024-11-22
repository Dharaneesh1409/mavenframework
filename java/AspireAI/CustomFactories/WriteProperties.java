package AspireAI.CustomFactories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import AspireAI.CustomFactories.CustomData;

public class WriteProperties {

	public static String basePath = null, locIAlgo = null, locIAlgoList = null;
	public static FileHandler fh, fhfail;
	public static Logger logger, faillogger;
	static {
		try {
			basePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
					+ File.separator + "resources" + File.separator + "inteligentAlgoritham" + File.separator;
			locIAlgoList = basePath + "StablizedListJS.js";
			locIAlgo = basePath + "StablizedJS.js";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param key
	 * @param fileName
	 * @throws IOException
	 */
	public static synchronized void storePropertyKey(String key, String fileName) throws IOException {

		String value = "";

		FileInputStream fileInput = null;
		try {
			String basePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
					+ File.separator + "resources" + File.separator + "properties" + File.separator;
			String configFilePath = basePath + fileName + ".properties";
			fileInput = new FileInputStream(configFilePath);
			Properties prop = new Properties();

			// load properties file
			prop.load(fileInput);
			fileInput.close();

			FileOutputStream out = new FileOutputStream(configFilePath);
			prop.setProperty(key, "");
			prop.store(out, null);
			out.close();

			// System.out.println("properties values are updated for key " + key + " values
			// are " + value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param key
	 * @param fileName
	 * @throws IOException
	 */
	public static synchronized void storePropertyParentKey(String key,String pvalue, String fileName) throws IOException {

		String value = "";

		FileInputStream fileInput = null;
		try {
			String basePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
					+ File.separator + "resources" + File.separator + "properties" + File.separator;
			String configFilePath = basePath + fileName + ".properties";
			fileInput = new FileInputStream(configFilePath);
			Properties prop = new Properties();

			// load properties file
			prop.load(fileInput);
			fileInput.close();

			FileOutputStream out = new FileOutputStream(configFilePath);
			prop.setProperty("P"+key, pvalue);
			prop.store(out, null);
			out.close();

			// System.out.println("properties values are updated for key " + key + " values
			// are " + value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param key
	 * @param values
	 * @param fileName
	 * @throws IOException
	 */
	public static synchronized void storeProperties(String key, ArrayList values, String fileName) throws IOException {

		String value = "";

		FileInputStream fileInput = null;
		try {
			String basePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
					+ File.separator + "resources" + File.separator + "properties" + File.separator;
			String configFilePath = basePath + fileName + ".properties";
			fileInput = new FileInputStream(configFilePath);
			Properties prop = new Properties();

			// load properties file
			prop.load(fileInput);
			fileInput.close();
			for (Object cssLocators : values)
				value = value + cssLocators.toString() + ",";

			FileOutputStream out = new FileOutputStream(configFilePath);
			prop.setProperty(key, value.substring(0, value.length() - 1));
			prop.store(out, null);
			out.close();

			// System.out.println("properties values are updated for key " + key + " values
			// are " + value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param key
	 * @param values
	 * @param fileName
	 * @param child
	 * @throws IOException
	 */
	public static synchronized void storeProperties(String key, ArrayList values, String fileName, String child) throws IOException {

		String value = "";

		FileInputStream fileInput = null;
		try {
			String basePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
					+ File.separator + "resources" + File.separator + "properties" + File.separator;
			String configFilePath = basePath + fileName + ".properties";
			fileInput = new FileInputStream(configFilePath);
			Properties prop = new Properties();

			// load properties file
			prop.load(fileInput);
			fileInput.close();
			for (Object cssLocators : values)
				value = value + cssLocators.toString() +" "+child + ",";

			FileOutputStream out = new FileOutputStream(configFilePath);
			prop.setProperty(key, value.substring(0, value.length() - 1));
			prop.store(out, null);
			out.close();

			// System.out.println("properties values are updated for key " + key + " values
			// are " + value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param driver
	 * @param webelement
	 * @param propertyKey
	 * @param className
	 * @throws IOException
	 */
	public static void getLocatorForWebelement(WebDriver driver, WebElement webelement, String propertyKey,
			String className) throws IOException {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		@SuppressWarnings("deprecation")
		String algoContent = com.google.common.io.Files.toString(new File(locIAlgo),
				com.google.common.base.Charsets.UTF_8);
		@SuppressWarnings("rawtypes")
		ArrayList properties = (ArrayList) jse.executeScript(algoContent, webelement);
		WriteProperties.storeProperties(propertyKey, properties, className);

	}

	/**
	 * @param driver
	 * @param webelement
	 * @param propertyKey
	 * @param className
	 * @param child
	 * @throws IOException
	 */
	public static void getLocatorForWebelement(WebDriver driver, WebElement webelement, String propertyKey,
			String className, String child) throws IOException {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		@SuppressWarnings("deprecation")
		String algoContent = com.google.common.io.Files.toString(new File(locIAlgo),
				com.google.common.base.Charsets.UTF_8);
		@SuppressWarnings("rawtypes")
		ArrayList properties = (ArrayList) jse.executeScript(algoContent, webelement);
		WriteProperties.storeProperties(propertyKey, properties, className, child);
		WriteProperties.writeLog(className, propertyKey, "AI"+properties.toString(), true);
	}

	/**
	 * @param driver
	 * @param webelement
	 * @param propertyKey
	 * @param className
	 * @throws IOException
	 */
	public static void getLocatorForListOfWebelement(WebDriver driver, List<WebElement> webelement, String propertyKey,
			String className) throws IOException {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		@SuppressWarnings("deprecation")
		String algoContent = com.google.common.io.Files.toString(new File(locIAlgoList),
				com.google.common.base.Charsets.UTF_8);
		@SuppressWarnings("rawtypes")
		ArrayList properties = (ArrayList) jse.executeScript(algoContent, webelement);
		WriteProperties.storeProperties(propertyKey, properties, className);

	}



	/**
	 * @param driver
	 * @param webelement
	 * @throws IOException
	 */
	public static void callAlgo(WebDriver driver, WebElement webelement) throws IOException {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		@SuppressWarnings("deprecation")
		String algoContent = com.google.common.io.Files.toString(new File(locIAlgo),
				com.google.common.base.Charsets.UTF_8);
		@SuppressWarnings("rawtypes")
		ArrayList properties = (ArrayList) jse.executeScript(algoContent, webelement);
		//	System.out.println(properties);

	}

	/**
	 * @param className
	 * @param elementName
	 * @throws IOException
	 */
	public static synchronized void writeLog(String className,String elementName, String value, Boolean flag) throws IOException {
		if(flag) {
			if(value == null || value.isEmpty()) {
				logger.info("AIElement possibles are not created === PropertiesFileName: "+className+" ==== ElementName: "+elementName+" so used actual element intraction is Passed");
			}else if(value.startsWith("Changed")) {
				logger.info("AI found the given element is changed from Previously Existing one so updated the new one === PropertiesFileName: P"+className+" ==== ElementName: "+elementName+" :: "+value.replaceAll("Changed", "") );
			}else if(value.startsWith("Parent")) {
				logger.info("AI Parent Key added === PropertiesFileName: "+className+" ==== ElementName: "+elementName+" :: "+value.replaceAll("Parent", ""));
			}else if(value.startsWith("AI")) {
				logger.info("AI Possibles created for === PropertiesFileName: "+className+" ==== ElementName: "+elementName+" :: "+value.replaceAll("AI", ""));
			}else {
				logger.info("AIElement Intracted === PropertiesFileName: "+className+" ==== ElementName: "+elementName+" :: "+value);
			}
		}else {
			if(value == null || value.isEmpty()) {
				faillogger.warning("AIElement possibles are not created === PropertiesFileName: "+className+" ==== ElementName: "+elementName+" so used actual element intraction is Failed");
			}else {
				faillogger.warning("AIElement Failed === PropertiesFileName: "+className+" ==== ElementName: "+elementName+" :: "+value);  
			}
		}
	}
	
	public static synchronized void writeFailElementLog(String element) {
		faillogger.warning("Given actual element is wrong - "+element);
	}

	public static synchronized void setLog() throws IOException {
		String LogbasePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + File.separator + "properties" + File.separator;
		String LogFilePath = LogbasePath + "AI" + ".log";
		String failLogFilePath = LogbasePath + "AIfail" + ".log";

		File Logfile = new File(LogFilePath);
		if(!Logfile.exists()) {
			Logfile.createNewFile();
		}
		File failLogfile = new File(failLogFilePath);
		if(!failLogfile.exists()) {
			failLogfile.createNewFile();
		}
		try {  
			logger = Logger.getLogger("MyLog");  
			faillogger =  Logger.getLogger("MyFailLog");
			fh = new FileHandler(LogFilePath,true); 
			fhfail = new FileHandler(failLogFilePath,true);
			logger.addHandler(fh);
			faillogger.addHandler(fhfail);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  
			fhfail.setFormatter(formatter);
			logger.setUseParentHandlers(false);
			faillogger.setUseParentHandlers(false);
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date dateobj = new Date();
			logger.info("********************* "+df.format(dateobj)+" ************************");  
			faillogger.info("********************* "+df.format(dateobj)+" ************************");  
		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}

	public static synchronized void closeLog() throws IOException {
		logger.info("********************* End ************************");
		faillogger.info("********************* End ************************");
		fh.close();
		fhfail.close();
	}
}
