package com.aa.utility;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aa.utility.Library;

import java.util.logging.Level;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import net.bytebuddy.utility.RandomString;

public class Library extends BaseClass {

    public static ExtentTest test;
    
    public static String getRowNumberFromRowId(String gridRowId) {
        WebDriver driver = getDriver();
        // Extract numeric digits from the row ID
        Pattern pattern = Pattern.compile("^row(\\d+)jqxWidget");
        Matcher matcher = pattern.matcher(gridRowId);

        if (matcher.find()) {
            return matcher.group(1); // Extracted row number as a String
        } else {
            throw new IllegalArgumentException("Invalid grid row ID format: " + gridRowId);
        }
    }

    public static int logStepMessage(int step, String msg) {
        // Creating the HTML structure for the step log
        StringBuilder logvar = new StringBuilder();

        // Styling the step message box
        logvar.append("<div style=\"background-color:lightgreen; border:2px solid green; ");
        logvar.append("border-radius:10px; padding:10px 15px; text-align:left; ");
        logvar.append("margin:10px 0; display:inline-block;\">");

        // Message content with step number
        logvar.append("<p style='font-size:18px; margin:0;'><b>Step ")
             .append(step)
             .append(":</b> ")
             .append(msg)
             .append("</p>");

        logvar.append("</div>");

        // Logging the HTML in the console and report
        Reporter.log(logvar.toString(), true); // Logs in both console & report

        return step + 1; // Increment and return the next step number
    }


