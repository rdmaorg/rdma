package ie.clients.gdma2.test.step;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class BeforeAndAfter extends Init {

	@Before
	public void initialize() {

		DOMConfigurator.configure("src/test/resources/configurationFiles/log4j.xml"); // Log.info("test
																						// log");
		/*
		 * extent.init("src/test/resources/configurationFiles/myreport.html",
		 * true); extent.config().reportTitle("GDMA 2 app test report");
		 * extent.config().documentTitle("GDMA 2 app test report");
		 */

		switch (browserType) {
		case "firefox": {
			driver = new FirefoxDriver();
			break;
		}
		case "chrome": {
			String driverPath = "src/test/resources/browserDrivers/chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", driverPath);
			ChromeOptions options = new ChromeOptions();
	        options.addArguments("--no-sandbox");
			driver = new ChromeDriver(options);
			break;
		}
		case "IE": {
			String driverPath = "src/test/resources/browserDrivers/IEDriverServer.exe";
			System.setProperty("webdriver.ie.driver", driverPath);
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			driver = new EdgeDriver(capabilities);
			// driver.navigate().to("javascript:document.getElementById('overridelink').click()");
			break;
		}
		default:
			driver = new FirefoxDriver();
			break;
		}
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(appURL);
		wait = new WebDriverWait(driver,10);
	}

	@After
	public static void closeDriver() {
		driver.close();
		driver.quit();
		/*
		 * extent.log(LogStatus.INFO, "Browser closed"); extent.endTest();
		 */
	}
}
