package main.java.core_framework;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	private String excelDataFile;
	private String excelDataSheet;
	private List<HashMap<String, String>> testData;
	private FormulaEvaluator evaluator;
	private static String executeDecider="Status";

	public ExcelUtils(String testDataFile, String testDataSheet) {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Constructor: ExcelUtils Class. ExcelFile: "+testDataFile+" | ExcelSheet: "+testDataSheet);
		this.excelDataFile=System.getProperty("user.dir")+testDataFile;
		this.excelDataSheet=testDataSheet;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	public String getCellData(Cell cell,Workbook workbook) {
		String cellDataValue="";
		
		if(cell==null)
			return cellDataValue;
		
		switch(cell.getCellType()) {
		
		case BOOLEAN:
			cellDataValue=Boolean.toString(cell.getBooleanCellValue());
			break;

		case _NONE:
		case BLANK:
		case ERROR:
			break;

		case NUMERIC:
			cellDataValue=Double.toString(cell.getNumericCellValue());
			break;

		case STRING:
			cellDataValue=cell.getStringCellValue();
			break;

		case FORMULA:
			CellValue cellValue = evaluator.evaluate(cell);
			switch(cellValue.getCellType())
			{
			case BOOLEAN:
				cellDataValue=Boolean.toString(cellValue.getBooleanValue());
				break;

			case _NONE:
			case BLANK:
			case ERROR:
				break;

			case NUMERIC:
				cellDataValue=Double.toString(cellValue.getNumberValue());
				break;

			case STRING:
				cellDataValue=cellValue.getStringValue();
				break;

			default:
				break;
			}
		}
		return cellDataValue;
	}

	public List<HashMap<String, String>> getTestData(boolean ignoreHeaderDescriptions) {

		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: ExcelUtils.getTestData()");

		FileInputStream fis = null;
		Workbook workbook = null;
		this.testData = new ArrayList<HashMap<String, String>>();
		
		try {
			//System.out.println(this.excelDataFile+"::"+this.excelDataSheet);
			fis = new FileInputStream(this.excelDataFile);
			workbook = new XSSFWorkbook(fis);
			evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			Sheet sheet = workbook.getSheet(this.excelDataSheet);
			int lastRow = sheet.getLastRowNum();
			Row firstRow= sheet.getRow(0);
			int i=1;
			if(ignoreHeaderDescriptions)
				i=2;
			for(; i<=lastRow; i++){

				HashMap<String, String> dataMap=new HashMap<String, String>();
				Row row = sheet.getRow(i);
				//System.out.println(i+"::"+row);
				int lastCell=firstRow.getLastCellNum();
				for(int j=0;j<lastCell;j++)
				{
					Cell dataCell=row.getCell(j);
					Cell headerCell=firstRow.getCell(j);
					String header=getCellData(headerCell,workbook);
					String value=getCellData(dataCell,workbook);
					dataMap.put(header.trim(), value);
				}
				if(dataMap.get(executeDecider)!=null && (dataMap.get(executeDecider).equalsIgnoreCase("Y") || dataMap.get(executeDecider).equalsIgnoreCase("Yes")))
				{
					this.testData.add(dataMap);
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			ReportingUtils.failLog("Error while reading test data file. File Path: "+this.excelDataFile+" | Sheet Name: "+this.excelDataSheet+". Error Details: "+e,null);

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.ERROR, "Error while reading test data file. File Path: "+this.excelDataFile+" | Sheet Name: "+this.excelDataSheet+". Error Details: "+sw.toString());
		}
		finally
		{
			try
			{
				workbook.close();
				fis.close();
			}
			catch (Exception e) {
				e.printStackTrace();
				ReportingUtils.failLog("Error while closing test data file. File Path: "+this.excelDataFile+" | Sheet Name: "+this.excelDataSheet+". Error Details: "+e,null);
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.ERROR, "Error while closing test data file. File Path: "+this.excelDataFile+" | Sheet Name: "+this.excelDataSheet+". Error Details: "+sw.toString());
			}

		}

		return this.testData;
	}


	public static void csvToXLSX(String csvFilePath, String excelFilePath,String testSelectionId) {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: ExcelUtils.csvToXLSX(). CSVFile: "+csvFilePath+" | ExcelFile: "+excelFilePath);
		FileOutputStream fileOutputStream = null;
		try {
			XSSFWorkbook workBook = new XSSFWorkbook();
			Sheet sheet = workBook.createSheet(testSelectionId);
			String currentLine=null;
			int rowNum=-1;
			// BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
			BufferedReader br = Files.newBufferedReader(Paths.get(csvFilePath), StandardCharsets.UTF_8);
			while ((currentLine = br.readLine()) != null) {
				if(currentLine.startsWith(",") || currentLine.equals(""))
					continue;
				String str[] = currentLine.split(",");
				rowNum++;
				Row currentRow=sheet.createRow(rowNum);
				for(int i=0;i<str.length;i++){
					if(i==str.length-1)
					{
						if(rowNum==0)
							currentRow.createCell(i).setCellValue(str[i].replace("\uFEFF", ""));
						else
						{
							Cell cell = currentRow.createCell((short)i);
							cell.setCellType(CellType.NUMERIC);
							//System.out.println("String: "+str[i]);
							try {
								int value=Integer.parseInt(str[i].replace("\uFEFF", ""));
								cell.setCellValue(value);
							}
							catch (Exception e) {
								currentRow.createCell(i).setCellValue(str[i].replace("\uFEFF", ""));
							}


						}
					}
					else
						currentRow.createCell(i).setCellValue(str[i].replace("\uFEFF", ""));
				}
			}

			fileOutputStream =  new FileOutputStream(excelFilePath);
			workBook.write(fileOutputStream);
			fileOutputStream.close();
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "CSV to Excel converted at path: "+excelFilePath);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.ERROR, "Error while converting CSV to XLSX. Error Details: "+sw.toString());
		}
		finally
		{
			try
			{
				fileOutputStream.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}