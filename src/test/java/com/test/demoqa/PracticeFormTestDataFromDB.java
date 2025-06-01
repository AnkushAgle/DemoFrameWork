package com.test.demoqa;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aa.utility.BaseClass;
import com.aa.utility.DataBaseUtils;
import com.aa.utility.DataBaseUtils.DBTables;
import com.aa.utility.Library;
import com.aa.utility.ScreenshotUtil;
import com.pom.demoqa.PracticeFormPage;

public class PracticeFormTestDataFromDB extends BaseClass {

    public String loginSheetPath = System.getProperty("user.dir") + "\\ExcelFiles\\LOGIN.xlsx";
    public String tableName = "PracticeFormData";

    @Test(dataProvider = "indexProvider")
    public void fillPracticeFormTest(int index) throws Exception {
        // Initialize DB Utility
        DataBaseUtils dbUtils = new DataBaseUtils(loginSheetPath,"");

        // Fetch full data from DB
        List<Map<String, String>> allData = dbUtils.getAllDataFromTable(DBTables.DB_TABLE_PracticeFormData);

        // Validate index
        if (index <= 0 || index > allData.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        Map<String, String> row = allData.get(index - 1);
        String formId = row.get("FormId");

        String firstName = row.get("First Name");
        String lastName = row.get("Last Name");
        String email = row.get("Email");
        String gender = row.get("Gender");
        String mobile = row.get("Mobile Number");
        String dob = row.get("Date of Birth");
        String subjects = row.get("Subjects");
        String hobbies = row.get("Hobbies");
        String picture = row.get("Picture");
        String address = row.get("Current Address");
        String state = row.get("State");
        String city = row.get("City");

        String uploadFilePath = row.get("UploadFilePath");
        if (uploadFilePath == null || uploadFilePath.trim().equalsIgnoreCase("NODATA")) {
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

        String path = ScreenshotUtil.captureScreenshotWithRandomName();
        Library.dialogSkipMessageLog("Screenshot saved at: " + path);

        formPage.verifyForm();
        
        String massage = "✅ [Automation Tester] Automation Testing Successfully Done and Form Successfully Submitted :: On Timestamp " 
                + Library.DateTimeString_LOGGING_24_HRS();
        dbUtils.updateAutomationTestingStatus(DBTables.DB_TABLE_PracticeFormData, formId,"FormId", massage);
    }

    @DataProvider(name = "indexProvider")
    public Object[][] provideIndex() throws Exception {
        DataBaseUtils dbUtils = new DataBaseUtils(loginSheetPath,"");
        List<Map<String, String>> allData = dbUtils.getAllDataFromTable(DBTables.DB_TABLE_PracticeFormData);

        int maxData = allData.size(); // Dynamically get row count from DB
        Object[][] data = new Object[maxData][1];

        for (int i = 0; i < maxData; i++) {
            data[i][0] = i + 1;
        }

        return data;
    }
}