	public static void showQueryLogging(String msg) {
		System.out.println(msg);

		try {
			// Directly log the query in a nicely formatted way
			String queryHtml = "<div style='background-color:#f8f9fa; padding:10px; border:1px solid #ccc; "
					+ "border-radius:5px;'><h4 style=\"color:#0c4a22;\"><b>SQL Query:</b> " + msg + "</h4></div>";

			// Log the formatted query directly in TestNG Report
			Reporter.log(queryHtml);

			// Log to Extent Report with the formatted query content
			test.log(Status.INFO, "<span style='color:black;'>" + queryHtml + "</span>");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showQueryButtonLogging(String msg) {

		showQueryLogging(msg);

		if (true) {

			return;

		}

		System.out.println(msg);

		try {
			// HTML content to create a "Show Query" button and hide/display the query
			String buttonHtml = "<button onclick=\"var q = document.getElementById('query'); "
					+ "q.style.display = q.style.display === 'none' ? 'block' : 'none';\">" + "Show Query</button>";

			// Hidden query content that becomes visible when button is clicked
			String queryHtml = "<div id='query' style='display:none;'><h4 style=\"color:#0c4a22;\"><b>" + msg
					+ "</b></h4></div>";

			// Log the button and hidden query in TestNG Report
			Reporter.log(buttonHtml + queryHtml);

			// Log to Extent Report with HTML content
			test.log(Status.INFO, "<span style='color:black;'>" + buttonHtml + queryHtml + "</span>");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void dialogSkipMessageLog(String msg) {
    // Creating the HTML structure for the skip dialog box
    StringBuffer logvar = new StringBuffer();

    // Div style adjusted for skip message with enhanced visibility
    logvar.append("<div style=\"background-color:#ffeeba; border:2px solid #856404; ");
    logvar.append("border-radius:10px; padding:10px 15px; text-align:left; ");
    logvar.append("margin:10px 0; display:inline-block; color:#856404; ");
    logvar.append("box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);\">");

    // Message content styled for emphasis
    logvar.append("<p style='font-size:18px; margin:0;'><b>Skipped: " + msg + "</b></p>");

    logvar.append("</div>");

    Reporter.log(logvar.toString(), true); // 'true' ensures the message is printed to the report and console
}
	
	
public static void logBrowserName(String browser) {
    // Optimized Base64 logos (small but clear)
    String chromeLogo = "<img src='data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0NDggNTEyIj48cGF0aCBmaWxsPSIjRkZDMTAyIiBkPSJNMjI0IDI1NmMwIDEyLjktNyAyNC40LTE3LjcgMjkuNkwxODcgMzA5LjljLTUuNCAzLTEyLjMgMy4xLTE3LjcuMS0xMC40LTUuOS0xNi43LTE2LjktMTYuNy0yOC42VjE5OGMwLTEyLjkgNy0yNC40IDE3LjctMjkuNkwxODcgMTQyLjFjNS40LTMgMTIuMy0zLjEgMTcuNy0uMSAxMC40IDUuOSAxNi43IDE2LjkgMTYuNyAyOC42djU4LjJ6Ii8+PHBhdGggZmlsbD0iIzRBNDlGNSIgZD0iTTIyNCAyNTZjMCAxMi45LTcgMjQuNC0xNy43IDI5LjZMMTg3IDMwOS45Yy01LjQgMy0xMi4zIDMuMS0xNy43LjEtMTAuNC01LjktMTYuNy0xNi45LTE2LjctMjguNlYxOThjMC0xMi45IDctMjQuNCAxNy43LTI5LjZMMTg3IDE0Mi4xYzUuNC0zIDEyLjMtMy4xIDE3LjctLjEgMTAuNCA1LjkgMTYuNyAxNi45IDE2LjcgMjguNnY1OC4yeiIvPjxwYXRoIGZpbGw9IiNGRkMxMDIiIGQ9Ik0yMjQgMjU2YzAgMTIuOS03IDI0LjQtMTcuNyAyOS42TDE4NyAzMDkuOWMtNS40IDMtMTIuMyAzLjEtMTcuNy4xLTEwLjQtNS45LTE2LjctMTYuOS0xNi43LTI4LjZWMTk4YzAtMTIuOSA3LTI0LjQgMTcuNy0yOS42TDE4NyAxNDIuMWM1LjQtMyAxMi4zLTMuMSAxNy43LS4xIDEwLjQgNS45IDE2LjcgMTYuOSAxNi43IDI4LjZ2NTguMnoiLz48cGF0aCBmaWxsPSIjRkZDMTAyIiBkPSJNMjI0IDI1NmMwIDEyLjktNyAyNC40LTE3LjcgMjkuNkwxODcgMzA5LjljLTUuNCAzLTEyLjMgMy4xLTE3LjcuMS0xMC40LTUuOS0xNi43LTE2LjktMTYuNy0yOC42VjE5OGMwLTEyLjkgNy0yNC40IDE3LjctMjkuNkwxODcgMTQyLjFjNS40LTMgMTIuMy0zLjEgMTcuNy0uMSAxMC40IDUuOSAxNi43IDE2LjkgMTYuNyAyOC42djU4LjJ6Ii8+PC9zdmc+' height='20' width='20' style='vertical-align: middle;' />";
    
    String firefoxLogo = "<img src='data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA1MTIgNTEyIj48cGF0aCBmaWxsPSIjRUE5NDIwIiBkPSJNNDU3LjEgNzQuM2MtMzAuNSAzNi41LTQ5LjIgNzkuNS01Ny40IDEyNS45TDM1MiAxMjUuNWMwLTI0LjkgMTQuMy00Ny4zIDM2LjgtNTcuN0M0MDMuMyA1Mi4yIDQyOC4xIDQ4IDQ1MyA0OGMxNi4yIDAgMzIuNCAzLjIgNDcuNiA5LjUtLjIgMS44LS4zIDMuNS0uMyA1LjMgMCA0NC4xIDI1LjYgODQuMyA2NS43IDEwMy40LTEwLjIgMjAuMi0yMi4zIDM5LjItMzYuMiA1Ni44LTEzLjktMTcuNi0yNi0zNi42LTM2LjItNTYuOEgzMDAuMWMtMTYuOSAwLTMwLjYgMTMuNy0zMC42IDMwLjYgMCAxNi45IDEzLjcgMzAuNiAzMC42IDMwLjZoODUuNGMtNDYuNCA3OS4yLTExOS4zIDEzNS0yMDYuNCAxNTIuN0MxNjYuMiA0MTYuNiAxMzAuMSA0MzIgOTAuOSA0MzJjLTE2LjIgMC0zMi4zLTIuNS00OC4yLTcuNEMxNi4xIDQyMi4zIDAgNDA2LjIgMCAzODYuNXYtLjZjMC0yMy4zIDEyLjktNDQuNiAzMy41LTU1LjgtNy4yLTE4LjktMTEuNS0zOS4xLTExLjUtNjAuMiAwLTU0LjcgMjIuMS0xMDQuMyA1OC4xLTE0MC4xIDM2LjEtMzUuOCA4NS44LTU4LjEgMTQwLjUtNTguMSAzMC4zIDAgNTkuMiA3LjIgODQuNiAxOS43IDI1LjQtMTIuNSA1NC4yLTE5LjcgODQuNi0xOS43IDU0LjcgMCAxMDQuNCAyMi4zIDE0MC41IDU4LjEgMzYgMzUuOCA1OC4xIDg1LjQgNTguMSAxNDAuMSAwIDIxLjEtNC4zIDQxLjMtMTEuNSA2MC4yIDIwLjYgMTEuMiAzMy41IDMyLjUgMzMuNSA1NS44di42YzAgMTkuNi0xNi4xIDM1LjctMzUuNyAzNy45LTE1LjkgNC45LTMyIDcuNC00OC4yIDcuNC0zOS4yIDAtNzUuMy0xNS40LTEwMi40LTQwLjYtODcuMS0xNy43LTE2MC0yMy41LTIwNi40LTE1Mi43aDEyLjZjMTYuOSAwIDMwLjYtMTMuNyAzMC42LTMwLjYgMC0xNi45LTEzLjctMzAuNi0zMC42LTMwLjZIMjA2LjRjMTAtMjAuMiAyMi4zLTM5LjIgMzYuMi01Ni44IDEzLjkgMTcuNiAyNiAzNi42IDM2LjIgNTYuOGg0MC4xYzQwLjEtMTkuMSA2NS43LTU5LjMgNjUuNy0xMDMuNCAwLTEuOC0uMS0zLjUtLjMtNS4zIDE1LjItNi4zIDMxLjQtOS41IDQ3LjYtOS41IDI0LjkgMCA0OS43IDQuMiA3Mi4zIDEyLjIgMjIuNiA4LjEgNDEuOSAyMS4xIDU2LjggMzcuOXoiLz48L3N2Zz4=' height='20' width='20' style='vertical-align: middle;' />";
    
    String edgeLogo = "<img src='data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0NDggNTEyIj48cGF0aCBmaWxsPSIjMDA3OEQ3IiBkPSJNMjI0LjEgMzQxLjhjMjEuMi0yLjUgNDAuNS0xMC42IDU1LjQtMjMuN2wtNTUuNC0zMi4zdjU2eiIvPjxwYXRoIGZpbGw9IiM1RENGRiIgZD0iTTIyNC4xIDI4NS44djU2bDU1LjQtMzIuM2MtMTQuOS0xMy4xLTM0LjItMjEuMi01NS40LTIzLjd6Ii8+PHBhdGggZmlsbD0iIzAwQTFFNiIgZD0iTTIyNC4xIDI4NS44Yy0yMS4yIDIuNS00MC41IDEwLjYtNTUuNCAyMy43bDU1LjQgMzIuM3YtNTZ6Ii8+PHBhdGggZmlsbD0iIzAwNTlBNyIgZD0iTTIyNC4xIDI4NS44djU2bC01NS40LTMyLjNjMTQuOS0xMy4xIDM0LjItMjEuMiA1NS40LTIzLjd6Ii8+PHBhdGggZmlsbD0iIzVEM0ZGRiIgZD0iTTE2OC43IDM0MS44Yy0xNi4zLTE0LjUtMjYuNS0zNS4xLTI4LjEtNTcuNWg4My42diU1NmwtNTUuNSAzMi4zYy0uMS0xMC4zIDAtMjAuNiAwLTMwLjl6Ii8+PHBhdGggZmlsbD0iIzAwQUFGNiIgZD0iTTE2OC43IDMwOS41YzAgMTAuMyAwIDIwLjYgMCAzMC45bDU1LjUtMzIuM3YtNTZoLTgzLjZjMS42IDIyLjQgMTEuOCA0MyAyOC4xIDU3LjV6Ii8+PHBhdGggZmlsbD0iIzAwNzhENyIgZD0iTTM5NS4yIDE2MC4xYy0yMC4yLTI3LjktNDguNC01MC4xLTgwLjktNjQuMS0yNi4yLTExLjItNTQuOS0xNy02NC4yLTE3LTQ1LjQgMC0xMDQuNCAyMy4xLTEzOC4xIDU3LjggMzQuNyAzNC43IDkyLjcgNTcuOCAxMzguMSA1Ny44IDkuMyAwIDM4LTEuOCA2NC4yLTE3IDMyLjUtMTQgNjAuNy0zNi4yIDgwLjktNjQuMXoiLz48cGF0aCBmaWxsPSIjMDA3OEQ3IiBkPSJNMzk1LjIgMzUxLjljLTIwLjIgMjcuOS00OC40IDUwLjEtODAuOSA2NC4xLTI2LjIgMTEuMi01NC45IDE3LTY0LjIgMTctNDUuNCAwLTEwNC40LTIzLjEtMTM4LjEtNTcuOCAzNC43LTM0LjcgOTIuNy01Ny44IDEzOC4xLTU3LjggOS4zIDAgMzggMS44IDY0LjIgMTcgMzIuNSAxNCA2MC43IDM2LjIgODAuOSA2NC4xeiIvPjwvc3ZnPg==' height='20' width='20' style='vertical-align: middle;' />";

    // Beautiful HTML message with logo
    String browserMessage = "<div style='"
            + "border: 1px solid #e0e0e0;"
            + "padding: 8px 12px;"
            + "margin: 8px 0;"
            + "border-radius: 6px;"
            + "background: linear-gradient(to right, #f8f9fa, #ffffff);"
            + "box-shadow: 0 2px 4px rgba(0,0,0,0.05);"
            + "display: inline-block;"
            + "font-family: Arial, sans-serif;"
            + "'>"
            + "<span style='"
            + "font-weight: bold;"
            + "color: #333;"
            + "'>🌍 Browser Info:</span> "
            + "<span style='"
            + "color: #2c7be5;"
            + "font-weight: 500;"
            + "'>";

    if (browser.equalsIgnoreCase("Chrome")) {
        browserMessage += chromeLogo + " <span style='color: #4285F4;'>Google Chrome</span>";
    } else if (browser.equalsIgnoreCase("Firefox")) {
        browserMessage += firefoxLogo + " <span style='color: #FF7139;'>Mozilla Firefox</span>";
    } else if (browser.equalsIgnoreCase("Edge")) {
        browserMessage += edgeLogo + " <span style='color: #0078D7;'>Microsoft Edge</span>";
    } else {
        browserMessage += "🌐 " + browser;
    }

    browserMessage += "</span></div>";

    try {
        // For Extent Report
        Reporter.log(browserMessage);
        
        // For TestNG console
        System.out.println("Browser: " + browser);
        
        // For Extent log
        test.log(Status.INFO, browserMessage);
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}


public static void logPcNameAndHostName() {
	

try {
    String userName = System.getProperty("user.name").toUpperCase();
    String hostName = InetAddress.getLocalHost().getHostName();

    String userInfoMessage = "<div style=\"border: 1px solid #ddd; padding: 8px; margin: 5px 0; border-radius: 4px; background-color: #f7f7f7; display: inline-block;\">" +
            "<strong>💻 #System Info</strong> :: <strong>PC Name</strong> ⇒ <span style=\"color: #001dff;\">" + hostName + "</span> &nbsp;&nbsp;|&nbsp;&nbsp; <strong>User</strong> ⇒ <span style=\"color: #006400;\">" + userName + "</span></div>";

    // Logging the System Info message
    Reporter.log(userInfoMessage);
    test.log(Status.INFO, MarkupHelper.createLabel(" PC: " + hostName + " | User: " + userName, ExtentColor.BLUE));

} catch (Exception e) {
    e.printStackTrace(); // or use custom logging
}

	
}


    
    public static String getBrowserName(WebDriver driver) {
        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
        return caps.getBrowserName(); // returns values like "chrome", "firefox", "edge"
    }
    
    public static void checkCheckboxStatus(WebDriver driver, WebElement checkboxElement) {
        
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Using JavaScript to fetch the checkbox's properties
        Boolean isChecked = (Boolean) jsExecutor.executeScript("return arguments[0].checked;", checkboxElement);
        String className = (String) jsExecutor.executeScript("return arguments[0].className;", checkboxElement);
        String value = (String) jsExecutor.executeScript("return arguments[0].value;", checkboxElement);
        String ariaChecked = (String) jsExecutor.executeScript("return arguments[0].getAttribute('aria-checked');",
                checkboxElement);

        // Logging the checkbox status in the console
        Library.dialogMassageLog("Checked state: " + isChecked);
        Library.dialogMassageLog("Class attribute: " + className);
        Library.dialogMassageLog("Value attribute: " + value);
        Library.dialogMassageLog("Aria-checked attribute: " + ariaChecked);
    }

    
    
    public static void KeyValueHeadingArrayLoggingStart(String Arrray[], String Array2[]) {
		int arraylength = Arrray.length;

		StringBuilder sb = new StringBuilder("<table>");
		String rowNumArray[] = new String[arraylength];
		for (int i = 0; i < arraylength; i++) {
			rowNumArray[i] = Integer.toString(i);
			sb.append("<tr>");
			sb.append("<th style='font-weight: normal;'>" + Arrray[i] + "</th>");
			sb.append("<th>" + Array2[i] + "</th>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		System.out.println(sb.toString());
		Reporter.log(sb.toString());
		// We are Closing table tag at Final

		System.out.println();

	}
    public static void homePageNavigationFunction() {
        WebDriver driver = getDriver();
        String currentUrl = driver.getCurrentUrl();

        if (currentUrl.contains("home")) {
            Reporter.log("✅ Already on Home Page: " + currentUrl, true);
            return;
        }

        Reporter.log("📋 Navigating to Home Page...", true);

        try {
            Reporter.log("📋 Attempting to close modal popup (if present)...", true);
        } catch (Exception e) {
            Reporter.log("⚠️ Modal popup close failed: " + e.getMessage(), true);
        }

        String homeUrl = "";
        String errorMsg = "";

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            Reporter.log("⏳ Waiting for Home link to be clickable...", true);
            WebElement homeLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href,'https://demoqa.com')]")));

            homeUrl = homeLink.getAttribute("href");
            Reporter.log("✅ Home link found: " + homeUrl, true);
//https://demoqa.com/
            try {
                Reporter.log("📋 Clicking on Home link...", true);
                homeLink.click();
            } catch (Exception clickEx) {
                errorMsg = clickEx.getMessage();
                Reporter.log("⚠️ Home link click failed: " + errorMsg, true);
            }

            if (!errorMsg.isEmpty()) {
                Reporter.log("📋 Fallback: Navigating directly to Home URL...", true);
                driver.navigate().to(homeUrl);
            }

            Reporter.log("⏳ Waiting for loader to disappear...", true);
            

            Reporter.log("⏳ Verifying home page load...", true);
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("home"));

            Reporter.log("✅ Successfully navigated to Home Page!", true);

        } catch (Exception e) {
            Reporter.log("❌ Navigation to Home Page failed: " + e.getMessage(), true);
        }
    }

    public static void WaitElementToBeVisible(WebElement elemennt, int time) {
    	 WebDriver driver = getDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
		wait.until(ExpectedConditions.visibilityOf(elemennt));
		System.out.println("Wait Statement Succesfully Executed");

	}
    
    public static String DateTimeString() {
		String dateTime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());
		System.out.println(dateTime);
		dateTime = dateTime.replace("-", "");
		dateTime = dateTime.replace(":", "");
		dateTime = dateTime.replace(" ", "");
		System.out.println(dateTime);
		return dateTime;
	}
    
    
    public static void notificationMassgeCaptureOnly() {
        WebDriver driver = getDriver();
        System.out.println("Waiting For ASsertion ");
        Library.WaitElementToBeVisible(driver.findElement(By.xpath("//p[contains(@class,'message')]")), 15);
        WebElement ERROR = driver.findElement(By.xpath("//p[contains(@class,'message')]"));

        boolean actual = ERROR.isDisplayed();
        String msg = ERROR.getText();

        if (msg.contains("ucces") || msg.contains("WO Receipt")) {// ucces
            System.out.println(msg);
            test.log(Status.INFO, MarkupHelper.createLabel(" " + msg, ExtentColor.GREEN));
            Reporter.log("<b><font color =" + "Green" + ">" + msg + "</font></b>");
            try {
                ScreenshotUtil.attachScreenshotBase64Format("SAVE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (msg.contains("Enter Labour Production QC Quantity")) {
            Assert.fail(msg);
        } else if (msg.contains("Issue Group! is required")) {
            System.out.println(msg);
            test.log(Status.INFO, MarkupHelper.createLabel(" " + msg, ExtentColor.RED));
            Reporter.log("<font color =" + "red" + ">" + msg + "</font>");
            System.out.println(msg);
            try {
                ScreenshotUtil.captureScreenshotWithRandomName();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Assert.fail(msg);
        }
        else {
            System.out.println(msg);
            test.log(Status.INFO, MarkupHelper.createLabel(" " + msg, ExtentColor.RED));
            Reporter.log("<font color =" + "red" + ">" + msg + "</font>");
            System.out.println(msg);
            try {
                ScreenshotUtil.captureScreenshotWithRandomName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void UnHandledErrorKEY() {
        WebDriver driver = getDriver();
        System.out.println("Waiting For ASsertion ");
        Library.WaitElementToBeClickable(driver.findElement(By.xpath("//p[contains(@class,'message')]")), 10);
        WebElement ERROR = driver.findElement(By.xpath("//p[contains(@class,'message')]"));

        boolean actual = ERROR.isDisplayed();
        String msg = ERROR.getText();
        System.out.println(msg);
        try {
            ScreenshotUtil.attachScreenshotBase64Format("ERROR");
        } catch (Exception e) {
            e.printStackTrace();
        }
        test.log(Status.INFO, MarkupHelper.createLabel(" " + msg, ExtentColor.RED));
        Reporter.log("<font color =" + "red" + ">" + msg + "</font>");
    }

    public static void fluentWaitForVisibility(final WebElement Element) {
        WebDriver driver = getDriver();
        Library.errorMassageLogRedColor("Waiting For Element To BE Visible");
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(500L))
                .pollingEvery(Duration.ofSeconds(5L)).ignoring(NoSuchElementException.class);
        Library.errorMassageLogRedColor("Pollinng Time Out Waiting For Element To BE Visible");
        WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                Library.errorMassageLogRedColor("Waiting For Element To BE l");
                return Element;
            }
        });
    }


	public static void dialogMassageLog(String msg) {

		// Creating the HTML structure for the dialog box
		StringBuffer logvar = new StringBuffer();

		// Div style now adjusts based on the content size and aligns at the start
		logvar.append("<div style=\"background-color:lightblue; border:2px solid blue; ");
		logvar.append("border-radius:10px; padding:10px 15px; text-align:left; ");
		logvar.append("margin:10px 0; display:inline-block;\">");

		// Message content
		logvar.append("<p style='font-size:18px; margin:0;'><b>" + msg + "</b></p>");

		logvar.append("</div>");

		// Logging the HTML in the console and report
	//	System.out.println("Dialog text :: " + logvar.toString());
		Reporter.log(logvar.toString(), true); // 'true' ensures the message is printed to the report and console
	}
    
    
    
    
    public static String selectOptionFromDropdown(WebElement element, String optionname, String Fieldname) {
        WebDriver driver = getDriver();
        String newfieldname=Fieldname.toUpperCase();
        
        if(newfieldname.contains("GROUP")) {
            String selectedornot=selectOptionFromDropdownStartsWith(element, optionname, Fieldname);
            if(selectedornot.equalsIgnoreCase("selected")) {
                return selectedornot;
            }
        }
        
        String selectedOrNot="NotSelected";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        System.out.println("The Option Name Is ==" + optionname);
        Library.WaitElementToBeClickable(element, 20);
        System.out.println("Wait Succesfully Applied On Field");
        System.out.print("");

        String DropDownAttribute = element.getAttribute("class");
        System.out.println(DropDownAttribute);

        if (DropDownAttribute.contains("ng-select-disabled")) {
            try {
                String formcontrolname = element.getAttribute("formcontrolname");
                String Totalxpatllabel = "//ng-select[@formcontrolname='" + formcontrolname
                        + "']/preceding-sibling::label";
                WebElement label = driver.findElement(By.xpath(Totalxpatllabel));
                String labeltext = Library.getTextOfWebElementFunction(label);
                String dropdowntextele = "//ng-select[@formcontrolname='" + formcontrolname
                        + "']//span[@class='ng-value-label']";
                WebElement elememt = driver.findElement(By.xpath(dropdowntextele));
                String dropdowntext = Library.getTextOfWebElementFunction(elememt);
                String msg = "#DRPDWN  Read Only Mode ::" + labeltext + "::  Default Value :: <b>" + dropdowntext
                        + "</b> ";
                Reporter.log(msg);
            } catch (Exception tai) {
            }
        } else {
            String Exception = "";
            
            try {
                element.click();
                Thread.sleep(500);
            } catch (Exception e1) {
                Exception = e1.getMessage();
            } 
            
            if(!Exception.isEmpty()) {
                Library.dialogMassageLog(Exception);
                
                element.click();
            }

            try {
                WebElement Option = driver
                        .findElement(By.xpath("//div[@role='option']//span[contains(text(),'" + optionname + "')]"));
                Library.WaitElementToBeClickable(Option, 25);
                String text = Option.getText();
                String massage = "#DRPDWN &#9013; " + Fieldname + " &#10154; Option Selected::<b>" + text + "</b>";
                test.log(Status.PASS, massage);
                logDropdownSelection(Fieldname, text);
                selectedOrNot="selected";
                js.executeScript("arguments[0].click()", Option);
            } catch (Exception e) {
                System.err.println("Cannot Click " + e.getMessage());
                Reporter.log("<b><FONT COLOR=\"#ff0000\"> INVALID  Option/No such Option Present ==</FONT></b>"
                        + "<b><FONT COLOR=\"#0000ff\">" + Fieldname + "==" + optionname + "</FONT></b>");
                System.out.println(optionname + "==INVALID  Option/No such Option Present" + "Exception is Equals to "
                        + e.getMessage());
                test.log(Status.PASS, "<b><FONT COLOR=\"#ff0000\"> INVALID  Option/No such Option Present ==</FONT></b>"
                        + "<b>" + Fieldname + "==" + optionname + "</b>");
                Assert.assertNotEquals(true, false, "No such Option Present");
            }
        }
        return selectedOrNot;
    }
    
    
    
    public static void logDropdownSelection(String fieldName, String optionText) {
		// Create the formatted log message for dropdown selection with a dropdown icon
		String formattedLog = "<div style='border: 1px solid #ddd; padding: 8px; margin: 5px 0; border-radius: 4px; background-color: #f7f7f7; display: inline-block;'>"
				+ "<strong>#DRPDWN 🔽</strong> :: <strong>" + fieldName
				+ "</strong> Option Selected ⇒ <span style='color: #001dff;'>" + optionText + "</span>" + "</div>";
		// Log the formatted message using Reporter
		Reporter.log(formattedLog);
		
		 test.log(Status.PASS, formattedLog);
	}
	
    
    public boolean selectOptionByVisibleText(WebDriver driver, WebElement selectElement, String optionName, String fieldName) {
        boolean isSelected = false;
        
        ScrollHelper.scrollToElement(driver, selectElement);

        try {
            Select dropdown = new Select(selectElement);
            dropdown.selectByVisibleText(optionName);
            isSelected = true;
            logDropdownSelection(fieldName, optionName);
        } catch (Exception e) {
            Reporter.log("<div style='color: red;'><strong>Dropdown selection failed for field: " + fieldName + " with option: " + optionName + "</strong></div>");
            e.printStackTrace();
        }
        return isSelected;
    }
    
    /**
     * Select option in React-Select styled dropdown by passing locator
     * @param driver WebDriver instance
     * @param dropdownLocator By locator for dropdown container (e.g. By.id("state"))
     * @param optionText The visible option text you want to select
     * @param fieldName Friendly field name for logging
     * @return true if option was selected successfully, false otherwise
     */
    public static boolean selectReactSelectOption(WebDriver driver, By dropdownLocator, String optionText, String fieldName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 1. Click on the dropdown container to activate input
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dropdownLocator));
            ScrollHelper.scrollToElement(driver, dropdown);
            dropdown.click();

            // 2. Locate the input inside the dropdown and clear/type option text
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(
                dropdown.findElement(By.cssSelector("input[type='text']"))));
            input.clear();
            input.sendKeys(optionText);

            // Optional: send ENTER to select first filtered option
            input.sendKeys(Keys.ENTER);

            // 3. Wait for the selected option to appear as singleValue inside the dropdown container
            String xpathSelectedValue = ".//div[contains(@class,'singleValue') and text()='" + optionText + "']";
            wait.until(ExpectedConditions.visibilityOf(dropdown.findElement(By.xpath(xpathSelectedValue))));

            // Log success (replace with your actual logger)
            System.out.println("#DRPDWN 🔽 :: " + fieldName + " :: Option Selected ⇒ " + optionText);
            return true;

        } catch (Exception e) {
            System.err.println("Failed to select option '" + optionText + "' for field " + fieldName);
            e.printStackTrace();
            return false;
        }
    }
    
