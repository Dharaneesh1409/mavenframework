package ICar.support;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.testng.Assert;

import com.helger.commons.csv.CSVReader;

@SuppressWarnings("unchecked")
public class TestDataExtractor {

    private String workBookName;
    private String workSheet;
    private String testCaseId;
    private boolean doFilePathMapping;
    private HashMap<String, String> data;
    private Hashtable<String, Integer> excelHeaders = new Hashtable<String, Integer>();
    private Hashtable<String, Integer> excelrRowColumnCount = new Hashtable<String, Integer>();

    public TestDataExtractor() {
    }

    /**
     * Function TestDataExtractor
     * 
     * @param xlWorkBook
     *            - Workbook name
     * @param xlWorkSheet
     *            - Sheet name
     */
    public TestDataExtractor(String xlWorkBook, String xlWorkSheet) {
        this.workBookName = xlWorkBook;
        this.workSheet = xlWorkSheet;
    }

    /**
     * Function TestDataExtractor
     * 
     * @param xlWorkBook
     *            - Workbook name
     * @param xlWorkSheet
     *            - Sheet name
     * @param tcID
     *            - Testcase ID
     */
    public TestDataExtractor(String xlWorkBook, String xlWorkSheet, String tcID) {
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

    public String get(String key) {
        if (data.isEmpty())
            readData();
        return data.get(key);
    }

    /**
     * Fetch Data from Excel
     * 
     * @return testData - Data from Excel as HashMap format
     */
    public HashMap<String, String> readData() {
        HashMap<String, String> testData = new HashMap<String, String>();
        ICar.support.ReadFromExcel readTestData = new ICar.support.ReadFromExcel();
        boolean isDataFound = false;
        testCaseId = testCaseId != null ? testCaseId.trim() : "";
        HSSFSheet sheet = null;
        HSSFRow row = null;
        HSSFCell cell = null;

        try {
            sheet = readTestData.initiateExcelConnection(workSheet, workBookName, doFilePathMapping);
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
                    for (String key : excelHeaders.keySet()) {
                        testData.put(key, readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(key))));
                    }
                    break;
                }
                if (isDataFound)
                    break;
            }
            if (!isDataFound)
                Assert.fail("\nTest Data not found in test data sheet for Test Case Id  : " + testCaseId);
        } catch (RuntimeException e) {
            Assert.fail("Error During Execution; Execution Failed More details " + e);
            e.printStackTrace();
        }
        data = testData;
        return testData;
    }

    /**
     * Fetch all Data from Excel
     * 
     * @return dataList - Data from Excel as list HashMap format
     */
    public List<HashMap<String, String>> readAllData() {
        List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
        ICar.support.ReadFromExcel readTestData = new ICar.support.ReadFromExcel();
        HSSFSheet sheet = null;
        HSSFRow row = null;

        try {
            sheet = readTestData.initiateExcelConnection(workSheet, workBookName, doFilePathMapping);
            excelrRowColumnCount = readTestData.findRowColumnCount(sheet, excelrRowColumnCount);
            excelHeaders = readTestData.readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);

            for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
                row = sheet.getRow(r);
                if (row == null)
                    continue;
                HashMap<String, String> testData = new HashMap<String, String>();
                for (int c = 0; c < excelrRowColumnCount.get("ColumnCount"); c++) {

                    for (String key : excelHeaders.keySet()) {
                        testData.put(key, readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(key))));
                    }
                }
                dataList.add(testData);
            }
        } catch (RuntimeException e) {
            Assert.fail("Error During Execution; Execution Failed More details " + e);
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Initialization the test data.
     *
     * @param workbook
     *            - workbook name
     * @param sheetName
     *            - sheet name
     * @return testData - Data from Excel as HashMap format
     */
    public static HashMap<String, String> initTestData(String workbook, String sheetName) {
        /** Loading the test data from excel using the test case id */
        TestDataExtractor testData = new TestDataExtractor();
        testData.setWorkBookName(workbook);
        testData.setWorkSheet(sheetName);
        testData.setFilePathMapping(true);

        Throwable t = new Throwable();
        String testCaseId = t.getStackTrace()[1].getMethodName();
        testData.setTestCaseId(testCaseId);
        return testData.readData();
    }

    /**
     * Initialization the BRD test data.
     *
     * @param workbook
     *            - workbook name
     * @param sheetName
     *            - sheet name
     * @return testData - Data from Excel as HashMap format
     */
    public static HashMap<String, String> initBRDTestData(String workbook, String sheetName) {
        /** Loading the test data from excel using the test case id */
        TestDataExtractor testData = new TestDataExtractor();
        testData.setWorkBookName(workbook);
        testData.setWorkSheet(sheetName);
        testData.setFilePathMapping(true);

        Throwable t = new Throwable();
        String testCaseId = t.getStackTrace()[1].getMethodName();
        testData.setTestCaseId(testCaseId);
        return testData.readAvailBRDData();
    }

    /**
     * Initialization the GC test data.
     *
     * @param workbook
     *            - workbook name
     * @param sheetName
     *            - sheet name
     * @return testData - Data from Excel as HashMap format
     */
    public static HashMap<String, String> initGCTestData(String workbook, String sheetName) {
        TestDataExtractor testData = new TestDataExtractor();
        testData.setWorkBookName(workbook);
        testData.setWorkSheet(sheetName);
        testData.setFilePathMapping(true);

        Throwable t = new Throwable();
        String testCaseId = t.getStackTrace()[1].getMethodName();
        testData.setTestCaseId(testCaseId);
        return testData.readData();
    }

    /**
     * Fetch available BRD Data from Excel
     * 
     * @return brdTestData - Data from Excel as HashMap format
     */
    public HashMap<String, String> readAvailBRDData() {
        HashMap<String, String> brdTestData = new HashMap<String, String>();
        ICar.support.ReadFromExcel readTestData = new ICar.support.ReadFromExcel();
        HSSFSheet sheet = null;
        HSSFRow row = null;

        try {
            sheet = readTestData.initiateExcelConnection(workSheet, workBookName, doFilePathMapping);
            excelrRowColumnCount = readTestData.findRowColumnCount(sheet, excelrRowColumnCount);
            excelHeaders = readTestData.readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);

            for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
                row = sheet.getRow(r);
                if (row == null)
                    continue;
                if (readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get("BRD_Status")))
                        .equalsIgnoreCase("notavail")) {
                    brdTestData.put(readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get("BRD_Number"))),
                            readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get("BRD_Status"))));
                } else
                    continue;
            }
        } catch (RuntimeException e) {
            Assert.fail("Error During Execution; Execution Failed More details " + e);
            e.printStackTrace();
        }
        return brdTestData;
    }

    /**
     * Initialization the test data.
     *
     * @param workbook
     *            - workbook name
     * @param sheetName
     *            - sheet name
     * @param col1
     *            - Column name
     * @param col2
     *            - Column name
     * @param conditionColumn
     *            - condition column
     * @return testData - Data from Excel as HashMap format
     */
    public static HashMap<String, String> initTestData(String workbook, String sheetName, String col1, String col2,
            List<String>... conditionColumn) {
        /** Loading the test data from excel using the test case id */
        TestDataExtractor testData = new TestDataExtractor();
        testData.setWorkBookName(workbook);
        testData.setWorkSheet(sheetName);
        testData.setFilePathMapping(true);
        if (conditionColumn.length > 0)
            return testData.readData(col1, col2, conditionColumn);
        else
            return testData.readData(col1, col2);
    }

    /**
     * Fetch Data from Excel
     * 
     * @param col1
     *            - Column name
     * @param col2
     *            - Column name
     * @param conditionColumn
     *            - condition column
     * @return testData - Data from Excel as HashMap format
     */
    public HashMap<String, String> readData(String col1, String col2, List<String>... conditionColumn) {
        HashMap<String, String> testData = new HashMap<String, String>();
        ICar.support.ReadFromExcel readTestData = new ICar.support.ReadFromExcel();
        HSSFSheet sheet = null;
        HSSFRow row = null;

        try {
            sheet = readTestData.initiateExcelConnection(workSheet, workBookName, doFilePathMapping);
            excelrRowColumnCount = readTestData.findRowColumnCount(sheet, excelrRowColumnCount);
            excelHeaders = readTestData.readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);

            if (conditionColumn.length > 0
                    && ((conditionColumn[0].get(2) == null) || (conditionColumn[0].get(2).equals("equals")))) {
                for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
                    row = sheet.getRow(r);
                    if (row == null)
                        continue;

                    if (readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(conditionColumn[0].get(0))))
                            .equalsIgnoreCase(conditionColumn[0].get(1))) {
                        testData.put(readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(col1))),
                                readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(col2))));
                    } else
                        continue;
                }
            } else if (conditionColumn.length > 0 && (conditionColumn[0].get(2) != null)) {
                for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
                    row = sheet.getRow(r);
                    float first;
                    float second;
                    if (row == null)
                        continue;
                    if (conditionColumn[0].size() > 3) {
                        String data = readTestData
                                .convertHSSFCellToString(row.getCell(excelHeaders.get(conditionColumn[0].get(0))));
                        System.out.print("Value " + conditionColumn[0].get(3));
                        if (data.equals(conditionColumn[0].get(3)))
                            continue;
                    }
                    if ((readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(conditionColumn[0].get(0))))
                            .equals(conditionColumn[0].get(3))))
                        continue;
                    else {
                        System.out.print("balance : " + readTestData
                                .convertHSSFCellToString(row.getCell(excelHeaders.get(conditionColumn[0].get(0)))));
                        String data = readTestData
                                .convertHSSFCellToString(row.getCell(excelHeaders.get(conditionColumn[0].get(0))));
                        first = Float.parseFloat(data.equalsIgnoreCase("no balance") ? "0.0" : data);
                        second = Float.parseFloat(conditionColumn[0].get(1));
                        if (conditionColumn[0].get(2).equalsIgnoreCase("greaterThan")) {
                            if (first > second) {
                                testData.put(readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(col1))),
                                        readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(col2))));
                            } else
                                continue;
                        } else if (conditionColumn[0].get(2).equalsIgnoreCase("lesserThan")) {
                            if (first < second) {
                                testData.put(readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(col1))),
                                        readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(col2))));
                            } else
                                continue;
                        }
                    }
                }
            } else {
                for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
                    row = sheet.getRow(r);
                    if (row == null)
                        continue;
                    if (!readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get("Balance")))
                            .equalsIgnoreCase("no balance")) {
                        testData.put(readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(col1))),
                                readTestData.convertHSSFCellToString(row.getCell(excelHeaders.get(col2))));
                    }
                }
            }
        } catch (RuntimeException e) {
            Assert.fail("Error During Execution; Execution Failed More details " + e);
            e.printStackTrace();
        }
        return testData;
    }
	
	public static int readAllData(String filePath) throws IOException{
		
		CSVReader datareader = new CSVReader(new FileReader(filePath));
		     // Iterate over the file rather than consuming the whole thing into memory
	        int count = 0;
	        while(datareader.readNext() != null) {
	            count++;
	        }

	    	datareader.close();
	    	
	    
	     return count;
	}
public static String readColumnData(String filePath) throws IOException{
		
	
		      
		     String line=null;
		     System.out.println(line);
		      //Non empty Last cell Number or index return
	//	      ArrayList<String> ar=new ArrayList<>();
		      BufferedReader br = new BufferedReader(new FileReader(filePath)); 
		      String headers = br.readLine();
		          
		     
	     return headers;
	}
	
	
} // TestDataExtractor
