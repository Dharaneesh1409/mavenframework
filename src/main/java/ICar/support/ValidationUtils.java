package ICar.support;


import java.io.File;
import java.util.List;

import org.openqa.selenium.WebElement;

public class ValidationUtils {

	/**
	 * isElementPresent : This function is used for verifying if an element is displayed
	 * 
	 * @param driver
	 * @throws Exception
	 */
	public static boolean isElementPresent( WebElement element) throws Exception {
        try {
                    if(element.isDisplayed() || element.isEnabled())
                                return true;
                    else {
                                return false;
                                }
                    }
                    catch(Exception e)
                    {
                                return false;
                    }
	}

	
	/**
	 * verifyUI : This function is used for verifying the UI parameter of an element
	 * 
	 * @param driver
	 * @throws Exception
	 */
	public static String getCSSvalue(WebElement element, String UIparameter) throws Exception {
		try
		{
			return element.getCssValue(UIparameter);
		}
		catch(Exception e) {
			Log.exception(new Exception("Exception at ValidationUtils.getCSSvalue : " + e.getMessage()));			
		}//End catch
		return null;
	}

	/**
	 * isElementPresent : This function is used for verifying if an element is displayed
	 * @param List<WebElement>
	 * @throws Exception
	 */
	
	public static boolean isElementPresent(List<WebElement> planList) throws Exception {
		try
		{
			boolean ret = true;
			for(WebElement a : planList)
			{
				if(a.isDisplayed())
					ret = true;
				else
				{	ret = false;
					break;
				}
			}
			return ret;
		}
		catch(Exception e) {
			return false;		
		}//End catch
	}

/**
	 * checkFileExists : This function is used for verifying if a file exists in a directory
	 * @param String
 * @throws InterruptedException 
	 * @throws Exception
	 */
	
	public static boolean checkFileExists(String directory ,String file) throws InterruptedException {
		
		boolean retr = false;
		Thread.sleep(60000);
	    File dir = new File(directory);
	    File[] dir_contents = dir.listFiles();
	    for (int i = 0; i < dir_contents.length; i++) {
	        if (dir_contents[i].getName().contains(file))
	        {
	          retr = true;
	          break;
	        }
	        }
	    return retr; 
	}
	
	/**
	 * deleteFile : This function is used for deleting a file in directory
	 * @return 
	 * @throws Exception
	 */
	
    public static Boolean  deleteFile(String file) {
		
		File file1 = new File(file);
		return file1.delete();

	}

	
}
