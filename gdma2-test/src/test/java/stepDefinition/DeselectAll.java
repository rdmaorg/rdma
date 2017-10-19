package stepDefinition;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pageObjects.DATAModule_Page;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeselectAll extends Init {


	@When("^User select Deselect ALL button$")
	public void user_select_Deselect_ALL_button() throws Throwable {
		
		DATAModule_Page dataModule= new DATAModule_Page(driver);
		dataModule.selectAll.click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("a[class='dt-button buttons-select-none']")).click();
		Log.info("User press deselect all button");
		
	}

	@Then("^All items are now deselected$")
	public void all_items_are_now_deselected() throws Throwable {
	   
		List<WebElement> element= driver.findElements(By.cssSelector("td[class='select-checkbox']"));
		int lenght=element.size();
		for (int i=1; i<=lenght; i++)
		{
			WebElement el=driver.findElements(By.cssSelector("td[class='select-checkbox']")).get(i);
			if(!el.isSelected())
			{
				Log.info("Deselect functionality works as expected");	
			}
			else
			{
				Log.error("ERROR:Deselect all functionality doesn't work as expected");
			}
		}
		
	
	
	}

}
