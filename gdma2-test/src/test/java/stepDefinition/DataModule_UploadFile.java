package stepDefinition;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pageObjects.DATAModule_Page;
import appModules.Navigation;
import appModules.UploadFile;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DataModule_UploadFile extends Init {
	
	@When("^User navigates to the ClassicModels - Books table in Data module Page$")
	public void user_navigates_to_the_ClassicModels_Books_table_in_Data_module_Page() throws Throwable {
		
		Navigation.login(driver, username, password);
		WebElement serverInData= driver.findElement(By.cssSelector("li[id='server6'][class='treeview']"));
		serverInData.click();
		WebElement tableInData = driver.findElement(By.cssSelector("a[class='table-data'][data-servername='classicmodel'][data-serverid='6'][data-tablename='books'][data-id='7067']"));
		Assert.assertTrue(tableInData.isDisplayed());
		Log.info("Table is visible in Data module");
		tableInData.click();
		Thread.sleep(4000);
		Log.info("User navigates to DATA module");
	   
	}

	@And("^User select Upload button$")
	public void user_select_Upload_button() throws Throwable {
		//Robot class is used to (generate native system input events) take the control of mouse and keyboard. 
		//Once you get the control, you can do any type of operation related to mouse and keyboard through with java code
		//Step 1- We have to copy the file location in system clipboard.
		//Step 2- We have to click on upload button and use CTR+V and ENTER.
		
		DATAModule_Page datamodule= new DATAModule_Page(driver);
		datamodule.uploadButton.click();
		UploadFile.setClipboardData("C:\\GDMA\\Upload.csv");
		//native key strokes for CTRL, V and ENTER keys
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Log.info("User successfully upload the file");
		Thread.sleep(3000);
		driver.navigate().refresh();
	   
	}

	@Then("^File is successfully uploaded$")
	public void file_is_successfully_uploaded() throws Throwable {
		
		WebElement table= driver.findElement(By.id("table_data")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td")); //locate columns
            int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (cellText.contains("Hamlet"))
				{
					Log.info("File is successfully uploaded");
					break;
				}
				else
				{
					Log.error("Error:File upload failed");
				}
				
			}
		}
	
		
	  
	}

}
