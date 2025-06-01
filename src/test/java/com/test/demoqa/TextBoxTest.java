package com.test.demoqa;


import org.testng.annotations.*;

import com.aa.utility.BaseClass;
import com.aa.utility.ExcelReader;
import com.aa.utility.Library;
import com.pom.demoqa.TextBoxPage;

import org.testng.Assert;
import org.openqa.selenium.WebDriver;



public class TextBoxTest extends BaseClass{
    public String excelPath = System.getProperty("user.dir") + "\\ExcelFiles\\TestData.xlsx";

	@Test(dataProvider = "indexProvider")
	public void verifyTextBoxFormSubmission(int index) {
        Library.iterationNumberLoggingForDataDriven(index);

	    String sheetName = "TextBoxData";

	    ExcelReader excelReader = new ExcelReader(excelPath);

	    String fullName = excelReader.getCellData(sheetName, "Full Name", index);
	    String email = excelReader.getCellData(sheetName, "Email", index);
	    String currentAddress = excelReader.getCellData(sheetName, "Current Address", index);
	    String permanentAddress = excelReader.getCellData(sheetName, "Permanent Address", index);

	    WebDriver driver = getDriver();

	    String browser = Library.getBrowserName(driver);
	    Library.logBrowserName(browser);
	    Library.logPcNameAndHostName();

	    driver.navigate().to("https://demoqa.com/text-box");

	    TextBoxPage textBoxPage = new TextBoxPage(driver);
	    textBoxPage.enterFullName(fullName);
	    textBoxPage.enterEmail(email);
	    textBoxPage.enterCurrentAddress(currentAddress);
	    textBoxPage.enterPermanentAddress(permanentAddress);
	    textBoxPage.clickSubmitButton();

	    Assert.assertTrue(textBoxPage.getSubmittedName().contains(fullName));
	    Assert.assertTrue(textBoxPage.getSubmittedEmail().contains(email));
	}


	@DataProvider(name = "indexProvider")
	public Object[][] provideIndex() {
		  String sheetName = "TextBoxData";
		  ExcelReader excelReader = new ExcelReader(excelPath);

		    String fullName[] = excelReader.getColumnData(sheetName, "Full Name");
		
	    int maximumNumberOfData = fullName.length; // You can change this value as needed

	    Object[][] data = new Object[maximumNumberOfData][1];

	    for (int i = 0; i < maximumNumberOfData; i++) {
	        data[i][0] = i + 1; // Excel data usually starts from row 1 (excluding header)
	    }

	    return data;
	}

  
}
