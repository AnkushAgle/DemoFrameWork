

package com.aa.utility;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IEntryConstants {
	

	
	
	enum TestEnum {
	 //commnet is Added On This
		
		LOGINSHEET,SITECODE,USERNAME;
		
		
	}
	
	public static final String DISPLAY_TOTAL_EXECUTION_TIME="YES";
	  public static final String FINANCIAL_YEAR_IN_CURRENT_DB="25-26";
    public static final boolean REQUIRED_DYNAMIC_DATA=false;
    public static final boolean DO_YOU_WANTS_TO_CREATE_ITEM_MASTER_ENTRY=false;
    public static final boolean DO_YOU_WANTS_TO_CREATE_WAREHOUSE_INVENTORY_ENTRY=false;
    public static final boolean DO_YOU_WANTS_TO_CREATE_ITEM_MASTER_PURCHASE_ENTRY=false;
    public static final boolean DO_YOU_WANTS_TO_CREATE_OPENNING_BALANCE_ENTRY=false;
    public static final boolean DO_YOU_WANTS_TO_CREATE_ITEM_VENDOR_PURCHASE_MASTER_ENTRY=false;
    public static final boolean DO_YOU_WANTS_TO_CREATE_ITEM_VENDOR_LABOUR_PURCHASE_MASTER_ENTRY=false;
    public static final boolean DO_YOU_WANTS_TO_PRODUCT_STRUCTURE_MASTER_ENTRY=false;
    public static final boolean DO_YOU_WANTS_TO_POCESS_SHEET_MASTER_ENTRY=false;

    String FixedClassCodesRegularProduct[]= {"AUTFO","AUTAC","AUTMG"};
    String FixedSubclassesFIFO[]= {"FOINW","FOHNI","FOREG","FOBAT","FOSER"};	    	
    String FixedSubclassesAtAtcual[]= {"AAINW","AAHNI","AAREG","AABAT","AASER"};	    	
	String FixedSubclasseMovingAverage[]= {"MAINW","MAHNI","MAREG","MABAT","MASER"};	    	

	/**
	 * 
	 * 
	 * Fetches SiteID from MLOCMST table for a given SiteCode.
	 * @param siteCode The SiteCode to fetch the SiteID.
	 * @param common keys LOGIN_SHEET_PATH,username,sitecode
	 * @return SiteID as a String, or null if not found.
	 */
    // Static block to initialize GLOBAL_VARIABLES
   public static Map<String, String> GLOBAL_VARIABLES = new HashMap<>();
    
   public static EnumMap<TestEnum, String> EnumMapVar = new EnumMap<>(TestEnum.class);

    
    //below two variable is added On timeStamp  "Ankush Agle at 30-01-2025 16:54"
    // List of possible success messages
    List<String> successKeywords = Arrays.asList(
        "Labour Production Details Insertion Done", "Allocation Done", "Generated WO Receipt", "success",
        "Internal UOM and Purchase UOM", "Maximum WO Qty", "All the un-consumed extra Issues for this work",
        "Purchase Quotation Created", "GRN Details Updation Done", "Updation Done","Updated Successfully","uccess","Updated","updated"
   ,"Inserted Succesfully","ucces","Class Code already exists","Sub Class already exists"
    		
    		);

    
    
    // List of error messages
    List<String> errorKeywords = Arrays.asList(
        "The timeout period", "Vendor Rejection Insertion Failed", "Timeout expired", "Account", "daily messages limit expected",
        "CSP_", "UnhandledErrorKey", "Sequence contains no elements", "XSTKONHAND", "Cannot insert the value NULL",
        "Invalid object name", "Something went wrong", "Work Order Due", "TDS Turn Over Limit 5000000", "Invalid item code",
        "is less than MOQ", "should be in multiple of EOQ", "Indent is close or Po Qty is more than", "PKXDOCTAXDTL"
    ,"Attach","Blanket PO GRN Date should between"
    		
    		)
    		;

 // HashMap to store item nature descriptions
    Map<String, String> ITEM_NATURE_MAP = new HashMap<String, String>() {{
    	put("Code", "Nature of Item");
    	put("B", "BOM");
    	put("M", "Product");
        put("S", "Spare (Trading)");
        put("V", "Service");
        put("C", "Capital Goods");
        put("U", "Consumables");
        put("O", "Tools");
        put("E", "Maintenance Parts");
        put("N", "Stationery Items");
        put("R", "Raw Material - Scrap");
        put("X", "Miscellaneous");
    }};
    
 // Convert Keys to an Array
    String[] itemNatureKeys = ITEM_NATURE_MAP.keySet().toArray(new String[0]);

    // Convert Values to an Array
    String[] itemNatureValues = ITEM_NATURE_MAP.values().toArray(new String[0]);

}
