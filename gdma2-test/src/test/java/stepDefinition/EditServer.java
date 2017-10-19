package stepDefinition;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pageObjects.InsertNewServer_Page;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EditServer extends Init {

	@When("^User choose one server from the list and select edit button$")
	public void user_choose_one_server_from_the_list_and_select_edit_button() throws Throwable {
		
		WebElement editButton= driver.findElement(By.cssSelector("button[class='btn btn-primary btn-xs editServer'][data-serverid='36']"));
		editButton.click();
		Log.info("User select edit button for server with ID=10");

	}

	@And("^User edit server name$")
	public void user_edit_server_name() throws Throwable {
		
		InsertNewServer_Page insertserver= new InsertNewServer_Page(driver);
		insertserver.insNameField.sendKeys("EDITAUT");
		Log.info("User edit server name");
	
	}

	@Then("^Server details are successfully edit and new changes are visible on the list$")
	public void server_details_are_successfully_edit_and_new_changes_are_visible_on_the_list() throws Throwable {
		
		WebElement table= driver.findElement(By.id("tbl_server")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td")); //locate columns
            int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (cellText.contains("EDITAUT"))
				{
					Log.info("Server name is successfully edit");
					break;
				}
				else
				{
					if(column<=columns_count) continue;
					Log.error("Error:Server name is not edit successfully");
				}
				
			}
		}
	
	   
	}

}
