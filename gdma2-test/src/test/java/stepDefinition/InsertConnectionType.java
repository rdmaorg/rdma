package stepDefinition;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pageObjects.ConnectionTypes_Page;
import utility.PropertyHandler;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class InsertConnectionType extends Init{

	String name=PropertyHandler.getPropretyFrom("src/test/resources/testData/ConnectionType.properties","name");
	String connectionClass=PropertyHandler.getPropretyFrom("src/test/resources/testData/ConnectionType.properties","connectionClass");
	String sqlToGetTable=PropertyHandler.getPropretyFrom("src/test/resources/testData/ConnectionType.properties","SQLToGetTables");
	
//Scenario: Insert new connection type - success
	
	@When("^User press Insert Connection button$")
	public void user_press_Insert_Connection_button() throws Throwable {
		
		ConnectionTypes_Page connectiontype= new ConnectionTypes_Page(driver);
		connectiontype.insertConnectionButton.click();
		Log.info("User press insert connection button");
	   
	}

	@And("^Populate all fields on the form$")
	public void populate_all_fields_on_the_form() throws Throwable {
		
		ConnectionTypes_Page connectiontype= new ConnectionTypes_Page(driver);
		connectiontype.insertConnectionNameField.sendKeys(name);
		connectiontype.insertConnectionCCField.sendKeys(connectionClass);
		connectiontype.insertConnectionSTGField.sendKeys(sqlToGetTable);
		Log.info("User populates all fields on the form");
		
	   
	}

	@And("^Press Save button on the Insert New Connection form$")
	public void press_Save_button() throws Throwable {
		ConnectionTypes_Page connectiontype= new ConnectionTypes_Page(driver);
		connectiontype.saveButton.click();
		Log.info("User select save button");
		
	
	}
	
	@And("^Press Save Connection type button to confirm the save$")
	public void press_Save_Connection_type_button_to_confirm_the_save() throws Throwable {
		
		 WebElement confirmSave= driver.findElement(By.className("popover-content"));
		 confirmSave.findElement(By.className("btn-success")).click();
		 driver.navigate().refresh();
		 Thread.sleep(2000);
		 Log.info("User press save connection type button");
		
		
	}

	@Then("^New connection type is created and it is displayed on the connection types list page$")
	public void new_connection_type_is_created_and_it_is_displayed_on_the_connection_types_list_page() throws Throwable {
	   
		
		WebElement table= driver.findElement(By.id("tbl_connection")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td")); //locate columns
			int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (cellText.contains(name))
				{
					Log.info("New connection type is successfully created");
				}
				//else
				{
					Log.error("Error:New connection type is not created successfully");
				}
				
			}
		}
			
		
	}
	
	
	
//Scenario: Insert new connection type - fail
	
	@And("^User doesnt populate any field on the Insert New Connection form$")
	public void user_doesnt_populate_any_field_on_the_Insert_New_Connection_form() throws Throwable {
	  
		ConnectionTypes_Page connectiontype= new ConnectionTypes_Page(driver);
		connectiontype.insertConnectionNameField.clear();
		connectiontype.insertConnectionCCField.clear();
		connectiontype.insertConnectionSTGField.clear();
		Log.info("All fields on the form are empty");
		
	}

	@Then("^Create new connection action faild and correct warning message is displayed$")
	public void create_new_connection_action_faild_and_correct_warning_message_is_displayed() throws Throwable {
		
		ConnectionTypes_Page connectiontype= new ConnectionTypes_Page(driver);
		connectiontype.nameError.isDisplayed();
		connectiontype.connectionClassError.isDisplayed();
		connectiontype.sqlGetTablesError.isDisplayed();
		Log.info("Correct warning message is displayed when all fields on the form are empty");

	}
	
	
	
	
//  Scenario: Cancel save action
	
	@And("^Populate all fields on the form with new test data$")
	public void populate_all_fields_on_the_form_with_new_test_data() throws Throwable {
		
		ConnectionTypes_Page connectiontype= new ConnectionTypes_Page(driver);
		connectiontype.insertConnectionNameField.sendKeys("Name - test cancel save");
		connectiontype.insertConnectionCCField.sendKeys("Connection class - test cancel save");
		connectiontype.insertConnectionSTGField.sendKeys("SQl to get table - test cancel save");
		Log.info("User populates all fields on the form");
	  
	}

	@And("^Press Cancel Connection type button to cancel the save action$")
	public void press_Cancel_Connection_type_button_to_cancel_the_save_action() throws Throwable {
	
		WebElement cancelSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-default'][data-dismiss='confirmation']"));
		cancelSave.click();
		Log.info("User select cancel save button");
		WebElement cancel= driver.findElement(By.cssSelector("button[class='btn btn-default btn-lg pull-left'][data-dismiss='modal']"));
		cancel.click();
		Log.info("User press cancel button");
		
	}

	@Then("^Save action is successfully canceled and connection type is not created$")
	public void save_action_is_successfully_canceled_and_connection_type_is_not_created() throws Throwable {
		
		WebElement table= driver.findElement(By.id("tbl_connection")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td")); //locate columns
			int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (cellText.contains("Name - test cancel save"))
				{
					Log.error("Error:Cancel save button is not working as expected");
				}
				else
				{
					Log.info("Save Connection type action is successfully canceled");
				}
				
			}
		}
	  
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
