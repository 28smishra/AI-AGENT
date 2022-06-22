package pages;

import java.util.ArrayList;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import core_framework.BrowserDriver;
import core_framework.ConsoleLogger;
import core_framework.ReportingUtils;
import core_framework.WebElementUtils;

public class TestAgent {
	
	WebDriver driver;
	WebElementUtils webUtils;
	private static final int elementClickableWaitTime = 60;
	private static final int elementClickablePollingInterval = 5;
	private static final String applicationURL="https://c6.avaamo.com/web_channels/444588bc-92fe-477f-87c1-88a92946346a/demo.html?theme=avm-messenger&banner=true&demo=true&banner_text=%20&banner_title=This%20is%20how%20the%20chat%20agent%20shows%20up";
	private String genderXPath = "//span[text()='<<gender>>']//parent::label";
	private String selectXPath = "//li[text()='<<select>>']";
	private String ratingXPath = "//label[@aria-label='rating <<rating>>']";
	private String testOptionXpath = "//a[text()='<<testOption>>']";
	
	@FindBy(xpath="//div[contains(@class,'notification')]//h3")
	WebElement firstLine;

	@FindBy(xpath="//div[contains(@class,'notification')]//h3//small")
	WebElement secondLine;
	
	@FindBy(xpath="//div[contains(@class,'notification')]")
	WebElement chatBotLink;
	
	@FindBy(xpath="//a[text()='Get Started']")
	WebElement getStartedBtn;
	
	@FindBy(xpath="//iframe[@title='Chat area']")
	WebElement chatBotFrame;

	@FindBy(xpath="//button[@title='Close Menu']")
	WebElement closeMenuBtn;
	
	@FindBy(xpath="//textarea[@role='textbox']")
	WebElement txtBoxText;
	
	@FindBy(xpath="//button[text()='Send']")
	WebElement sendBtn;
	
	@FindBy(xpath="//button[@aria-label='Switch to chat agent menu']")
	WebElement switchToChatAgentBtn;
	
	@FindBy(xpath="//a[text()='Start Over']")
	WebElement startOverBtn;
	
	@FindBy(xpath="//a[@title='Download Motor Policy']")
	WebElement downloadMotorPolicyLink;
	
	@FindBy(xpath="//a[@title='Download']")
	WebElement downloadLink;
	
	@FindBy(xpath="//div[@data-ele-name='Full name']//parent::div//following-sibling::div//input")
	WebElement fullNameText;
	
	@FindBy(xpath="//div[@data-ele-name='Address']//parent::div//following-sibling::div//textarea")
	WebElement addressText;
	
	@FindBy(xpath="//input[@placeholder='Select']")
	WebElement selectDropDown;
	
	@FindBy(xpath="//button[@aria-label='Submit']")
	WebElement submitBtn;
	
	@FindBy(xpath="//button[@aria-label='close webview popup']")
	WebElement closeWebViewPopupBtn;
	
	@FindBy(xpath="//a[@title='Renew Motor Policy']")
	WebElement renewMotorPolicyLink;
	
	@FindBy(xpath="//a[@title='Yes']")
	WebElement yesBtn;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	public TestAgent() {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Constructor: TestAgent Class");
		this.driver = BrowserDriver.getDriver();
		webUtils = new WebElementUtils();
		PageFactory.initElements(driver, this);
	}
	
	public void launchApplication() {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: TestAgent.launchApplication()");
		if(this.driver.getTitle().equalsIgnoreCase("Test Agent - IRA")) {
			ReportingUtils.infoLog("Application is already launched", null);
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Application is already launched");
		} else {
			this.driver.get(applicationURL);
			ReportingUtils.infoLog("Launching the application", null);
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Launching the application");
			displayNotification();
		}
	}
	
