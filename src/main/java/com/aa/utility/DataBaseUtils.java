
package com.aa.utility;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.testng.Assert;
import org.testng.Reporter;

import com.aventstack.extentreports.Status;

public class DataBaseUtils extends Library {
    
    public static Statement adminStatement;
    public static Statement companyStatement;
    public static String COMPANY_DATABASE;
    public static String ADMIN_DATABASE;
    private Connection adminConnection;
    private Connection companyConnection;
    public int databaseOpStep=1;
    
    /**
     * @Author: Ankush Agle at 14-02-2025 12:28
     *
     * @implNote :- Below Class Used To Interact With DataBase And Fetch The Values From The Database
     * */
    public DataBaseUtils(String LOGINSHEETPATH) throws Exception {
        String path = LOGINSHEETPATH;
        ExcelReader Excel = new ExcelReader(path);
        String sheet = "JDBC";
        String sqlserver = Excel.getCellData(sheet, "SQLSERVER", 1);
        String companyDatabase = Excel.getCellData(sheet, "DATABASE", 1);
        String adminDatabase = Excel.getCellData(sheet, "ADMIN_DATABASE", 1);
        
        if(adminDatabase.contains("NODATA")) {
        	  
            // Standardized error message with file path and sheet name
            String errorMessage = "❌ Missing required data in the Excel file.\n" +
                                  "📂 File Path: " + path + "\n" +
                                  "📄 Sheet Name: '" + sheet + "'\n\n" +
                                  "Please ensure the following:\n" +
                                  "✅ Column 'ADMIN_DATABASE' exists in the sheet.\n" +
                                  "✅ Your admin database value is provided in row number 2.";

            Library.dialogErrorMessageLog(errorMessage);
            Excel.addColumn(sheet, "ADMIN_DATABASE");
            Assert.fail(errorMessage);
        	
        }
        
        String user = Excel.getCellData(sheet, "USER", 1);
        String pass = Excel.getCellData(sheet, "PASSWORD", 1);
		pass="";
        String encrypt = Excel.getCellData(sheet, "encrypt", 1);
        String trustServerCertificate = Excel.getCellData(sheet, "trustServerCertificate", 1);
        String loginTimeout = Excel.getCellData(sheet, "loginTimeout", 1);
        
        this.COMPANY_DATABASE = companyDatabase;
        this.ADMIN_DATABASE = adminDatabase;
        
        String adminConnectionUrl = "jdbc:sqlserver://" + sqlserver + ";"
                + "database=" + adminDatabase + ";"
                + "user=" + user + ";"
                + "password=" + pass + ";"
                + "encrypt=" + encrypt + ";"
                + "trustServerCertificate=" + trustServerCertificate + ";"
                + "loginTimeout=" + loginTimeout + ";";
        
        Library.dialogSkipMessageLog("Admin Connection URL : "+adminConnectionUrl);
        String companyConnectionUrl = "jdbc:sqlserver://" + sqlserver + ";"
                + "database=" + companyDatabase + ";"
                + "user=" + user + ";"
                + "password=" + pass + ";"
                + "encrypt=" + encrypt + ";"
                + "trustServerCertificate=" + trustServerCertificate + ";"
                + "loginTimeout=" + loginTimeout + ";";
        Library.dialogSkipMessageLog("Company Connection URL : "+companyConnectionUrl);

        this.adminConnection = DriverManager.getConnection(adminConnectionUrl);
        this.companyConnection = DriverManager.getConnection(companyConnectionUrl);
        adminStatement = adminConnection.createStatement();
        companyStatement = companyConnection.createStatement();
    }
    
    /**
     * Fetches SiteCode, SiteName, and StateCode from ADMIN_DATABASE.
     * @return List of Map containing Site details.
     */
    public List<Map<String, String>> getSiteDetails() {
        List<Map<String, String>> siteDetailsList = new ArrayList<>();
        String query = "SELECT LTRIM(RTRIM(a.MLOCCODE)) AS SiteCode, "
                + "LTRIM(RTRIM(a.MLOCNAME)) AS SiteName, "
                + "LTRIM(RTRIM(MCLSTATE)) AS StateCode "
                + "FROM MLOCMST(NOLOCK) a "
                + "INNER JOIN MCOMPANYLOCDL b ON a.MLOCCODE = b.MCLOCCODE "
                + "INNER JOIN MCOMPANYMST c ON b.MCLCOMPCODE = c.MCMCOMPCODE "
                + "INNER JOIN MCSUMAST R ON R.MCSCSUCODE = MCLSTATE AND R.MCSCSUFLAG = 'S'";
        
        try (PreparedStatement preparedStatement = adminConnection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Map<String, String> siteMap = new HashMap<>();
                siteMap.put("SiteCode", resultSet.getString("SiteCode"));
                siteMap.put("SiteName", resultSet.getString("SiteName"));
                siteMap.put("StateCode", resultSet.getString("StateCode"));
                siteDetailsList.add(siteMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Library.massagelog("Error fetching site details: " + e.getMessage());
        }
        return siteDetailsList;
    }
    
    public List<Map<String, String>> getSiteDetails(String siteCode) {
        List<Map<String, String>> siteDetailsList = new ArrayList<>();
        String query = "SELECT LTRIM(RTRIM(a.MLOCCODE)) AS SiteCode, "
                + "LTRIM(RTRIM(a.MLOCNAME)) AS SiteName, "
                + "LTRIM(RTRIM(MCLSTATE)) AS StateCode "
                + "FROM MLOCMST(NOLOCK) a "
                + "INNER JOIN MCOMPANYLOCDL b ON a.MLOCCODE = b.MCLOCCODE "
                + "INNER JOIN MCOMPANYMST c ON b.MCLCOMPCODE = c.MCMCOMPCODE "
                + "INNER JOIN MCSUMAST R ON R.MCSCSUCODE = MCLSTATE AND R.MCSCSUFLAG = 'S' "
                + "WHERE a.MLOCCODE = ?";
        
        try (PreparedStatement preparedStatement = adminConnection.prepareStatement(query)) {
            preparedStatement.setString(1, siteCode);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, String> siteMap = new HashMap<>();
                    siteMap.put("SiteCode", resultSet.getString("SiteCode"));
                    siteMap.put("SiteName", resultSet.getString("SiteName"));
                    siteMap.put("StateCode", resultSet.getString("StateCode"));
                    siteDetailsList.add(siteMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Library.massagelog("Error fetching site details: " + e.getMessage());
        }
        return siteDetailsList;
    }

    
    /**
     * Fetches inventory valuation method based on MCLSBINVFLG.
     *
     * @param inventoryValuationMethod The inventory valuation flag to determine the method type.
     *                                  - 'F' = FIFO Type
     *                                  - 'M' = Moving Average Type
     *                                  - 'A' = At Actual Type
     * @return The corresponding inventory valuation method details if found; otherwise, returns an empty result.
     */
    public String getClassCodeFromDataBase(String inventoryValuationMethod) {
        int step = 1;

        // Log the purpose of this function execution
        String message = "Fetching Inventory Valuation Method for MCLSBINVFLG: " + inventoryValuationMethod;
        step = Library.logStepMessage(step, message);

        // Initialize variable to store the retrieved inventory method
        String inventoryMethodCode = "";
        boolean recordFound = false;

        
        String moreLogic="and (MCLSINWBCH='0' and MCLSHEATNO='0' and  MCLSSRNO='0' and  MCLSMFGBCH='0')";
        // Updated SQL Query to fetch Inventory Valuation Method
        String query = "SELECT MCLSBCODE, * FROM MCLSSUBCLS " +
                       "WHERE MCLSBTYP = 'C' " +
                       "AND MCLSBNAT IN ('M') " +moreLogic+
                       " AND MCLSBINVFLG = ?"; // Using PreparedStatement to prevent SQL Injection

        // Log the SQL Query being executed
        Library.showQueryLogging("Executing Query: " + query + " with MCLSBINVFLG: " + inventoryValuationMethod);

        try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
            // Set the inventory valuation parameter in the query
            preparedStatement.setString(1, inventoryValuationMethod);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) { 
                    Library.dialogErrorMessageLog("No matching Inventory Valuation Method found for MCLSBINVFLG: " + inventoryValuationMethod);
                } else {
                    Library.massagelog("Query executed successfully. Processing results...");
                    while (resultSet.next()) {
                        inventoryMethodCode = resultSet.getString("MCLSBCODE");
                        recordFound = true;
                        Library.dialogMassageLog("Found Inventory Valuation Method: Code = " + inventoryMethodCode + " for MCLSBINVFLG: " + inventoryValuationMethod);
                        return inventoryMethodCode;
                    }
                }
            }
        } catch (SQLException e) {
            Library.dialogErrorMessageLog("SQL Exception while fetching Inventory Valuation Method for MCLSBINVFLG '" + inventoryValuationMethod + "': " + e.getMessage());
        }

