package ie.clients.gdma2.test.step.table;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.app.SqlConnection;
import ie.clients.gdma2.test.page.HomePage;
import ie.clients.gdma2.test.step.Init;

public class DeactivateTable extends Init {
	
	String id="7078";  // Before test execution set valid id of table which you want to deactivate (id of test_table1)

	@When("^Choose one active table from the list$")
	public void chooseOneActiveTableFromTheList() throws Throwable {
	  
		WebElement table= driver.findElement(By.id("tbl_tables6")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{ 
			
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td"));//locate column
			int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (cellText.contains(id))
				{
					Log.info("Table is displayed on the page");
				}
				else
		
				{
					Log.error("Error:Table is not displayed on the page");
				}
				
			}
		
		}	
	}
		
		
	

	@And("^Connect to DB and deactivate the table$")
	public void connectToDBAndDeactivateTheTable() throws Throwable {
		
		SqlConnection.update(id);
		Log.info("User execute update sql statement");
	
		
	   
	}

	@Then("^Verify that table is successfully deactivated$")
	public void verifyThatTableIsSuccessfullyDeactivated() throws Throwable {
		
		HomePage homepage= new HomePage(driver);
		homepage.configurationButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		homepage.serverButton.click();
		Thread.sleep(11000);
		Select dropdownShowEntries= new Select(driver.findElement(By.name("tbl_server_length")));
		dropdownShowEntries.selectByValue("100");
		WebElement tableButtonForClassicModelServer= driver.findElement(By.cssSelector("button[data-servername='classicmodel']"));
		tableButtonForClassicModelServer.click();
		Thread.sleep(11000);
		WebElement table= driver.findElement(By.id("tbl_tables6")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{ 
			
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td"));//locate column
			int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (!cellText.contains(id))
				{
					Log.info("Table is successfully deactivated");
				}
				else
		
				{
					Log.error("Error:Table is still displayed on the table list");
				}
				
			}
		
		}	
	 
		
	}



}
