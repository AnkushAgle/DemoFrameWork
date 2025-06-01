package com.test.demoqa;

import org.testng.annotations.*;
import com.aa.utility.BaseClass;
import com.aa.utility.Library;
import com.google.gson.*;
import com.pom.demoqa.TextBoxPage;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import java.io.FileReader;

public class TextBoxTestUsingJsonFile extends BaseClass {

    public String jsonPath = System.getProperty("user.dir") + "\\JsonFiles\\TestData.json";
    private final String sheetName = "TextBoxTest";  // JSON key for this test

    private JsonArray loadJsonArrayFromFile() throws Exception {
        JsonObject jsonObject = JsonParser.parseReader(new FileReader(jsonPath)).getAsJsonObject();
        return jsonObject.getAsJsonArray(sheetName);
    }

    @Test(dataProvider = "indexProvider")
    public void verifyTextBoxFormSubmission(int index) throws Exception {
    	
        Library.iterationNumberLoggingForDataDriven(index);

        JsonArray dataArray = loadJsonArrayFromFile();
        JsonObject data = dataArray.get(index - 1).getAsJsonObject();  // 0-based index adjustment

        String fullName = data.get("Full Name").getAsString();
        String email = data.get("Email").getAsString();
        String currentAddress = data.get("Current Address").getAsString();
        String permanentAddress = data.get("Permanent Address").getAsString();

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
    }

    @DataProvider(name = "indexProvider")
    public Object[][] provideIndex() throws Exception {
        JsonArray dataArray = loadJsonArrayFromFile();
        int maxData = dataArray.size();

        Object[][] data = new Object[maxData][1];
        for (int i = 0; i < maxData; i++) {
            data[i][0] = i + 1;
        }
        return data;
    }
}
