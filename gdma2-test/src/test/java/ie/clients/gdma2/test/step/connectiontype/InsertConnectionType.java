package ie.clients.gdma2.test.step.connectiontype;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.page.ConnectionTypesPage;
import ie.clients.gdma2.test.step.Init;
import ie.clients.gdma2.test.utility.PropertyHandler;


public class InsertConnectionType extends Init{

	String name=PropertyHandler.getPropertyFrom("src/test/resources/testData/ConnectionType.properties","name");
	String connectionClass=PropertyHandler.getPropertyFrom("src/test/resources/testData/ConnectionType.properties","connectionClass");
	String sqlToGetTable=PropertyHandler.getPropertyFrom("src/test/resources/testData/ConnectionType.properties","SQLToGetTables");
	
//Scenario: Insert new connection type - success
	
	@When("^User press Insert Connection button$")
	public void userPressInsertConnectionButton() throws Throwable {
		
		ConnectionTypesPage connectiontype= new ConnectionTypesPage(driver);
		connectiontype.insertConnectionButton.click();
		Log.info("User press insert connection button");
	   
	}

	@And("^Populate all fields on the form$")
	public void populateAllFieldsOnTheForm() throws Throwable {
		
		ConnectionTypesPage connectiontype= new ConnectionTypesPage(driver);
		connectiontype.insertConnectionNameField.sendKeys(name);
		connectiontype.insertConnectionCCField.sendKeys(connectionClass);
		connectiontype.insertConnectionSTGField.sendKeys(sqlToGetTable);
		Log.info("User populates all fields on the form");
		
	   
	}

	@And("^Press Save button on the Insert New Connection form$")
	public void pressSaveButton() throws Throwable {
		ConnectionTypesPage connectiontype= new ConnectionTypesPage(driver);
		connectiontype.saveButton.click();
		Log.info("User select save button");
		
	
	}
	
	@And("^Press Save Connection type button to confirm the save$")
	public void pressSaveConnectionTypeButtonToConfirmTheSave() throws Throwable {
		
		 WebElement confirmSave= driver.findElement(By.className("popover-content"));
		 confirmSave.findElement(By.className("btn-success")).click();
		 driver.navigate().refresh();
		 Thread.sleep(2000);
		 Log.info("User press save connection type button");
		
		
	}

	@Then("^New connection type is created and it is displayed on the connection types list page$")
	public void newConnectionTypeIsCreatedAndItIsDisplayedOnTheConnectionTypesListPage() throws Throwable {
	   
		
		WebElement table= driver.findElement(By.id("tbl_connection")); //locate table
		List<WebElement>rowTable=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=rowTable.size(); //calculate number of rows
		
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{
			List<WebElement>columnsTable=rowTable.get(i).findElements(By.tagName("td")); //locate columns
			int columnsCount=columnsTable.size();
			
			for (int column = 0; column < columnsCount; column++)
			{
				String cellText= columnsTable.get(column).getText();
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
	public void userDoesntPopulateAnyFieldOnTheInsertNewConnectionForm() throws Throwable {
	  
		ConnectionTypesPage connectiontype= new ConnectionTypesPage(driver);
		connectiontype.insertConnectionNameField.clear();
		connectiontype.insertConnectionCCField.clear();
		connectiontype.insertConnectionSTGField.clear();
		Log.info("All fields on the form are empty");
		
	}

	@Then("^Create new connection action faild and correct warning message is displayed$")
	public void createNewConnectionActionFaildAndCorrectWarningMessageIsDisplayed() throws Throwable {
		
		ConnectionTypesPage connectiontype= new ConnectionTypesPage(driver);
		connectiontype.nameError.isDisplayed();
		connectiontype.connectionClassError.isDisplayed();
		connectiontype.sqlGetTablesError.isDisplayed();
		Log.info("Correct warning message is displayed when all fields on the form are empty");

	}
	
	
	
	
//  Scenario: Cancel save action
	
	@And("^Populate all fields on the form with new test data$")
	public void populateAllFieldsOnTheFormWithNewTestData() throws Throwable {
		
		ConnectionTypesPage connectiontype= new ConnectionTypesPage(driver);
		connectiontype.insertConnectionNameField.sendKeys("Name - test cancel save");
		connectiontype.insertConnectionCCField.sendKeys("Connection class - test cancel save");
		connectiontype.insertConnectionSTGField.sendKeys("SQl to get table - test cancel save");
		Log.info("User populates all fields on the form");
	  
	}

	@And("^Press Cancel Connection type button to cancel the save action$")
	public void pressCancelConnectionTypeButtonToCancelTheSaveAction() throws Throwable {
	
		WebElement cancelSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-default'][data-dismiss='confirmation']"));
		cancelSave.click();
		Log.info("User select cancel save button");
		WebElement cancel= driver.findElement(By.cssSelector("button[class='btn btn-default btn-lg pull-left'][data-dismiss='modal']"));
		cancel.click();
		Log.info("User press cancel button");
		
	}

	@Then("^Save action is successfully canceled and connection type is not created$")
	public void saveActionIsSuccessfullyCanceledAndConnectionTypeIsNotCreated() throws Throwable {
		
		WebElement table= driver.findElement(By.id("tbl_connection")); //locate table
		List<WebElement>rowTable=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=rowTable.size(); //calculate number of rows
		
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{
			List<WebElement>columnsTable=rowTable.get(i).findElements(By.tagName("td")); //locate columns
			int columnsCount=columnsTable.size();
			
			for (int column = 0; column < columnsCount; column++)
			{
				String cellText= columnsTable.get(column).getText();
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
