package ie.clients.gdma2.test.step;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import ie.clients.gdma2.test.utility.PropertyHandler;

public class Init {
	
	protected static WebDriver driver;

	String fileGen = "src/test/resources/configurationData/general.properties";
	Properties propGen = PropertyHandler.getPropFile(fileGen);
	String browserType = propGen.getProperty("browser");
	protected String appURL = propGen.getProperty("appURL");
	protected String username = propGen.getProperty("username");
	protected String password = propGen.getProperty("password");
	protected String tableAlias=propGen.getProperty("tableAlias"); // for Table= Customers (Server=classicModels) 
	
	protected static Logger Log = Logger.getLogger(Init.class.getName());

}