	public static void logJqxListboxSelection(String fieldName, String optionText) {
	    // Create the formatted log message for JQX Listbox selection
	    String formattedLog = "<div style='border: 1px solid #ddd; padding: 8px; margin: 5px 0; border-radius: 4px; background-color: #f7f7f7; display: inline-block;'>"
	            + "<strong>#JQXLISTBOX 📋</strong> :: <strong>" + fieldName
	            + "</strong> Option Selected ⇒ <span style='color: #007bff;'>" + optionText + "</span>" + "</div>";
	    // Log the formatted message using Reporter
	    Reporter.log(formattedLog);
	}
    
    
    
    public static String selectOptionFromDropdownStartsWith(WebElement element, String optionPrefix, String fieldName) {
        WebDriver driver = getDriver();
        String selectedOrNot = "NotSelected";
        JavascriptExecutor js = (JavascriptExecutor) driver;

        System.out.println("The Option Prefix Is == " + optionPrefix);
        Library.WaitElementToBeClickable(element, 20);
        System.out.println("Wait Successfully Applied On Field");

        String dropDownAttribute = element.getAttribute("class");
        System.out.println(dropDownAttribute);

        if (dropDownAttribute.contains("ng-select-disabled")) {
            try {
                String formControlName = element.getAttribute("formcontrolname");
                String totalXpathLabel = "//ng-select[@formcontrolname='" + formControlName + "']/preceding-sibling::label";
                WebElement label = driver.findElement(By.xpath(totalXpathLabel));
                String labelText = Library.getTextOfWebElementFunction(label);
                String dropdownTextEle = "//ng-select[@formcontrolname='" + formControlName + "']//span[@class='ng-value-label']";
                WebElement dropdownElement = driver.findElement(By.xpath(dropdownTextEle));
                String dropdownText = Library.getTextOfWebElementFunction(dropdownElement);
                String msg = "#DRPDWN  Read Only Mode ::" + labelText + "::  Default Value :: <b>" + dropdownText + "</b> ";
                Reporter.log(msg);
            } catch (Exception e) {
                System.err.println("Exception while handling read-only dropdown: " + e.getMessage());
            }
        } else {
            try {
                element.click();
                
                String xpathForOption = "//div[@role='option']//span[starts-with(text(), '" + optionPrefix + "')]";
                WebElement option = driver.findElement(By.xpath(xpathForOption));
                Library.WaitElementToBeClickable(option, 25);
                String text = option.getText();
                String message = "#DRPDWN \u279D " + fieldName + " \u27A4 Option Selected::<b>" + text + "</b>";
                test.log(Status.PASS, message);
                logDropdownSelection(fieldName, text);
                js.executeScript("arguments[0].click()", option);
                selectedOrNot = "selected";
            } catch (Exception e) {
                System.err.println("Cannot Click: " + e.getMessage());
                Reporter.log("<b><FONT COLOR=\"#ff0000\"> INVALID  Option/No such Option Present ==</FONT></b>"
                        + "<b><FONT COLOR=\"#0000ff\">" + fieldName + "==" + optionPrefix + "</FONT></b>");
                test.log(Status.FAIL, "<b><FONT COLOR=\"#ff0000\"> INVALID  Option/No such Option Present ==</FONT></b>"
                        + "<b>" + fieldName + "==" + optionPrefix + "</b>");
            }
        }
        return selectedOrNot;
    }
    
