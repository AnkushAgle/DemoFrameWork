package com.aa.utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import net.bytebuddy.utility.RandomString;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class ScreenshotUtil extends Library {
	
	

	
	
public static void screenshot(String name) throws Exception {
		
	
	WebDriver driver=getDriver();
	 
	 	String path=System.getProperty("user.dir")+"\\ScreenShots\\"+name+".png";
	   //TakesScreenshot ts=(TakesScreenshot)driver;
		
		//File src=ts.getScreenshotAs(OutputType.FILE);
		//File Destnation=new  File(path);
		//FileHandler.copy(src, Destnation);
	//Reporter.log("Screenshot taken Succesfully=="+path);
	
	//test.log(Status.PASS,"Screenshot taken Succesfully=="+path);
//		String path = "<img src="\"file://"" alt="\"\"/" />";
//		Reporter.log(path);
	}





public static void attachscreenshotreport(String Title) throws Exception {
	
	test.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromPath(reportpath(), Title).build());
	
	
}


public static String reportpath() throws Exception {
	
	WebDriver driver=getDriver();

	String rm=RandomString.make(2);
	TakesScreenshot ts=(TakesScreenshot)driver;
	File src=ts.getScreenshotAs(OutputType.FILE);
	String path=System.getProperty("user.dir")+"\\ScreenShots\\failedtest"+rm+".png";
	File destn=new File(path);
	//FileHandler.copy(src, destn);
	
	
	return path;
}



public static  String  captureScreenshotWithRandomName() throws Exception {
	WebDriver driver=getDriver();

	Date date = Calendar.getInstance().getTime(); 
	   DateFormat dateFormat = new SimpleDateFormat("dd_mm_yyyy_hh_mm_ss");
	  String rm=dateFormat.format(date); 
	  String path=System.getProperty("user.dir")+"\\ScreenShots\\Validation\\valdn"+rm+".png";
	//  test.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromPath(path, "rm").build());
	  test.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(reportpathBase(), "<b>Validation Screenshot</b>").build());
	Library.massagelog_TIME_DATE("Sceenshot Path::"+path);

	  System.out.println("Screenshot path=="+path);
         TakesScreenshot ts=(TakesScreenshot)driver;
	
	File src=ts.getScreenshotAs(OutputType.FILE);
	File Destnation=new  File(path);
	FileHandler.copy(src, Destnation);
	
	
	//Reporter.log("<a href=\""+path+"\""+">"+"Look In To Screenshot"+"</a>");
	
	String Time=Library.DateTimeString_LOGGING_24_HRS();
	Reporter.log("<h4>Time :"+Time+"</h4>");
	Reporter.log("<br>");
	Reporter.log("<img src=\""+path+"\""+" width=\"1000\" height=\"500\" >"+""+"</img>");
	
	Reporter.log("<br>");
	Reporter.log("<br>");
	
	
	
//	<a href="https://pagedart.com">
//	  My Great Link
//	</a>
//	  
//	  
//Reporter.log("Screenshot taken Succesfully=="+path);

//test.log(Status.PASS,"Screenshot taken Succesfully=="+path);
//	String path = "<img src="\"file://"" alt="\"\"/" />";
//	Reporter.log(path);
	
	
	return path;
}


public static String reportpathBase() throws Exception {
	
	WebDriver driver=getDriver();

	TakesScreenshot ts = (TakesScreenshot)driver;

    String dest = ts.getScreenshotAs(OutputType.BASE64);

    return "data:image/jpg;base64, " + dest ;

}



public static void attachScreenshotBase64Format(String Title) throws Exception {
	System.out.println("Lokking for screenshot ");
	captureScreenshot(Title);
	System.out.println("SCreenshot taken Succesfully ");
	
	

}


