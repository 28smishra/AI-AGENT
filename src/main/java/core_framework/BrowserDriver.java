package core_framework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrowserDriver {

	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static final List<WebDriver> driverList=new ArrayList<WebDriver>();
	public static String executionBrowserName;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static WebDriver getDriver() {
		
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: BrowserDriver.getDriver()");
		
		if(driver.get()==null) {
			String os=System.getProperty("os.name").toLowerCase();
			
			if(os.contains("windows")) {
				
				switch(BrowserDriver.executionBrowserName) {
				
				case "Chrome":
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/src/main/resources/chromedriver.exe");
					ChromeOptions chromeOptions = new ChromeOptions();
					chromeOptions.addArguments("--window-size=1400,600");
					chromeOptions.addArguments("--incognito");
					
					//chromeOptions.addArguments("--headless");

					driver.set(new ChromeDriver(chromeOptions));
					break;
				
				default:
					ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.ERROR, "Invalid browser input: "+executionBrowserName+". Valid values: Chrome");
					System.exit(1);
					break;	
				}
			} else {

				switch(BrowserDriver.executionBrowserName) {
				
				case "Chrome":
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/src/main/resources/chromedriver");
					ChromeOptions chromeOptions = new ChromeOptions();
					chromeOptions.addArguments("--window-size=1400,600");
					chromeOptions.addArguments("--headless");
					chromeOptions.addArguments("--no-sandbox");
					chromeOptions.addArguments("--incognito");

					driver.set(new ChromeDriver(chromeOptions));
					break;

				default:
					ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.ERROR, "Invalid browser input: "+executionBrowserName+". Valid values: Chrome");
					System.exit(1);
					break;	
				}

			}

			driverList.add(driver.get());
			driver.get().manage().window().maximize();
			driver.get().manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			ReportingUtils.initializeReports();
		}
		return driver.get();
	}

	public static void quitBrowsers() {
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Method: BrowserDriver.quitBrowsers()");
		ConsoleLogger.writeConsoleLog(ConsoleLogger.LogLevel.INFO, "Closing browsers. Open browser instances: "+driverList.size());
		
		for(WebDriver d : driverList) {
			d.quit();
			d=null;
			driverList.remove(d);
			driver.set(null);
		}
	}
}