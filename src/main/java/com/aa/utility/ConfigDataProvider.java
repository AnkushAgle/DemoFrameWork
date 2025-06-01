package com.aa.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.aventstack.extentreports.Status;

public class ConfigDataProvider {
	Properties pro;
	
	
	public static String FilePath;
	
	
	public ConfigDataProvider() throws Exception{
		String path=System.getProperty("user.dir")+"\\ConfigFiles\\LOGIN.properties";
	//Library.filelocationreport(path);
		//Library.test.log(Status.INFO, path);
		FileInputStream fis=new FileInputStream(path);
		 pro=new Properties();
		 System.out.println(path);
		pro.load(fis);
	}
	public ConfigDataProvider(String path) throws Exception{
	
		path=System.getProperty("user.dir")+"\\ConfigFiles\\LOGIN.properties";
		this.FilePath=path;
		//String path=System.getProperty("user.dir")+"\\ConfigFiles\\"+filename+".properties";
		//Library.filelocationreport(path);
	//	Library.test.log(Status.INFO, path);
		FileInputStream fis=new FileInputStream(path);
		 pro=new Properties();
		 System.out.println(path);
		pro.load(fis);
	}

	
	public ConfigDataProvider(String modulename,String filename) throws Exception{
		String path=System.getProperty("user.dir")+"\\ConfigFiles\\"+modulename+"\\"+filename+".properties";
		//Library.filelocationreport(path);
		
	//	Library.test.log(Status.INFO, path);
		FileInputStream fis=new FileInputStream(path);
		 pro=new Properties();
		System.out.println(path);
		pro.load(fis);
	}
	public String getURL() {
		
		
		return pro.getProperty("URL");
	}
	
	public String readConfigData(String key) {
		
		return pro.getProperty(key);
	}

	
	public static void main(String[] args) throws Exception {
		
		
		ConfigDataProvider config=new ConfigDataProvider("filepathmate");
		
		String configdata=config.readConfigData("username");
		
		System.out.println(configdata);
		
		
		
		String datatobewrite="supe duper user";
	
		config.writeDataIntoConfigFile("username", datatobewrite);
	
	}
	
	
	
	
	public void writeDataIntoConfigFile(String ConfigKey,String DatatobeWrite) { 
		
		
		
		
		try (FileOutputStream fos = new FileOutputStream(FilePath)) {
        Properties properties = new Properties();

        // Set or update values in the properties file
        properties.setProperty(ConfigKey,DatatobeWrite);
              properties.store(fos, "Updated configuration");

        System.out.println("Configuration updated successfully.");
    } catch (IOException e) {
        e.printStackTrace();
    }}
	
	
	
}
