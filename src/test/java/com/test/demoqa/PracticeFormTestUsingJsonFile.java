package com.test.demoqa;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aa.utility.BaseClass;
import com.aa.utility.JSONUtils;
import com.aa.utility.Library;
import com.aa.utility.ScreenshotUtil;
import com.google.gson.*;
import com.pom.demoqa.PracticeFormPage;

import java.io.FileReader;
import java.util.List;

public class PracticeFormTestUsingJsonFile extends BaseClass {

    public String jsonPath = System.getProperty("user.dir") + "\\JsonFiles\\TestData.json";
    private final String sheetName = "PracticeFormData";  // Corresponds to the JSON key

    private JsonArray loadJsonArrayFromFile() throws Exception {
        JsonObject jsonObject = JsonParser.parseReader(new FileReader(jsonPath)).getAsJsonObject();
        return jsonObject.getAsJsonArray(sheetName);
    }

    @Test(dataProvider = "indexProvider")
    public void fillPracticeFormTest(int index) throws Exception {
    	
        Library.iterationNumberLoggingForDataDriven(index);

        JsonArray dataArray = loadJsonArrayFromFile();
        JsonObject data = dataArray.get(index - 1).getAsJsonObject();  // Adjust for 0-based index

        String firstName = data.get("First Name").getAsString();
        String lastName = data.get("Last Name").getAsString();
        String email = data.get("Email").getAsString();
        String gender = data.get("Gender").getAsString();
        String mobile = data.get("Mobile Number").getAsString();
        String dob = data.get("Date of Birth").getAsString();
        String address = data.get("Current Address").getAsString();
        String hobbies = data.get("Hobbies").getAsString();
        String picture = data.get("Picture").getAsString();
        String currentAddress = data.get("Current Address").getAsString();
        String state = data.get("State").getAsString();
        String city = data.get("City").getAsString();
        String subjects = data.get("Subjects").getAsString();
        String uploadFilePath = data.get("UploadFilePath").getAsString();

        if (uploadFilePath.contains("NODATA")) {
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
        String screenshotPath = ScreenshotUtil.captureScreenshotWithRandomName();

        // Optionally log or update this path in log only since JSON is not overwritten here
        System.out.println("Screenshot saved at: " + screenshotPath);

        formPage.verifyForm();
    }

    @DataProvider(name = "indexProvider")
    public Object[][] provideIndex() throws Exception {
        JsonArray dataArray = loadJsonArrayFromFile();
        int maxData = Math.min(dataArray.size(), 5);  // Limit to 5 for demo, or remove for full data

        Object[][] data = new Object[maxData][1];
        for (int i = 0; i < maxData; i++) {
            data[i][0] = i + 1;
        }
        return data;
    }
}
