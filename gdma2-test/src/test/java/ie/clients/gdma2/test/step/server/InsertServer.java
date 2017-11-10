package ie.clients.gdma2.test.step.server;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.page.InsertNewServerPage;
import ie.clients.gdma2.test.page.ServersPage;
import ie.clients.gdma2.test.step.Init;
import ie.clients.gdma2.test.utility.PropertyHandler;

public class InsertServer extends Init{
	
	//active server
	
	String serverName=PropertyHandler.getPropertyFrom("src/test/resources/testData/ActiveServer.properties", "serverName");
	String username=PropertyHandler.getPropertyFrom("src/test/resources/testData/ActiveServer.properties", "username");
	String password=PropertyHandler.getPropertyFrom("src/test/resources/testData/ActiveServer.properties", "password");
	String connectionURL=PropertyHandler.getPropertyFrom("src/test/resources/testData/ActiveServer.properties", "connectionURL");
	String connectionName=PropertyHandler.getPropertyFrom("src/test/resources/testData/ActiveServer.properties", "connectionName");
	
	String serverName2=PropertyHandler.getPropertyFrom("src/test/resources/testData/InactiveServer.properties", "serverName");
	String username2=PropertyHandler.getPropertyFrom("src/test/resources/testData/InactiveServer.properties", "username");
	String password2=PropertyHandler.getPropertyFrom("src/test/resources/testData/InactiveServer.properties", "password");
	String connectionURL2=PropertyHandler.getPropertyFrom("src/test/resources/testData/InactiveServer.properties", "connectionURL");
	String connectionName2=PropertyHandler.getPropertyFrom("src/test/resources/testData/InactiveServer.properties", "connectionName");
	
	@When("^User press Insert Server button$")
	public void userPressInsertServerButton() throws Throwable {
		
		ServersPage serverpage= new ServersPage(driver);
		serverpage.insertServerButton.click();
		Log.info("User press Insert server button");
	}
	
	@And("^Populate all fields on the insert new server form$")
	public void populateAllFieldsOnTheInsertNewServerForm() throws Throwable {
		
		InsertNewServerPage insertserver= new InsertNewServerPage(driver);
		insertserver.insNameField.sendKeys(serverName);
		insertserver.insUserNameField.sendKeys(username);
		insertserver.insPasswordField.sendKeys(password);
		insertserver.insConnectionURLField.sendKeys(connectionURL);
		WebElement element=driver.findElement(By.id("connectionType"));
		Select oSelect=new Select(element);
		oSelect.selectByValue("2");
		Log.info("User populate all fields on the form");
	   
	}

	@And("^Press Save button on the insert new server form$")
	public void pressSaveButtonOnTheInsertNewServerForm() throws Throwable {
		
		InsertNewServerPage insertserver= new InsertNewServerPage(driver);
		insertserver.insSaveButton.click();
		Log.info("User press Save button");
		
	}

	@And("^Confirm the save action by clicking on the save server button$")
	public void confirmTheSaveActionByClickingOnTheSaveServerButton() throws Throwable {
		
		WebElement confirmSave=driver.findElement(By.className("btn-success"));
		confirmSave.click();
		Log.info("User confirm the save action");
		driver.navigate().refresh();
		Thread.sleep(2000);
		Log.info("User refresh the page");
	}

	@Then("^New server is successfully created and it is displayed on the server list page$")
	public void newServerIsSuccessfullyCreatedAndItIsDisplayedOnTheServerListPage() throws Throwable {
	   
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
				if (cellText.contains(serverName))
				{
					Log.info("New server is successfully created");
					break;
				}
				else
				{
					if(column<=columns_count) continue;
					Log.error("Error:Server is not created successfully");
				}
				
			}
		}
		
	}
	
	
	// Scenario: Insert new inactive server - success
	
	@And("^Populate again all fields on the insert new server form$")
	public void populateAgainAllFieldsOnTheInsertNewServerForm() throws Throwable {
		
		InsertNewServerPage insertserver= new InsertNewServerPage(driver);
		insertserver.insNameField.sendKeys(serverName2);
		insertserver.insUserNameField.sendKeys(username2);
		insertserver.insPasswordField.sendKeys(password2);
		insertserver.insConnectionURLField.sendKeys(connectionURL2);
		WebElement element=driver.findElement(By.id("connectionType"));
		Select oSelect=new Select(element);
		oSelect.selectByValue("2");
		Log.info("User populate all fields on the form");
	   
	}
	
	@And("^User set Active field to be False$")
	public void userSetActiveFieldToBeFalse() throws Throwable {
		
		InsertNewServerPage insertserver= new InsertNewServerPage(driver);
		insertserver.insActiveField.click();
		Log.info("User set active field to be false");
	
	}
	
	//Scenario: Insert new server - fail
	
	@And("^User doesnt populate any field on the Insert New Server form$")
	public void userDoesntPopulateAnyFieldOnTheInsertNewServerForm() throws Throwable {
		
		InsertNewServerPage insertserver= new InsertNewServerPage(driver);
		insertserver.insNameField.clear();
		insertserver.insUserNameField.clear();
		insertserver.insPasswordField.clear();
		insertserver.insConnectionURLField.clear();
		insertserver.insPrefixField.clear();
		Log.info("All fields on the form are empty");
	   
	}

	@Then("^Create new server action faild and correct warning message is displayed$")
	public void createNewServerActionFaildAndCorrectWarningMessageIsDisplayed() throws Throwable {
		
		InsertNewServerPage insertserver= new InsertNewServerPage(driver);
		insertserver.insNameWarning.isDisplayed();
		insertserver.insUsernameWarning.isDisplayed();
		insertserver.insPasswordWarning.isDisplayed();
		insertserver.insURLWarning.isDisplayed();
		Log.info("Correct warning message is displayed for all mandatory fields on the form");
	   
	}
	
	
	
   //Scenario: Cancel save action
	
	@And("^Populate all fields on the insert new server form with new test data$")
	public void populateAllFieldsOnTheFormWithNewTestData() throws Throwable {
		
		InsertNewServerPage insertserver= new InsertNewServerPage(driver);
		insertserver.insNameField.sendKeys("Name - test cancel");
		insertserver.insUserNameField.sendKeys("Username - test cancel");
		insertserver.insPasswordField.sendKeys("Password - test cancel");
		insertserver.insConnectionURLField.sendKeys("URL - text cancel");
		Log.info("User populates all fields on the form");
	}
	
	
	@And("^Press Cancel button on the Insert new server page to cancel the save action$")
	public void pressCancelButtonOnTheInsertNewServerPageToCancelTheSaveAction() throws Throwable {
	
		InsertNewServerPage insertserver= new InsertNewServerPage(driver);
		WebElement cancelSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-default'][data-dismiss='confirmation']"));
		cancelSave.click();
		insertserver.insCancelButton.click();
		Log.info("User select cancel save button");
		
	}

	@Then("^Save action is successfully canceled and server is not created$")
	public void saveActionIsSuccessfullyCanceledAndServerIsNotCreated() throws Throwable {
		
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
				if (cellText.contains("Name - test cancel"))
				{
					Log.error("Error:Cancel save button is not working as expected");
					break;
				}
				else
				{
					Log.info("Save Server action is successfully canceled");
				}
				
			}
		}
		
	    
	}
	
}