        // Return the Inventory Method Code if found, otherwise return an empty string
        return inventoryMethodCode;
    }

    public boolean checkSystemPoliciesFlagStatus(PolicyTables header, String columnName) throws SQLException {
        
    	
    	String tableName=header.getTableName();
    	
    	
    	// Log the SQL operation being performed
        Library.dialogMassageLog("JDBC Script Code To Check System Policies Flag Status Through JDBC");
        
        // SQL query to check the flag status
        String selectQuery = "SELECT " + columnName + " FROM " + tableName;
        
        // Log the query that will be executed in the database
        Library.showQueryButtonLogging(selectQuery);
        
        try {
            // Execute the SQL query and retrieve the result
            ResultSet resultSet = companyStatement.executeQuery(selectQuery);
            
            // Check if a record is found and return status
            if (resultSet.next()) {
                int flagStatus = resultSet.getInt(columnName);
                boolean status = (flagStatus == 1);
                Library.massagelog_TIME_DATE("Flag " + columnName + " in table " + tableName + " has status: " + flagStatus);
                return status;
            } else {
                Library.massagelog_TIME_DATE("No records found in table " + tableName + " for column " + columnName);
                return false;
            }
        } catch (SQLException e) {
            // Log and rethrow any SQL exceptions that occur
            e.printStackTrace();
            throw e;
        }
    }

    
    public boolean checkClassIsExistInDatabase(String classCode) throws SQLException {
        // Log the SQL operation being performed
        Library.dialogMassageLog("JDBC Script Code To Check If Class Exists In Database Through JDBC");

        // SQL query to check if a specific class exists in the MCLSSUBCLS table
        String selectQuery = "SELECT * FROM MCLSSUBCLS WHERE MCLSBTYP='C' AND MCLSBCODE='" + classCode + "'";

        // Log the query that will be executed in the database
        Library.showQueryButtonLogging(selectQuery);

        try {
            // Execute the SQL query and retrieve the result
            ResultSet resultSet = companyStatement.executeQuery(selectQuery);

            // Check if a record was found
            if (resultSet.next()) {
                Library.massagelog_TIME_DATE("Class with code " + classCode + " exists in the database.");
                return true;  // Class exists
            } else {
                Library.massagelog_TIME_DATE("Class with code " + classCode + " does not exist in the database.");
                return false;  // Class does not exist
            }
        } catch (SQLException e) {
            // Log and rethrow any SQL exceptions that occur
            e.printStackTrace();
            throw e;
        }
    }


    public boolean checkSubClassIsExistInDatabase(String subclassCode) throws SQLException {
        // Log the SQL operation being performed
        Library.dialogMassageLog("JDBC Script Code To Check If Class Exists In Database Through JDBC");

        // SQL query to check if a specific class exists in the MCLSSUBCLS table
        String selectQuery = "SELECT * FROM MCLSSUBCLS WHERE MCLSBTYP='S' AND MCLSBCODE='" + subclassCode + "'";

        // Log the query that will be executed in the database
        Library.showQueryButtonLogging(selectQuery);

        try {
            // Execute the SQL query and retrieve the result
            ResultSet resultSet = companyStatement.executeQuery(selectQuery);

            // Check if a record was found
            if (resultSet.next()) {
                Library.massagelog_TIME_DATE("Class with code " + subclassCode + " exists in the database.");
                return true;  // Class exists
            } else {
                Library.massagelog_TIME_DATE("Class with code " + subclassCode + " does not exist in the database.");
                return false;  // Class does not exist
            }
        } catch (SQLException e) {
            // Log and rethrow any SQL exceptions that occur
            e.printStackTrace();
            throw e;
        }
    }

public boolean checkSalesAutoAllocateByRsItemFlagStatus() throws SQLException {
    
    // Auto Allocate BY/RS Items Flag Status check
    
    // This function was created on 11/12/2024
    
    // Log the SQL operation being performed
    Library.dialogMassageLog("JDBC Script Code To Check Auto Allocate BY/RS Items Flag Status In Database Through JDBC");

    // SQL query to fetch the MSCAUTOALLOCITMONOAFCREATE flag, which determines whether auto-allocation for BY/RS items is enabled
    String selectQuery = "SELECT MSCAUTOALLOCITMONOAFCREATE FROM MSCSYSSAL";
    
    // Log the query that will be executed in the database
    Library.showQueryButtonLogging(selectQuery);
    
    try {
        // Execute the SQL query and retrieve the result
        ResultSet resultSet = companyStatement.executeQuery(selectQuery);
        
        // Check if a record was found
        if (resultSet.next()) {
            // Retrieve the boolean value of the MSCAUTOALLOCITMONOAFCREATE flag
            boolean autoAllocateByRsItemFlag = resultSet.getBoolean("MSCAUTOALLOCITMONOAFCREATE");
            
            // Log and return based on the flag's value
            if (autoAllocateByRsItemFlag) {
                Library.massagelog_TIME_DATE("Auto Allocation for BY/RS Items flag is On.");
                return true;  // Flag is On
            } else {
                Library.massagelog_TIME_DATE("Auto Allocation for BY/RS Items flag is Off.");
                return false;  // Flag is Off
            }
        } else {
            // Log when no record is found in the MSCSYSSAL table
            Library.massagelog_TIME_DATE("No record found in MSCSYSSAL table.");
            return false;  // No record found, treating as flag Off
        }
    } catch (SQLException e) {
        // Log and rethrow any SQL exceptions that occur
        e.printStackTrace();
        throw e;
    }
}



/**
 * Fetches SiteID from MLOCMST table for a given SiteCode.
 * @param siteCode The SiteCode to fetch the SiteID.
 * @return SiteID as a String, or null if not found.
 */
public String getSiteIdFromAdminDataBase(String siteCode) {
    String query = "SELECT MLOCID FROM MLOCMST WHERE MLOCCODE = ?";
    Library.showQueryButtonLogging(query);
    try (PreparedStatement preparedStatement = adminConnection.prepareStatement(query)) {
        preparedStatement.setString(1, siteCode);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
            	
            	Library.dialogMassageLog("Fetched location Id for Site Code :"+siteCode+" , : Id :"+resultSet.getString("MLOCID"));
                return resultSet.getString("MLOCID");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        Library.massagelog("Error fetching Site ID: " + e.getMessage());
    }
    return null;
}

public String getWareHouseFromTheDataBaseDynamic(String sitecode) {
    
    Map<String, Integer> docMap = new HashMap<>();
    String wareHouseCode = "";
    
    // Retrieve the username from global variables
    String username = GLOBAL_VARIABLES.get("username");
    
    // Fetch the list of physical warehouse codes based on GST flag
    List<String> lists = getPhysicalWarehouseCodeListFromCompanyDatabase(sitecode, 1);
    
    if (!lists.isEmpty()) {
        String logMessage = "Fetching all warehouses with 'Is GST Warehouse' set to true from the database.";
        Library.dialogErrorMessageLog(logMessage);
    } else {
        lists = getPhysicalWarehouseCodeListFromCompanyDatabase(sitecode, 0);
        String logMessage = "Fetching all warehouses with 'Is GST Warehouse' set to false from the database.";
        Library.dialogErrorMessageLog(logMessage);
    }

    // Iterate through the warehouse list and populate the count map
    for (String ware : lists) {
        String usercd = getUserIdFromCompanyDatabase(username);
        int totalRecord = getTotalRowsFromWarehouseUserMapping(ware, usercd);
        docMap.put(ware, totalRecord);
    }

    // Find the warehouse with the highest count
    int maxCount = -1;
    for (Map.Entry<String, Integer> entry : docMap.entrySet()) {
        if (entry.getValue() > maxCount) {
            maxCount = entry.getValue();
            wareHouseCode = entry.getKey();
        }
    }

    // Log the selected warehouse code
    if (!wareHouseCode.isEmpty()) {
        String logMessage = "Selected Warehouse Code: " + wareHouseCode + " with highest mapped record count: " + maxCount;
        Library.dialogErrorMessageLog(logMessage);
    } else {
        Library.dialogErrorMessageLog("No warehouse found with mapped records.");
    }

    // Fetch additional document details if necessary
    fetchDocumentDetails();
    
    return wareHouseCode.trim();
}

/**
 * Fetches Physical Warehouse Code List for a given SiteCode.
 * @param siteId The Site ID to fetch warehouse codes.
 * @return List of warehouse codes.
 */