    public static void massagelog(String msg) {
		System.out.println(msg);
		try {
			Reporter.log(msg);
			test.log(Status.INFO, MarkupHelper.createLabel(" " + msg, ExtentColor.BLACK));
		} catch (Exception t) {
		}

	}

    public static void Comboclick(WebElement element, String optionname, String Fieldname) {
        WebDriver driver = getDriver();
        String Elementclassatrribute = element.getAttribute("class");
        System.out.println(Elementclassatrribute);

        if (Elementclassatrribute.contains("ng-select-disabled")) {
            Library.massagelog(Elementclassatrribute);
            Library.massagelog(
                    " Can  not able to Select value from The Dropdown As a Dropdown Field Is Read Only Mode ");
        } else {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            System.out.println("The Option Name Is ==" + optionname);
            Library.WaitElementToBeClickable(element, 20);
            System.out.println("Wait Succesfully Applied On Field");
            System.out.print("");
            element.click();

            try {
                Reporter.log("Click sucessfully==" + Fieldname);
                test.log(Status.PASS, "Click sucessfully==" + Fieldname);
                WebElement Option = driver.findElement(By.xpath("//span[contains(text(),'" + optionname + "')]"));
                Library.WaitElementToBeClickable(Option, 25);
                String text = Option.getText();
                String massage = "#DRPDWN &#9013; " + Fieldname + " &#10154; Option Selected::<b>" + text + "</b>";
                test.log(Status.PASS, massage);
                logDropdownSelection(Fieldname, text);
                js.executeScript("arguments[0].click()", Option);
            } catch (Exception e) {
                System.err.println("Cannot Click " + e.getMessage());
                Reporter.log("<b><FONT COLOR=\"#ff0000\"> INVALID  Option/No such Option Present ==</FONT></b>"
                        + "<b><FONT COLOR=\"#0000ff\">" + Fieldname + "==" + optionname + "</FONT></b>");
                System.out.println(optionname + "==INVALID  Option/No such Option Present" + "Exception is Equals to "
                        + e.getMessage());
                test.log(Status.PASS, "<b><FONT COLOR=\"#ff0000\"> INVALID  Option/No such Option Present ==</FONT></b>"
                        + "<b>" + Fieldname + "==" + optionname + "</b>");
                Assert.assertNotEquals(true, false, "No such Option Present");
            }
        }
    }

