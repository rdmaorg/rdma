package ie.clients.gdma2.test.step.data;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.app.Navigation;
import ie.clients.gdma2.test.page.DataModulePage;
import ie.clients.gdma2.test.page.EditUserAccessPage;
import ie.clients.gdma2.test.page.HomePage;
import ie.clients.gdma2.test.step.Init;
import ie.clients.gdma2.test.utility.PropertyHandler;

public class InsertAndDeleteEntry extends Init{
	
	 String mobileID=PropertyHandler.getPropertyFrom("src/test/resources/testData/NewEntry_DataModule.properties","mobileID");
	 String mobilePrice=PropertyHandler.getPropertyFrom("src/test/resources/testData/NewEntry_DataModule.properties","mobilePrice");
	 String mobileName=PropertyHandler.getPropertyFrom("src/test/resources/testData/NewEntry_DataModule.properties","mobileName");
	 String datetime=PropertyHandler.getPropertyFrom("src/test/resources/testData/NewEntry_DataModule.properties","datetime");
	 String timestamp=PropertyHandler.getPropertyFrom("src/test/resources/testData/NewEntry_DataModule.properties","timestamp");

	@Given("^User set full access rights for Classic Models - Mobile_Phone table$")
	public void user_set_full_access_rights_for_Classic_Models_Mobile_Phone_table() throws Throwable {
	    
		Navigation.login(driver, username, password);
		HomePage homepage= new HomePage(driver);
		homepage.configurationButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		homepage.serverButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		WebElement tableButtonForClassicModelServer= driver.findElement(By.cssSelector("button[data-servername='classicmodel']"));
		tableButtonForClassicModelServer.click();
		Thread.sleep(9000);
		WebElement editUserAccessBtnForCustomers= driver.findElement(By.cssSelector("button[class='btn btn-primary btn-xs editAccess'][data-tableid='7065']"));
		Thread.sleep(9000);
		editUserAccessBtnForCustomers.click();
		Thread.sleep(9000);
		
	    EditUserAccessPage edituseraccess= new EditUserAccessPage(driver);
		
		WebElement fullAccessCheckbox= driver.findElement(By.id("2884fullA"));
		if(fullAccessCheckbox.isSelected())
		{
			Log.info("User 805680 has already have full access on mobile_phone table");
			edituseraccess.closeButton.click();
			Thread.sleep(2000);
			
			
		}
		else
		{
			fullAccessCheckbox.click();
			edituseraccess.saveButton.click();
			Log.info("User select confirm yes button");
			WebElement confirmSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success']"));
			confirmSave.click();
			Log.info("User select save button");
			edituseraccess.closeButton.click();
			Log.info("User 805680 got full access on mobile_phone table");
		}		
		
	}
	
	@When("^User navigates to the Classic Models - Mobile_Phone table in DATA module$")
	public void user_navigates_to_the_Classic_Models_Mobile_Phone_table_in_DATA_module() throws Throwable {
	   
		WebElement serverInData= driver.findElement(By.cssSelector("li[id='server6'][class='treeview']"));
		serverInData.click();
		WebElement tableInData = driver.findElement(By.cssSelector("a[class='table-data'][data-servername='classicmodel'][data-serverid='6'][data-tablename='mobile_phone'][data-id='7065']"));
		Assert.assertTrue(tableInData.isDisplayed());
		Log.info("Table is visible in Data module");
		tableInData.click();
		Thread.sleep(2000);
		Log.info("User navigates to DATA module");
	}

	@And("^User press New button$")
	public void user_press_New_button() throws Throwable {
		
		DataModulePage datamodule= new DataModulePage(driver);
		datamodule.newButton.click();
		Log.info("User select new button");
		
	   
	}

	@And("^User populate all fields on the create new entry form$")
	public void user_populate_all_fields_on_the_create_new_entry_form() throws Throwable {
		
	   
		
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
		oselect.selectByValue("6");
		Log.info("User populate all fields on the form");
		
	   
	}

	@And("^Select Create button$")
	public void select_Create_button() throws Throwable {
		
		driver.findElement(By.cssSelector("button[class='btn'][tabindex='0']")).click();
		Thread.sleep(2000);
		Log.info("User press Create button");
	    
	}

	@Then("^New entry is successfully saved$")
	public void new_entry_is_successfully_saved() throws Throwable {
	  
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
				if (cellText.contains(mobileName))
				{
					Log.info("New record is successfully created");
					break;
				}
				else
				{
					if(column<=columns_count) continue;
					Log.error("Error:New record is not created successfully");
				}
				
			}
		}
		
	}
	
	@And("^User successfully removed new record from database$")
	public void User_successfully_removed_new_record_from_database() throws Throwable {
		
		driver.findElement(By.xpath(".//*[@id='table_data']/tbody/tr[6]/td[1]")).click();
		driver.findElement(By.cssSelector("a[class='dt-button buttons-selected buttons-remove']")).click();
		driver.findElement(By.cssSelector("button[class='btn'][tabindex='0']")).click();
		Thread.sleep(2000);
		Log.info("User remove the record");
		
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
				if (cellText.contains(mobileName))
				{
					Log.error("Error: User is still displayed on the list");
					break;
				}
				else
				{
					Log.info("User is successfully deleted and it is not displayed on the list");
				}
				
			}
		}
	   
	    
	}
	

}