public List<String> getPhysicalWarehouseCodeListFromCompanyDatabase(String sitecode,int gstWarehouseFlag) {
	
	
	if(gstWarehouseFlag==1||gstWarehouseFlag==0) {
		
		//Ok
	}
	
	else {
		
		gstWarehouseFlag=1;
	}
	
	String gstFlagStatus=Integer.toString(gstWarehouseFlag);
	
	String siteId=getSiteIdFromAdminDataBase(sitecode);
	
    List<String> warehouseCodes = new ArrayList<>();
    List<String> gstFlagSttaus = new ArrayList<>();
    
    String query = "SELECT MWMWhCode , * FROM MWARMAST WHERE MWMWHTYPE = 'P' and MWMExciseWh='"+gstFlagStatus+"' AND MWMFRLOCCD ='"+siteId+"'";
    Library.dialogMassageLog(query);

    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                warehouseCodes.add(resultSet.getString("MWMWhCode"));
              //  gstFlagSttaus.add(resultSet.getString("MWMExciseWh"));
                
                gstFlagSttaus.add(gstFlagStatus);
                
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        Library.massagelog("Error fetching warehouse codes: " + e.getMessage());
    }
    
    
    
    databaseOpStep=Library.logStepMessage(databaseOpStep, "Fetching All Physical WareHouse Code Mapped With Site :: "+sitecode);
    
    int totalList=warehouseCodes.size();
    String array1[] = new String[totalList+1];
    String array2[] = new String[totalList+1];
    String array3[] = new String[totalList+1];
    String array4[] = new String[totalList+1];

    array1[0]="Sr No.";
    array2[0]="WareHouse Code";
    array3[0]="Site Code";
    array4[0]="Is GST Warehouse?";

    
    if(!gstFlagSttaus.isEmpty()) {
    for(int i=0;i<warehouseCodes.size();i++) {
    	
    	array1[i+1]=Integer.toString(i+1);
    	array2[i+1]=warehouseCodes.get(i);
    	array3[i+1]=sitecode;
    	array4[i+1]=gstFlagSttaus.get(i);
    }
    
    Library.KeyValueHeadingArrayLoggingStart(array1, array2, array3,array4);
    
    }
    return warehouseCodes;
}


/**
 * Fetches the list of Drawing Master IDs from the database.
 * @return List of Drawing Master IDs.
 */


/**
 * Fetches trimmed MAGACGRPCD list from Accounting Group Master (maccgrpmst)
 * used in Vendor Master, based on MAGGRPTYPE and MAGSBGRTYPE criteria.
 * 
 * @return List of accounting group codes (MAGACGRPCD)
 */


/**
 * Fetches Ledger Name List (MLMLEDGRNM) used in Vendor Master based on MLMLDGRTYP2 check.
 * @return List of ledger names (MLMLEDGRNM)
 */
public List<String> getLedgerNameListForVendorMaster() {
    
    List<String> ledgerNames = new ArrayList<>();
    List<String> ledgerCodes = new ArrayList<>();

    String query = "SELECT MLMLEDGRNM, MLMLEDGRCD FROM MLEDGERMST " +
                   "WHERE ISNULL(MLMLDGRTYP2, '') = '' OR ISNULL(MLMLDGRTYP2, '') = 'C'";

    Library.dialogMassageLog("Executing Query: " + query);

    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ledgerNames.add(resultSet.getString("MLMLEDGRNM").trim());
                ledgerCodes.add(resultSet.getString("MLMLEDGRCD").trim());
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        Library.massagelog("Error fetching ledger names: " + e.getMessage());
    }

    databaseOpStep = Library.logStepMessage(databaseOpStep,
        "Fetching All Ledger Names Used in Vendor Master");

    int totalList = ledgerNames.size();
    String[] array1 = new String[totalList + 1];
    String[] array2 = new String[totalList + 1];
    String[] array3 = new String[totalList + 1];

    array1[0] = "Sr No.";
    array2[0] = "Ledger Name";
    array3[0] = "Ledger Code";

    if (!ledgerNames.isEmpty()) {
        for (int i = 0; i < totalList; i++) {
            array1[i + 1] = Integer.toString(i + 1);
            array2[i + 1] = ledgerNames.get(i);
            array3[i + 1] = ledgerCodes.get(i);
        }
        Library.flagKeyValueHeadingArrayLoggingStart(array1, array2, array3);
    }

    return ledgerNames;
}

public List<String> getAccountingGroupCodeForVendorMaster() {
    
    List<String> groupCodes = new ArrayList<>();
    List<String> groupNames = new ArrayList<>();
    List<String> reportTypes = new ArrayList<>();
    List<String> subGroupTypes = new ArrayList<>();

    String query = "SELECT MAGACGRPCD, MAGACGRPNM, MAGSBGRTYPE, MAGGRPTYPE, MAGRPTCD FROM maccgrpmst " +
                   "WHERE (MAGGRPTYPE = 'N' AND (MAGSBGRTYPE IS NOT NULL AND MAGSBGRTYPE <> '')) " +
                   "OR MAGGRPTYPE = 'V' OR MAGGRPTYPE = 'W' " +
                   "ORDER BY MAGACGRPCD, MAGACGRPNM";
    
    Library.dialogMassageLog("Executing Query: " + query);

    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                groupCodes.add(resultSet.getString("MAGACGRPCD").trim());
                groupNames.add(resultSet.getString("MAGACGRPNM").trim());
                subGroupTypes.add(resultSet.getString("MAGSBGRTYPE") != null ? resultSet.getString("MAGSBGRTYPE").trim() : "");
                reportTypes.add(resultSet.getString("MAGGRPTYPE").trim());
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        Library.massagelog("Error fetching accounting group codes: " + e.getMessage());
    }

    databaseOpStep = Library.logStepMessage(databaseOpStep, 
        "Fetching All Accounting Group Codes Used in Vendor Master");

    int totalList = groupCodes.size();
    String[] array1 = new String[totalList + 1];
    String[] array2 = new String[totalList + 1];
    String[] array3 = new String[totalList + 1];
    String[] array4 = new String[totalList + 1];

    array1[0] = "Sr No.";
    array2[0] = "Group Code";
    array3[0] = "Group Name";
    array4[0] = "Group Type";

    if (!groupCodes.isEmpty()) {
        for (int i = 0; i < totalList; i++) {
            array1[i + 1] = Integer.toString(i + 1);
            array2[i + 1] = groupCodes.get(i);
            array3[i + 1] = groupNames.get(i);
            array4[i + 1] = reportTypes.get(i);
        }
        Library.KeyValueHeadingArrayLoggingStart(array1, array2, array3, array4);
    }

    return groupCodes;
}


public boolean isDrawingNumberPresent(String drawingnm) {
    List<String> drawingMasterList = getDrDrawingMasterList(); // Fetch list from DB
    return drawingMasterList.contains(drawingnm); // Check if drawingnm exists in list
}


public List<String> getDrDrawingMasterList() {
    List<String> drawingMasterList = new ArrayList<>();

    // SQL Query to fetch Drawing Master Data
    String query = "SELECT MDRWNUMBER FROM MDRWMAST";
    Library.dialogMassageLog(query); // Log the query

    // Execute the query and fetch results
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
            drawingMasterList.add(resultSet.getString("MDRWNUMBER").trim());
        }
    } catch (SQLException e) {
        e.printStackTrace();
        Library.massagelog("Error fetching Drawing Master IDs: " + e.getMessage());
    }

    // Log the retrieved data
    int totalList = drawingMasterList.size();
    String[] serialNumbers = new String[totalList];
    String[] drawingIds = new String[totalList];

    for (int i = 0; i < drawingMasterList.size(); i++) {
        serialNumbers[i] = Integer.toString(i + 1);
        drawingIds[i] = drawingMasterList.get(i);
    }

    Library.KeyValueHeadingArrayLoggingStart(serialNumbers, drawingIds);

    return drawingMasterList;
}




/**
 * Fetches XDSGRCD value from XDOCSRNO using company connection.
 * @param MDOCTYP Document type.
 * @param MDOCSUBTYP Document subtype.
 * @param sitecode Location code.
 * @return XDSGRCD value as String, or null if not found.
 */
