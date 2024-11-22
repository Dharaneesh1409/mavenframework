package ICar.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FileUtils consists copy/move a file from source to destination location
 */
public class FileUtils {

	/**
	 * Copy a file from one location to another
	 * 
	 * @param f1
	 *            - Source file
	 * @param f2
	 *            - Destination File
	 * @throws IOException
	 *             - java IO exception
	 */
	public static void copyFile(File f1, File f2) throws IOException {
		InputStream in = new FileInputStream(f1);

		// For Overwrite the file.
		OutputStream out = new FileOutputStream(f2);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/***
	 * Method move the file from source location, rename it and place it in the
	 * destination location and deletes the old file from the destination
	 * location if any
	 * 
	 * @param oldFile
	 *            - source file name
	 * @param newFile
	 *            - destination file name
	 * @throws IOException
	 *             - java IO exception
	 */
	public static void moveFile(String oldFile, String newFile) throws IOException {
		File oldfile = new File(oldFile);
		File newfile = new File(newFile);
		copyFile(oldfile, newfile);
		oldfile.delete();
	}
	public static File createReportFolder(File reportFolder) throws IOException {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
		String time = dateFormat.format(now);
		
		String reportFolderPath = reportFolder.getParent() + File.separator   + "AutomationTestResults_" + time + File.separator ;
		reportFolder = new File(reportFolderPath);
		
		
		if (!reportFolder.exists()) {
			reportFolder.mkdir();
		}
		return reportFolder;
	} 
	public static void copyFolder(File src, File dest)
			throws IOException{
		
		if(src.isDirectory()){
			
			//if directory not exists, create it
			if(!dest.exists()){
				dest.mkdir();
				System.out.println("Directory copied from "
						+ src + "  to " + dest);
			}
	    		//list all the directory contents
	    		String files[] = src.list();

	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}

	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest);

	    	        byte[] buffer = new byte[1024];

	    	        int length;
	    	        //copy the file content in bytes
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }

	    	        in.close();
	    	        out.close();
	    	        
	    	}
	    } 
	
	/**
	 * @author venkata.vadlapudi
	 * To verify if the downloaded file is available in the specified location
	 * @param dirPath, fileName
	 * @return true if file available else false
	 * @throws Exception
	 */
	public static Boolean isFileDownloaded(String dirPath, String fileName) throws Exception {
		File tempLastModifiedFile = getLastModifiedFile(dirPath);
		File lastModifiedFile = null;
		int counter = 0;
		while(counter < 10) {
			BrowserActions.nap(1); //This nap is used for internal logic
			lastModifiedFile = getLastModifiedFile(dirPath);
			if(lastModifiedFile != null && lastModifiedFile.lastModified() >= tempLastModifiedFile.lastModified()
					&& lastModifiedFile.getName().contains(fileName)) 
						return true;
			counter++;
		}
		return false;
	}
	/**
	 * @author venkata.vadlapudi
	 * To get the physical number of records in the downloaded file
	 * @param dirPath
	 * @param fileName
	 * @return int
	 * @throws Exception
	 */
	public static int getNumberOfRecordsFromDownloadedFile(String dirPath, String fileName) throws Exception {
		File downloadedFile = getLastModifiedFile(dirPath);
		return TestDataExtractor.readAllData(downloadedFile.getAbsolutePath())-1; //Record starts from 2rd row in the file
	}
	/**
	 * @author venkata.vadlapudi
	 * To get the physical number of records in the downloaded file
	 * @param dirPath
	 * @param fileName
	 * @return int
	 * @throws Exception
	 */
	public static String getColumnNameOfRecordsFromDownloadedFile(String dirPath, String fileName) throws Exception {
		File downloadedFile = getLastModifiedFile(dirPath);
		return TestDataExtractor.readColumnData(downloadedFile.getAbsolutePath()); //Record starts from 2rd row in the file
	}
	/**
	 * @author venkata.vadlapudi
	 * To get the last modified file from the specified location
	 * @param dirPath
	 * @return last modified file
	 * @throws Exception
	 */
	public static File getLastModifiedFile(String dirPath) throws Exception {
		File lastModifiedFile = null;
		File dir = new File(dirPath);
		File[] dirContents = dir.listFiles();
		if(dirContents == null || dirContents.length == 0)
			return null;
		else {
			lastModifiedFile = dirContents[0];
			for (int i = 1; i < dirContents.length; i++) {
				if(dirContents[i].isFile()) {
					if (dirContents[i].lastModified() > lastModifiedFile.lastModified())
						lastModifiedFile = dirContents[i];
				}
			}
		}
		return lastModifiedFile;
	}
}
