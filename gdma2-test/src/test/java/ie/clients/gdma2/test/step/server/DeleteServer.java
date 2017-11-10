package ie.clients.gdma2.test.step.server;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import ie.clients.gdma2.test.app.Navigation;
import ie.clients.gdma2.test.app.SqlConnection;
import ie.clients.gdma2.test.page.HomePage;
import ie.clients.gdma2.test.step.Init;


public class DeleteServer extends Init {
	
	String id="39";

	@And("^Connect to DB and deactivate the server$")
	public void connectToDBAndDeactivateTheServer() throws Throwable {
		
		SqlConnection.delete(id);
		Log.info("User execute delete sql statement");
		
	}

	@Then("^Verify that server is successfully deleted$")
	public void verifyThatServerIsSuccessfullyDeleted() throws Throwable {
		
		Navigation.login(driver, username, password);
		HomePage homepage= new HomePage(driver);
		homepage.configurationButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		homepage.serverButton.click();
		Thread.sleep(2000);
		Select dropdownShowEntries= new Select(driver.findElement(By.name("tbl_server_length")));
		dropdownShowEntries.selectByValue("100");
		
		WebElement table= driver.findElement(By.id("tbl_server")); //locate table
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
					Log.info("Server is successfully deleted");
					break;
				}
				else
		
				{
					if(column<=columns_count) continue;
					Log.error("Error:Server is still displayed on the servers list");
				}
				
			}
		
		}	
	   
	}

}