public String returnDocumentGroupCode(String MDOCTYP, String MDOCSUBTYP, String sitecode) {
    
String modulenam="";

	
	String query = "SELECT XDSGRCD,XDSGRCDDESC FROM XDOCSRNO "
                 + "LEFT OUTER JOIN VWMLOCMST ON XDSFRLOCID = MLOCID "
                 + "WHERE XDSDOCTYP ='"+MDOCTYP+"' AND XDSDOCSBTYP ='"+MDOCSUBTYP+"' AND MLOCCODE = '"+sitecode+"'";
    
	
	Library.showQueryButtonLogging(query);
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
//          
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
            	
            	
            	try {
                	modulenam=resultSet.getString("XDSGRCDDESC").trim();

				} catch (Exception e) {
					// TODO: handle exception
				}
            	
            	
            	
            	String goupcode=resultSet.getString("XDSGRCD").trim();
               
            	String massage="Fetching Group Code For Doc Name :: "+modulenam+" and for site :"+sitecode+":: Fetched Code ::'"+goupcode+"'";

            	Library.massagelog_TIME_DATE(massage);
            	
            	
            	return goupcode;
                
                
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
/**
 * Fetches MDTDOCTYP, MDTDOCSUBTYP, and MDTDOCDESC from MDOCTYPDTLS and stores them in three separate arrays.
 * @return A list containing three String arrays: [docTypes, docSubTypes, docDescriptions].
 */
public List<String[]> fetchDocumentDetails() {
    List<String> docTypes = new ArrayList<>();
    List<String> docSubTypes = new ArrayList<>();
    List<String> docDescriptions = new ArrayList<>();
    
    docTypes.add("<b>Main  Doc Type Code</b>");
    docSubTypes.add("<b>Sub Document Type Code </b>");
    docDescriptions.add("<b> Description </b>");
    
    String query = "SELECT MDTDOCTYP, MDTDOCSUBTYP, MDTDOCDESC FROM MDOCTYPDTLS";
    
    
    Library.dialogMassageLog(query);
    
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        
        while (resultSet.next()) {
            docTypes.add(resultSet.getString("MDTDOCTYP"));
            docSubTypes.add(resultSet.getString("MDTDOCSUBTYP"));
            docDescriptions.add(resultSet.getString("MDTDOCDESC"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    databaseOpStep=Library.logStepMessage(databaseOpStep, "Fetching All Doc Type And SubDoc Type Code And Description");

    List<String[]> result = new ArrayList<>();
    result.add(docTypes.toArray(new String[0]));
    result.add(docSubTypes.toArray(new String[0]));
    result.add(docDescriptions.toArray(new String[0]));
    Library.flagKeyValueHeadingArrayLoggingStart(docTypes.toArray(new String[0]), docSubTypes.toArray(new String[0]),docDescriptions.toArray(new String[0]) );
    
    return result;
}

/**
 * Retrieves the User ID (MUSRIDNTY) from the database based on the given User Code (MUSRID).
 *
 * @param userCode The user code for which the User ID needs to be fetched.
 * @return The User ID (MUSRIDNTY) if found, otherwise returns null.
 */
public String getUserIdFromCompanyDatabase(String userCode) {
    // SQL query to fetch MUSRIDNTY from VWMUSRMST where MUSRID matches the input user code
    String query = "SELECT MUSRIDNTY FROM VWMUSRMST WHERE MUSRID = ?";

    // Using try-with-resources to ensure resources (PreparedStatement and ResultSet) are closed automatically
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        
        // Set the userCode as a parameter in the query to prevent SQL injection
        preparedStatement.setString(1, userCode);

        // Execute the query and store the result
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            
            // Check if a record exists
            if (resultSet.next()) {
                // Return the fetched User ID
                return resultSet.getString("MUSRIDNTY");
            }
        }
    } catch (SQLException e) {
        // Print stack trace for debugging purposes
        e.printStackTrace();
        
        // Log the error message for tracking issues
        Library. errorMassageLogRedColor("Error fetching User ID for user '" + userCode + "': " + e.getMessage());
    }

    // Return null if no matching user is found or an error occurs
    return null;
}
/**
 * Retrieves the total number of rows based on the given Warehouse Code (MWMWhCode) and User ID (MWUUSRID).
 *
 * @param warehouseCode The warehouse code (MWMWhCode) used for filtering records.
 * @param userId The user ID (MWUUSRID) used for filtering records.
 * @return The total number of matching rows in the database.
 */
public int getTotalRowsFromWarehouseUserMapping(String warehouseCode, String userId) {
    // SQL query to count rows based on the given warehouse code and user ID
    String query = "SELECT COUNT(*) AS totalRows " +
                   "FROM mwarmast, VWMUSRMST, MWHUSERS, MWHUSTRANS " +
                   "WHERE MWMWhId = MWUWHID " +
                   "AND MUSRIDNTY = MWUUSRID " +
                   "AND MWUTLINKID = MWURECNO " +
                   "AND MWMWhCode = ? " +
                   "AND MWUUSRID = ?";

    // Initialize row count to 0
    int totalRows = 0;

    // Using try-with-resources to ensure resources are closed properly
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        
        // Set parameters to prevent SQL injection
        preparedStatement.setString(1, warehouseCode);
        preparedStatement.setString(2, userId);

        // Execute the query
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            
            // Retrieve the count value if a record is found
            if (resultSet.next()) {
                totalRows = resultSet.getInt("totalRows");
            }
        }
    } catch (SQLException e) {
        // Print stack trace for debugging
        e.printStackTrace();
        
        // Log the error message in red color for visibility
        Library.errorMassageLogRedColor("Error fetching total rows for Warehouse Code '" + warehouseCode + 
                                        "' and User ID '" + userId + "': " + e.getMessage());
    }

    // Return the total count of rows
    String saveMessageLog = "Total Warehouse User Authority Master transactions for Warehouse: " 
            + warehouseCode + " | User ID: " + userId + " | Rows Processed: " + totalRows;
Library.dialogMassageLog(saveMessageLog);

    return totalRows;
}
/**
 * Fetches an 8-digit HSN Code from the company database based on the applicable GST percentage.
 *
 * @param applicableGSTpercentage The GST percentage to filter the HSN codes.
 * @return The 8-digit HSN code if found, otherwise returns null.
 */


public String getHsnCodeFromCompanyDatabase(float floatApplicableGSTpercentage) {

	

	// Convert float to BigDecimal for precision checking
    BigDecimal bd = BigDecimal.valueOf(floatApplicableGSTpercentage);
    String applicableGSTpercentage;

    // If the float is a whole number, convert it to an integer
    if (bd.stripTrailingZeros().scale() <= 0) {
        applicableGSTpercentage = String.valueOf(bd.intValue());  // Convert to integer string
    } else {
        applicableGSTpercentage = String.valueOf(floatApplicableGSTpercentage);  // Keep as float string
    }

	
	// SQL query to fetch HSN Code (MHCFNCODE) where GST percentage matches and ordered by latest creation date
    String query = "SELECT MHCFNCODE FROM MHSNCodMst WHERE MHCAPPPER = ? ORDER BY MHCCREUSRDT DESC";

    Library.showQueryLogging(query);
    
    // Using try-with-resources to ensure resources (PreparedStatement and ResultSet) are closed automatically
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        
        // Set the applicable GST percentage as a parameter to prevent SQL injection
        preparedStatement.setString(1, applicableGSTpercentage);

        // Execute the query and store the result
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            
            // Check if a record exists
            while (resultSet.next()) {
                String hsnCode = resultSet.getString("MHCFNCODE");

                // Ensure HSN code is exactly 8 digits long before returning
                if (hsnCode != null && hsnCode.length() == 8 && hsnCode.matches("\\d{8}")) {
                    return hsnCode.trim();
                }
            }
        }
    } catch (SQLException e) {
        // Print stack trace for debugging purposes
        e.printStackTrace();

        // Log the error message for tracking issues
        Library.errorMassageLogRedColor("Error fetching HSN Code for GST percentage '" + applicableGSTpercentage + "': " + e.getMessage());
    }

    // Return null if no matching HSN Code is found or an error occurs
    return null;
}



/**
 * Fetches the applicable GST percentage from the company database based on the given HSN Code.
 *
 * @param hsnCode The 8-digit HSN code to find the GST percentage.
 * @return The applicable GST percentage as a String, or "N/A" if not found.
 */
public String getApplicableGSTPercentageFromHsnCode(String hsnCode) {
    // Validate HSN Code: It must be exactly 8 digits
    if (hsnCode == null || !hsnCode.matches("\\d{8}")) {
        Library.errorMassageLogRedColor("Invalid HSN Code format: " + hsnCode);
        return "N/A"; // Return "N/A" for invalid input
    }

    // SQL query to fetch GST percentage (MHCAPPPER) for the given HSN Code
    String query = "SELECT MHCAPPPER FROM MHSNCodMst WHERE MHCFNCODE = ? ORDER BY MHCCREUSRDT DESC";

    // Using try-with-resources to ensure resources (PreparedStatement and ResultSet) are closed automatically
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        
        // Set the HSN Code as a parameter to prevent SQL injection
        preparedStatement.setString(1, hsnCode);

        // Execute the query and store the result
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            
            // If a record exists, return the GST percentage as a String
            if (resultSet.next()) {
                return resultSet.getString("MHCAPPPER"); // Fetch GST percentage as String
            }
        }
    } catch (SQLException e) {
        // Print stack trace for debugging purposes
        e.printStackTrace();

        // Log the error message for tracking issues
        Library.errorMassageLogRedColor("Error fetching GST percentage for HSN Code '" + hsnCode + "': " + e.getMessage());
    }

    // Return "N/A" if no matching GST percentage is found or an error occurs
    return "N/A";
}



/**
 * Retrieves the state code (MCLSTATE) for a given site code (MLOCCODE) from the admin database.
 * 
 * @param siteCode The site code for which the state code needs to be fetched.
 *                 - This should match the MLOCCODE column in the MLOCMST table.
 *                 - Example: "CT2"
 * @return The corresponding state code (MCLSTATE) if found; otherwise, returns null.
 * 
 * Usage Example:
 *     String stateCode = getLoginSiteStateCodeFromAdminDatabase("CT2");
 *     System.out.println("State Code: " + stateCode);
 */