	public void displayNotification() {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: TestAgent.displayNotification()");
		
		ReportingUtils.infoLog(firstLine.getText() + '\n' + secondLine, driver);
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, firstLine.getText() + '\n' + secondLine);
	}
	
	public void sendGreetings(String greetings) {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: TestAgent.sendGreetings()");
		
		try {
			webUtils.waitAndClick(driver, chatBotLink, elementClickableWaitTime, elementClickablePollingInterval);
			webUtils.forceWait(2);
			webUtils.waitAndClick(driver, getStartedBtn, elementClickableWaitTime, elementClickablePollingInterval);
			webUtils.forceWait(2);
		} catch(Exception e) {
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Chat Bot is already opened.");
		}
		
		try {
			driver.switchTo().frame(chatBotFrame);
			webUtils.forceWait(2);
			
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", closeMenuBtn);
			webUtils.forceWait(2);
		} catch(Exception e) {
		}
		
		webUtils.clearAndSetValue(driver, txtBoxText, "#clear", elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, sendBtn, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		
		webUtils.clearAndSetValue(driver, txtBoxText, greetings, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, sendBtn, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(10);
		
		ReportingUtils.infoLog("Sent the greetings message and fetched the response message", driver);
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Sent the greetings message and fetched the response message");
	}
	
	public void downloadMotorPolicy() {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: TestAgent.downloadMotorPolicy()");
		
		try {
			webUtils.waitAndClick(driver, chatBotLink, elementClickableWaitTime, elementClickablePollingInterval);
			webUtils.forceWait(2);
			webUtils.waitAndClick(driver, getStartedBtn, elementClickableWaitTime, elementClickablePollingInterval);
			webUtils.forceWait(2);
		} catch(Exception e) {
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Chat Bot is already opened.");
		}
		
		try {
			driver.switchTo().frame(chatBotFrame);
			webUtils.forceWait(2);
		} catch(Exception e) {
		}
		
		try {
			webUtils.waitAndClick(driver, switchToChatAgentBtn, elementClickableWaitTime, elementClickablePollingInterval);
			webUtils.forceWait(5);
		} catch(Exception e) {
		}
		
		webUtils.waitAndClick(driver, startOverBtn, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, downloadMotorPolicyLink, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, downloadLink, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(5);
		
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		webUtils.forceWait(10);
		
		ReportingUtils.infoLog("Clicked on the Download link for Download Motor Policy", driver);
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Clicked on the Download link for Download Motor Policy");
		
		driver.close();
		webUtils.forceWait(2);
		driver.switchTo().window(tabs.get(0));
		webUtils.forceWait(5);
	}
	
	public void testBot(String fullName, String address, String gender, String select, String rating) {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: TestAgent.testBot()");
		
		webUtils.clearAndSetValue(driver, fullNameText, fullName, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.clearAndSetValue(driver, addressText, address, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, webUtils.getElementByXPath(driver, genderXPath.replace("<<gender>>", gender)), elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, selectDropDown, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, webUtils.getElementByXPath(driver, selectXPath.replace("<<select>>", select)), elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		
		WebElement element = webUtils.getElementByXPath(driver, ratingXPath.replace("<<rating>>", rating));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element);
		
		webUtils.waitAndClick(driver, submitBtn, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		
		ReportingUtils.infoLog("Performed the Test Bot operation by providing all the required details", driver);
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Performed the Test Bot operation by providing all the required details");
	}
	
	public void newTest(String testOption) {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: TestAgent.newTest()");
		
		webUtils.waitAndClick(driver, webUtils.getElementByXPath(driver, testOptionXpath.replace("<<testOption>>", testOption)), elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		
		if(testOption.equalsIgnoreCase("Google")) {
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", closeWebViewPopupBtn);
			
			ReportingUtils.infoLog("Performed the New Test operation by clicking on Google option", driver);
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Performed the New Test operation by clicking on Google option");
			
		} else if(testOption.equalsIgnoreCase("Call")) {
			
			ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
			webUtils.forceWait(10);
		
			ReportingUtils.infoLog("Performed the New Test operation by clicking on Call option", null);
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Performed the New Test operation by clicking on Call option");
			
			driver.close();
			webUtils.forceWait(2);
			driver.switchTo().window(tabs.get(0));
			webUtils.forceWait(5);
		}
	}
	
	public void renewMotorPolicy(String policyNumber, String registrationNumber, String phoneNumber, String email) {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: TestAgent.renewMotorPolicy()");
		
		try {
			webUtils.waitAndClick(driver, chatBotLink, elementClickableWaitTime, elementClickablePollingInterval);
			webUtils.forceWait(2);
			webUtils.waitAndClick(driver, getStartedBtn, elementClickableWaitTime, elementClickablePollingInterval);
			webUtils.forceWait(2);
		} catch(Exception e) {
			ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Chat Bot is already opened.");
		}
		
		try {
			driver.switchTo().frame(chatBotFrame);
			webUtils.forceWait(2);
			
		} catch(Exception e) {
		}
		
		try {
			webUtils.waitAndClick(driver, switchToChatAgentBtn, elementClickableWaitTime, elementClickablePollingInterval);
			webUtils.forceWait(5);
		} catch(Exception e) {
		}
		
		webUtils.waitAndClick(driver, startOverBtn, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, renewMotorPolicyLink, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(5);
		
		webUtils.clearAndSetValue(driver, txtBoxText, policyNumber, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, sendBtn, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(5);
		
		webUtils.clearAndSetValue(driver, txtBoxText, registrationNumber, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, sendBtn, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(5);
		
		webUtils.clearAndSetValue(driver, txtBoxText, phoneNumber, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, sendBtn, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(5);
		
		webUtils.clearAndSetValue(driver, txtBoxText, email, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(2);
		webUtils.waitAndClick(driver, sendBtn, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(5);
		
		webUtils.waitAndClick(driver, yesBtn, elementClickableWaitTime, elementClickablePollingInterval);
		webUtils.forceWait(5);
		
		ReportingUtils.infoLog("Renewed Motor Policy for the policy number - " + policyNumber, driver);
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Renewed Motor Policy for the policy number - " + policyNumber);
		
	}
}
