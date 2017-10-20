package ie.clients.gdma2.test.step.data;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import ie.clients.gdma2.test.page.DataModulePage;
import ie.clients.gdma2.test.step.Init;
import ie.clients.gdma2.test.utility.PropertyHandler;

public class EditExistingEntry extends Init {
	
	 String mobileID=PropertyHandler.getPropertyFrom("src/test/resources/testData/NewEntry2_DataModule.properties","mobileID");
	 String mobilePrice=PropertyHandler.getPropertyFrom("src/test/resources/testData/NewEntry2_DataModule.properties","mobilePrice");
	 String mobileName=PropertyHandler.getPropertyFrom("src/test/resources/testData/NewEntry2_DataModule.properties","mobileName");
	 String datetime=PropertyHandler.getPropertyFrom("src/test/resources/testData/NewEntry2_DataModule.properties","datetime");
	 String timestamp=PropertyHandler.getPropertyFrom("src/test/resources/testData/NewEntry2_DataModule.properties","timestamp");

	// single row
	
	@And("^User press Edit button for this new record$")
	public void User_press_Edit_button_for_this_new_record() throws Throwable {
		
		driver.findElement(By.xpath(".//*[@id='table_data']/tbody/tr[6]/td[1]")).click();
		driver.findElement(By.cssSelector("a[class='dt-button buttons-selected buttons-edit']")).click();
		Log.info("User press Edit button");
	   
	}

	@And("^User edit record details$")
	public void user_edit_record_details() throws Throwable {
	   
		Select oSelect= new Select(driver.findElement(By.id("DTE_Field_columns-vendorID")));
		oSelect.selectByValue("5");
		WebElement mobileNameInput=driver.findElement(By.id("DTE_Field_columns-mobileName"));
		mobileNameInput.clear();
		mobileNameInput.sendKeys("LG G4 Dual");
		Log.info("User changed vendorID");
		
	}

	@And("^Select Update button$")
	public void select_Update_button() throws Throwable {
	    
		driver.findElement(By.cssSelector("button[class='btn'][tabindex='0']")).click();
		Log.info("User select update button");
		Thread.sleep(2000);
		
	}

	@Then("^Details are successfully edit$")
	public void details_are_successfully_edit() throws Throwable {
		
		WebElement table= driver.findElement(By.id("table_data")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{ 
			
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td"));//locate column
			int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (cellText.contains("LG G4 Dual"))
				{
					Log.info("User details is successfully updated");
				}
				else
		
				{
					Log.error("Error:User details is not updated successfully");
				}
				
			}
		
		}
		
		driver.findElement(By.xpath(".//*[@id='table_data']/tbody/tr[6]/td[1]")).click();
		driver.findElement(By.cssSelector("a[class='dt-button buttons-selected buttons-remove']")).click();
		driver.findElement(By.cssSelector("button[class='btn'][tabindex='0']")).click();
		Log.info("User remove the record");
	   
	}
	
	//multiple row
	
	@And("^User create one more record$")
	public void user_create_one_more_record() throws Throwable {
	   
		
		DataModulePage datamodule= new DataModulePage(driver);
		datamodule.newButton.click();
		Log.info("User select new button");
		WebElement mobileIDF=driver.findElement(By.id("DTE_Field_columns-mobileID"));
		mobileIDF.sendKeys(mobileID);
		WebElement mobilePriceF=driver.findElement(By.id("DTE_Field_columns-mobilePrice"));
		mobilePriceF.sendKeys(mobilePrice);
		WebElement mobileNameF=driver.findElement(By.id("DTE_Field_columns-mobileName"));
		mobileNameF.sendKeys(mobileName);
		WebElement dateTimeF=driver.findElement(By.id("DTE_Field_columns-datetime"));
		dateTimeF.sendKeys(datetime);
		WebElement timestampF=driver.findElement(By.id("DTE_Field_columns-timestamp"));
		timestampF.sendKeys(timestamp);
		Select oselect= new Select(driver.findElement(By.id("DTE_Field_columns-vendorID")));
		oselect.selectByValue("5");
		Log.info("User populate all fields on the form");
		driver.findElement(By.cssSelector("button[class='btn'][tabindex='0']")).click();
		Thread.sleep(2000);
		Log.info("User press Create button");	
		
	}

	@And("^User press Edit button for these two new records$")
	public void user_press_Edit_button_for_these_two_new_records() throws Throwable {
		
		driver.findElement(By.xpath(".//*[@id='table_data']/tbody/tr[6]/td[1]")).click();
		Actions action= new Actions(driver);
		action.sendKeys(Keys.CONTROL).build().perform();
		driver.findElement(By.xpath(".//*[@id='table_data']/tbody/tr[7]/td[1]")).click();
		driver.findElement(By.cssSelector("a[class='dt-button buttons-selected buttons-edit']")).click();
		Thread.sleep(2000);
		Log.info("User press Edit button");
	
	}

	@And("^User edit record details for these two new items$")
	public void user_edit_record_details_for_these_two_new_items() throws Throwable {
		
		driver.findElement(By.xpath("html/body/div[3]/div/div/div/div[1]/div[3]/div/form/div/div[2]/div/div[2]")).click();
		Select oselect= new Select(driver.findElement(By.id("DTE_Field_columns-vendorID")));
		oselect.selectByValue("6");
		driver.findElement(By.xpath("html/body/div[3]/div/div/div/div[1]/div[3]/div/form/div/div[3]/div/div[2]")).click();
		driver.findElement(By.id("DTE_Field_columns-mobilePrice")).sendKeys("650");
		Log.info("User successfully edit records details");

	}

	@Then("^Details for both items are successfully edit$")
	public void details_for_both_items_are_successfully_edit() throws Throwable {
	    
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
				if (cellText.contains(mobilePrice))
				{
					Log.info("Details are updated successfully");
					break;
				}
				else
				{
					Log.error("Error: Details are not updated as expected");
				}
				
			}
		}
	   
		driver.findElement(By.xpath(".//*[@id='table_data']/tbody/tr[6]/td[1]")).click();
		driver.findElement(By.cssSelector("a[class='dt-button buttons-selected buttons-remove']")).click();
		driver.findElement(By.cssSelector("button[class='btn'][tabindex='0']")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(".//*[@id='table_data']/tbody/tr[6]/td[1]")).click();
		driver.findElement(By.cssSelector("a[class='dt-button buttons-selected buttons-remove']")).click();
		driver.findElement(By.cssSelector("button[class='btn'][tabindex='0']")).click();
		Log.info("User remove the records");
		
	}

}
