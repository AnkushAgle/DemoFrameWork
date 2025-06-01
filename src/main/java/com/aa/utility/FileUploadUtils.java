package com.aa.utility;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.openqa.selenium.JavascriptExecutor;
import com.aventstack.extentreports.Status;



public class FileUploadUtils extends Library{
	
	
	
	public static void roboupload(String filename) throws Exception {
		try {
		 
	//	String img="D:\\ANKUSH\\NEW_HORIZON\\ExcelFiles\\"+filename+".xlsx";
		
			String img=System.getProperty("user.dir")+"\\ExcelFiles\\"+filename+".xlsx";
		//"\\ConfigFiles\\"+filename+".properties";
		Robot rb=new Robot();
			rb.delay(2000);
		
		
			StringSelection ss=new StringSelection(img);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			rb.keyPress(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_V);
			rb.keyRelease(KeyEvent.VK_CONTROL);
			rb.keyRelease(KeyEvent.VK_V);
			rb.keyPress(KeyEvent.VK_ENTER);
			rb.keyRelease(KeyEvent.VK_ENTER);	
			test.log(Status.INFO,"File Upload Successfully Path=="+img);
			Reporter.log("File Upload Successfully Path=="+img);
		
	}
		
		
		catch(Exception e) {
			
			test.log(Status.FAIL,"File Not Found");
		}
	}