public String getLoginSiteStateCodeFromAdminDatabase(String siteCode) {
    // SQL query to fetch MCLSTATE from MLOCMST and its related tables based on the input siteCode
    String query = "SELECT MCLSTATE FROM MLOCMST a " +
                   "INNER JOIN MCOMPANYLOCDL b ON a.MLOCCODE = b.MCLOCCODE " +
                   "INNER JOIN MCOMPANYMST c ON b.MCLCOMPCODE = c.MCMCOMPCODE " +
                   "INNER JOIN MCSUMAST P ON P.MCSCSUCODE = MCLCOUNTRY AND P.MCSCSUFLAG = 'U' " +
                   "INNER JOIN MCSUMAST Q ON Q.MCSCSUCODE = MCLCITY AND Q.MCSCSUFLAG = 'C' " +
                   "INNER JOIN MCSUMAST R ON R.MCSCSUCODE = MCLSTATE AND R.MCSCSUFLAG = 'S' " +
                   "WHERE a.MLOCCODE = ?";

    // Using try-with-resources to ensure resources are closed automatically
    try (PreparedStatement preparedStatement = adminConnection.prepareStatement(query)) {
        
        // Set the siteCode as a parameter to prevent SQL injection
        preparedStatement.setString(1, siteCode);

        // Execute the query and store the result
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            
            // Check if a record exists
            if (resultSet.next()) {
                // Return the fetched State Code (MCLSTATE)
                return resultSet.getString("MCLSTATE");
            }
        }
    } catch (SQLException e) {
        // Print stack trace for debugging purposes
        e.printStackTrace();
        
        // Log the error message with a standard automation test engineer message
        Library.errorMassageLogRedColor("Error fetching State Code for site '" + siteCode + "': " + e.getMessage());
    }

    // Return null if no matching site is found or an error occurs
    return null;
}


/**
 * @Created Date: 2025-02-14
 *@Last Modified: 2025-02-14
 * @Author: Automation Test Engineer
 * Fetches the Rate Structure Code for IGST based on the given GST percentage.
 *
 * @param gstPercentage The GST percentage for which the Rate Structure Code is fetched.
 *                      - Example: "18"
 * @return The Rate Structure Code if found; otherwise, returns an empty string.
 *
 *Example:- 18
 *
 */

/**
 * Determines the type of the entered rate structure (CGST/SGST or IGST) by checking it against the database.
 *
 * @param rateStructureCode The rate structure code to validate.
 * @return Returns "CGST/SGST" if the rate structure is valid for CGST/SGST, "IGST" if valid for IGST,
 *         or "UNKNOWN" if the rate structure is not valid for either type.
 */
public String checkRateStructureType(String rateStructureCode) {
    // Step 1: Check if the rate structure is valid for CGST/SGST
    boolean isCgstSgst = isRateStructureOfTypeCGSTSGST(rateStructureCode);
    if (isCgstSgst) {
        Library.massagelog("Rate Structure Code '" + rateStructureCode + "' is valid for CGST/SGST.");
        return "CGST/SGST"; // Return "CGST/SGST" if the rate structure is valid for CGST/SGST.
    }

    // Step 2: Check if the rate structure is valid for IGST
    boolean isIgst = isRateStructureOfTypeIGST(rateStructureCode);
    if (isIgst) {
        Library.massagelog("Rate Structure Code '" + rateStructureCode + "' is valid for IGST.");
        return "IGST"; // Return "IGST" if the rate structure is valid for IGST.
    }

    // Step 3: If the rate structure is not valid for either type, return "UNKNOWN"
    Library.errorMassageLogRedColor("Rate Structure Code '" + rateStructureCode + "' is not valid for CGST/SGST or IGST.");
    return "UNKNOWN"; // Return "UNKNOWN" if the rate structure is not valid for either type.
}

/**
 * Checks if the given rate structure code is of type CGST/SGST.
 *
 * @param rateStructureCode The rate structure code to validate.
 * @return Returns `true` if the rate structure is valid for CGST/SGST, otherwise `false`.
 */
private boolean isRateStructureOfTypeCGSTSGST(String rateStructureCode) {
    String query = "SELECT TOP 1 1 FROM MSPRTSTR PR " +
                  "INNER JOIN MPURRATE T1 ON PR.MSPRTCD = T1.MprRTCOD AND T1.MprTaxTyp IN ('M','N') " +
                  "WHERE PR.MSPTYPE = 'P' " +
                  "AND PR.MSPSTRCD = ?"; // Check for CGST/SGST type

    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        preparedStatement.setString(1, rateStructureCode); // Set the rate structure code as a parameter
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next(); // Return true if a record is found
        }
    } catch (SQLException e) {
        Library.errorMassageLogRedColor("SQL Exception while checking CGST/SGST type for Rate Structure Code '" + rateStructureCode + "': " + e.getMessage());
        return false;
    }
}

/**
 * Checks if the given rate structure code is of type IGST.
 *
 * @param rateStructureCode The rate structure code to validate.
 * @return Returns `true` if the rate structure is valid for IGST, otherwise `false`.
 */
private boolean isRateStructureOfTypeIGST(String rateStructureCode) {
    String query = "SELECT TOP 1 1 FROM MSPRTSTR PR " +
                  "INNER JOIN MPURRATE T1 ON PR.MSPRTCD = T1.MprRTCOD AND T1.MprTaxTyp IN ('L') " +
                  "WHERE PR.MSPTYPE = 'P' " +
                  "AND PR.MSPSTRCD = ?"; // Check for IGST type

    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        preparedStatement.setString(1, rateStructureCode); // Set the rate structure code as a parameter
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next(); // Return true if a record is found
        }
    } catch (SQLException e) {
        Library.errorMassageLogRedColor("SQL Exception while checking IGST type for Rate Structure Code '" + rateStructureCode + "': " + e.getMessage());
        return false;
    }
}
public String getRateStructureFromDatabaseIGST(String gstPercentage) {
    int step = 1;

    // Log the purpose of this function execution
    String message = "Fetching Rate Structure Code for IGST with GST Percentage: " + gstPercentage;
    step = Library.logStepMessage(step, message);

    // Initialize variable to store the retrieved Rate Structure Code
    String rateStructureCode = "";
    boolean recordFound = false;

    // Updated SQL Query to fetch Rate Structure for IGST
    String query = "SELECT DISTINCT PR.MSPSTRCD, PR.* FROM MSPRTSTR PR " +
                   "INNER JOIN MPURRATE T1 ON PR.MSPRTCD = T1.MprRTCOD AND T1.MprTaxTyp IN ('L') " +
                   "WHERE PR.MSPTYPE = 'P' " +
                   "AND PR.MSPRTVAL = ?"; // Using PreparedStatement to prevent SQL Injection

    // Log the SQL Query being executed
    Library.showQueryLogging("Executing Query: " + query + " with GST Percentage: " + gstPercentage);

    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        // Set the GST percentage parameter in the query
        preparedStatement.setString(1, gstPercentage);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (!resultSet.isBeforeFirst()) { 
                Library.dialogErrorMessageLog("No matching Rate Structure found for GST Percentage: " + gstPercentage);
            } else {
                Library.massagelog("Query executed successfully. Processing results...");
                while (resultSet.next()) {
                    rateStructureCode = resultSet.getString("MSPSTRCD");
                    gstPercentage = resultSet.getString("MSPRTVAL");
                    recordFound = true;
                    Library.dialogMassageLog("Found Rate Structure: Code = " + rateStructureCode + " for GST Percentage: " + gstPercentage);
                    return rateStructureCode;
                }
            }
        }
    } catch (SQLException e) {
        Library.dialogErrorMessageLog("SQL Exception while fetching Rate Structure for GST Percentage '" + gstPercentage + "': " + e.getMessage());
    }

    // If no record was found, execute fallback query
    query = "SELECT DISTINCT PR.MSPSTRCD, PR.* FROM MSPRTSTR PR " +
            "INNER JOIN MPURRATE T1 ON PR.MSPRTCD = T1.MprRTCOD AND T1.MprTaxTyp IN ('L') " +
            "WHERE PR.MSPTYPE = 'P'";
    step = Library.logStepMessage(step, "Executing Fallback Query: " + query);

    if (!recordFound) {
        try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) { 
                    Library.dialogErrorMessageLog("No Rate Structure found in fallback query for GST Percentage: " + gstPercentage);
                } else {
                    Library.massagelog("Fallback query executed successfully. Processing results...");
                    while (resultSet.next()) {
                        rateStructureCode = resultSet.getString("MSPSTRCD");
                        recordFound = true;
                        gstPercentage = resultSet.getString("MSPRTVAL");
                        Library.dialogMassageLog("Fallback Found Rate Structure: Code = " + rateStructureCode + " for GST Percentage: " + gstPercentage);
                        return rateStructureCode;
                    }
                }
            }
        } catch (SQLException eg) {
            Library.dialogErrorMessageLog("SQL Exception in fallback query for GST Percentage '" + gstPercentage + "': " + eg.getMessage());
        }
    }

    // Return the Rate Structure Code if found, otherwise return an empty string
    return rateStructureCode;
}


