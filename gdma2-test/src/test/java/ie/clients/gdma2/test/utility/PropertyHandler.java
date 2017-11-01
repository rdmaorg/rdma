package ie.clients.gdma2.test.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHandler {

	public static Properties getPropFile(String fileName) {
		Properties prop = new Properties();
		try {
			InputStream input = new FileInputStream(fileName);
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static String[] propReader(String propValue) {
		return propValue.split("\\|", 2);
	}

	public static String getProperty(String propValue) {
		return getPropertyFrom("src/test/resources/testdata.properties",propValue);
	}
	
	public static String getPropertyFrom(String fileName, String propValue) {
		Properties propUrls = PropertyHandler.getPropFile(fileName);
		return propUrls.getProperty(propValue);

	}
	

}
