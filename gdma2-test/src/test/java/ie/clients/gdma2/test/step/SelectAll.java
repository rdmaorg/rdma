package ie.clients.gdma2.test.step;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.page.DataModulePage;


public class SelectAll extends Init{

	@When("^User select Select ALL button$")
	public void user_select_Select_ALL_button() throws Throwable {
		
		DataModulePage dataModule= new DataModulePage(driver);
		dataModule.selectAll.click();
		Log.info("User press select all button");
	  
	}

	@Then("^All items are selected$")
	public void all_items_are_selected() throws Throwable {
		
		
		List<WebElement> element= driver.findElements(By.cssSelector("td[class='select-checkbox']"));
		int lenght=element.size();
		for (int i=1; i<=lenght; i++)
		{
			WebElement el=driver.findElements(By.cssSelector("td[class='select-checkbox']")).get(i);
			boolean value=el.isSelected();
			if(value==true)
			{
				Log.info("All checkboxes are selected");	
			}
			else
			{
				Log.error("ERROR:Select all functionality doesn't work as expected");
			}
		}
		
		//correct this last step
		
/*		List<WebElement> element= driver.findElements(By.cssSelector("td[class='select-checkbox']"));
		int count=driver.findElements(By.cssSelector("td[class='select-checkbox']")).size();
		for (int i=0; i<=count; i++)
		{
			if (element.get(i).isSelected())
			{
			Log.info("All checkboxes are selected");
			}
			else
			{
				Log.error("ERROR:Select all functionality doesn't work as expected");
			}
		}	
	    
	}
	*/
	/////////////
		
	/*	WebElement table= driver.findElement(By.id("table_data")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td")); //locate columns
            int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				List<WebElement> element= driver.findElements(By.cssSelector("td[class='select-checkbox']"));
				if (element.get(column).isSelected())
				{
					Log.info("all selected");
					break;
				}
				else
				{
					if(column<=columns_count) continue;
					Log.error("Error");
				}
				
			}
		}
*/
}
}