/**@author Ankush Agle at 27-02-2025 18:11
 * Validates the vendor code, rate structure code, and ensures the correct GST type (IGST or CGST/SGST)
 * based on whether the vendor's state code matches the site's state code.
 *
 * @param siteCode           The site code to validate against the vendor's state code.
 * @param vendorCode         The vendor code to validate.
 * @param rateStructureCode  The rate structure code to validate.
 * @return                   Returns `true` if the rate structure type matches the expected GST type
 *                           (IGST for different states, CGST/SGST for the same state), otherwise returns `false`.
 */

public boolean validateVendorAndRateStructure(String siteCode, String vendorCode, String rateStructureCode) {
    // Step 1: Validate Vendor Code
    // Check if the vendor code exists in the MVNDMAST table.
    if (!isVendorCodePresent(vendorCode)) {
        Library.errorMassageLogRedColor("Vendor Code '" + vendorCode + "' is not valid.");
        return false; // Return false if the vendor code is invalid.
    }

    // Step 2: Validate Rate Structure Code
    // Check if the rate structure code exists in the MSPRTSTR table.
    if (!isRateStructurePresent(rateStructureCode)) {
        Library.errorMassageLogRedColor("Rate Structure Code '" + rateStructureCode + "' is not valid.");
        return false; // Return false if the rate structure code is invalid.
    }

    // Step 3: Fetch Site State Code
    // Retrieve the state code for the given site code from the MLOCMST table.
    String siteStateCode = getLoginSiteStateCodeFromAdminDatabase(siteCode);
    if (siteStateCode == null) {
        Library.errorMassageLogRedColor("Unable to fetch State Code for Site Code '" + siteCode + "'.");
        return false; // Return false if the site state code cannot be fetched.
    }

    // Step 4: Fetch Vendor State Code
    // Retrieve the state code for the given vendor code from the MVNDMAST table.
    String vendorStateCode = getVendorStateCodeFromCompanyDatabase(vendorCode);
    if (vendorStateCode == null) {
        Library.errorMassageLogRedColor("Unable to fetch State Code for Vendor Code '" + vendorCode + "'.");
        return false; // Return false if the vendor state code cannot be fetched.
    }

    // Step 5: Determine GST Type Based on State Codes
    if (!siteStateCode.equals(vendorStateCode)) {
        // If the site state code and vendor state code are different, validate IGST type.
        Library.massagelog("Site State Code and Vendor State Code are different. Validating IGST type for Rate Structure Code: " + rateStructureCode);
        String igstRateStructure = getRateStructureFromDatabaseIGST(rateStructureCode);
        if (igstRateStructure != null && !igstRateStructure.isEmpty()) {
            Library.massagelog("Rate Structure Code '" + rateStructureCode + "' is valid for IGST.");
            return true; // Return true if the rate structure is valid for IGST.
        } else {
            Library.errorMassageLogRedColor("Rate Structure Code '" + rateStructureCode + "' is not valid for IGST.");
            return false; // Return false if the rate structure is not valid for IGST.
        }
    } else {
        // If the site state code and vendor state code are the same, validate CGST/SGST type.
        Library.massagelog("Site State Code and Vendor State Code are the same. Validating CGST/SGST type for Rate Structure Code: " + rateStructureCode);
        String cgstSgstRateStructure = getRateStructureFromDatabaseCGSTSGST(rateStructureCode);
        if (cgstSgstRateStructure != null && !cgstSgstRateStructure.isEmpty()) {
            Library.massagelog("Rate Structure Code '" + rateStructureCode + "' is valid for CGST/SGST.");
            return true; // Return true if the rate structure is valid for CGST/SGST.
        } else {
            Library.errorMassageLogRedColor("Rate Structure Code '" + rateStructureCode + "' is not valid for CGST/SGST.");
            return false; // Return false if the rate structure is not valid for CGST/SGST.
        }
    }
}
/**
 * Validates if a given vendor code exists in the MVNDMAST table.
 * 
 * @param vendorCode The vendor code to check for existence.
 *                   - This should match the MVmVndCode column in the MVNDMAST table.
 *                   - Example: "M047"
 * @return true if the vendor code exists, false otherwise.
 * 
 * Usage Example:
 *     boolean isVendorValid = isVendorCodePresent("M047");
 *     System.out.println("Is Vendor Present: " + isVendorValid);
 */
public boolean isVendorCodePresent(String vendorCode) {
    // SQL query to check if the vendor code exists in the MVNDMAST table
    String query = "SELECT TOP  1 *  FROM MVNDMAST WHERE MVmVndCode ='"+vendorCode+"'";

    // Using try-with-resources to ensure the PreparedStatement and ResultSet are closed automatically
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        
      
        // Execute the query and store the result in ResultSet
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            
            // If a record exists, return true
            return resultSet.next();
        }
    } catch (SQLException e) {
        // Log the error message in a standardized format
        Library.errorMassageLogRedColor("Error validating Vendor Code '" + vendorCode + "': " + e.getMessage());
    }

    // Return false if no matching vendor is found or an error occurs
    return false;
}


/**
 * Validates if a given item code exists in the MITMMAST table.
 * 
 * @param itemCode The item code to check for existence.
 *                 - This should match the MIMITMICOD column in the MITMMAST table.
 *                 - Example: "AT_ITEM_HAI_10"
 * @return true if the item code exists, false otherwise.
 * 
 * Usage Example:
 *     boolean isItemValid = isItemMasterCreated("AT_ITEM_HAI_10");
 *     System.out.println("Is Item Present: " + isItemValid);
 */
public boolean isItemMasterCreated(String itemCode) {
    // SQL query to check if the item code exists in the MITMMAST table
    String query = "SELECT TOP 1 * FROM MITMMAST WHERE MIMITMICOD = '" + itemCode + "'";

    // Using try-with-resources to ensure the PreparedStatement and ResultSet are closed automatically
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {

        // Execute the query and store the result in ResultSet
        try (ResultSet resultSet = preparedStatement.executeQuery()) {

            // If a record exists, return true
            return resultSet.next();
        }
    } catch (SQLException e) {
        // Log the error message in a standardized format
        Library.errorMassageLogRedColor("Error validating Item Code '" + itemCode + "': " + e.getMessage());
    }

    // Return false if no matching item is found or an error occurs
    return false;
}


/**
 * Retrieves the state code (MVmVStaCod) for a given vendor code (MVmVndCode) from the company database.
 * 
 * @param vendorCode The vendor code for which the state code needs to be fetched.
 *                   - This should match the MVmVndCode column in the MVNDMAST table.
 *                   - Example: "M047"
 * @return The corresponding vendor state code (MVmVStaCod) if found; otherwise, returns null.
 * 
 * Usage Example:
 *     String vendorState = getVendorStateCodeFromCompanyDatabase("M047");
 *     System.out.println("Vendor State Code: " + vendorState);
 */
public String getVendorStateCodeFromCompanyDatabase(String vendorCode) {
    // SQL query to fetch the vendor state code (MVmVStaCod) from MVNDMAST
    String query = "SELECT MVmVStaCod FROM MVNDMAST WHERE MVmVndCode ='"+vendorCode+"'";

    
    Library.showQueryLogging(query);
    
    // Using try-with-resources to ensure the PreparedStatement and ResultSet are closed automatically
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        // Execute the query and store the result in ResultSet
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            
            // Check if a matching record exists
            if (resultSet.next()) {
                // Return the fetched Vendor State Code (MVmVStaCod)
            	
            	Library.dialogMassageLog(vendorCode+" State :"+resultSet.getString("MVmVStaCod"));
                return resultSet.getString("MVmVStaCod");
            }
        }
    } catch (SQLException e) {
        // Print stack trace for debugging purposes
        e.printStackTrace();
        
        // Log the error message with a standardized automation test engineer message format
        Library.errorMassageLogRedColor("Error fetching Vendor State Code for vendor '" + vendorCode + "': " + e.getMessage());
    }

    // Return null if no matching vendor is found or an error occurs
    return null;
}

/**
 * Fetches a list of vendor codes where the vendor's state code does not match the site's state code.
 * 
 * @param siteCode The site code for which vendor filtering is performed.
 *                 - This should match the MLOCCODE column in the MLOCMST table.
 *                 - Example: "CT2"
 * @return A list of vendor codes that have a state code different from the site's state code.
 * 
 * Usage Example:
 *     List<String> vendorList = getVendorsWithDifferentState("CT2");
 *     System.out.println("Vendors with different state: " + vendorList);
 */
