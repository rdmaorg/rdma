package ie.clients.gdma2.test.step;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import ie.clients.gdma2.test.utility.PropertyHandler;

public class Init {
	
	protected static WebDriver driver;
	protected static WebDriverWait wait;

	String fileGen = "src/test/resources/test.properties";
	Properties propGen = PropertyHandler.getPropFile(fileGen);
	String browserType = propGen.getProperty("test.browser");
	protected String appURL = propGen.getProperty("test.appURL");
	protected String username = propGen.getProperty("test.username");
	protected String password = propGen.getProperty("test.password");
	protected String tableAlias=propGen.getProperty("test.tableAlias"); // for Table= Customers (Server=classicModels) 
	
	protected static Logger Log = Logger.getLogger(Init.class.getName());

}
