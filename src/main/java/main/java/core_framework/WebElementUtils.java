package main.java.core_framework;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import main.java.core_framework.ConsoleLogger.LogLevel;

public class WebElementUtils {


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void selectDropDownByVisibleText(WebDriver driver, WebElement element, String visibleText, int elementClickableWaitTime, int elementClickablePollingInterval) {
		// TODO Auto-generated method stub
		waitAndClick(driver, element, elementClickableWaitTime, elementClickablePollingInterval);
		Select select= new Select(element);
		select.selectByVisibleText(visibleText);
	}

	public boolean verifyElementClickableByXpath(WebDriver driver, String locator, int waitTime)
	{
		waitForLoad(driver);
		boolean clickableStatus=false;
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, waitTime); 
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
			clickableStatus=true;
		}
		catch (Exception e) {
			clickableStatus=false;
		}
		return clickableStatus;
	}

	public boolean verifyElementClickable(WebDriver driver, WebElement element, int waitTime)
	{
		waitForLoad(driver);
		boolean clickableStatus=true;
		/*
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, waitTime); 
			wait.until(ExpectedConditions.elementToBeClickable(element));
			clickableStatus=true;
		}
		catch (Exception e) {
			clickableStatus=false;
		}
		 */
		try
		{
			FluentWait wait = new FluentWait<WebDriver>(driver)
					.pollingEvery(2, TimeUnit.SECONDS)
					.withTimeout(waitTime, TimeUnit.SECONDS)
					.ignoring(StaleElementReferenceException.class)
					.ignoring(NoSuchElementException.class)
					.ignoring(Exception.class);
			wait.until(ExpectedConditions.elementToBeClickable(element));
		}
		catch (Exception e) {
			clickableStatus=false;
		}
		return clickableStatus;
	}



	public void waitUntilElementClickable(WebDriver driver, WebElement element, int elementClickableWaitTime, int elementClickablePollingInterval)
	{
		FluentWait wait = new FluentWait<WebDriver>(driver)
				.pollingEvery(elementClickablePollingInterval, TimeUnit.SECONDS)
				.withTimeout(elementClickableWaitTime, TimeUnit.SECONDS)
				.ignoring(StaleElementReferenceException.class)
				.ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	

	public void waitForLoad(WebDriver driver) {
		
		JSWaiter jsWaiter=new JSWaiter();
		jsWaiter.setDriver(driver);
		jsWaiter.waitAllRequest();
	}


	public void waitAndClick(WebDriver driver, WebElement element, int elementClickableWaitTime, int elementClickablePollingInterval) {
		// TODO Auto-generated method stub
		waitForLoad(driver);
		waitUntilElementClickable(driver, element, elementClickableWaitTime, elementClickablePollingInterval);
		//forceWait(2);
		try {
			element.click();
		}
		catch(Exception e)
		{
			try
			{
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("arguments[0].click();", element);
			}
			catch(Exception e1)
			{
				ConsoleLogger.writeConsoleLog(LogLevel.ERROR, "Trying to click element using actions: "+element);
				Actions actions = new Actions(driver);
				actions.moveToElement(element).click().build().perform();
			}
		}
		//JavascriptExecutor jse = (JavascriptExecutor)driver;
		//jse.executeScript("arguments[0].click();", element);
		//Actions actions = new Actions(driver);
		//actions.moveToElement(element).click().build().perform();
	}


	public void clearAndSetValue(WebDriver driver, WebElement element, String testData, int elementClickableWaitTime, int elementClickablePollingInterval) {
		// TODO Auto-generated method stub
		waitForLoad(driver);
		waitAndClick(driver, element, elementClickableWaitTime, elementClickablePollingInterval);
		element.clear();
		element.sendKeys(testData);

	}

	public WebElement getElementByXPath(WebDriver driver, String locator)
	{
		waitForLoad(driver);
		return driver.findElement(By.xpath(locator));
	}

	public String getTextFromElement(WebElement element)
	{
		return element.getText();
	}


	public void forceWait(int seconds) {
		// TODO Auto-generated method stub
		try
		{
			Thread.sleep(seconds*1000);	
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	public String getCurrentWindowHadle(WebDriver driver)
	{
		return driver.getWindowHandle();
	}

	public Set<String> getAllWindowHandles(WebDriver driver)
	{
		return driver.getWindowHandles();
	}

	public void windowHandles(WebDriver driver) {
		String parentWinHandle = driver.getWindowHandle();
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Parent window handle: " + parentWinHandle);
		Set<String> winHandles = driver.getWindowHandles();
		for (String handle : winHandles) {
			if (!handle.equals(parentWinHandle)) {
				driver.switchTo().window(handle);
				ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "New Opened window handle: " + handle);
				ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO,
						"Title of the new window: " + driver.getTitle());
				ReportingUtils.infoLog("Navigated to new window", driver);
				driver.close();
			}
		}
		driver.switchTo().window(parentWinHandle);
	}

	public void uploadFile(String filePath) throws Exception {
		StringSelection stringSelection = new StringSelection(filePath);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);
		//Using Robot class, doing a CTRL+V followed by ENTER
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

}