public List<String> getVendorsListWithDifferentStateThanLoginSiteState(String siteCode) {
    // Initialize the list to store vendor codes
    List<String> vendorCodeList = new ArrayList<>();

    // Fetch the state code of the given site from the Admin Database
    String siteStateCode = getLoginSiteStateCodeFromAdminDatabase(siteCode);

    // SQL query to fetch vendor state codes (MVmVStaCod) and vendor codes (MVmVndCode) where the currency is INR
    String query = "SELECT MVmVStaCod, MVmVndCode FROM MVNDMAST WHERE MVmCurCod = 'INR'";

    // Using try-with-resources to ensure PreparedStatement and ResultSet are closed automatically
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        System.out.println("Fetching vendors where state code is different from site state code...");

        // Iterate through the result set
        while (resultSet.next()) {
            String vendorStateCode = resultSet.getString("MVmVStaCod");
            String vendorCode = resultSet.getString("MVmVndCode");

            // Check if the vendor's state code is different from the site's state code
            if (!vendorStateCode.equalsIgnoreCase(siteStateCode)) {
                // Add vendor code to the list
                vendorCodeList.add(vendorCode);
            }
        }

    } catch (SQLException e) {
        // Print stack trace for debugging purposes
        e.printStackTrace();
        
        // Log the error message with a standardized format
        Library.errorMassageLogRedColor("Error fetching vendors with different state for site '" + siteCode + "': " + e.getMessage());
    }

    Library.dialogMassageLog(vendorCodeList.toString());
    // Return the list of vendor codes
    return vendorCodeList;
}

/**
 * Fetches a list of vendor codes where the vendor's state code matches the site's state code.
 *
 * @param siteCode The site code for which vendor filtering is performed.
 *                 - This should match the MLOCCODE column in the MLOCMST table.
 *                 - Example: "CT2"
 * @return A list of vendor codes that have the same state code as the site's state code.
 *
 * Usage Example:
 * 
 *     List<String> vendorList = getVendorsWithSameState("CT2");
 *     System.out.println("Vendors with same state: " + vendorList);
 */
public List<String> getVendorsListWithSameStateAsLoginSite(String siteCode) {
    // Initialize the list to store vendor codes
    List<String> vendorCodeList = new ArrayList<>();

    // Fetch the state code of the given site from the Admin Database
    String siteStateCode = getLoginSiteStateCodeFromAdminDatabase(siteCode);

    // SQL query to fetch vendor state codes (MVmVStaCod) and vendor codes (MVmVndCode) where the currency is INR
    String query = "SELECT MVmVStaCod, MVmVndCode FROM MVNDMAST WHERE MVmCurCod = 'INR'";

    // Using try-with-resources to ensure PreparedStatement and ResultSet are closed automatically
    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        System.out.println("Fetching vendors where state code matches the site state code...");

        // Iterate through the result set
        while (resultSet.next()) {
            String vendorStateCode = resultSet.getString("MVmVStaCod");
            String vendorCode = resultSet.getString("MVmVndCode");

            // Check if the vendor's state code matches the site's state code
            if (vendorStateCode.equalsIgnoreCase(siteStateCode)) {
                // Add vendor code to the list
                vendorCodeList.add(vendorCode);
            }
        }
    } catch (SQLException e) {
        // Print stack trace for debugging purposes
        e.printStackTrace();
        
        // Log the error message with a standardized format
        Library.errorMassageLogRedColor("Error fetching vendors with same state for site '" + siteCode + "': " + e.getMessage());
    }

    // Return the list of vendor codes
    
    Library.dialogMassageLog(vendorCodeList.toString());
    return vendorCodeList;
}


/**
 * @Date 15/02/2024
 * Inserts a new Code Master Entry into the database.
 *
 * @param codeType The type of code to insert.
 * @param codeValue The code value to insert.
 * @param codeDescription The description of the code.
 * @return true if the insertion is successful, false otherwise.
 *
 * @Example
 * boolean inserted = addCodeMasterEntry("BT", "GV", "Government Business");
 * System.out.println("Code Master Entry Inserted: " + inserted);
 */
public boolean addCodeMasterEntry(String codeType, String codeValue, String codeDescription) {
    String query = "DECLARE @p10 INT; SET @p10=0; " +
                   "EXEC CSP_MCDSMAST_INSERT @MCDCODETYP='"+codeType+"', @MCDCODEVAL='"+codeValue+"', @MCDCODEDES='"+codeDescription+"', " +
                   "@MCDDIGAFTDEC=0, @MCDLOCCODE=0, @MCDCREUSR=0, @MCDGSTUQC=NULL, @MCDGSTTR=NULL, " +
                   "@MCDMRPER=0, @ErrorCode=@p10 OUTPUT; SELECT @p10";

    Library.showQueryLogging("Executing Stored Procedure CSP_MCDSMAST_INSERT with Code Type: " + codeType + ", Code Value: " + codeValue + ", Code Description: " + codeDescription);

    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {

   

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                Library.massagelog("Successfully inserted Code Master Entry for Code Type: " + codeType + " and Code Value: " + codeValue);
                return true;
            }
        }
        
        catch (SQLException e) {
            Library.dialogErrorMessageLog("SQL Exception while inserting Code Master Entry for Code Type '" + codeType + "' and Code Value '" + codeValue + "': " + e.getMessage());
        }
    } catch (SQLException e) {
        Library.dialogErrorMessageLog("SQL Exception while inserting Code Master Entry for Code Type '" + codeType + "' and Code Value '" + codeValue + "': " + e.getMessage());
    }
    return false;
}




/**
 * Checks the MipIndtReq flag for the given Item Code in the database.
 * If the flag is '0', it updates it to '1'.
 *
 * @param itemCode The Item Code to check and update.
 *                - Example: "ITEM123"
 * @return true if the flag was already '1' or successfully updated, false if an error occurs.
 *@author  Ankush Agle at 14-02-2025 14:38
 * Usage Example:
 *     boolean status = checkAndUpdateMipIndtReq("ITEM123");
 *     System.out.println("MipIndtReq status updated: " + status);
 */
public boolean checkAndUpdateMipIndtReq(String itemCode) {
    // SQL query to fetch the current MipIndtReq status
    String selectQuery = "SELECT MipIndtReq FROM MITMPURMAST WHERE MipItemCode = ?";
    
    // SQL query to update MipIndtReq to '1' if it is '0'
    String updateQuery = "UPDATE MITMPURMAST SET MipIndtReq = '1' WHERE MipItemCode = ? AND MipIndtReq = '0'";

    // Log the query execution
    Library.showQueryLogging("Executing Query: " + selectQuery + " with Item Code: " + itemCode);

    try (PreparedStatement selectStmt = companyConnection.prepareStatement(selectQuery)) {
        // Set the parameter for the select query
        selectStmt.setString(1, itemCode);

        // Execute the query
        try (ResultSet resultSet = selectStmt.executeQuery()) {
            if (resultSet.next()) {
                String mipIndtReq = resultSet.getString("MipIndtReq");

                // If the flag is already '1', return true
                if ("1".equals(mipIndtReq)) {
                    Library.massagelog("MipIndtReq is already '1' for Item Code: " + itemCode);
                    return true;
                }
            }
        }
    } catch (SQLException e) {
        // Log the SQL exception
        Library.errorMassageLogRedColor("SQL Exception while checking MipIndtReq for Item Code '" + itemCode + "': " + e.getMessage());
        return false;
    }

    // Perform the update if needed
    Library.showQueryLogging("Updating MipIndtReq to '1' for Item Code: " + itemCode);

    try (PreparedStatement updateStmt = companyConnection.prepareStatement(updateQuery)) {
        // Set the parameter for the update query
        updateStmt.setString(1, itemCode);

        // Execute the update query
        int rowsUpdated = updateStmt.executeUpdate();
        
        if (rowsUpdated > 0) {
            Library.massagelog("MipIndtReq updated to '1' for Item Code: " + itemCode);
            return true;
        } else {
            Library.massagelog("No update needed for MipIndtReq. Already '1' or Item Code not found: " + itemCode);
        }
    } catch (SQLException e) {
        // Log the SQL exception
        Library.errorMassageLogRedColor("SQL Exception while updating MipIndtReq for Item Code '" + itemCode + "': " + e.getMessage());
    }
    
    return false;
}

/**
 * @Date 15/02/2024
 * Checks if an Opening Balance Entry exists for the given Item Code and Warehouse Code.
 *
 * @param itemCode The item code to check.
 * @param warehouseCode The warehouse code to check.
 * @return true if an Opening Balance Entry exists, false otherwise.
 *
 * @Example
 * boolean exists = isOpeningBalanceEntryCreatedForItemCheck("ITEM_FO_REG71", "WH123");
 * System.out.println("Opening Balance Entry Exists: " + exists);
 */
