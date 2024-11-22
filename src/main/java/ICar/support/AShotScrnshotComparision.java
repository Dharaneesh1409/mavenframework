package ICar.support;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class AShotScrnshotComparision {


	public void TakeScreenshotAndCompare(WebDriver driver, String ImageName, WebElement element, String ExpectedImage) throws Exception {

		try {	
			//wait for element to load
			WebDriverWait wait = new WebDriverWait(driver,30);
			wait.until(ExpectedConditions.visibilityOf(element));
			
			// Take screen shot of a particular element 
			Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver,element);
			String path = ".\\src\\main\\resources\\testdata\\ActualScreenshots\\" + ImageName + ".jpg";

			// save the taken screenshot 
			ImageIO.write(screenshot.getImage(), "jpg", new File(path));			
			Log.message("Captured the screen shot of the " + ImageName + " page.");

			// read the image to compare
			String expectpath = ".\\src\\main\\resources\\testdata\\expectedscreenshots\\" + ExpectedImage + ".jpg";
			BufferedImage expectedImage = ImageIO.read(new File(expectpath));
			BufferedImage actualImage = screenshot.getImage();

			// Create ImageDiffer object and call method makeDiff()
			ImageDiffer imgDiff = new ImageDiffer();
			ImageDiff diff = imgDiff.makeDiff(actualImage, expectedImage);

			if (diff.hasDiff() == false) {
				Log.message("Images are same");
				

			} else {	
				String diffpath = ".\\src\\main\\resources\\testdata\\actualscreenshots\\" + ImageName + "diff_.jpg";
				ImageIO.write(diff.getDiffImage(), "jpg", new File(diffpath));
				Log.message("Images are different");				
				

			}		
		}
		catch (Exception e) {
			Log.exception(e, driver);
		}
	}
	
	
	
    public void imageComparison(WebDriver driver, String ImageName, WebElement element, String ExpectedImage) throws Exception
    {	
    	
		// Take screen shot of a particular element
		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
				.takeScreenshot(driver, element);
		String actualimgpath = ".\\src\\main\\resources\\testdata\\actualscreenshots\\" + ImageName + ".jpg";

		// save the taken screenshot
		ImageIO.write(screenshot.getImage(), "jpg", new File(actualimgpath));
		Log.message("Captured the screen shot of the " + ImageName + " page.");
		
		String expectedimgpath = ".\\src\\main\\resources\\testdata\\expectedscreenshots\\" + ExpectedImage + ".jpg";
		
		// Encoding image file
		Image img1 = Toolkit.getDefaultToolkit().getImage(actualimgpath);
		Image img2 = Toolkit.getDefaultToolkit().getImage(expectedimgpath);

		try {
			// Getting pixels
			PixelGrabber pixGrab1 = new PixelGrabber(img1, 0, 0, -1, -1, false);
			PixelGrabber pixGrab2 = new PixelGrabber(img2, 0, 0, -1, -1, false);

			// Integer array to store the pixels
			int[] dataArry1 = null;
			int[] dataArry2 = null;

			// Getting Height & Width of the pixels
			if (pixGrab1.grabPixels()) {
				int height = pixGrab1.getHeight();
				int width = pixGrab1.getWidth();
				dataArry1 = new int[width * height];
				dataArry1 = (int[]) pixGrab1.getPixels();
			}

			if (pixGrab2.grabPixels()) {
				int height2 = pixGrab2.getHeight();
				int width2 = pixGrab2.getWidth();
				dataArry2 = new int[width2 * height2];
				dataArry2 = (int[]) pixGrab2.getPixels();
			}

			Log.message("Image comparison result: " + Arrays.equals(dataArry1, dataArry2));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}