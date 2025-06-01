package com.aa.utility;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aa.utility.ExcelReader;
import com.paulhammant.ngwebdriver.NgWebDriver;
import com.aa.utility.DriverFactory;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass implements ITestConstants {
    
    public static int TOTAL_NUMBER_OF_TEST_CASES = 0;
    public static String SCREENSHOTPATH = "";
    public static String TOASTERMASSAGE = ""; 
    protected String TESTLOGINSHEET = "";
    public static String TEST_CASE_STATUS = "PASS";
    public static String TESTURL;
    public static long TEST_START_TIME;
    public static long TEST_END_TIME;
    public static String TESTBROWSER;
    protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    public static String BROWSER_OPEN_OR_CLOSE;
    public static long high;
    public static long low;
    public static long mdm = 10;
    public static int logStepVar = 1;

    @Parameters({"browser", "environment"})
    @BeforeClass(groups = {"smoke","Regression","sanity"}, alwaysRun = true)
    public void BeforeClass(@Optional("chrome") String browser, @Optional("openqa") String environment) {
        String excelpath = System.getProperty("user.dir") + "\\ExcelFiles\\LOGIN.xlsx";
        ExcelReader Ecel = new ExcelReader(excelpath);
        String sheetloin = "LOGIN";
        TESTURL = Ecel.getCellData(sheetloin, "value", 6);
        BROWSER_OPEN_OR_CLOSE = Ecel.getCellData(sheetloin, "value", 8);

        String LOW ="2";
        low = Long.parseLong(LOW);
        String High = "3";
        high = Long.parseLong(High);
        
        long implicitlywait = 1;
        String ImplicitlyWait = Ecel.getCellData("WAITS", "ImplicitlyWait", 1);
        
        TESTBROWSER = browser;
        WebDriver driver_local = DriverFactory.createDriverInstance(browser);
        threadLocalDriver.set(driver_local);
        
        // Initialize NgWebDriver for Angular apps

        if(environment.equalsIgnoreCase("NHP")) {
            TESTURL = "http://192.168.0.147:6014/login";
        } else if(environment.equalsIgnoreCase("main")) {
            TESTURL = "http://192.168.0.147:8014/login";
        } else if(environment.equalsIgnoreCase("prod")) {
            TESTURL = "http://192.168.0.189:8014/login";
        }
        
        
        else if(environment.equalsIgnoreCase("openqa")) {
            TESTURL = "https://demoqa.com/";
        }
        getDriver().get(TESTURL);
        getDriver().manage().window().maximize();
        
        if(!ImplicitlyWait.equalsIgnoreCase("NOWAIT")) {
            if(ImplicitlyWait.length() == 1) {
                implicitlywait = Long.parseLong(ImplicitlyWait);
            }
            getDriver().manage().timeouts().implicitlyWait(implicitlywait, TimeUnit.SECONDS);
        }
    }

    @BeforeMethod(groups = {"smoke","Regression"}, alwaysRun = true)
    public void beforemethod() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Test case started in thread: " + Thread.currentThread().getId());
    }

    @AfterMethod(groups = {"smoke","Regression"}, alwaysRun = true)
    public void Aftermethod() throws Exception {
        System.out.println("Test case status: " + TEST_CASE_STATUS);
        
        if(TEST_CASE_STATUS.equalsIgnoreCase("FAIL")) {
            try {
                getDriver().navigate().to(TESTURL);
                WebElement load = getDriver().findElement(By.xpath("//div[contains(@class,'loading-foreground')]"));
                WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(100));
                wait.until(ExpectedConditions.invisibilityOf(load));
            } catch (Exception e) {
                System.out.println("Error in AfterMethod: " + e.getMessage());
            }
        }
        
        System.out.println("Test script finished with status: " + TEST_CASE_STATUS);
        System.out.println("------------------------------------------------------------------");
    }

    @AfterClass(groups = {"smoke","Regression","sanity"}, alwaysRun = true)
    public void Tear_Down() throws Exception {
        Thread.sleep(1000);
        
        if (BROWSER_OPEN_OR_CLOSE.equalsIgnoreCase("close")) {
            getDriver().quit();
        } else {
            getDriver().quit();
        }
        
        // Clean up thread-local instances
        threadLocalDriver.remove();
    }
    
    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }
    
 
}