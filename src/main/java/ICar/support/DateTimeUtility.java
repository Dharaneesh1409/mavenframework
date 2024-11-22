package ICar.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * DateTimeUtility consists Calendar/Date/Time format related manipulations
 */
public class DateTimeUtility {

	static String sampleDateFormat1 = "yyyy-MM-dd";
	static String sampleDateFormat2 = "MM/dd/yyyy";

	/**
	 * Method returns current date and time in format ddMMyyHHmmss
	 * 
	 * @return date - in the above mentioned format
	 */
	public static String getCurrentDateAndTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}

	/**
	 * Method returns current date and time in logger format dd-MMM-yyyy
	 * HH-mm-ss
	 * 
	 * @return date - in the above mentioned format
	 */
	public static String getCurrentDateAndTimeInLoggerFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH-mm-ss");
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}

	/**
	 * Method returns current date in format dd/MM/yyyy
	 * 
	 * @return currentdate - in the above mentioned format
	 */
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String currentdate = sdf.format(date);
		return currentdate;
	}

	/**
	 * Method returns current date and time in format dd-MMM-yy
	 * hh.mm.ss.SSSSSSSSS a
	 * 
	 * @return date - in the above mentioned format
	 */
	public static String getCurrentDateAndTimeDDMMYY() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSSSSS a");
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}

	/**
	 * Method returns current year in format yyyy
	 * 
	 * @return year - in the above mentioned format
	 */
	public static String getCurrentYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}
	
	
	
	public static String DateMinusAdditionalDays(String date,int days,String SampleDateFormat) throws Exception
	{
        SimpleDateFormat sdf = new SimpleDateFormat(SampleDateFormat);
        Date d1 = sdf.parse(date);
        // Convert Date to Instant
        Instant currentInstant = d1.toInstant();
        // Convert Instant to ZonedDateTime for easy manipulation
        ZonedDateTime currentDateTime = currentInstant.atZone(ZoneId.systemDefault());
        // Get and Print Date
        ZonedDateTime aftersubractDays = currentDateTime.minus(days, ChronoUnit.DAYS);
        Instant afterDaysInstant = aftersubractDays.toInstant();
        //Print the date in a readable format
        SimpleDateFormat outputFormat = new SimpleDateFormat(SampleDateFormat);
        String newDate= outputFormat.format(Date.from(afterDaysInstant));
	    return newDate;
	}

	/**
	 * Method returns current date in format MM/dd/yyyy
	 * 
	 * @return date - in the above mentioned format
	 */
	public static String getToday() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
		String today = dateFormat.format(date.getTime());
		return today;
	}

	/**
	 * Method gets current date from the calendar in the format MM/dd/yy
	 * 
	 * @param day
	 *            current day
	 * @return expDate - in the above mentioned format
	 */
	public static String getDate(int day) {
		int millsInDay = 1000 * 60 * 60 * 24;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		String expDate = dateFormat.format(date.getTime() + (day * millsInDay));
		return expDate;
	}

	/**
	 * Method gets current time in format hhmm
	 * 
	 * @return date - in the above mentioned format
	 */
	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hhmm");
		String today = dateFormat.format(date.getTime());
		return today;
	}

	/**
	 * Method gets yesterday date in format MM/dd/yyyy
	 * 
	 * @return yesterday date - in the above mentioned format
	 */
	public static String getYesterday() {
		int millsInDay = 1000 * 60 * 60 * 24;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(sampleDateFormat2);
		String yesterday = dateFormat.format(date.getTime() - (1 * millsInDay));
		return yesterday;
	}

	/**
	 * Method gets previous day date in format MM/dd/yyyy from the data
	 * parameter passed
	 * 
	 * @param testDate
	 *            current date
	 * @return prevDate - in format MM/dd/yyyy
	 */
	@SuppressWarnings("deprecation")
	public static String getPrevday(String testDate) {
		int millsInDay = 1000 * 60 * 60 * 24;
		Date date = new Date(testDate);
		SimpleDateFormat dateFormat = new SimpleDateFormat(sampleDateFormat2);
		String yesterday = dateFormat.format(date.getTime() - (1 * millsInDay));
		return yesterday;
	}

	/**
	 * Method gets next day date in format MM/dd/yyyy from the data parameter
	 * passed
	 * 
	 * @param testDate
	 *            current date
	 * @return nextDate - in format MM/dd/yyyy
	 */
	@SuppressWarnings("deprecation")
	public static String getNextday(String testDate) {
		int millsInDay = 1000 * 60 * 60 * 24;
		Date date = new Date(testDate);
		SimpleDateFormat dateFormat = new SimpleDateFormat(sampleDateFormat2);
		String yesterday = dateFormat.format(date.getTime() + (1 * millsInDay));
		return yesterday;
	}

	/**
	 * Method formats the date from MM/dd/yyyy to dd-MMM-yy
	 * 
	 * @param dateToformat
	 *            current date
	 * @return date - in format dd-MMM-yy
	 * @throws ParseException
	 *             - java data parse exception
	 */
	public static String formatDateToddMMMyy(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat2);
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from MM/dd/yyyy to yyyy-MM-dd
	 * 
	 * @param dateToformat
	 *            date
	 * @return date - in format DD-MMM-yyyy
	 * @throws ParseException
	 *             - java data parse exception
	 * 
	 */
	public static String formatDateToyyyyMMdd(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat2);
		SimpleDateFormat format2 = new SimpleDateFormat(sampleDateFormat1);
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from MM/dd/yyyy to dd-MM-yy
	 * 
	 * @param dateToformat
	 *            date
	 * @return date - in format dd/MM/yy
	 * @throws ParseException
	 *             - java data parse exception
	 * 
	 */
	public static String formatDateToDDMMYY(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat2);
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from yyyd-dd-mm to dd/mm/yyyy
	 * 
	 * @param dateToformat
	 *            testDate
	 * @return nextDate - in format dd/mm/yyyy
	 * 
	 * @throws ParseException
	 *             - java data parse exception
	 */
	public static String formatDateToDDMMYYYY(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-dd-mm");
		SimpleDateFormat format2 = new SimpleDateFormat("dd/mm/yyyy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from yyyy-MM-dd to dd/MM/yyyy
	 * 
	 * @param dateToformat
	 *            testDate
	 * @return nextDate - in format dd/MM/yyyy
	 * 
	 * @throws ParseException
	 *             - java data parse exception
	 */
	public static String formatDateToddMMYYYY(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat1);
		SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from yyyy-MM-dd to MM/dd/yyyy
	 * 
	 * @param dateToformat
	 *            testDate
	 * @return date - in format MM/dd/yyyy
	 * @throws ParseException
	 *             - java data parse exception
	 */
	public static String formatDateToMMDDYYYY(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat1);
		SimpleDateFormat format2 = new SimpleDateFormat(sampleDateFormat2);
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from yyyy-MM-dd to dd/MMM/yy
	 * 
	 * @param dateToformat
	 *            testDate
	 * @return date - in format dd/MMM/yy
	 * @throws ParseException
	 *             - java data parse exception
	 */
	public static String formatDateToddMMMYY(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat1);
		SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method gets the current today date in format dd/MM/yy
	 * 
	 * @return today date - in the above mentioned format
	 */
	public static String getTodayddMMyyyy() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String today = dateFormat.format(date.getTime());
		return today;
	}

	/**
	 * Method formats the date from yyyy-MM-dd to dd.MM.yyyy
	 * 
	 * @param dateToformat
	 *            testDate
	 * @return date - in format dd.MM.yyyy
	 * @throws ParseException
	 *             - java data parse exception
	 */
	public static String formatDateToddMMyyyy(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat1);
		SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method formats the date from dd-MM-yy to MM/dd/yyyy
	 * 
	 * @param dateToformat
	 *            testDate
	 * @return date - in format MM/dd/yyyy
	 * @throws ParseException
	 *             - java data parse exception
	 */
	public static String formatDateToMMddyyyy(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat format2 = new SimpleDateFormat(sampleDateFormat2);
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method verifies the given date and time in format dd.MM.yyyy hh:mma
	 * 
	 * @param dateToVerify
	 *            - actual date
	 * @param datetimeFormat
	 *            - date format
	 * @return true/false
	 */
	public static boolean verifyDateTimeFormatddmmyy(String dateToVerify, String datetimeFormat) {
		try {
			String dateTimeFormatToVerify = datetimeFormat.substring(0, 16);
			SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormatToVerify);
			dateFormat.parse(dateToVerify);
			if (datetimeFormat.length() > 16) {
				String am = dateToVerify.substring(16, 17);
				return ("p".equals(am)) || ("a".equals(am));
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method gets the current time from the web clock
	 * 
	 * @param time
	 *            - web clock time
	 * 
	 * @return time - web clock time
	 */
	public static ArrayList<String> getTimeForWebclock(String time) {

		String[] st1 = time.split(":");
		String st2 = st1[0];
		String st3 = st1[1];
		int j = Integer.parseInt(st3);
		int s = j - 1;
		int d = j + 1;
		String befTime = "" + s;
		String afTime = "" + d;
		if (j == 59) {
			afTime = "00";
		}
		if (j == 0) {
			befTime = "59";
		}
		ArrayList<String> data = new ArrayList<String>();
		data.add(st2);
		data.add(st3);
		data.add(befTime);
		data.add(afTime);
		return data;
	}
	
	public static boolean verifyDateFormat(String dateToVerify,
			String datetimeFormat) {

		boolean status = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(datetimeFormat);
			Date date = sdf.parse(dateToVerify);
			if (dateToVerify.equals(sdf.format(date))) {
				status = true;
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return status;

	}
	/**
	 * Method returns current month in format mm
	 * 
	 * @return month - in the above mentioned format
	 */
	public static String getCurrentMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}

	public static int getMonthNumberFromMonthString(String MMM){
		int monthNumber = 0;
		try{   
			Date date = new SimpleDateFormat("MMM").parse(MMM);//put your month name here
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			monthNumber=cal.get(Calendar.MONTH);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return monthNumber+1;
	}
	public String fetchCurrentDate()
	{
	    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
	    Calendar c = Calendar.getInstance();
	    String newDate = sdf.format(c.getTime());
	    return newDate;
	}
	
	//pass number of days in the attribute to add to current date
	public String currentDatePlusDaysAddition(int days)
	{

	    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
	    sdf.setTimeZone(TimeZone.getTimeZone("CST"));
	    Calendar c = Calendar.getInstance();
	    //30 Days to add
	    c.add(Calendar.DAY_OF_MONTH, days);
	  
	    //Date after adding the days to the current date
	    String newDate = sdf.format(c.getTime());
	    return newDate;
	}
	//pass number of days in the attribute to add to current date
	public static String currentDatePlusDaysAddition(int days,String sampleDateFormat)
	{
	    SimpleDateFormat sdf = new SimpleDateFormat(sampleDateFormat);
	    sdf.setTimeZone(TimeZone.getTimeZone("CST"));
	    Calendar c = Calendar.getInstance();
	    //30 Days to add
	    c.add(Calendar.DAY_OF_YEAR,days);
	  
	    //Date after adding the days to the current date
	    String newDate = sdf.format(c.getTime());
	    return newDate;
	}
	public static  String fetchCurrentDateSampleFormate()
	{
	    SimpleDateFormat sdf = new SimpleDateFormat(sampleDateFormat2);
	    Calendar c = Calendar.getInstance();
	    String newDate = sdf.format(c.getTime());
	    return newDate;
	}
	public static  String fetchCurrentDateSampleFormate(String sampleDateFormat)
	{
	    SimpleDateFormat sdf = new SimpleDateFormat(sampleDateFormat);
	    Calendar c = Calendar.getInstance();
	    String newDate = sdf.format(c.getTime());
	    return newDate;
	}
	public static  String fetchNextYearSampleFormate()
	{
	    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, 1); // to get previous year add -1
	    Date nextYear = c.getTime();
	    String newDate = sdf.format(nextYear);
		 return newDate;
	}
	
	public static  String fetchYearSampleFormate(int Year)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
	    sdf.setTimeZone(TimeZone.getTimeZone("CST"));
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, Year); // to get previous year add -1
	    Date nextYear = c.getTime();
	    String newDate = sdf.format(nextYear);
		 return newDate;
	}
	
	
	public static  String fetchYearSub1SampleFormate(int Year)
	{
	    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
	    sdf.setTimeZone(TimeZone.getTimeZone("CST"));
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, Year); // to get previous year add -1
	    c.add(Calendar.DATE, -1);
	    Date nextYear = c.getTime();
	    String newDate = sdf.format(nextYear);
		 return newDate;
	}
	
	public static String getUSTodaysDay(String d) {
		
			
		TimeZone t = TimeZone.getTimeZone("CST"); 
		//getTodaysDay("MM/dd/YYYY",timeZone)
		Date date=new Date();
		DateFormat requiredFormat=new SimpleDateFormat(d);
		requiredFormat.setTimeZone(t);
		String strCurrentDay=requiredFormat.format(date).toUpperCase();
		return strCurrentDay; 
	}
	public static  String fetchTimeSampleFormateForUS(String d,int i)
	{
	    SimpleDateFormat sdf = new SimpleDateFormat(d);
	    sdf.setTimeZone(TimeZone.getTimeZone("CST"));
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.MINUTE, i); // to get previous year add -1
	    Date nextYear = c.getTime();
	    String newDate = sdf.format(nextYear);
		 return newDate;
	}
	public static  String fetchTimeNextYearSampleFormate()
	{
	    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
	    sdf.setTimeZone(TimeZone.getTimeZone("CST"));
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, 1); // to get previous year add -1
	    Date nextYear = c.getTime();
	    String newDate = sdf.format(nextYear);
		 return newDate;
	}
	
	public static  String fetchTimeSampleFormate(int i)
	{
	    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
	    sdf.setTimeZone(TimeZone.getTimeZone("CST"));
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DAY_OF_YEAR, i); // to get previous year add -1
	    Date nextYear = c.getTime();
	    String newDate = sdf.format(nextYear);
		 return newDate;
	}
	public static  String fetchTimeNextYearSub1SampleFormate()
	{
	    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
	    sdf.setTimeZone(TimeZone.getTimeZone("CST"));
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, 1); // to get previous year add -1
	    c.add(Calendar.DATE, -1);
	    Date nextYear = c.getTime();
	    String newDate = sdf.format(nextYear);
		 return newDate;
	}
	
