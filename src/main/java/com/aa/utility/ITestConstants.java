
package com.aa.utility;

import java.util.HashMap;
import java.util.Map;

public interface ITestConstants extends  IEntryConstants {
     public String resultexcelpath = System.getProperty("user.dir") + "\\ResultExcel\\RESULT.xlsx";
	 public static final String DoYouWantsToResultInExcelSheet="YES";
	 
		static String jsonFilepath = System.getProperty("user.dir") + "\\JSONFiles\\loader.json";

	 String NewSheet="Result";
	    public  Map<String, String> testCaseIdMap = new HashMap<>(); // Map to store test case IDs

    public ExcelReader EXCELREADER=new ExcelReader(resultexcelpath, DoYouWantsToResultInExcelSheet);
	public static final String TR_ROW_TAG_OPEN_VAR="<tr>";
	public static final String TR_ROW_TAG_CLOSE_VAR="</tr>";
	public static final String TABLETAG_TAG_CLOSE_VAR="</table>";
	public static final String ORDERED_TAG_OPEN_VAR="<ol>";
	public static final String ORDERED_TAG_CLOSE_VAR="</ol>";
	public static final String NAVIGATION_URL = "";
	public static final long MAXTIMEOUTVAR=1800000;
	
	public static final boolean DO_YOU_WANTS_TO_ADD_EXPANDED_MENU_REPORT=false;

	public static final boolean DO_YOU_WANTS_TO_ADD_NAVIGATION_SCREENSHOT_TO_REPORT=false;
    public static final boolean DO_YOU_WANTS_TO_SKIP_GRN_WITHOUT_PO_ENTRY=false;
	public static final boolean DO_YOU_WANTS_TO_RECORD_TEST_CASE_LEVEL_VIDEO=false;
	public static final boolean DO_YOU_WANTS_TO_ATTACH_TEST_CASE_LEVEL_VIDEO_TO_REPORT=false;
	public static final boolean DO_YOU_WANTS_TO_CAPTURE_VIEW_ENTRY_SCREENSHOT_OF_EACH_TEST_SCRIPT=false;
    public static final boolean DO_YOU_WANTS_TO_LOG_RECORD_TABLE_IN_REPORT=false;

	 static    String HeightWidth = JSONUtils.getValueFromJSON(jsonFilepath, "heightWidth");
	//ExcelReader Excel=new ExcelReader("");
	String DISPLAY_TOTAL_EXCECUTION_TIME="YES";
	String pdffile1 = System.getProperty("user.dir") + "\\UploadFiles\\pdfFileNo_1.pdf";
	String AllFlag = System.getProperty("user.dir") + "\\UploadFiles\\AllFlag.pdf";
    String pdffile2 = System.getProperty("user.dir") + "\\UploadFiles\\pdfFileNo_2.pdf";

	String docFile1 = System.getProperty("user.dir") + "\\UploadFiles\\docFileNo_1.doc";
	String docFile2 = System.getProperty("user.dir") + "\\UploadFiles\\docFileNo_2.doc";

	String docxFile1 = System.getProperty("user.dir") + "\\UploadFiles\\docxFileNo_1.docx";
	String docxFile2 = System.getProperty("user.dir") + "\\UploadFiles\\docxFileNo_2.docx";

	String dwgFile1 = System.getProperty("user.dir") + "\\UploadFiles\\dwg_File_No_1.dwg";
	String dwgFile2 = System.getProperty("user.dir") + "\\UploadFiles\\dwg_File_No_2.dwg";

	String excelXlsFile1 = System.getProperty("user.dir") + "\\UploadFiles\\Excel_xls_File_NO_1.xls";
	String excelXlsFile2 = System.getProperty("user.dir") + "\\UploadFiles\\Excel_xls_File_NO_2.xls";

	String excelXlsxFile1 = System.getProperty("user.dir") + "\\UploadFiles\\EXCEL_xlsx_file_No_1.xlsx";
	String excelXlsxFile2 = System.getProperty("user.dir") + "\\UploadFiles\\EXCEL_xlsx_file_No_2.xlsx";

