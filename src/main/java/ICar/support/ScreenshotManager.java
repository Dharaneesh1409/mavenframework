package ICar.support;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.log4testng.Logger;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

/**
 * ScreenshotManager to take screenshots using logger class
 * 
 */
public class ScreenshotManager {
    private static final Logger logger = Logger.getLogger(ScreenshotManager.class);
    private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	private static IPropertiesReader propertiesReader = PropertiesReaderUtil.getInstance();
	private static String screenShotFolderPath;
    /**
    * ScreenshotManager to take screenshots using logger class
    *
    */
     
    	public static void takeScreenshot(WebDriver driver, String filepath, String fileName) {
    		WebDriver webDriver = driver;// driver.getDelegate();
    		File screenshot = null;
     
    		if (webDriver instanceof TakesScreenshot) {
    			if (propertiesReader.fullPageScreenshot() != null && propertiesReader.fullPageScreenshot() == true) {
    				fileName = fileName.split("\\.")[0];
    				Shutterbug.shootPage(webDriver, Capture.FULL_SCROLL).withName(fileName).save(filepath);
    				logger.debug("screenshot taken and stored at " + filepath + fileName);
    			} else {
    				screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
    				try {
    					File destFile = new File(filepath + fileName);
    					destFile.getParentFile().mkdirs();
    					FileUtils.copyFile(screenshot, destFile);
    					screenshot.delete(); // it will delete the previous screenshots
    					logger.debug("screenshot taken and stored at " + destFile.getAbsolutePath());
    				} catch (IOException e) {
    					logger.error(e.getMessage(), e);
    				}
    			}
    		} else {
    			WebDriver augmentedDriver = new Augmenter().augment(webDriver);
    			screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
    		}
     
    	}
    /**
     * takeScreenshot to take screenshots by passing driver as parameter with date
     * and time
     * 
     * @param driver
     *            - webdriver
     * @param filepath
     *            - file path location
     */
    public static void takeSingleScreenshot(WebDriver driver, String filepath) {
      File screenshot = null;

        if (driver instanceof TakesScreenshot) {
            screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        } else {
            WebDriver augmentedDriver = new Augmenter().augment(driver);
            screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
        }
        try {
            File destFile = new File(filepath);
            destFile.getParentFile().mkdirs();
            FileUtils.copyFile(screenshot, destFile);
            screenshot.delete(); // it will delete the previous screenshots
            logger.debug("screenshot taken and stored at " + destFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    	  
           
    }

    /**
     * takeScreenshot to take screenshots by passing driver as parameter with date
     * and time
     * 
     * @param driver
     *            - webdriver
     */
  /*  public static void takeScreenshot(WebDriver driver) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-hhmmss-SSS");
        String path = "screenshots/Test-" + sdf.format(cal.getTime()) + ".jpg";
    	try {      takeScreenshot(driver,screenShotFolderPath, path);
    }catch (TimeoutException  e) {
    	try {
 	   Utils.waitForPageLoad(driver);
 	  takeScreenshot(driver,screenShotFolderPath, path);
    	}catch (TimeoutException e1) {
    		takeSingleScreenshot(driver, path);
		}
	}
    }*/

} // ScreenshotManager