	public static  void fileAttachmentFunction(WebDriver driver,String filepath) throws Exception {
		
		
		
		// ANGULAR COMPONNENT NAME : app-multi-uploader
		Library.sectionNameLoggingFunction("Attachment Section");
		boolean expanded=false;
		
		
		String headerxpath="//div[@class='card']//button[contains(text(),'Drawing Files') or text()='Attachment' or contains(text(),'Attachment')]";
		WebElement attachmentbutton=driver.findElement(By.xpath(headerxpath));
		ScrollHelper.scrollToElement(driver, attachmentbutton);
		attachmentbutton=driver.findElement(By.xpath(headerxpath));
		do {
			
			String expandedornot=attachmentbutton.getAttribute("class");
	
			
			if(expandedornot.equalsIgnoreCase("btn btn-link")) {
				
				expanded=true;
				
				break;
			}
			
			
			else {
				
				
				Library.isLoadedAndClick(attachmentbutton, attachmentbutton.getText());
			}
		    // Code block to be executed
		} 
		
		
		while (expanded);
		
		
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		WebElement attachmentinputtext = driver.findElement(By.xpath("//app-multi-uploader//input[@type='file']"));
	
		Library.massagelog_TIME_DATE("Upload File Path :: "+filepath);
	//	Library.customSendkeys(attachmentinputtext, filepath, "file");
		attachmentinputtext.sendKeys(filepath);
	
	    // Execute JavaScript to set the value of the file input element
	    ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", attachmentinputtext);
	    ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1]);", attachmentinputtext, filepath);
	
	
	
	try {
		Thread.sleep(10000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	ScrollHelper.Scrolldown(driver, 0, 500);
	try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	String screenshotname=appbreadcrumbTextReturn(driver);
	      screenshotname=screenshotname+Library.DateTimeString();
	ScreenshotUtil.captureScreenshotWithOriginalName(screenshotname);
	
	/*
	// Find all the elements matching the given XPath
	List<WebElement> elements = driver.findElements(By.xpath("//table[contains(@class,'table-attachment')]//tr//th[not(@style)]"));
	
	// Create a String array to store the text from the elements
	String[] textArray = new String[elements.size()];
	
	// Iterate through each element to retrieve text and store in the array
	for (int i = 0; i < elements.size(); i++) {
	    textArray[i] = elements.get(i).getText();
	    System.out.println( textArray[i]);
	}
	
	
	
	
	
	
	
	Library.tableHeadingArrayLoggingStart(textArray);
	
	
	
	// here we have to fetch upload files details 
	StringBuffer logvar=new StringBuffer();
	
	List<WebElement> listoffilerows = driver.findElements(By.xpath("//table[contains(@class,'table-attachment')]//tbody//tr"));
	
	System.out.println("Total Rows Size Are :: "+listoffilerows);
	for(int i=0;i<listoffilerows.size();i++) {
		logvar.append(TR_ROW_TAG_OPEN_VAR);
		
		String datalist="//table[contains(@class,'table-attachment')]//tbody//tr["+Integer.toString(i+1)+"]//td[not(@style)]";
		
		List<WebElement> dataelements = driver.findElements(By.xpath(datalist));
		
		for(int j=0;j<dataelements.size();j++) {
			
			String data = Library.GETTextWebElement(dataelements.get(i));
		
			System.out.println(data);
			logvar.append(Library.td_tag_Data_Fill_Function(data));
			
		}
		
		logvar.append(TABLETAG_TAG_CLOSE_VAR);
	
	}
	
	logvar.append( TABLETAG_TAG_CLOSE_VAR);
	
	Reporter.log(logvar.toString());
	
	
	
	*/
	
	}

	

    /**
     * Logs a formatted PASS message for file upload actions (using Extent & Reporter).
     */
    public static void logFileUpload(String fieldName, String fileName) {
        String formattedLog = "<div style='border: 1px solid #ddd; padding: 8px; margin: 5px 0; "
                + "border-radius: 4px; background-color: #f7f7f7; display: inline-block;'>"
                + "<strong>#FILEUP 🔼</strong> :: <strong>" + fieldName
                + "</strong> File Uploaded ⇒ <span style='color: #001dff;'>" + fileName + "</span>"
                + "</div>";
        Reporter.log(formattedLog);
        test.log(Status.PASS, formattedLog);
    }

    /**
     * Scrolls to the file‐input element, sends the local path, and logs success/failure.
     * @param driver           your WebDriver instance
     * @param fileInputElement the <input type="file"> WebElement
     * @param filePath         local absolute path of the file to upload
     * @param fieldName        friendly name for logging
     * @return true if upload & log succeeded, false otherwise
     */
    public static boolean uploadFile(WebDriver driver, WebElement fileInputElement, String filePath, String fieldName) {
        boolean isUploaded = false;

        try {
            // Scroll into view first (using your ScrollHelper)
            ScrollHelper.scrollToElement(driver, fileInputElement);

            // Send the local path to the <input type="file"> element
            fileInputElement.sendKeys(filePath);

            // Extract just the file name (e.g. "photo.jpg") from the full path
            String[] parts = filePath.replace("\\", "/").split("/");
            String fileName = parts[parts.length - 1];

            // Log the successful upload
            logFileUpload(fieldName, fileName);
            isUploaded = true;

        } catch (Exception e) {
            // Log a failure message in red styling
            Reporter.log("<div style='color: red;'><strong>File upload failed for field: "
                    + fieldName + " with path: " + filePath + "</strong></div>");
            e.printStackTrace();
        }

        return isUploaded;
    }
	
	
	
	public static  void multipleFileAttachmentFunction(WebDriver driver,String[] filepath) throws Exception {
		
		
		// ANGULAR COMPONNENT NAME : app-multi-uploader
		int totalnumberoffiles=filepath.length;
		
	
		Library.sectionNameLoggingFunction("Attachment Section");
		
		Library.subCalculationHeadingLoggingFunction("Total Number File To Be Upload Are :: "+Integer.toString(totalnumberoffiles));
	
		boolean expanded=false;
		
		
		String headerxpath="//div[@class='card']//button[contains(text(),'Drawing Files') or text()='Attachment' or contains(text(),'Attachment')]";
		WebElement attachmentbutton=driver.findElement(By.xpath(headerxpath));
		ScrollHelper.scrollToElement(driver, attachmentbutton);
		attachmentbutton=driver.findElement(By.xpath(headerxpath));
		do {
			
			String expandedornot=attachmentbutton.getAttribute("class");
	
			
			if(expandedornot.equalsIgnoreCase("btn btn-link")) {
				
				expanded=true;
				ScrollHelper.Scrolldown(driver, 0, 500);
				break;
			}
			
			
			else {
				
				
				Library.isLoadedAndClick(attachmentbutton, attachmentbutton.getText());
			}
		    // Code block to be executed
		} 
		
		
		while (expanded);
		
		
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(int i=0;i<filepath.length;i++) {
		
		
		WebElement attachmentinputtext = driver.findElement(By.xpath("//app-multi-uploader//input[@type='file']"));
	
		Library.massagelog_TIME_DATE("Upload File Path :: "+filepath[i]);
	//	Library.customSendkeys(attachmentinputtext, filepath, "file");
		attachmentinputtext.sendKeys(filepath[i]);
	
	    // Execute JavaScript to set the value of the file input element
	    ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", attachmentinputtext);
	    ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1]);", attachmentinputtext, filepath);
	
	
	
	try {
		Thread.sleep(10000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	
	}
		
	
	ScrollHelper.Scrolldown(driver, 0, 500);
	try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	String screenshotname=appbreadcrumbTextReturn(driver);
	      screenshotname=screenshotname+Library.DateTimeString();
	ScreenshotUtil.captureScreenshotWithOriginalName(screenshotname);
	
	
	}

	public static String appbreadcrumbTextReturn(WebDriver driver) {
		
		List<WebElement> Headings = driver.findElements(By.xpath("//app-breadcrumb//li//span"));
	
		StringBuffer sb=new StringBuffer("We Navigated To :: ");
	
	
	
		for(int i=0;i<Headings.size();i++) {
			
			 WebElement element = Headings.get(i);
			 String Headingstring=Library.getTextOfWebElementFunction(element);
			 sb.append(Headingstring);
			 if((i<Headings.size()-1)) {
				sb.append(" /  ");
				 
			 }
			 
			
			 
			 
			 
			
		}
	
	
		String desc=sb.toString();
		
		
	
	
		// Split the input string based on "/"
		String[] parts = desc.split(" / ");
	
	
		String doc= parts[1].trim();
	
		doc=doc.replace(" ", "");
	
	return doc;
	}
}