public static void Fullpagescreenshot(String Screenshotname) throws Exception {
	 
	
	WebDriver driver=getDriver();

	Date date = Calendar.getInstance().getTime(); 
	  DateFormat dateFormat = new SimpleDateFormat("dd_mm_yyyy_hh_mm_ss");
	  String rm=dateFormat.format(date); 
	  String path=System.getProperty("user.dir")+"\\ScreenShots\\Validation\\"+Screenshotname+rm+".png";
	 Screenshot s=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(5000)).takeScreenshot(driver);
     ImageIO.write(s.getImage(),"PNG",new File(path));
	  System.out.println("Full page screenshot successfully taken=="+path);
}





public static void captureScreenshot(String screenshotname) throws Exception {

	WebDriver driver=getDriver();

	
	// Generate timestamp for unique filename
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
    String timeStamp = dateFormat.format(calendar.getTime());
  

    // Define screenshot file path
    String path = System.getProperty("user.dir") + "\\ScreenShots\\Validation\\" + screenshotname + timeStamp + ".png";
    System.out.println("Screenshot path == " + path);

    // Capture screenshot
    TakesScreenshot ts = (TakesScreenshot) driver;
    File src = ts.getScreenshotAs(OutputType.FILE);
    File destination = new File(path);
    FileHandler.copy(src, destination);
	
    
    // Log screenshot in test report
	  test.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(reportpathBase(), "<b>Validation Screenshot::"+screenshotname+"</b>").build());

	String Time=Library.DateTimeString_LOGGING_24_HRS();
	Reporter.log("<h4>Time :"+Time+"</h4>");
	Reporter.log("<br>");
	Reporter.log("<img src=\""+path+"\""+" width=\"1000\" height=\"500\" "+" title=\""+path+"\""+"></img>");
	
	Reporter.log("<br>");
	Reporter.log("<br>");
}




public static String captureScreenshotWithOriginalName(String screenshotname) throws Exception {
	WebDriver driver=getDriver();


    // Define screenshot file path
    String path = System.getProperty("user.dir") + "\\ScreenShots\\Validation\\" + screenshotname+ ".png";
    System.out.println("Screenshot path == " + path);

    // Capture screenshot
    TakesScreenshot ts = (TakesScreenshot) driver;
    File src = ts.getScreenshotAs(OutputType.FILE);
    File destination = new File(path);
    FileHandler.copy(src, destination);
	
    
    // Log screenshot in test report
	  test.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(reportpathBase(), "<b>Validation Screenshot::"+screenshotname+"</b>").build());

	String Time=Library.DateTimeString_LOGGING_24_HRS();
	Reporter.log("<h4>Time :"+Time+"</h4>");
	Reporter.log("<br>");
	Reporter.log("<img src=\""+path+"\""+" width=\"1000\" height=\"500\" "+" title=\""+path+"\""+"></img>");
	
	Reporter.log("<br>");
	Reporter.log("<br>");
	
	
	return path;
}


public static  String captureFullPageScreenshot(WebDriver driver, String filePath) {
    // Take screenshot using Selenium WebDriver
    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

    try {
        // Read the screenshot as a BufferedImage
        BufferedImage fullImage = ImageIO.read(screenshot);

        // Get the current URL
        String currentUrl = driver.getCurrentUrl();

        // Get current timestamp
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String timestamp = dateFormat.format(date);

        // Create a graphics object to draw on the image
        Graphics2D graphics = fullImage.createGraphics();

        // Set font and color for the text
        Font font = new Font("Arial", Font.BOLD, 14);
        graphics.setFont(font);
        graphics.setColor(Color.RED);

        // Draw the URL and timestamp onto the image
        graphics.drawString("URL: " + currentUrl, 10, fullImage.getHeight() - 30);
        graphics.drawString("Timestamp: " + timestamp, 10, fullImage.getHeight() - 50);

        // Save the screenshot as a file
        ImageIO.write(fullImage, "png", new File(filePath));
    } catch (IOException e) {
        e.printStackTrace();
    }
return filePath;

}
}
