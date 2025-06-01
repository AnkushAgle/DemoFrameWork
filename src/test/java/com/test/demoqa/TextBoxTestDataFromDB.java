package com.test.demoqa;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aa.utility.BaseClass;
import com.aa.utility.DataBaseUtils;
import com.aa.utility.DataBaseUtils.DBTables;
import com.aa.utility.Library;
import com.pom.demoqa.TextBoxPage;

public class TextBoxTestDataFromDB extends BaseClass {

    public String loginSheetPath = System.getProperty("user.dir") + "\\ExcelFiles\\LOGIN.xlsx";

    @Test(dataProvider = "indexProvider")
    public void verifyTextBoxFormSubmission(int index) throws Exception {
        // Initialize DB Utility
        DataBaseUtils dbUtils = new DataBaseUtils(loginSheetPath, "");

        // Fetch all records
        List<Map<String, String>> allData = dbUtils.getAllDataFromTable(DBTables.DB_TABLE_TextBoxData);

        // Index validation
        if (index <= 0 || index > allData.size()) {
            throw new IndexOutOfBoundsException("Invalid data index: " + index);
        }

        // Read data row
        Map<String, String> row = allData.get(index - 1);
        String fullName = row.get("Full Name");
        String email = row.get("Email");
        String currentAddress = row.get("Current Address");
        String permanentAddress = row.get("Permanent Address");

        WebDriver driver = getDriver();
        Library.logBrowserName(Library.getBrowserName(driver));
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

        String message = "✅ [Automation Tester] TextBox Form Submitted Successfully at " + Library.DateTimeString_LOGGING_24_HRS();
        dbUtils.updateAutomationTestingStatus(DBTables.DB_TABLE_TextBoxData, row.get("NameId"),"NameId", message);
    }

    @DataProvider(name = "indexProvider")
    public Object[][] provideIndex() throws Exception {
        DataBaseUtils dbUtils = new DataBaseUtils(loginSheetPath, "");
        List<Map<String, String>> allData = dbUtils.getAllDataFromTable(DBTables.DB_TABLE_TextBoxData);

        int maxData = allData.size();
        //int maxData = 1;

        Object[][] data = new Object[maxData][1];

        for (int i = 0; i < maxData; i++) {
            data[i][0] = i + 1;
        }

        return data;
    }
}