public boolean isOpeningBalanceEntryCreatedForItemCheck(String itemCode, String warehouseCode) {
    String query = "SELECT TOP 1 MWMWhCode, XODITMCD FROM XOPNDTL INNER JOIN MWARMAST " +
                   "ON MWARMAST.MWMWhId = XOPNDTL.XODWHID " +
                   "WHERE XODITMCD = ? AND MWMWhCode = ? ORDER BY XODOPNID";

    Library.showQueryLogging("Executing Query: " + query + " with Item Code: " + itemCode + " and Warehouse Code: " + warehouseCode);

    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        preparedStatement.setString(1, itemCode);
        preparedStatement.setString(2, warehouseCode);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next();
        }
        
        catch (SQLException e) {
            Library.dialogErrorMessageLog("SQL Exception while checking Opening Balance Entry for Item '" + itemCode + "' and Warehouse '" + warehouseCode + "': " + e.getMessage());
        }
    } catch (SQLException e) {
        Library.dialogErrorMessageLog("SQL Exception while checking Opening Balance Entry for Item '" + itemCode + "' and Warehouse '" + warehouseCode + "': " + e.getMessage());
    }
    return false;
}

/**
 * Checks if a Rate Structure exists for the given Rate Structure Code in the database.
 *
 * @param rateStructure The Rate Structure Code to check.
 *                      - Example: "RST123"
 * @return true if the Rate Structure exists, false otherwise.
 *
 * Usage Example:
 *     boolean isRateStructurePresent = isRateStructurePresent("RST123");
 *     System.out.println("Is Rate Structure Present: " + isRateStructurePresent);
 */
public boolean isRateStructurePresent(String rateStructure) {
    // SQL query to check if the Rate Structure Code exists in MSPSTRCD column
    String query = "SELECT TOP 1 * FROM MSPRTSTR WHERE MSPSTRCD ='"+rateStructure+"'"; // Optimized query to check existence

    // Log the query execution
    Library.showQueryLogging("Executing Query: " + query + " with Rate Structure: " + rateStructure);

    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        // Set the Rate Structure parameter in the query

        // Execute the query and store the result in ResultSet
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            // Return true if a record is found
            return resultSet.next();
        }
    } catch (SQLException e) {
        // Log the SQL exception
        Library.errorMassageLogRedColor("SQL Exception while checking Rate Structure for Code '" + rateStructure + "': " + e.getMessage());
    }
    
    // Return false if no matching record is found or an error occurs
    return false;
}


/*
 * CGST (Central GST) - Applied on intra-state sales, collected by the Central Govt.
 * SGST (State GST) - Applied on intra-state sales, collected by the State Govt.
 * IGST (Integrated GST) - Applied on inter-state sales, collected by the Central Govt.
 * TCS (Tax Collected at Source) - Extra tax collected by sellers on e-commerce or specific transactions.
 */

/**
 * Fetches the Rate Structure Code for CGST/SGST based on the given GST percentage.
 *
 * @param gstPercentage The GST percentage for which the Rate Structure Code is fetched.
 *                      - Example: "18"
 * @return The Rate Structure Code if found; otherwise, returns an empty string.
 *
 *Usage Example:
 *     String rateStructure = getRateStructureFromDatabaseCGSTSGST("18");
 *     System.out.println("Rate Structure Code: " + rateStructure);
 *
 */
public String getRateStructureFromDatabaseCGSTSGST(String gstPercentage) {
    int step = 1;

    // Log the purpose of this function execution
    String message = "Fetching Rate Structure Code for CGST/SGST with GST Percentage: " + gstPercentage;
    step = Library.logStepMessage(step, message);

    // Initialize variable to store the retrieved Rate Structure Code
    String rateStructureCode = "";
    boolean recordFound = false;

    // Updated SQL Query to fetch Rate Structure for CGST/SGST
    String query = "SELECT DISTINCT PR.MSPSTRCD, PR.* FROM MSPRTSTR PR " +
                   "INNER JOIN MPURRATE T1 ON PR.MSPRTCD = T1.MprRTCOD AND T1.MprTaxTyp IN ('M','N') " +
                   "WHERE PR.MSPTYPE = 'P' " +
                   "AND PR.MSPRTVAL = ?"; // Using PreparedStatement to prevent SQL Injection

    // Log the SQL Query being executed
    Library.showQueryLogging("Executing Query: " + query + " with GST Percentage: " + gstPercentage);

    try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
        // Set the GST percentage parameter in the query (dividing by 2 as per original logic)
        preparedStatement.setDouble(1, Double.parseDouble(gstPercentage) / 2);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (!resultSet.isBeforeFirst()) { 
                Library.dialogErrorMessageLog("No matching Rate Structure found for GST Percentage: " + gstPercentage);
            } else {
                Library.massagelog("Query executed successfully. Processing results...");
                while (resultSet.next()) {
                    rateStructureCode = resultSet.getString("MSPSTRCD");
                    gstPercentage = resultSet.getString("MSPRTVAL");
                    recordFound = true;
                    Library.dialogMassageLog("Found Rate Structure: Code = " + rateStructureCode + " for GST Percentage: " + gstPercentage);
                    return rateStructureCode;
                }
            }
        }
    } catch (SQLException e) {
        Library.dialogErrorMessageLog("SQL Exception while fetching Rate Structure for GST Percentage '" + gstPercentage + "': " + e.getMessage());
    }

    // If no record was found, execute fallback query
    query = "SELECT DISTINCT PR.MSPSTRCD, PR.* FROM MSPRTSTR PR " +
            "INNER JOIN MPURRATE T1 ON PR.MSPRTCD = T1.MprRTCOD AND T1.MprTaxTyp IN ('M','N') " +
            "WHERE PR.MSPTYPE = 'P'";
    step = Library.logStepMessage(step, "Executing Fallback Query: " + query);

    if (!recordFound) {
        try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) { 
                    Library.dialogErrorMessageLog("No Rate Structure found in fallback query for GST Percentage: " + gstPercentage);
                } else {
                    Library.massagelog("Fallback query executed successfully. Processing results...");
                    while (resultSet.next()) {
                        rateStructureCode = resultSet.getString("MSPSTRCD");
                        recordFound = true;
                        gstPercentage = resultSet.getString("MSPRTVAL");
                        Library.dialogMassageLog("Fallback Found Rate Structure: Code = " + rateStructureCode + " for GST Percentage: " + gstPercentage);
                        return rateStructureCode;
                    }
                }
            }
        } catch (SQLException eg) {
            Library.dialogErrorMessageLog("SQL Exception in fallback query for GST Percentage '" + gstPercentage + "': " + eg.getMessage());
        }
    }

    // Return the Rate Structure Code if found, otherwise return an empty string
    return rateStructureCode;
}
/**
 * @author  Created By Ankush Agle at 15-04-2025 16:49
 * Fetches the Work Order number based on the given year and SJO (Sales Job Order) number.
 *
 * @param year The financial year of the Work Order.
 *             - This corresponds to the XWOYR column in the XWOHDR table.
 *             - Example: "25-26"
 *
 * @param sjoNumber The SJO number used to fetch the related Work Order.
 *                  - This corresponds to the XSHSJONO column in the XSJOHDR table.
 *                  - Example: "000101"
 *
 * @return The Work Order number (XWONO) associated with the provided year and SJO number.
 *         - Returns null if no matching Work Order is found.
 *
 * Usage Example:
 *     String workOrder = returnWorkOrderDocumentNumberForSjoNumber("25-26", "000101");
 *     System.out.println("Work Order Number: " + workOrder);
 *
 * Notes:
 *     - Sample Full SJO Number Format: "25-26/SJ/PUN/000101"
 *     - This method only matches using Year ("25-26") and SJO Number ("000101")
 */



public String returnWorkOrderDocumentNumberForSjoNumber(String year, String sjoNumber) {
    String workOrderNumber = "";
    Connection connection = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        String logMessage = String.format("Fetching Work Order for Year: %s and SJO Number: %s", year, sjoNumber);
        Library.dialogSkipMessageLog(logMessage);

        connection = companyConnection;

        String sql = "SELECT TOP 1 H.XWONO " +
                     "FROM XWOHDR H " +
                     "INNER JOIN XWOSJODTL D ON D.XWSREFID = H.XWOAUTOID " +
                     "INNER JOIN XSJOHDR S ON S.XSHSJAUTONO = D.XWSSJOID " +
                     "INNER JOIN VWMLOCMST SJOLOC ON SJOLOC.MLOCID = S.XSHSJOFRLOCCD " +
                     "WHERE H.XWOYR = ? AND S.XSHSJONO = ? " +
                     "ORDER BY H.CREUSRDT DESC";

        pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, year.trim());       // e.g., "25-26"
        pstmt.setString(2, sjoNumber.trim());  // e.g., "000101"

        rs = pstmt.executeQuery();
        if (rs.next()) {
            workOrderNumber = rs.getString("XWONO");
            Library.dialogSkipMessageLog("Found Work Order Number: " + workOrderNumber);
        } else {
            Library.dialogErrorMessageLog("No Work Order found for SJO Number: " + sjoNumber);
        }

    } catch (Exception e) {
        Library.dialogErrorMessageLog("Error fetching Work Order: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (connection != null) connection.close();
        } catch (SQLException ex) {
            Library.dialogErrorMessageLog("Error closing resources: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    return workOrderNumber;
}

}

