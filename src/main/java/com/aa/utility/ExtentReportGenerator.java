package com.aa.utility;

import java.io.File;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportGenerator {
	static ExtentReports extent;
	
	 public static ExtentReports getReports() {
	        String excelpath = System.getProperty("user.dir") + "\\ExcelFiles\\LOGIN.xlsx";
	        ExcelReader Ecel = new ExcelReader(excelpath);
	        String sheet = "REPORT";
	        String DocumentTitle = Ecel.getCellData(sheet, "DocumentTitle", 1);
	        String ReportName = Ecel.getCellData(sheet, "ReportName", 1);
	        String ProjectName = Ecel.getCellData(sheet, "Project Name", 1);
	        String CreatedBy = Ecel.getCellData(sheet, "Created By", 1);
	        String OS = System.getProperty("os.name");
	        String QAName = Ecel.getCellData(sheet, "QA Name", 1);
	        String TeamLead = Ecel.getCellData(sheet, "Team Lead", 1);
	        String Reporttheme = Ecel.getCellData(sheet, "ReportTheme", 1);

	        Date d = new Date();
	        String date1 = d.toString();
	        System.out.println(date1);
	        String date2 = date1.replaceAll(":", "_");
	        String date = date2.replaceAll(" ", "_");
	        String Reportpath = System.getProperty("user.dir") + "\\Reports\\" + "index" + date + ".html";
	        File file = new File(Reportpath);

	        System.out.println(Reportpath);
	        ExtentSparkReporter reporter = new ExtentSparkReporter(file);

	        // Basic Report Configurations
	        reporter.config().setDocumentTitle(DocumentTitle);
	        reporter.config().setReportName(ReportName);
	        
	        // Theme Configuration
	        if (Reporttheme.equalsIgnoreCase("DARK")) {
	            reporter.config().setTheme(Theme.DARK);
	        } else if (Reporttheme.equalsIgnoreCase("STANDARD")) {
	            reporter.config().setTheme(Theme.STANDARD);
	        } else {
	            reporter.config().setTheme(Theme.DARK);
	        }

	        // Adding Custom CSS for Class Name Filtering
	        String customCSS = ".category-group { font-size: 14px; font-weight: bold; color: #007bff; }";
	        reporter.config().setCss(customCSS);

	        extent = new ExtentReports();
	        extent.attachReporter(reporter);

	        // Adding System Information
	        extent.setSystemInfo("Project Name", ProjectName);
	        extent.setSystemInfo("Created By", CreatedBy);
	        extent.setSystemInfo("OS", OS);
	        extent.setSystemInfo("QA Name", QAName);
	        extent.setSystemInfo("Team Lead", TeamLead);
	        extent.setSystemInfo("Java Runtime Version", System.getProperty("java.runtime.version"));

	        return extent;
	    }
	
	
	
	public static ExtentReports getReportsold() {
		
		
		String excelpath = System.getProperty("user.dir")+"\\ExcelFiles\\LOGIN.xlsx";
		ExcelReader Ecel = new ExcelReader(excelpath);
		String sheet = "REPORT";
		String DocumentTitle = Ecel.getCellData(sheet, "DocumentTitle", 1);
		String ReportName = Ecel.getCellData(sheet, "ReportName", 1);
		String ProjectName = Ecel.getCellData(sheet, "Project Name", 1);
		String CreatedBy = Ecel.getCellData(sheet, "Created By", 1);
		String OS= System.getProperty("os.name");
		String QAName= Ecel.getCellData(sheet, "QA Name", 1);
		String TeamLead=Ecel.getCellData(sheet, "Team Lead", 1);
		String Reporttheme=Ecel.getCellData(sheet, "ReportTheme", 1);
		
		Date d=new Date();
		
		String date1=d.toString();
		System.out.println(date1);
		String date2=date1.replaceAll(":","_");
		String date=date2.replaceAll(" ","_");
		String Reportpath=System.getProperty("user.dir")+"\\Reports\\"+"index"+date+".html";
		File file=new File(Reportpath);
	
	//
		System.out.println(Reportpath);
		ExtentSparkReporter reportes=new ExtentSparkReporter(file);
	     reportes.config().setDocumentTitle(DocumentTitle);
	     reportes.config().setReportName(ReportName);
	     //reportes.config().setReportName("ANKUSH AGLE");
	     
	     if(Reporttheme.equalsIgnoreCase("DARK")) {
	     reportes.config().setTheme(Theme.DARK);
	     }
	     
	     else if(Reporttheme.equalsIgnoreCase("STANDARD")) {
		     reportes.config().setTheme(Theme.STANDARD);
		     }
	     
	     else {
	    	 
	    	 reportes.config().setTheme(Theme.DARK); 
	    	 
	     }
	        
	     
	     extent=new  ExtentReports();
	     
	     extent.attachReporter(reportes);
	     extent.setSystemInfo("Project Name", ProjectName);
	     extent.setSystemInfo("Created By",CreatedBy);
	  //   extent.setSystemInfo("Module Name", "Materials Module");
	     extent.setSystemInfo("O.S", OS);
	     extent.setSystemInfo("QA Name",QAName);
	     extent.setSystemInfo("Team Lead",TeamLead);
	     extent.setSystemInfo("Java runtime version:",System.getProperty("java.runtime.version" ));
		
		return extent;
	}

}
