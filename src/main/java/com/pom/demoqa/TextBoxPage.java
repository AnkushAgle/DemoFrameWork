package com.pom.demoqa;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.aa.utility.Library;



public class TextBoxPage {

	public WebDriver driver;

	public TextBoxPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(how = How.ID, using = "userName")
	private WebElement fullNameInput;

	@FindBy(how = How.ID, using = "userEmail")
	private WebElement emailInput;

	@FindBy(how = How.ID, using = "currentAddress")
	private WebElement currentAddressInput;

	@FindBy(how = How.ID, using = "permanentAddress")
	private WebElement permanentAddressInput;

	@FindBy(how = How.ID, using = "submit")
	private WebElement submitButton;

	@FindBy(how = How.ID, using = "name")
	private WebElement outputName;

	@FindBy(how = How.ID, using = "email")
	private WebElement outputEmail;

	// Business Methods with Library Utility
	public void enterFullName(String name) {
		Library.customSendkeysWithValdation(fullNameInput, name, "Full Name");
	}

	public void enterEmail(String email) {
		Library.customSendkeysWithValdation(emailInput, email, "Email");
	}

	public void enterCurrentAddress(String address) {
		Library.customSendkeysWithValdation(currentAddressInput, address, "Current Address");
	}

	public void enterPermanentAddress(String address) {
		Library.customSendkeysWithValdation(permanentAddressInput, address, "Permanent Address");
	}

	public void clickSubmitButton() {
		Library.isLoadedAndClick(submitButton, "Submit Button");
	}

	public String getSubmittedName() {
		return Library.getTextOfWebElementFunction(outputName);
	}

	public String getSubmittedEmail() {
		return Library.getTextOfWebElementFunction(outputEmail);
	}

	// Getter Methods (if needed)
	public WebElement getFullNameInput() {
		return fullNameInput;
	}

	public WebElement getEmailInput() {
		return emailInput;
	}

	public WebElement getCurrentAddressInput() {
		return currentAddressInput;
	}

	public WebElement getPermanentAddressInput() {
		return permanentAddressInput;
	}

	public WebElement getSubmitButton() {
		return submitButton;
	}

	public WebElement getOutputName() {
		return outputName;
	}

	public WebElement getOutputEmail() {
		return outputEmail;
	}
}