//	public static String getTodayyyyymmdd() {
//		Date date = new Date();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//		String today = dateFormat.format(date.getTime());
//		return today;
//	}
	
	public static String getTodayyyyyMMdd() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String today = dateFormat.format(date.getTime());
		return today;
	}


	public static String getUSNextDay(String d) {
		
		DateFormat requiredFormat=new SimpleDateFormat(d);
		requiredFormat.setTimeZone(TimeZone.getTimeZone("CST"));
		    Calendar c = Calendar.getInstance();
		    c.add(Calendar.DAY_OF_YEAR, 1); // to get previous year add -1
		    Date nextYear = c.getTime();
		    String newDate = requiredFormat.format(nextYear);
		    return newDate;
	}
public static String getNextDay(String d) {
		
		DateFormat requiredFormat=new SimpleDateFormat(d);
	
		    Calendar c = Calendar.getInstance();
		    c.add(Calendar.DAY_OF_YEAR, 1); // to get previous year add -1
		    Date nextYear = c.getTime();
		    String newDate = requiredFormat.format(nextYear);
		    return newDate;
	}
public static String getDay(String d,int i) {
	
	DateFormat requiredFormat=new SimpleDateFormat(d);

	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DAY_OF_YEAR, i); // to get previous year add -1
	    Date nextYear = c.getTime();
	    String newDate = requiredFormat.format(nextYear);
	    return newDate;
}
	
	public static String formatDateMMddyyyy(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}
	
	
	/*
	 * Get month end date
	 * 
	 */

	public static String getLastDayOfMonth() {

		Date today = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.YEAR, 1);
		calendar.add(Calendar.MONTH, 1);
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		Date lastDayOfMonth = calendar.getTime();
		DateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
		String date = sdf.format(lastDayOfMonth);
		System.out.println("Today            : " + sdf.format(today));
		System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
		return date;
	}
	
	public static String currentDatePlusAdditionalDays(int days)
	{
		
		
	    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
	    sdf.setTimeZone(TimeZone.getTimeZone("CST"));
	    Calendar c = Calendar.getInstance();
	    //30 Days to add
	    c.add(Calendar.DAY_OF_MONTH, days);
	  
	    //Date after adding the days to the current date
	    String newDate = sdf.format(c.getTime());
	    return newDate;
	}

	public static String DatePlusAdditionalDays(String date,int days,String SampleDateFormat) throws Exception
	{
        SimpleDateFormat sdf = new SimpleDateFormat(SampleDateFormat);
        Date d1 = sdf.parse(date);
        // Convert Date to Instant
        Instant currentInstant = d1.toInstant();
        // Convert Instant to ZonedDateTime for easy manipulation
        ZonedDateTime currentDateTime = currentInstant.atZone(ZoneId.systemDefault());
        // Get and Print Date 
        ZonedDateTime afteraddedDays = currentDateTime.plus(days, ChronoUnit.DAYS);
        Instant afterDaysInstant = afteraddedDays.toInstant();
        //Print the date in a readable format
        SimpleDateFormat outputFormat = new SimpleDateFormat(SampleDateFormat);
        String newDate= outputFormat.format(Date.from(afterDaysInstant));
	    return newDate;
	}
	
}