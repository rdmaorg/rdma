package ie.clients.gdma2.test.step.connectiontype;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.step.Init;


public class DeleteConnectionType extends Init {

	
	@When("^User choose one connection type from the list and press Delete button$")
	public void userAttempstToDeleteConnectionTypeFromList() throws Throwable {
		
		WebElement deleteButton = driver.findElement(By.cssSelector("button[class='btn btn-warning btn-xs deleteConnection'][data-connectionid='35']"));
	    deleteButton.click();
	    Log.info("User select delete button for Connection type where ID=20");
	    
	}

	@And("^Press Delete Connection type button to confirm the delete action$")
	public void userConfirmsDeleteConnectionTypeAction() throws Throwable {
		
		WebElement confirmDelete= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success'][data-apply='confirmation']"));
		confirmDelete.click();
		Log.info("User select confirm delete button");
	
	}

	@Then("^Connection type is successfully deleted and it is not displayed on the list$")
	public void connectionTypeIsSuccessfullyDdeletedAndItIsNotDisplayedOnTheList() throws Throwable {
		
		WebElement table= driver.findElement(By.id("tbl_connection")); //locate table
		List<WebElement> rowTable = table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows = rowTable.size(); //calculate number of rows
		
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{
			List<WebElement>columns_table=rowTable.get(i).findElements(By.tagName("td")); //locate columns
			int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (cellText.contains("Test Automation"))
				{
					Log.error("Error: Connection type is still displayed on the list");
				}
				else
				{
					Log.info("Connection type is successfully deleted and it is not displayed on the list");
				}
				
			}
		}
		
	 
	}

}
