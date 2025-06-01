package com.aa.utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {

    public static WebDriver createDriverInstance(String browserType) {
        WebDriver driver = null;

        if (browserType.equalsIgnoreCase("chrome")) {
        	
        	WebDriverManager.chromedriver().setup();;
           // WebDriverManager.chromedriver().browserVersion("latest").setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
           
            
            java.util.logging.Level logLevel = java.util.logging.Level.ALL;
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, logLevel);
            options.setCapability("goog:loggingPrefs", logPrefs);

            
            
            
            driver = new ChromeDriver(options);
            
        } else if (browserType.equalsIgnoreCase("hchrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            driver = new ChromeDriver(options);
        } else if (browserType.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            driver = new FirefoxDriver(options);
        } else if (browserType.equalsIgnoreCase("ie")) {
            WebDriverManager.iedriver().setup();
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.introduceFlakinessByIgnoringSecurityDomains();
            options.ignoreZoomSettings();
            driver = new InternetExplorerDriver(options);
        } else if (browserType.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--inprivate");
            driver = new EdgeDriver(options);
        }
        else if (browserType.equalsIgnoreCase("opera")) { 
            WebDriverManager.operadriver().setup();
              //  driver = new OperaDriver();
        }
        if (driver != null) {
            driver.manage().deleteAllCookies();
        }

        return driver;
    }


}
