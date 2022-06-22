package core_framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class ReportingUtils {

	private static ExtentHtmlReporter  htmlReporter;
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test=new ThreadLocal<>();
	private static ThreadLocal<ExtentTest> testCase=new ThreadLocal<>();
	private static String reportsDirectory;
	private static boolean reportsInitialized=false;
	public static final int maxRetryCount=3;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReportingUtils.setReportsDirectory("D:/Eclipse_Workspace/OracleFusion/Reports");
		WebDriver driver = BrowserDriver.getDriver();
		driver.get("http://www.google.com");
		infoLog("INFO", driver);
		passLog("PASS", driver);
		failLog("FAIL", driver);
		driver.close();
	}

	public static void initializeReports()
	{
		if(!reportsInitialized) {
			
			reportsInitialized=true;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");

			String reportFilePath=reportsDirectory+"/Report_"+simpleDateFormat.format(new Date())+".html";
			
			htmlReporter = new ExtentHtmlReporter(reportFilePath);
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "HTML Report created at:"+reportFilePath);

			//initialize ExtentReports and attach the HtmlReporter
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);

			extent.setSystemInfo("OS", System.getProperty("os.name"));
			String browserName="";
			
			switch(BrowserDriver.executionBrowserName) {
			case "Chrome":
				browserName="Google Chrome";
				break;
			case "Firefox":
				browserName="Mozilla Firefox";
				break;
			default:
				browserName="NA";
				break;
					
			}
			extent.setSystemInfo("Browser", browserName);
			extent.setSystemInfo("User", System.getProperty("user.name"));
			extent.setAnalysisStrategy(AnalysisStrategy.SUITE);
			
			htmlReporter.config().setDocumentTitle("Test Execution Report");
			htmlReporter.config().setReportName("Test Execution Report");

			extent.flush();
		}
	}

	public static void removeIteration() {
		extent.removeTest(test.get());
		extent.flush();
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Initiating Retry");
	}

	public static void createNewTest(String tcName, String tcDescription) {
		testCase.set(extent.createTest(tcName));
		Markup m1 = MarkupHelper.createLabel("<b>TC Description: </b>"+tcDescription, ExtentColor.WHITE);
		testCase.get().info(m1);
		extent.flush();
	}

	public static void createNewIteration(String tcName,String iterationNumber, String moduleName) {
		test.set(testCase.get().createNode(tcName+"_Iteration_"+iterationNumber));
		test.get().assignCategory(moduleName.split("_")[0]);
		if(!(moduleName.equalsIgnoreCase(moduleName.split("_")[0])))
			test.get().assignCategory(moduleName);
		extent.flush();

	}

	public static void setReportsDirectory(String reportsDirectory) {
		ReportingUtils.reportsDirectory=System.getProperty("user.dir")+reportsDirectory;
	}

	public static void endTest() {
		extent.flush();
	}

	public static MediaEntityModelProvider getScreenshot(WebDriver driver) {
		MediaEntityModelProvider screenShot=null;
		
		try {
			TakesScreenshot newScreen = (TakesScreenshot) driver;
			String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
			String image = "data:image/jpg;base64, " + scnShot ;
			screenShot = MediaEntityBuilder.createScreenCaptureFromBase64String(image).build();	
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.FAIL, "Error taking screenshot for extent report: "+sw.toString());
			screenShot=null;
		}
		return screenShot;
	}

	public static void passLog(String message, WebDriver driver) {

		if(test.get()!=null) {
			
			if(driver==null)
				test.get().pass(message);
			else
				test.get().pass(message+"<br><br>", getScreenshot(driver));
		} else {
			
			if(driver==null)
				testCase.get().pass(message);
			else
				testCase.get().pass(message+"<br><br>", getScreenshot(driver));
		}
		extent.flush();
	}

	public static void failLog(String message, WebDriver driver) {
		
		if(test.get()!=null) {
			
			if(driver==null)
				test.get().fail(message);
			else
				test.get().fail(message+"<br><br>", getScreenshot(driver));
		} else {
			
			if(driver==null)
				testCase.get().fail(message);
			else
				testCase.get().fail(message+"<br><br>", getScreenshot(driver));
		}
		extent.flush();
	}

	public static void infoLog(String message, WebDriver driver) {
		
		if(test.get()!=null) {
			
			if(driver==null)
				test.get().info(message);
			else
				test.get().info(message+"<br><br>", getScreenshot(driver));
		} else {
			
			if(driver==null)
				testCase.get().info(message);
			else
				testCase.get().info(message+"<br><br>", getScreenshot(driver));
		}
		extent.flush();
	}
}