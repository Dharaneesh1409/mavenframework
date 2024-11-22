package ICar.support;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class TestDataWritter {

    private String workBookName;
    private String workSheet;
    private String testCaseId;
    private boolean doFilePathMapping;
    private HashMap<String, String> data;
    private Hashtable<String, Integer> excelHeaders = new Hashtable<String, Integer>();
    private Hashtable<String, Integer> excelrRowColumnCount = new Hashtable<String, Integer>();

    /**
     * Function TestDataWritter
     */
    public TestDataWritter() {
    }

    /**
     * Function TestDataWritter
     * 
     * @param xlWorkBook
     *            - Workbook name
     * @param xlWorkSheet
     *            - Sheet name
     */
    public TestDataWritter(String xlWorkBook, String xlWorkSheet) {
        this.workBookName = xlWorkBook;
        this.workSheet = xlWorkSheet;
    }

    /**
     * Function TestDataWritter
     * 
     * @param xlWorkBook
     *            - Workbook name
     * @param xlWorkSheet
     *            - Sheet name
     * @param tcID
     *            - Testcase ID
     */
    public TestDataWritter(String xlWorkBook, String xlWorkSheet, String tcID) {
        this.workBookName = xlWorkBook;
        this.workSheet = xlWorkSheet;
        this.testCaseId = tcID;
    }

    public String getWorkBookName() {
        return workBookName;
    }

    public void setWorkBookName(String workBookName) {
        this.workBookName = workBookName;
    }

    public void setFilePathMapping(boolean doFilePathMapping) {
        this.doFilePathMapping = doFilePathMapping;
    }

    public String getWorkSheet() {
        return workSheet;
    }

    public void setWorkSheet(String workSheet) {
        this.workSheet = workSheet;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    /**
     * Function To Write Data for BRD
     * 
     * @param workBookName
     *            - Workbook name
     * @param workSheetName
     *            - Sheet name
     * @param key
     *            - Column name
     * @param valueToOverRide
     *            - Override the cell value
     * @return boolean - true or false
     */
    public static boolean initBRDWrite(String workBookName, String workSheetName, String key, String valueToOverRide) {
        /** Loading the test data from excel using the test case id */
        TestDataWritter testData = new TestDataWritter();
        testData.setWorkBookName(workBookName);
        testData.setWorkSheet(workSheetName);
        testData.setFilePathMapping(true);

        return testData.writeBrdDataToExcel(key, valueToOverRide);
    }

    /**
     * Function To Write Data To Excel for BRD
     * 
     * @param key
     *            - Column name
     * @param valueToOverRide
     *            - Override the cell value
     * @return boolean - true or false
     */
    public boolean writeBrdDataToExcel(String key, String valueToOverRide) {
    	ICar.support.ReadFromExcel readTestData = new ICar.support.ReadFromExcel();

        try {
            String filePath = "";
            if (doFilePathMapping)
                filePath = ".\\src\\main\\resources\\" + workBookName;
            else
                filePath = workBookName;

            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            HSSFSheet sheet = workbook.getSheet(workSheet);
            HSSFRow row = null;
            int rowNumber = -1;
            HSSFCell cell = null;
            excelrRowColumnCount = readTestData.findRowColumnCount(sheet, excelrRowColumnCount);
            excelHeaders = readTestData.readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);

            for (int r = 0; r < excelrRowColumnCount.get("RowCount"); r++) {
                row = sheet.getRow(r);
                if (readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get("BRD_Number"))).equals(key)) {
                    rowNumber = row.getRowNum();
                    break;
                }
            }
            if (rowNumber != (-1)) {
                cell = sheet.getRow(rowNumber).getCell(excelHeaders.get("BRD_Status"));
                cell.setCellValue(valueToOverRide);
            }
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    /**
     * This program demonstrates how to write characters to a text file.
     * @return 
     *
     */
   
        public static void WriteDataTextFile(String filename, String orderType, String TCID, String Result ) {
            try {
                FileWriter writer = new FileWriter(filename, true);
                writer.write("\r\n");   // write new line
                writer.write(orderType);
                writer.write(",");
                writer.write(TCID);
                writer.write(",");                
                writer.write(Result);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
     
        }
     
  
  
    /**
     * Function To Write Data To Excel
     * 
     * @param workBookName
     *            - Workbook name
     * @param workSheetName
     *            - Sheet name
     * @param keyString
     *            - Column name
     * @param key
     *            - Column name
     * @param valueString
     *            - Value
     * @param valueToOverRide
     *            - Override the cell value
     * @return boolean - true or false
     */
    public static boolean initWrite(String workBookName, String workSheetName, String keyString, String key,
            String valueString, String valueToOverRide) {
        /** Loading the test data from excel using the test case id */
        TestDataWritter testData = new TestDataWritter();
        testData.setWorkBookName(workBookName);
        testData.setWorkSheet(workSheetName);
        testData.setFilePathMapping(true);

        return testData.writeDataToExcel(keyString, key, valueString, valueToOverRide);
    }
    
    /**
     * Function To Write Data To Excel
     * 
     * @param keyString
     *            - Column name
     * @param key
     *            - Column name
     * @param valueString
     *            - Value
     * @param valueToOverRide
     *            - Override the cell value
     * @return boolean - true or false
     */
    public boolean writeDataToExcel(String keyString, String key, String valueString, String valueToOverRide) {
    	ICar.support.ReadFromExcel readTestData = new ICar.support.ReadFromExcel();

        try {
            String filePath = "";
            if (doFilePathMapping)
                filePath = ".\\src\\main\\resources\\" + workBookName;
            else
                filePath = workBookName;

            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            HSSFSheet sheet = workbook.getSheet(workSheet);
            HSSFRow row = null;
            int rowNumber = -1;
            HSSFCell cell = null;
            excelrRowColumnCount = readTestData.findRowColumnCount(sheet, excelrRowColumnCount);
            excelHeaders = readTestData.readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);

            for (int r = 0; r < excelrRowColumnCount.get("RowCount"); r++) {
                row = sheet.getRow(r);
                if (readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(keyString))).equals(key)) {
                    rowNumber = row.getRowNum();
                    break;
                }
            }
            if (rowNumber != (-1)) {
                cell = sheet.getRow(rowNumber).getCell(excelHeaders.get(valueString));
                cell.setCellValue(valueToOverRide);
            }
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    
    /**
     * Initialization the test data.
     *
     * @param workbook
     *            - workbook name
     * @param sheetName
     *            - sheet name
     * @return testData - Excel from key string as HashMap format
     */
    public static boolean initTestDataWrite(String workBookName, String workSheetName, String key,
            String valueString, String valueToOverRide) {
        /** Loading the test data from excel using the test case id */
        TestDataWritter testData = new TestDataWritter();
        testData.setWorkBookName(workBookName);
        testData.setWorkSheet(workSheetName);
        testData.setFilePathMapping(true);
        
        Throwable t = new Throwable();
        String testCaseId = t.getStackTrace()[1].getMethodName();
        testData.setTestCaseId(testCaseId);
        return testData.writeData(testCaseId,key,valueString,valueToOverRide);
    }
    
    /**
     * Fetch Data from Excel
     * 
     * @return testData - Data from Excel as HashMap format
     */
    public boolean writeData(String testcaseId, String key, String valueString, String valueToOverRide) {
     	ICar.support.ReadFromExcel readTestData = new ICar.support.ReadFromExcel();
     	    
        try {
            String filePath = "";
            if (doFilePathMapping)
                filePath = ".\\src\\main\\resources\\" + workBookName;
            else
                filePath = workBookName;
            boolean isDataFound = false;
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            HSSFSheet sheet = workbook.getSheet(workSheet);
            HSSFRow row = null;
            int rowNumber = -1;
            HSSFCell cell = null;
            excelrRowColumnCount = readTestData.findRowColumnCount(sheet, excelrRowColumnCount);
            excelHeaders = readTestData.readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);
            
            for (int r = 0; r < excelrRowColumnCount.get("RowCount"); r++) {
                row = sheet.getRow(r);
                if (row == null)
                    continue;
                for (int c = 0; c < excelrRowColumnCount.get("ColumnCount"); c++) {
                    if (row.getCell(excelHeaders.get("TestID")) == null)
                        break;
                    cell = row.getCell(excelHeaders.get("TestID"));
                    if (!readTestData.convertHSSFCellToString(cell).toString().equalsIgnoreCase(testCaseId))
                        continue;
                    isDataFound = true;
                    rowNumber = row.getRowNum();
          
                    if (rowNumber != (-1)) {
                        cell = sheet.getRow(rowNumber).getCell(excelHeaders.get(valueString));
                        cell.setCellValue(valueToOverRide);
                    }
                    break;
                }
                if (isDataFound)
                   break;
            }
                
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
}

} // TestDataWritter