    public static void customClickFunction(WebElement element) {
        WebDriver driver = getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WaitElementToBeClickable(element, 20);
        
        ScrollHelper.scrollToElement(driver, element);

        String Fieldname = Library.getTextOfWebElementFunction(element);

        try {
            if (element.isEnabled()) {
                js.executeScript("arguments[0].click()", element);
                Reporter.log("Click Sucessfully == " + Fieldname);
                test.log(Status.PASS, "Click Sucessfully == " + Fieldname);
            } else {
                test.log(Status.PASS, "No such field present == " + Fieldname);
                Reporter.log("No such field present == " + Fieldname);
                System.out.println("Element Not Found");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Reporter.log("No such field present == " + Fieldname);
            test.log(Status.PASS, "No such field present == " + Fieldname + e.getMessage());
        }
    }

    
	public static void logTextBoxEntryWithCopyButton(String fieldName, String fieldValue) {
	    String formattedLog = "<div style='border: 1px solid #ddd; padding: 8px; margin: 5px 0; border-radius: 4px; background-color: #f7f7f7; display: inline-block; position: relative;'>"
	            + "<strong>#TXTBX ▭</strong> :: <strong>" + fieldName
	            + "</strong> Value entered ⇒ <span class='connection-string' style='color: #009;'>" + fieldValue + "</span>"
	            + "<button class='copy-button' style='display: none; position: absolute; top: 8px; right: 8px; padding: 5px 10px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 12px;' onclick='copyConnectionString(this)'>Copy</button>"
	            + "</div>"
	            + "<script>"
	            + "function copyConnectionString(button) {"
	            + "    const connectionString = button.parentElement.querySelector('.connection-string').textContent;"
	            + "    navigator.clipboard.writeText(connectionString)"
	            + "        .catch(err => console.error('Failed to copy text:', err));"
	            + "}"
	            + "document.querySelectorAll('div').forEach(container => {"
	            + "    container.addEventListener('mouseover', () => {"
	            + "        const button = container.querySelector('.copy-button');"
	            + "        if (button) button.style.display = 'block';"
	            + "    });"
	            + "    container.addEventListener('mouseout', () => {"
	            + "        const button = container.querySelector('.copy-button');"
	            + "        if (button) button.style.display = 'none';"
	            + "    });"
	            + "});"
	            + "</script>";
	    Reporter.log(formattedLog);
	}
		//below function is commented On TimeStamp Of 17/01/2025 and Time 09:34:10
		
		public static void logTextBoxEntry(String fieldName, String fieldValue) {
			String formattedLog = "<div style='border: 1px solid #ddd; padding: 8px; margin: 5px 0; border-radius: 4px; background-color: #f7f7f7; display: inline-block;'>"
					+ "<strong>#TXTBX ▭</strong> :: <strong>" + fieldName
					+ "</strong> Value entered ⇒ <span style='color: #009;'>" + fieldValue + "</span>" + "</div>";
			Reporter.log(formattedLog);

		}
    public static void customSendkeys(WebElement element, String value, String Fieldname) {
        WebDriver driver = getDriver();
        ScrollHelper.scrollToElement(driver, element);

        if (element.isEnabled()) {
            element.sendKeys(value);
            if(Fieldname.contains("Connection")||Fieldname.contains("Username")||Fieldname.contains("Password")) {
                Library.logTextBoxEntryWithCopyButton(Fieldname, value);
            } else {
                Library.logTextBoxEntry(Fieldname, value);
            }
            test.log(Status.PASS, Fieldname + " Text Box Value entered " + "&#8658;<b>" + value + "</b>");
        } else {
            Reporter.log("No Such " + Fieldname + "   present");
            test.log(Status.PASS,
                    MarkupHelper.createLabel("No Such " + Fieldname + "   present/Field is Disabled", ExtentColor.RED));
        }
    }

    public static void customSendkeys(By byelement, String value, String Fieldname) {
        WebDriver driver = getDriver();
        WebElement element=driver.findElement(byelement);
        ScrollHelper.scrollToElement(driver, element);

        if (element.isEnabled()) {
            element.sendKeys(value);
            if(Fieldname.contains("Connection")||Fieldname.contains("Username")||Fieldname.contains("Password")) {
                Library.logTextBoxEntryWithCopyButton(Fieldname, value);
            } else {
                Library.logTextBoxEntry(Fieldname, value);
            }
            test.log(Status.PASS, Fieldname + " Text Box Value entered " + "&#8658;<b>" + value + "</b>");
        } else {
            Reporter.log("No Such " + Fieldname + "   present");
            test.log(Status.PASS,
                    MarkupHelper.createLabel("No Such " + Fieldname + "   present/Field is Disabled", ExtentColor.RED));
        }
    }

    
    public static void iterationNumberLoggingForDataDriven(int iterrationnumber) {

		String msg = " Iteration Number &#10781; :" + Integer.toString(iterrationnumber);
		System.out.println(msg);
		try {
			Reporter.log("<h3 style=\"color:#132043;\">" + "<b>" + msg + "</b>" + "</h4>");
			test.log(Status.INFO, MarkupHelper.createLabel(" " + msg, ExtentColor.BLACK));
		} catch (Exception t) {
		}

	}

    
    public static void customSendkeysWithValdation(WebElement element, String value, String Fieldname) {
        WebDriver driver = getDriver();
        
        ScrollHelper.scrollToElement(driver, element);

        if (value.contains("NODATA")) {
            Library.errorMassageLogRedColor(
                    "Blank data/No Input Data found So can't  send Value In the Text Field  " + Fieldname);
        } else {
            String enableordisableattribute = element.getAttribute("disabled");
            System.out.println(enableordisableattribute);
            int InputValueLength = value.length();
            String MaxLenghAttributeintextField = element.getAttribute("maxlength");
            int MaxLentgthWebPageField = 0;

            if (MaxLenghAttributeintextField == null) {
                String massage = "Maxlength is not Defined/ Maxlength User Defined  For The Field :" + Fieldname;
                //Library.massagelog(massage);
            } else {
                MaxLentgthWebPageField = Integer.parseInt(MaxLenghAttributeintextField);
            }

            if (element.isEnabled() && enableordisableattribute == null) {
                if (InputValueLength > MaxLentgthWebPageField) {
                    if (MaxLentgthWebPageField == 0) {
                    } else {
                        Library.errorMassageLogRedColor("You Have Entered " + Fieldname
                                + " Greater Than Maximum Length i.e " + MaxLenghAttributeintextField + " Characters ");
                        Library.massagelog("So Considering First" + MaxLenghAttributeintextField
                                + " Character of the Entered String ");
                        System.out.println("The MAximum Length Of The " + MaxLentgthWebPageField);
                        value = value.substring(0, MaxLentgthWebPageField);
                    }
                }

                try {
                    element.clear();
                } catch (Exception tai) {
                    tai.getMessage();
                }

                element.sendKeys(value);
                if(Fieldname.contains("Connection")||Fieldname.contains("Username")||Fieldname.contains("Password")) {
                    Library.logTextBoxEntryWithCopyButton(Fieldname, value);
                } else {
                    Library.logTextBoxEntry(Fieldname, value);
                }
                test.log(Status.PASS, Fieldname + " Text Box  " + "&#8658; <b>" + value + "</b>");
            } else {
                String text = Library.getTextOfWebElementFunction(element);
                String Massage = "No Such " + Fieldname + "   present(Disabled)/The " + Fieldname
                        + " Is Read Only Mode Text :<b> " + text + " </b>";
                Reporter.log(Massage);
                test.log(Status.PASS, MarkupHelper.createLabel(Massage, ExtentColor.RED));
            }
        }
    }

    public static void selectTagNameDropdownByVisibleText(WebElement Element, String visibletext, String Fieldname) {
        WebDriver driver = getDriver();
        try {
            Select select = new Select(Element);
            List<WebElement> allOptions = select.getOptions();

            for (WebElement option : allOptions) {
                String optiontext = option.getText();
                if (optiontext.equals(visibletext) || optiontext.contains(visibletext)
                        || optiontext.equalsIgnoreCase(visibletext)) {
                    visibletext = optiontext;
                    break;
                }
            }

            select.selectByVisibleText(visibletext);
            logDropdownSelection(Fieldname, visibletext);
            test.log(Status.PASS, "Dropdown " + Fieldname + " selected==" + visibletext);
        } catch (Exception e) {
            Reporter.log("<b><FONT COLOR=\"#ff0000\"> INVALID  Option/No such Option Present ::</FONT></b>"
                    + "<b><FONT COLOR=\"#0000ff\">" + Fieldname + "::" + visibletext + "</FONT></b>");
            Library.errorMassageLogRedColor(e.getMessage());
            test.log(Status.FAIL, e.getMessage());
        }
    }

    public static void Scrolldown(int Horizontalpixel, int Verticalpixel) {
        WebDriver driver = getDriver();
        String xOffset = Integer.toString(Horizontalpixel);
        String yOffset = Integer.toString(Verticalpixel);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "window.scrollBy(" + xOffset + "," + yOffset + ")";
        js.executeScript(script);
        System.out.println(script);
    }

    public static void Explicitlywait(WebElement elemennt) {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOf(elemennt));
    }

    
    public static void errorMassageLogRedColor(String msg) {

		StringBuffer logvar = new StringBuffer();

		System.out.println(msg);
		try {
			test.log(Status.INFO, MarkupHelper.createLabel(" " + msg, ExtentColor.RED));

			logvar.append("<ul>");

			String massage = "<font color =" + "red" + ">" + msg + "</font>";

			logvar.append(Library.list_tag_Data_Fill_Function(massage));
			logvar.append("</ul>");
			Reporter.log(logvar.toString());

		} catch (Exception t) {
		}

	}
    public static String list_tag_Data_Fill_Function(String SingleStringData) {

		StringBuffer sb = new StringBuffer("");

		sb.append("<li>" + SingleStringData + "</li>");

		return sb.toString();
	}


