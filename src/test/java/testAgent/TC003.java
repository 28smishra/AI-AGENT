package testAgent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import core_framework.BrowserDriver;
import core_framework.ConsoleLogger;
import core_framework.ExcelUtils;
import core_framework.ReportingUtils;
import core_framework.WebElementUtils;
import pages.TestAgent;

public class TC003 {
	
	WebDriver driver;
	WebElementUtils webUtils;
	List<HashMap<String, String>> testData;
	String tcName;
	String tcDescription;
	String moduleName;
	ExcelUtils excelObj;

	@BeforeMethod
	@Parameters({ "browserName", "testDataFile", "testDataSheet", "reportsDirectory", "moduleName", "tcName",
			"tcDescription", "testSelectionId" })
	public void setUp(String browserName, String testDataFile, String testDataSheet, String reportsDirectory,
			String moduleName, String tcName, String tcDescription, String testSelectionId) {
		try {
			BrowserDriver.executionBrowserName = browserName;
			ReportingUtils.setReportsDirectory(reportsDirectory);
			this.tcName = tcName;
			this.tcDescription = tcDescription;
			this.moduleName = moduleName;
			this.driver = BrowserDriver.getDriver();
			webUtils = new WebElementUtils();
			excelObj = new ExcelUtils(testDataFile, testDataSheet);
			testData = excelObj.getTestData(true);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.FAIL, "Exception occured in setup function of test.java.hcm.tests.Goals_CareerDevelopment.responsive.TC001. Error: " + sw.toString());
		}
	}

	@Test
	public void Test_TC003() {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Test Started: test.java.stocks.TC003");
		try {
			ReportingUtils.createNewTest(this.moduleName + "_" + this.tcName, tcDescription);
			for (int iteration = 0; iteration < testData.size(); iteration++) {
				for (int i = 1; i <= ReportingUtils.maxRetryCount; i++) {
					try {
						Map<String, String> dataMap = testData.get(iteration);
						ReportingUtils.createNewIteration(this.tcName, dataMap.get("Iteration"), this.moduleName);
						ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "TC Started: " + this.moduleName + "_" + this.tcName + "_" + dataMap.get("Iteration"));
						ReportingUtils.infoLog("<b>Iteration Description: </b>" + dataMap.get("Iteration_Description"), null);
						ReportingUtils.infoLog("Test Data for " + this.moduleName + "_" + this.tcName + "_" + dataMap.get("Iteration") + ": <br><br>" + dataMap, null);
						ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Test Data: " + dataMap);
						
						String greetings = dataMap.get("GreetingsMessage");
						String fullName = dataMap.get("FullName");
						String address = dataMap.get("Address");
						String gender = dataMap.get("Gender");
						String select = dataMap.get("Select");
						String rating = dataMap.get("Rating");
						
						TestAgent agent = new TestAgent();
						agent.launchApplication();
						
						agent.sendGreetings(greetings);
						
						agent.testBot(fullName, address, gender, select, rating);
						
						ReportingUtils.passLog("TC Passed", null);
						ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.PASS, "TC Passed");
						ReportingUtils.endTest();
					} catch (Exception e) {

						if (i != ReportingUtils.maxRetryCount) {
							ReportingUtils.removeIteration();
							testData = excelObj.getTestData(true);
							continue;
						}
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.FAIL, sw.toString());
						ReportingUtils.failLog("TC Failed. Details: " + sw.toString(), driver);
						ReportingUtils.endTest();
					}
					webUtils.forceWait(3);
					break;
				}
			}
		} catch (Exception e) {

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.FAIL, sw.toString());
			ReportingUtils.failLog("TC Failed. Details: " + sw.toString(), driver);
			ReportingUtils.endTest();
		}
	}
}