	String jpegFile1 = System.getProperty("user.dir") + "\\UploadFiles\\jpeg_FileNo_1.jpeg";
	String jpegFile2 = System.getProperty("user.dir") + "\\UploadFiles\\jpeg_FileNo_2.jpeg";

	String jpgFile1 = System.getProperty("user.dir") + "\\UploadFiles\\jpgFileNo_1.jpg";
	String jpgFile2 = System.getProperty("user.dir") + "\\UploadFiles\\jpgFileNo_2.jpg";

	String pngFile1 = System.getProperty("user.dir") + "\\UploadFiles\\pnfFileNo_1.png";

	String rarFile1 = System.getProperty("user.dir") + "\\UploadFiles\\rarFileNo_1.rar";
	String rarFile2 = System.getProperty("user.dir") + "\\UploadFiles\\rarFileNo_2.rar";

	String zipFile1 = System.getProperty("user.dir") + "\\UploadFiles\\zipFileNo_1.zip";
	String zipFile2 = System.getProperty("user.dir") + "\\src.zip";
	String javacode= System.getProperty("user.dir") + "\\src.zip";

	String MachineImage = System.getProperty("user.dir") + "\\UploadFiles\\MachineImage.jpg";
	String WindingDirectionImage = System.getProperty("user.dir") + "\\UploadFiles\\WindingDirectionImage.jpg";
	String GADrawingImage = System.getProperty("user.dir") + "\\UploadFiles\\GADrawingImage.jpg";

	String[] filePaths = {
		    System.getProperty("user.dir") + "\\UploadFiles\\pdfFileNo_2.pdf",
		    System.getProperty("user.dir") + "\\UploadFiles\\docFileNo_1.doc",
		    System.getProperty("user.dir") + "\\UploadFiles\\docFileNo_2.doc",
		    System.getProperty("user.dir") + "\\UploadFiles\\docxFileNo_1.docx",
		    System.getProperty("user.dir") + "\\UploadFiles\\docxFileNo_2.docx",
		    System.getProperty("user.dir") + "\\UploadFiles\\dwg_File_No_1.dwg",
		    System.getProperty("user.dir") + "\\UploadFiles\\dwg_File_No_2.dwg",
		    System.getProperty("user.dir") + "\\UploadFiles\\Excel_xls_File_NO_1.xls",
		    System.getProperty("user.dir") + "\\UploadFiles\\Excel_xls_File_NO_2.xls",
		    System.getProperty("user.dir") + "\\UploadFiles\\EXCEL_xlsx_file_No_1.xlsx",
		    System.getProperty("user.dir") + "\\UploadFiles\\EXCEL_xlsx_file_No_2.xlsx",
		    System.getProperty("user.dir") + "\\UploadFiles\\jpeg_FileNo_1.jpeg",
		    System.getProperty("user.dir") + "\\UploadFiles\\jpeg_FileNo_2.jpeg",
		    System.getProperty("user.dir") + "\\UploadFiles\\jpgFileNo_1.jpg",
		    System.getProperty("user.dir") + "\\UploadFiles\\jpgFileNo_2.jpg",
		    System.getProperty("user.dir") + "\\UploadFiles\\pnfFileNo_1.png",
		    System.getProperty("user.dir") + "\\UploadFiles\\rarFileNo_1.rar",
		    System.getProperty("user.dir") + "\\UploadFiles\\rarFileNo_2.rar",
		    System.getProperty("user.dir") + "\\UploadFiles\\zipFileNo_1.zip",
		    System.getProperty("user.dir") + "\\UploadFiles\\zipFileNo_2.zip"
		};
	
	
	String pdffilepath[]= {
			   System.getProperty("user.dir") + "\\UploadFiles\\pdfFileNo_1.pdf",
			   System.getProperty("user.dir") + "\\UploadFiles\\pdfFileNo_2.pdf"
	};

}






