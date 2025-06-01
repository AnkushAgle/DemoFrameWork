package com.aa.utility;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class CalendarUtil {

	
	 
	 public static String monthNumberStringReturn(String month) {
	      
		 
		 
		 switch (month) {
	            case "01":
	            case "Jan":
	            case "January":
	                return "01";
	                
	            case "02":
	            case "Feb":
	            case "February":
	                return "02";
	            case "03":
	            case "Mar":
	            case "March":
	                return "03";
	            case "04":
	            case "Apr":
	            case "April":
	                return "04";
	            case "05":
	            case "May":
	                return "05";
	            case "06":
	            case "Jun":
	            case "June":
	                return "06";
	            case "07":
	            case "Jul":
	            case "July":
	                return "07";
	            case "08":
	            case "Aug":
	            case "August":
	                return "08";
	            case "09":
	            case "Sep":
	            case "Sept":
	            case "September":
	                return "09";
	            case "10":
	            case "Oct":
	            case "October":
	                return "10";
	            case "11":
	            case "Nov":
	            case "November":
	                return "11";
	            case "12":
	            case "Dec":
	            case "December":
	                return "12";
	            default:
	                return "Invalid Month";
	        }
	
	 
	 
	 
	 
	 
	 
	 
	 
	 }
	 
	 
	 public static String monthStringReturn(String month) {
			switch (month) {
			case "01":
				return "January";
			case "02":
				return "February";
			case "03":
				return "March";
			case "04":
				return "April";
			case "05":
				return "May";
			case "06":
				return "June";
			case "07":
				return "July";
			case "08":
				return "August";
			case "09":
				return "September";
			case "10":
				return "October";
			case "11":
				return "November";
			case "12":
				return "December";
			default:
				return "No any Month";

			}

			/*
			 * if (month.equalsIgnoreCase("01")) {
			 * 
			 * return "January"; }
			 * 
			 * else if (month.equalsIgnoreCase("02")) {
			 * 
			 * return "February"; } else if (month.equalsIgnoreCase("03")) {
			 * 
			 * return "March"; }
			 * 
			 * else if (month.equalsIgnoreCase("04")) {
			 * 
			 * return "April"; }
			 * 
			 * else if (month.equalsIgnoreCase("05")) {
			 * 
			 * return "May"; }
			 * 
			 * else if (month.equalsIgnoreCase("06")) {
			 * 
			 * return "June"; }
			 * 
			 * else if (month.equalsIgnoreCase("07")) {
			 * 
			 * return "July"; } else if (month.equalsIgnoreCase("08")) {
			 * 
			 * return "August"; }
			 * 
			 * //September October November December
			 * 
			 * else if (month.equalsIgnoreCase("09")) {
			 * 
			 * return "September"; }
			 * 
			 * else if (month.equalsIgnoreCase("10")) {
			 * 
			 * return "October"; }
			 * 
			 * else if (month.equalsIgnoreCase("11")) {
			 * 
			 * return "November"; }
			 * 
			 * else if (month.equalsIgnoreCase("12")) {
			 * 
			 * return "December"; }
			 * 
			 * return "No any Month";
			 */

		}

	
	public static void pickDateDemoQA(WebDriver driver, WebElement dateInputElement, String date, String fieldName) throws Exception {
	  
		
		ScrollHelper.scrollToElement(driver, dateInputElement);
		
		boolean isStartsWithYear=false;
		// Log the date
	   /// Library.dialogMassageLog(date);

	    if (date.contains("NODATA")) {
	        Assert.fail("Invalid Date Provided: " + date);
	    }

	    String day ="";
	    String month="";
	    String year="";
	    // Date format expected: "dd MMM yyyy" e.g. "21 Feb 1997"
	    String[] dateParts = date.split("-");
	    if (dateParts.length != 3) {
	       // Assert.fail("Date format should be 'dd MMM yyyy' but was: " + date);
	    }
	    
	    day=dateParts[0];

	    
	    if(day.length()==4) {
	    	
	    	isStartsWithYear=true;
	    	
	    }
	    
	    
	    
	    if(isStartsWithYear) {
	    	year=dateParts[0];
	    	month=dateParts[1];
	    	day=dateParts[2];
	    	 month=monthStringReturn(month);
	    }
	    else {
	    
		String modifieddate=date;
		 modifieddate = modifieddate.replaceAll("/", "");
       modifieddate=modifieddate.replaceAll("-", "");
    
       
		 day = modifieddate.substring(0, 2);//31 
         month = modifieddate.substring(2, 4);
        
        month=monthStringReturn(month);
        year = modifieddate.substring(4, 8);
	    }
		
      /// month=Library.mo
	    // Click the date input to open calendar popup
	    Library.Explicitlywait(dateInputElement);
	    dateInputElement.click();
	    Thread.sleep(1000); // Wait for calendar popup to open

	    // Select year from year dropdown
	    WebElement yearDropdown = driver.findElement(By.className("react-datepicker__year-select"));
	    Library.Explicitlywait(yearDropdown);
	    Select selectYear = new Select(yearDropdown);
	    selectYear.selectByVisibleText(year);

	    // Select month from month dropdown
	    WebElement monthDropdown = driver.findElement(By.className("react-datepicker__month-select"));
	    Library.Explicitlywait(monthDropdown);
	    Select selectMonth = new Select(monthDropdown);
	    selectMonth.selectByVisibleText(month);

	    // Wait shortly for days to update after changing month/year
	    Thread.sleep(500);

	    // Locate all day elements in the current calendar view
	    List<WebElement> dayElements = driver.findElements(By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'react-datepicker__day--outside-month'))]"));

	    boolean dayFound = false;
	    for (WebElement dayElement : dayElements) {
	    	
	    	
	    	if(day.startsWith("0")) {
	    		
	    		day=day.replaceAll("0", "");
	    		
	    	}
	    	
	    	
	        String dayText = dayElement.getText();
	        if (dayText.equals(day)) {
	            // Click the day
	            Library.DClick(dayElement, "Date Picker - " + fieldName);
	            Library.massagelog("#DATEPICKER 📅 :: " + fieldName + " :: Date successfully picked :: " + date);
	            dayFound = true;
	            break;
	        }
	    }

	    if (!dayFound) {
	        Assert.fail("Day " + day + " not found in the date picker for " + fieldName);
	    }
	}

}