	public static String getTextOfWebElementFunction(WebElement element) {

		// Library.WaitElementToBeVisible(element, 5);

		String text = "";
		// ------- The try Catch block is added in this Functin Due to no customer Po
		// field in autoallocation so this will remove if customer po field is
		// added---\\
		// ---- Try catch added date and time 19/09/2024 on the time of 10:30
		try {

			text = element.getText();
			if (text.equals("")) {

				text = element.getAttribute("innerText");
				if (text.equals("")) {

					text = element.getAttribute("value");
				}
			}
		} catch (Exception e) {
			String massgaemate = e.getLocalizedMessage();

			Library.errorMassageLogRedColor(massgaemate);
		}

		if (text == null) {

			text = "";
		}
		return text;
	}
    public static void isLoadedAndClick(WebElement element) {
        WebDriver driver = getDriver();
        String Exception="";
        try {
            String msg = Library.getTextOfWebElementFunction(element);
            element.click();
            test.log(Status.PASS, "Click Sucessfully==" + msg);
            Reporter.log("Click Sucessfully==" + msg);
            System.out.println("Click Sucessfully==" + msg);
        } catch (Exception e) {
            Exception=e.getMessage();
        }
        Library.gridNameHeadingLoggingMassage(Exception);
    }

    
	public static void gridNameHeadingLoggingMassage(String msg) {
		System.out.println(msg);

		try {

			// any text</h1>

			Reporter.log("<h3 style=\"color:#800080;\">" + "<b>" + msg + "</b>" + "</h3>");

			/// Reporter.log("<h3><font color =" + "#00a1ef" + ">" + "<b>"+msg +"</b>"+
			/// "</font></h3>");
			test.log(Status.INFO, MarkupHelper.createLabel(" " + msg, ExtentColor.BLACK));
		} catch (Exception t) {
		}

	}
	
	
	public static void dialogErrorMessageLog(String msg) {

	    // Creating the HTML structure for the dialog box
	    StringBuffer logvar = new StringBuffer();

	    // Div style adjusted for error message with red background and border
	    logvar.append("<div style=\"background-color:#f8d7da; border:2px solid #f5c2c7; ");
	    logvar.append("border-radius:10px; padding:10px 15px; text-align:left; ");
	    logvar.append("margin:10px 0; display:inline-block; color:#842029;\">");

	    // Message content styled for emphasis
	    logvar.append("<p style='font-size:18px; margin:0;'><b>Error: " + msg + "</b></p>");

	    logvar.append("</div>");

	    Reporter.log(logvar.toString(), true); // 'true' ensures the message is printed to the report and console
	}
	
	
	
	public static void WaitElementToBeClickable(WebElement elemennt, int time) {
		 WebDriver driver = getDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
		wait.until(ExpectedConditions.elementToBeClickable(elemennt));

		System.out.println("Wait Statement Succesfully Executed");
		wait.until(ExpectedConditions.visibilityOf(elemennt));
		System.out.println("Wait Statement Succesfully Executed");

	}


