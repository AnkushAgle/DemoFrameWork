package com.test.demoqa;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aa.utility.BaseClass;
import com.aa.utility.ExcelReader;
import com.aa.utility.Library;
import com.aa.utility.ScreenshotUtil;
import com.pom.demoqa.PracticeFormPage;

public class PracticeFormTest extends BaseClass {

    public String excelPath = System.getProperty("user.dir") + "\\ExcelFiles\\TestData.xlsx";

    
    
    
    
    
    @Test(dataProvider = "indexProvider")
    public void fillPracticeFormTest(int index) throws Exception {
        String sheetName = "PracticeFormData";

        Library.iterationNumberLoggingForDataDriven(index);
        
        ExcelReader excelReader = new ExcelReader(excelPath);
        String firstName = excelReader.getCellData(sheetName, "First Name", index);
        String lastName = excelReader.getCellData(sheetName, "Last Name", index);
        String email = excelReader.getCellData(sheetName, "Email", index);
        String gender = excelReader.getCellData(sheetName, "Gender", index);
        String mobile = excelReader.getCellData(sheetName, "Mobile Number", index);
        String dob = excelReader.getCellData(sheetName, "Date of Birth", index);
        String address = excelReader.getCellData(sheetName, "Current Address", index);
        String hobbies = excelReader.getCellData(sheetName, "Hobbies", index);
        String picture = excelReader.getCellData(sheetName, "Picture", index);
        String currentAddress = excelReader.getCellData(sheetName, "Current Address", index);
        String state = excelReader.getCellData(sheetName, "State", index);
        String city = excelReader.getCellData(sheetName, "City", index);	
        String subjects = excelReader.getCellData(sheetName, "Subjects", index);
        String uploadFilePath = excelReader.getCellData(sheetName, "UploadFilePath", index);
        
        if(uploadFilePath.contains("NODATA")) {
        	
        	uploadFilePath = System.getProperty("user.dir") + "\\UploadFiles\\Screenshot_3.png";

        }
        WebDriver driver = getDriver();
        Library.logBrowserName(Library.getBrowserName(driver));
        Library.logPcNameAndHostName();

        driver.navigate().to("https://demoqa.com/automation-practice-form");

        PracticeFormPage formPage = new PracticeFormPage(driver);
        formPage.enterFirstName(firstName);
        formPage.enterLastName(lastName);
        formPage.enterEmail(email);

        formPage.selectGender(gender);
        
        
        
        formPage.enterMobileNumber(mobile);
        formPage.pickDateOfBirth(dob);
        formPage.enterSubject(subjects);
        
        formPage.selectHobbies(hobbies);
        
        formPage.uploadSelectpicture(uploadFilePath);
        
        formPage.enterCurrentAddress(address);
        formPage.selectSate(state);
        formPage.selectCity(city);
        
        formPage.clickSubmitButton();

        Thread.sleep(1000);
       String path= ScreenshotUtil.captureScreenshotWithRandomName();
       
       excelReader.setCellData(sheetName, "UploadFilePath", index, path);
        formPage.verifyForm();
        
        // Optional: Add assertions here to validate submission
    }

    @DataProvider(name = "indexProvider")
    public Object[][] provideIndex() {
        String sheetName = "PracticeFormData";
        ExcelReader excelReader = new ExcelReader(excelPath);
        String[] firstNameData = excelReader.getColumnData(sheetName, "First Name");

        int maxData = 5;
        //int maxData = firstNameData.length;

        
        Object[][] data = new Object[maxData][1];

        for (int i = 0; i < maxData; i++) {
            data[i][0] = i + 1;
        }

        return data;
    }
}
