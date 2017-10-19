package appModules;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class ElementPresence {
	
	public static boolean isElementPresence(WebDriver driver, By by)
	{
		try
		{
			driver.findElement(by);
			return true;
		}
		catch(org.openqa.selenium.NoSuchElementException e)
		{
			return false;
		}
	}

}
