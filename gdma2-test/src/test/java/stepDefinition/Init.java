package stepDefinition;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import utility.PropertyHandler;

public class Init {
	
	protected static WebDriver driver;

	String fileGen = "src/test/resources/configurationData/general.properties";
	Properties propGen = PropertyHandler.getPropFile(fileGen);
	String browserType = propGen.getProperty("browser");
	String appURL = propGen.getProperty("appURL");
	String username = propGen.getProperty("username");
	String password = propGen.getProperty("password");
	String tableAlias=propGen.getProperty("tableAlias"); // for Table= Customers (Server=classicModels) 
	
	protected static Logger Log = Logger.getLogger(Init.class.getName());

}
