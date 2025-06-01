package com.pom.demoqa;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.aa.utility.CalendarUtil;
import com.aa.utility.FileUploadUtils;
import com.aa.utility.Library;


public class PracticeFormPage {

	public WebDriver driver;

	public PracticeFormPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(how = How.ID, using = "firstName")
	private WebElement firstNameInput;

	@FindBy(how = How.ID, using = "lastName")
	private WebElement lastNameInput;

	@FindBy(how = How.ID, using = "userEmail")
	private WebElement emailInput;

	@FindBy(how = How.XPATH, using = "//label[contains(text(),'Male')]")
	private WebElement genderMaleRadio;

	
	@FindBy(how = How.ID, using = "subjectsInput")
	private WebElement subjectInput;

	
	// Business Methods using Library Utilities
		public void enterSubject(String subject) {
			Library.customSendkeysWithValdation(subjectInput, subject, "Subject");
		}
	
	public void selectGender(String gender) {
	    WebElement genderRadio;

	    if (gender.equalsIgnoreCase("Male")) {
	        genderRadio = driver.findElement(By.id("gender-radio-1"));
	    } else if (gender.equalsIgnoreCase("Female")) {
	        genderRadio = driver.findElement(By.id("gender-radio-2"));
	    } else if (gender.equalsIgnoreCase("Other")) {
	        genderRadio = driver.findElement(By.id("gender-radio-3"));
	    } else {
	        throw new IllegalArgumentException("Invalid gender value: " + gender);
	    }
	    
	    // Click the label associated with the radio button for better reliability
	    String labelFor = genderRadio.getAttribute("id");
	    WebElement label = driver.findElement(By.cssSelector("label[for='" + labelFor + "']"));
	    Library.isLoadedAndClick(label, "Gender " + gender + " Radio");
	}

	@FindBy(id = "uploadPicture")
	private WebElement uploadPictureInput;

	public void uploadSelectpicture(String path) {
		
		boolean ok = FileUploadUtils.uploadFile(
			    driver,
			    uploadPictureInput,
			    path,
			    "Profile Picture"
			);

	}
	
	public void selectHobbies(String hobbiesCSV) {
	    // Split the input string by comma, trim spaces
	    String[] hobbiesArray = hobbiesCSV.split(",");
	    
	    for (String hobby : hobbiesArray) {
	        hobby = hobby.trim(); // remove leading/trailing spaces
	        
	        String checkboxId = "";
	        
	        if (hobby.equalsIgnoreCase("Sports")) {
	            checkboxId = "hobbies-checkbox-1";
	        } else if (hobby.equalsIgnoreCase("Reading")) {
	            checkboxId = "hobbies-checkbox-2";
	        } else if (hobby.equalsIgnoreCase("Music")) {
	            checkboxId = "hobbies-checkbox-3";
	        } else {
	            throw new IllegalArgumentException("Invalid hobby: " + hobby);
	        }
	        
	        WebElement checkbox = driver.findElement(By.id(checkboxId));
	        // Click the associated label for better reliability
	        WebElement label = driver.findElement(By.cssSelector("label[for='" + checkboxId + "']"));
	        
	        if (!checkbox.isSelected()) {
	            Library.isLoadedAndClick(label, "Hobby: " + hobby);
	        }
	    }
	}

	
	
	@FindBy(how = How.ID, using = "userNumber")
	private WebElement mobileNumberInput;

	@FindBy(how = How.ID, using = "dateOfBirthInput")
	private WebElement dateOfBirthInput;

	@FindBy(how = How.ID, using = "currentAddress")
	private WebElement currentAddressInput;

	@FindBy(how = How.ID, using = "submit")
	private WebElement submitButton;

	
	public void selectSate(String state) {
		
		boolean isStateSelected = Library.selectReactSelectOption(driver, By.id("state"), state, "State");
		
	}
	
	public void selectCity(String city) {
		boolean isCitySelected = Library.selectReactSelectOption(driver, By.id("city"), city, "City");

		
	}
	// Business Methods using Library Utilities
	public void enterFirstName(String firstName) {
		Library.customSendkeysWithValdation(firstNameInput, firstName, "First Name");
	}

	public void enterLastName(String lastName) {
		Library.customSendkeysWithValdation(lastNameInput, lastName, "Last Name");
	}

	public void enterEmail(String email) {
		Library.customSendkeysWithValdation(emailInput, email, "Email");
	}



	public void enterMobileNumber(String number) {
		Library.customSendkeysWithValdation(mobileNumberInput, number, "Mobile Number");
	}

	public void enterDateOfBirth(String dob) {
		Library.customSendkeysWithValdation(dateOfBirthInput, dob, "Date of Birth");
	}

	public void enterCurrentAddress(String address) {
		Library.customSendkeysWithValdation(currentAddressInput, address, "Current Address");
	}

	public void clickSubmitButton() {
		Library.isLoadedAndClick(submitButton, "Submit Button");
	}

	
	
	public void verifyForm() {
		
		// Locate all rows of the table body
		List<WebElement> rows = driver.findElements(By.xpath("//table//tbody/tr"));

		// Initialize label and value arrays
		String[] labels = new String[rows.size()];
		String[] values = new String[rows.size()];

		// Loop through each row and extract label & value
		for (int i = 0; i < rows.size(); i++) {
		    List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
		    labels[i] = cols.get(0).getText().trim(); // First column = label
		    values[i] = cols.get(1).getText().trim(); // Second column = value
		}

		// Example: print both arrays
		for (int i = 0; i < labels.length; i++) {
		    System.out.println(labels[i] + " => " + values[i]);
		}

		
		Library.KeyValueHeadingArrayLoggingStart(labels, values);
	}
	public void pickDateOfBirth(String dob) throws Exception {
		
		WebElement dobInput = driver.findElement(By.id("dateOfBirthInput"));
		CalendarUtil.pickDateDemoQA(driver, dobInput, dob, "Date of Birth");
		
	}
	// Getter Methods (Optional)
	public WebElement getFirstNameInput() {
		return firstNameInput;
	}

	public WebElement getLastNameInput() {
		return lastNameInput;
	}

	public WebElement getEmailInput() {
		return emailInput;
	}

	public WebElement getGenderMaleRadio() {
		return genderMaleRadio;
	}

	public WebElement getMobileNumberInput() {
		return mobileNumberInput;
	}

	public WebElement getDateOfBirthInput() {
		return dateOfBirthInput;
	}

	public WebElement getCurrentAddressInput() {
		return currentAddressInput;
	}

	public WebElement getSubmitButton() {
		return submitButton;
	}
} 