	public static void sectionNameLoggingFunction(String sectionName) {
	    
		
		

//		
//		 <details>
//	        <summary>Click to Expand/Collapse</summary>
//	        <p>This is the collapsible content. It will be shown when expanded.</p>
//	    </details>
//		
//		
		
	    // Creating the HTML structure for the step log with enhanced styling
	    StringBuilder logvar = new StringBuilder();

	    logvar.append("<div style=\"background: linear-gradient(to right, #85FFBD, #FFFB7D); ");
	    logvar.append("border: 2px solid #008000; border-radius: 10px; ");
	    logvar.append("padding: 12px 18px; margin: 12px 0; ");
	    logvar.append("box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1); ");
	    logvar.append("font-family: Arial, sans-serif; text-align: left; display: inline-block;\">");

	    logvar.append("<p style='font-size: 20px; font-weight: bold; color: #004d00; margin: 0;'>");
	    logvar.append("Step ").append(logStepVar).append(": ").append("Entering Details: ").append(sectionName);
	    logvar.append("</p>");

	    logvar.append("</div>");

	    // Logging the HTML in the console and report
	    Reporter.log(logvar.toString(), true);

	    // Increment the step counter
	    logStepVar++;
	    
	    try {
	    	test.log(Status.INFO, MarkupHelper.createLabel(sectionName, ExtentColor.PINK));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	
    public static void isLoadedAndClick(WebElement element, String Fieldname) {
        WebDriver driver = getDriver();
        
        ScrollHelper.scrollToElement(driver, element);
        String nextbtn = Fieldname.toUpperCase();
        String tagname = element.getTagName();

        if (tagname.equals("button")) {
            String butnattr = element.getAttribute("disabled");
            if (butnattr == null) {
            } else if (butnattr.equals("")) {
                Library.errorMassageLogRedColor("Not able click On " + Library.getTextOfWebElementFunction(element)
                        + " ::  Due to it is disable mode");
            }
        }
        if (nextbtn.contains("NEXT")) {
            
            try {
                Thread.sleep(4000);
            } catch (Exception e) {
            }
        }

        String massage = "";
        int mmassagelength = 0;
        String text = "";

        try {
            Library.WaitElementToBeClickable(element, 4);
            text = element.getText();
            element.click();
        } catch (Exception e) {
            massage = e.getMessage();
            mmassagelength = massage.length();
            System.out.println(massage);
            test.log(Status.PASS,
                    "Clicked On :: " + element.getText() + ":: " + Fieldname + "element click intercepted Exception ");
            System.out.println("Click Sucessfully==" + element.getText() + "==" + Fieldname);
            System.out.println(e.getMessage());
        } finally {
            if (mmassagelength == 0) {
                if (tagname.equals("button") || tagname.equals("a")) {
                    text = Library.getTextOfWebElementFunction(element).trim();
                    logButtonClick(text, Fieldname);
                } else {
                    Reporter.log("Clicked On :: " + Fieldname);
                }
                test.log(Status.PASS, "Clicked On :: " + text + ":: " + Fieldname);
                System.out.println("Click Sucessfully==" + text + "==" + Fieldname);
                System.out.println("Button clicked sucessfully ");
            } else if (massage.contains("ElementClickInterceptedException")) {
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ScrollHelper.scrollToElement(driver, element);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Library.customClickUsingJavascriptExecutorI(element, "Again Double Click==" + Fieldname);
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Library.doubleClickUsingActionClass(element, "Again Double Click==" + Fieldname);
            }
        }

        if (Fieldname.equals("OK BUTTON")) {
            try {
                Library.notificationMassgeCaptureOnly();
            } catch (Exception tai) {
            }
        }
    }

    
    public static void WaitElementToBeClickable(By byelemennt, int time) {
    	 WebDriver driver = getDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
		WebElement elemennt=driver.findElement(byelemennt);
		wait.until(ExpectedConditions.elementToBeClickable(elemennt));

		System.out.println("Wait Statement Succesfully Executed");
		wait.until(ExpectedConditions.visibilityOf(elemennt));
		System.out.println("Wait Statement Succesfully Executed");

	}
    
    public static void isLoadedAndClick(By Byelement, String Fieldname) {
        WebDriver driver = getDriver();
        WaitElementToBeClickable(Byelement, 3);
        WebElement element = driver.findElement(Byelement);
        isLoadedAndClick(element, Fieldname);
    }

    public static void DClick(WebElement element, String Fieldname) {
        WebDriver driver = getDriver();
        isLoadedAndClick(element, Fieldname);
    }

    public static void searchwindowpopupenter(WebElement element, String optionname) throws Exception {
        WebDriver driver = getDriver();
        Library.WaitElementToBeClickable(element, 20);
        element.click();
        
        WebElement itemsearch = driver.findElement(By.xpath("//input[@formcontrolname='item']"));

        Library.customSendkeys(itemsearch, optionname, "Item code Seacrch text box");
        itemsearch.sendKeys(Keys.ENTER);
        try {
            
        } catch (Exception t) {
        }
        
        System.out.println("Searching For Element ");
        Library.WaitElementToBeClickable(driver.findElement(By.xpath("//div[text()='" + optionname + "']")), 50);
        WebElement selectitem = driver.findElement(By.xpath("//div[text()='" + optionname + "']"));
        Library.Explicitlywait(selectitem);

        selectitem.click();
        selectitem.click();
        
        test.log(Status.PASS, "option succesfully selected==" + selectitem.getText());
        Reporter.log("option succesfully selected==" + selectitem.getText());
    }


    public static void info(WebElement element, String Fieldname) {
        WebDriver driver = getDriver();
        try {
            if (element.isEnabled() == true) {
                Library.Explicitlywait(element);
                test.log(Status.INFO, MarkupHelper.createLabel("New generated " + Fieldname + "==" + element.getText(),
                        ExtentColor.BLUE));
                Reporter.log("<b><FONT COLOR=\\\"1f1f7a\\\">" + "New generated " + Fieldname + "==" + element.getText()
                        + "</FONT></b>");
                System.out.println("New generated " + Fieldname + "==" + element.getText());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void customClickUsingActionClass(WebElement element, String Fieldname) {
        WebDriver driver = getDriver();
        try {
            Actions act = new Actions(driver);
            act.moveToElement(element).click().build().perform();
            Thread.sleep(2000);
            test.log(Status.PASS, "Click Sucessfully==" + Fieldname);
            Reporter.log("Click Sucessfully==" + Fieldname);
            System.out.println("Click Sucessfully==" + Fieldname);
        } catch (Exception e) {
            test.log(Status.PASS, "Click Failed==" + Fieldname);
        }
        Fieldname = Fieldname.toUpperCase();
        if (Fieldname.contains("NEXT")) {
        }
    }

    

	public static void logButtonClick(String whichButton, String buttonName) {
		// Button symbols for each type
		String buttonSymbol;
		String editbutton = buttonName.toUpperCase();

		if (whichButton.equalsIgnoreCase("Add New")) {
			buttonSymbol = "➕"; // Add New Button symbol
		} else if (whichButton.equalsIgnoreCase("Ok")) {
			buttonSymbol = "✔️"; // Ok Button symbol
		} else if (whichButton.equalsIgnoreCase("Submit")) {
			buttonSymbol = "📤"; // Submit Button symbol
		} else if (whichButton.equalsIgnoreCase("Next")) {
			buttonSymbol = "⏭️"; // Next Button symbol
		} else if (whichButton.equalsIgnoreCase("Save")) {
			buttonSymbol = "💾"; // Save Button symbol
		} else if (whichButton.equalsIgnoreCase("Edit") || editbutton.contains("EDIT")) {
			buttonSymbol = "✏️"; // Edit Button symbol
		}
	
		else if (whichButton.equalsIgnoreCase("Login")) {
			buttonSymbol = "🔒"; //
		}
		else if (whichButton.equalsIgnoreCase("Logout")) {
			buttonSymbol = "↩️"; //
		}
		
		else if (buttonName.equalsIgnoreCase("Expand All")) {
			buttonSymbol = "🔼";
		} else if (buttonName.equalsIgnoreCase("Collapse All")) {
			buttonSymbol = "🔽";
		}

		else if (whichButton.equalsIgnoreCase("Authorize")) {
			buttonSymbol = "🔑"; // Authorization Button
		} else if (whichButton.equalsIgnoreCase("Cancel")) {
			buttonSymbol = "❌"; // Authorization Button
		}
		else if (whichButton.equalsIgnoreCase("Notifications")||editbutton.equalsIgnoreCase("Notifications")) {
	        buttonSymbol = "🔔"; // Notifications Button symbol
	    } 
		else if (whichButton.contains("TAB")||buttonName.contains("TAB")) {
	        buttonSymbol = "🗂️"; // View All Button symbol
	    }
		
		else if (whichButton.equalsIgnoreCase("View All")) {
	        buttonSymbol = "👁️"; // View All Button symbol
	    }
		else {
			buttonSymbol = "🖱️"; // Default button icon
		}

		// Create the formatted log message for button click with a button icon
		String formattedLog = "<div style='border: 1px solid #ddd; padding: 8px; margin: 5px 0; border-radius: 4px; background-color: #f7f7f7; display: inline-block;'>"
				+ "<strong>#BTN " + buttonSymbol
				+ "</strong> :: <strong>Clicked On</strong> ⇒ <span style='color: #001dff;'>" + buttonName + "</span>"
				+ "</div>";

		// Log the formatted message using Reporter
		Reporter.log(formattedLog);
	}


	public static void customClickUsingJavascriptExecutorI(WebElement element, String Fieldname) {

		
		 WebDriver driver = getDriver();
		//TODO changes Time From 20 seconds to 1 sec By 
		WaitElementToBeClickable(element, 1);

		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {

			if (element.isEnabled()) {

				String tagname = element.getTagName();
				if (tagname.equalsIgnoreCase("button") || tagname.equalsIgnoreCase("a")||tagname.equalsIgnoreCase("div")) {
				
					System.out.println("Element display: " + element.isDisplayed());
					System.out.println("Element enabled: " + element.isEnabled());
					System.out.println("Element location: " + element.getLocation());
					Fieldname = Fieldname.replace("button", "");
					Fieldname = Fieldname.replace("Button", "");
					String text = Library.getTextOfWebElementFunction(element).trim();
					js.executeScript("arguments[0].click()", element);
					logButtonClick(text, Fieldname);
					test.log(Status.PASS, "#BTN &#8718;  Clicked  &#9758;" + Fieldname + " Button ");

				} else {
					Reporter.log("Click Sucessfully==" + Fieldname);
					test.log(Status.PASS, "Click Sucessfully==" + "==" + Fieldname);
					System.out.println("Click Sucessfully==" + "==" + Fieldname);

					js.executeScript("arguments[0].click()", element);
				}
			}

			else {
				test.log(Status.PASS, "No such field present == " + Fieldname);
				Reporter.log("No such field present == " + Fieldname);
				System.out.println("Element Not Found");
			}
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
			Reporter.log("No such field present == " + Fieldname);
			test.log(Status.PASS, "No such field present == " + Fieldname + e.getMessage());
		}
	}


	public static String DateTimeString_LOGGING_24_HRS() {
	    // Format the current date and time with AM/PM
	    String dateTime = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault()).format(new Date());
	    
	    System.out.println("Original DateTime: " + dateTime);
	    
	    // Convert to uppercase
	    dateTime = dateTime.toUpperCase();
	    
	    
	    return dateTime;
	}

	public static void subCalculationHeadingLoggingFunction(String msg) {
		System.out.println(msg);
		try {
			Reporter.log("<h4 style=\"color:#5F0F40;\">" + "<b>" + msg + "</b>" + "</h4>");
			test.log(Status.INFO, MarkupHelper.createLabel(" " + msg, ExtentColor.BLACK));
		} catch (Exception t) {
		}

	}
	
	public static void massagelog_TIME_DATE(String msg) {
		System.out.println(msg);
		try {
			Reporter.log("<font color =" + "#18122B" + ">" + "<b>" + msg + "</b>" + "</font>");
			test.log(Status.INFO, MarkupHelper.createLabel(" " + msg, ExtentColor.BLACK));
		} catch (Exception t) {
		}

	}
	
	

public static void closeAllWindowExceptParent(WebDriver driver) {
    // Get the handle of the parent window
    String parentWindowHandle = driver.getWindowHandle();

    // Get all open window handles
    Set<String> allWindowHandles = driver.getWindowHandles();

    // Iterate through each window handle
    for (String handle : allWindowHandles) {
        // If the handle is not the parent window, switch to it and close it
        if (!handle.equals(parentWindowHandle)) {
            driver.switchTo().window(handle); // Switch to the window
            driver.close(); // Close the window
        }
    }

    // Switch back to the parent window
    driver.switchTo().window(parentWindowHandle);
}
    public static void doubleClickUsingActionClass(WebElement element, String Fieldname) {
        WebDriver driver = getDriver();
        String exceptionmate="NO";
        try {
            Actions act = new Actions(driver);
            Thread.sleep(1000);
            act.moveToElement(element).doubleClick(element).build().perform();
            Thread.sleep(1000);
            test.log(Status.PASS, "Click Sucessfully==" + Fieldname);
            Reporter.log("Click Sucessfully==" + Fieldname);
            System.out.println("Click Sucessfully==" + Fieldname);
        } catch (Exception e) {
            exceptionmate=e.getMessage();
        } finally {
            if(!exceptionmate.equals("NO")) {
                Library.customClickUsingJavascriptExecutorI(element, Fieldname);
            }
        }
        
    }

    public static void backToSummaryButton() {

//		String btnhtml="<button style=\"background-color: blue; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;\">\r\n"
//				+ "    <a href=\"#summary\" style=\"text-decoration: none; color: white;\">Back To Summary</a>\r\n"
//				+ "</button>\r\n"
//				+ "";
//		
//		Reporter.log(btnhtml);
//		

		String newbtn = "<button style=\"background-color: blue; color: white; padding: 5px 10px; border: none; border-radius: 5px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;\" onmouseover=\"this.style.backgroundColor='green'\" onmouseout=\"this.style.backgroundColor='blue'\" onclick=\"window.location.href='#summary'\">Back To Summary</button>\r\n"
				+ "";

		// Reporter.log(newbtn);

		String anchorHTML = "<a href=\"#summary\" style=\"background-color: blue; color: white; padding: 5px 10px; border-radius: 5px; text-decoration: none; font-size: 16px; margin: 4px 2px; display: inline-block; cursor: pointer;\" onmouseover=\"this.style.backgroundColor='green'\" onmouseout=\"this.style.backgroundColor='blue'\">Back To Summary</a>";

		Reporter.log(anchorHTML);

		Reporter.log("<br>");
	}
	public static void flagKeyValueHeadingArrayLoggingStart(String[] Array1, String[] Array2, String[] Array3) {
		int arrayLength = Array1.length;
		StringBuilder sb = new StringBuilder("<table border='1' style='border-collapse: collapse;'>");

		// Add header row for better readability
		sb.append("<tr>");
		sb.append("<th style='font-weight: bold;'>Sr No.</th>");
		sb.append("<th style='font-weight: bold;'>Flag Name</th>");
		sb.append("<th style='font-weight: bold;'>Status</th>");
		sb.append("</tr>");

		// Iterate through the arrays and add each as a row in the HTML table
		for (int i = 0; i < arrayLength; i++) {
			sb.append("<tr>");

			// For Array1: Make the text bold
			sb.append("<td style='font-weight: bold;'>" + Array1[i] + "</td>");

			// For Array2: Align text to the start (left)
			sb.append("<td style='text-align: left;'>" + Array2[i] + "</td>");

			// For Array3: Check for true/false and color the text accordingly
			String color = "black"; // Default color for false
			String value = Array3[i];
			if ("true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)
					|| "Y".equalsIgnoreCase(value)) {
				color = "green"; // If true, color it green
			}

			else if ("false".equalsIgnoreCase(value) || "OFF".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value)
					|| "N".equalsIgnoreCase(value)) {
				color = "red"; // If true, color it green
			}

			sb.append("<td style='color: " + color + ";'>" + value + "</td>");

			sb.append("</tr>");
		}

		sb.append("</table>");

		// Output the HTML table
		System.out.println(sb.toString());
		Reporter.log(sb.toString());
		System.out.println();
	}


	
	public static void KeyValueHeadingArrayLoggingStart(String[] Array1, String[] Array2, String[] Array3, String[] Array4) {
	    int arrayLength = Array1.length;
	    StringBuilder sb = new StringBuilder("<table>");

	    // Iterate through the arrays and add each as a row in the HTML table
	    for (int i = 0; i < arrayLength; i++) {
	        sb.append("<tr>");
	        sb.append("<th style='font-weight: normal;'>" + Array1[i] + "</th>");
	        sb.append("<th>" + Array2[i] + "</th>");
	        sb.append("<th>" + Array3[i] + "</th>");
	        sb.append("<th>" + Array4[i] + "</th>");
	        sb.append("</tr>");
	    }

	    sb.append("</table>");

	    // Output the HTML table
	    System.out.println(sb.toString());
	    Reporter.log(sb.toString());
	    System.out.println();
	}

	
}