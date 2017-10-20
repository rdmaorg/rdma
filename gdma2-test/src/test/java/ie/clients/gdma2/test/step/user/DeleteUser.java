package ie.clients.gdma2.test.step.user;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.step.Init;
import ie.clients.gdma2.test.utility.PropertyHandler;

public class DeleteUser extends Init{
	
	
	@When("^User choose a user from the list and press Delete button$")
	public void user_choose_a_user_from_the_list_and_press_Delete_button() throws Throwable {
	  String userId = PropertyHandler.getProperty("test.data.user1.id");
	  WebElement deleteButton=driver.findElement(By.cssSelector("button[class='btn btn-warning btn-xs deleteUser'][data-userid='"+userId+"']"));
	  deleteButton.click();
	  Log.info("User select delete button for user with id=143");
		
	}

	@And("^User confirm delete button$")
	public void user_confirm_delete_button() throws Throwable {
	    
		WebElement confirmDelete=driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success'][data-apply='confirmation']"));
		confirmDelete.click();
		Thread.sleep(2000);
		Log.info("User confirm delete button");
		
	}

	@Then("^User is successfully removed from the page$")
	public void user_is_successfully_removed_from_the_page() throws Throwable {
		
		String name= PropertyHandler.getProperty("test.data.user1.name");
		String lastName=PropertyHandler.getProperty("test.data.user1.lastName");
		String userName=PropertyHandler.getProperty("test.data.user1.userName");
		String domain=PropertyHandler.getProperty("test.data.user1.domain");
		
		WebElement table= driver.findElement(By.id("tbl_user")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td")); //locate columns
			int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (cellText.contains(name)&&cellText.contains(lastName)&&cellText.contains(userName)&&cellText.contains(domain))
				{
					Log.error("Error: User is still displayed on the list");
				}
				else
				{
					Log.info("User is successfully deleted and it is not displayed on the list");
				}
				
			}
		}
	   
	}


}
