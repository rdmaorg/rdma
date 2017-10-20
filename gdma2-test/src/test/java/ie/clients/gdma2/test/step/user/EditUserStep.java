package ie.clients.gdma2.test.step.user;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.step.Init;

public class EditUserStep extends Init {
	
	@When("^User choose a user from the list and press Edit button$")
	public void user_choose_a_user_from_the_list_and_press_Edit_button() throws Throwable {
		
		WebElement editButton= driver.findElement(By.cssSelector("button[class='btn btn-primary btn-xs editUser'][data-userid='165']"));
		editButton.click();
		Log.info("User select edit button for the user with id=140");
	    
	}

	@And("^Change the user details$")
	public void change_the_user_details() throws Throwable {
		
		driver.findElement(By.id("name")).sendKeys("EDITAutomation");
		Log.info("User edit user name");
	
	}

	@And("^Press Submit button$")
	public void press_Submit_button() throws Throwable {
		driver.findElement(By.id("Save-user")).click();
		Log.info("User press submit button");
	   
	}

	@And("^Press Save User details button$")
	public void press_Save_User_details_button() throws Throwable {
		
		driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success'][data-apply='confirmation']")).click();
		Log.info("User select confirm button");
	   
	}

	@Then("^User details are successfully edit$")
	public void user_details_are_successfully_edit() throws Throwable {
		
		WebElement table= driver.findElement(By.id("tbl_user")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{ 
			
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td"));//locate column
			int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (cellText.contains("EDITAutomation"))
				{
					Log.info("User details is successfully updated");
				}
				else
		
				{
					Log.error("Error:User details is not updated successfully");
				}
				
			}
		
		}	
	   
	}



}
