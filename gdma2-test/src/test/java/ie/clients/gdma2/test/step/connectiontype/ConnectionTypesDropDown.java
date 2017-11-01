package ie.clients.gdma2.test.step.connectiontype;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import cucumber.api.java.en.Then;
import ie.clients.gdma2.test.step.Init;

public class ConnectionTypesDropDown extends Init{

	//Scenario: Check the values for Connection type drop-down menu
	
		@Then("^Verify that valid number of connection types are displayed within Connection types dropdown menu$")
		public void verifyValidNumberOfConnectionTypesAreDisplayedWithinConnectionTypesDropdownMenu() throws Throwable {
		   
			Select dropDown= new Select(driver.findElement(By.id("connectionType")));
			List<WebElement>options=dropDown.getOptions();
			int numberOfOptions= options.size();
			Log.info(numberOfOptions);
			driver.navigate().to("https://localhost/rdma/connections");
			WebElement table= driver.findElement(By.cssSelector("table[id='tbl_connection']"));
			List<WebElement> rows= table.findElements(By.tagName("tr"));
			int count=1; //check this
			for (int i=0; i<=rows.size(); i++)
			{
				count++;
				
			}
			Log.info(count);
			//int numberOfRows= rows.size();
			//Log.info(numberOfRows);
			if(numberOfOptions==count)
			{
				Log.info("Correct number of connection types are displayed within dropdown menu");
			}
			else
			{
				Log.error("Error: Number of connection types within drop down menu is not correct");
			}
			
		}

}
