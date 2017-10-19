package stepDefinition;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import appModules.ElementPresence;
import pageObjects.DATAModule_Page;
import pageObjects.EditUserAccess_Page;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class EditUserAccess extends Init{
	
    // Full access
	
	@When("^User choose one user from the list and grant him full access rights$")
	public void user_choose_one_user_from_the_list_and_grant_him_full_access_rights() throws Throwable {
		
		EditUserAccess_Page edituseraccess= new EditUserAccess_Page(driver);
		
		WebElement fullAccessCheckbox= driver.findElement(By.id("174fullA"));
		if(fullAccessCheckbox.isSelected())
		{
			Log.info("User 805680 has already have full access on customers table");
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
			Log.info("User 805680 got full access on customers table");
		}
	   
	}

	@Then("^Selected user has full access rights$")
	public void selected_user_has_full_access_rights() throws Throwable {
		
		WebElement serverInData= driver.findElement(By.cssSelector("li[id='server6'][class='treeview']"));
		serverInData.click();
		WebElement tableInData = driver.findElement(By.cssSelector("a[class='table-data'][data-servername='classicmodel'][data-serverid='6'][data-tablename='customers'][data-id='185']"));
		Assert.assertTrue(tableInData.isDisplayed());
		Log.info("Table is visible in Data module");
		tableInData.click();
		DATAModule_Page datamodule= new DATAModule_Page(driver);
		datamodule.newButton.isDisplayed();
		datamodule.editButton.isDisplayed();
		datamodule.deleteButton.isDisplayed();
		Log.info("User has full access rights");
		
	   
	}
	
	//Display access
	
	@When("^User choose one user from the list and grant him only display  rights$")
	public void user_choose_one_user_from_the_list_and_grant_him_only_display_rights() throws Throwable {
		
		
		EditUserAccess_Page edituseraccess= new EditUserAccess_Page(driver);
		
		WebElement fullAccessCheckbox= driver.findElement(By.id("174fullA"));
		WebElement displayCheckbox= driver.findElement(By.id("174allowD"));
		
		if(fullAccessCheckbox.isSelected())
		{
			Log.info("User 805680 has already have full access on customers table");
		    fullAccessCheckbox.click();
		    displayCheckbox.click();
		    edituseraccess.saveButton.click();
			Log.info("User select confirm yes button");
			WebElement confirmSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success']"));
			confirmSave.click();
			Log.info("User select save button");
			edituseraccess.closeButton.click();
			Log.info("User 805680 got display access on customers table");
		    
			
		}
		else
		{
			fullAccessCheckbox.click();
			fullAccessCheckbox.click();
			displayCheckbox.click();
			edituseraccess.saveButton.click();
			Log.info("User select confirm yes button");
			WebElement confirmSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success']"));
			confirmSave.click();
			Log.info("User select save button");
			edituseraccess.closeButton.click();
			Thread.sleep(2000);
			Log.info("User 805680 got display access on customers table");
		}
	   
	}

	@Then("^Selected user has display access rights$")
	public void selected_user_has_display_access_rights() throws Throwable {
		
		WebElement serverInData= driver.findElement(By.cssSelector("li[id='server6'][class='treeview']"));
		serverInData.click();
		WebElement tableInData = driver.findElement(By.cssSelector("a[class='table-data'][data-servername='classicmodel'][data-serverid='6'][data-tablename='customers'][data-id='185']"));
		Assert.assertTrue(tableInData.isDisplayed());
		Log.info("Table is visible in Data module");
		tableInData.click();
		Thread.sleep(3000);
		boolean value1=ElementPresence.isElementPresence(driver, By.cssSelector("a[class='dt-button buttons-create']"));
		Assert.assertFalse(value1);
		boolean value2=ElementPresence.isElementPresence(driver, By.cssSelector("a[class='dt-button buttons-selected buttons-edit']"));
		Assert.assertFalse(value2);
		boolean value3=ElementPresence.isElementPresence(driver, By.cssSelector("a[class='dt-button buttons-selected buttons-remove']"));
		Assert.assertFalse(value3);
		Log.info("User has only display access rights");
	  
	}
	
	//Update access
	
	@When("^User choose one user from the list and grant him only update rights$")
	public void user_choose_one_user_from_the_list_and_grant_him_only_update_rights() throws Throwable {
		
        EditUserAccess_Page edituseraccess= new EditUserAccess_Page(driver);
		
		WebElement fullAccessCheckbox= driver.findElement(By.id("174fullA"));
		WebElement updateCheckbox= driver.findElement(By.id("174allowU"));
		
		if(fullAccessCheckbox.isSelected())
		{
			Log.info("User 805680 has already have full access on customers table");
		    fullAccessCheckbox.click();
		    updateCheckbox.click();
		    edituseraccess.saveButton.click();
			Log.info("User select confirm yes button");
			WebElement confirmSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success']"));
			confirmSave.click();
			Log.info("User select save button");
			edituseraccess.closeButton.click();
			Log.info("User 805680 got update access on customers table");
		    
			
		}
		else
		{
			fullAccessCheckbox.click();
			fullAccessCheckbox.click();
			updateCheckbox.click();
			edituseraccess.saveButton.click();
			Log.info("User select confirm yes button");
			WebElement confirmSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success']"));
			confirmSave.click();
			Log.info("User select save button");
			edituseraccess.closeButton.click();
			Thread.sleep(2000);
			Log.info("User 805680 got update access on customers table");
		}
	    
	}

	@Then("^Selected user has update access rights$")
	public void selected_user_has_update_access_rights() throws Throwable {
	    
		WebElement serverInData= driver.findElement(By.cssSelector("li[id='server6'][class='treeview']"));
		serverInData.click();
		WebElement tableInData = driver.findElement(By.cssSelector("a[class='table-data'][data-servername='classicmodel'][data-serverid='6'][data-tablename='customers'][data-id='185']"));
		Assert.assertTrue(tableInData.isDisplayed());
		Log.info("Table is visible in Data module");
		tableInData.click();
		Thread.sleep(3000);
		DATAModule_Page datamodule= new DATAModule_Page(driver);
		datamodule.editButton.isDisplayed();
		boolean value1=ElementPresence.isElementPresence(driver, By.cssSelector("a[class='dt-button buttons-create']"));
		Assert.assertFalse(value1);
		boolean value3=ElementPresence.isElementPresence(driver, By.cssSelector("a[class='dt-button buttons-selected buttons-remove']"));
		Assert.assertFalse(value3);
		Log.info("User has only update access rights");
		
	}
	
	//Insert access
	
	@When("^User choose one user from the list and grant him only insert rights$")
	public void user_choose_one_user_from_the_list_and_grant_him_only_insert_rights() throws Throwable {
	    
        EditUserAccess_Page edituseraccess= new EditUserAccess_Page(driver);
		
		WebElement fullAccessCheckbox= driver.findElement(By.id("174fullA"));
		WebElement insertCheckbox= driver.findElement(By.id("174allowI"));
		
		if(fullAccessCheckbox.isSelected())
		{
			Log.info("User 805680 has already have full access on customers table");
		    fullAccessCheckbox.click();
		    insertCheckbox.click();
		    edituseraccess.saveButton.click();
			Log.info("User select confirm yes button");
			WebElement confirmSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success']"));
			confirmSave.click();
			Log.info("User select save button");
			edituseraccess.closeButton.click();
			Log.info("User 805680 got insert access on customers table");
		    
			
		}
		else
		{
			fullAccessCheckbox.click();
			fullAccessCheckbox.click();
			insertCheckbox.click();
			edituseraccess.saveButton.click();
			Log.info("User select confirm yes button");
			WebElement confirmSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success']"));
			confirmSave.click();
			Log.info("User select save button");
			edituseraccess.closeButton.click();
			Thread.sleep(2000);
			Log.info("User 805680 got insert access on customers table");
		}
		
	}

	@Then("^Selected user has insert access rights$")
	public void selected_user_has_insert_access_rights() throws Throwable {
		
		WebElement serverInData= driver.findElement(By.cssSelector("li[id='server6'][class='treeview']"));
		serverInData.click();
		WebElement tableInData = driver.findElement(By.cssSelector("a[class='table-data'][data-servername='classicmodel'][data-serverid='6'][data-tablename='customers'][data-id='185']"));
		Assert.assertTrue(tableInData.isDisplayed());
		Log.info("Table is visible in Data module");
		tableInData.click();
		Thread.sleep(3000);
		DATAModule_Page datamodule= new DATAModule_Page(driver);
		datamodule.newButton.isDisplayed();
		boolean value1=ElementPresence.isElementPresence(driver, By.cssSelector("a[class='dt-button buttons-selected buttons-edit']"));
		Assert.assertFalse(value1);
		boolean value3=ElementPresence.isElementPresence(driver, By.cssSelector("a[class='dt-button buttons-selected buttons-remove']"));
		Assert.assertFalse(value3);
		Log.info("User has only update access rights");
	    
	}
	
	//Delete access
	
	@When("^User choose one user from the list and grant him only delete rights$")
	public void user_choose_one_user_from_the_list_and_grant_him_only_delete_rights() throws Throwable {
	    
        EditUserAccess_Page edituseraccess= new EditUserAccess_Page(driver);
		
		WebElement fullAccessCheckbox= driver.findElement(By.id("174fullA"));
		WebElement deleteCheckbox= driver.findElement(By.id("174allowDel"));
		
		if(fullAccessCheckbox.isSelected())
		{
			Log.info("User 805680 has already have full access on customers table");
		    fullAccessCheckbox.click();
		    deleteCheckbox.click();
		    edituseraccess.saveButton.click();
			Log.info("User select confirm yes button");
			WebElement confirmSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success']"));
			confirmSave.click();
			Log.info("User select save button");
			edituseraccess.closeButton.click();
			Log.info("User 805680 got delete access on customers table");
		    
			
		}
		else
		{
			fullAccessCheckbox.click();
			fullAccessCheckbox.click();
			deleteCheckbox.click();
			edituseraccess.saveButton.click();
			Log.info("User select confirm yes button");
			WebElement confirmSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success']"));
			confirmSave.click();
			Log.info("User select save button");
			edituseraccess.closeButton.click();
			Thread.sleep(2000);
			Log.info("User 805680 got delete access on customers table");
		}
		
	}

	@Then("^Selected user has delete access rights$")
	public void selected_user_has_delete_access_rights() throws Throwable {

		WebElement serverInData= driver.findElement(By.cssSelector("li[id='server6'][class='treeview']"));
		serverInData.click();
		WebElement tableInData = driver.findElement(By.cssSelector("a[class='table-data'][data-servername='classicmodel'][data-serverid='6'][data-tablename='customers'][data-id='185']"));
		Assert.assertTrue(tableInData.isDisplayed());
		Log.info("Table is visible in Data module");
		tableInData.click();
		Thread.sleep(3000);
		DATAModule_Page datamodule= new DATAModule_Page(driver);
		datamodule.deleteButton.isDisplayed();
		boolean value1=ElementPresence.isElementPresence(driver, By.cssSelector("a[class='dt-button buttons-selected buttons-edit']"));
		Assert.assertFalse(value1);
		boolean value3=ElementPresence.isElementPresence(driver, By.cssSelector("a[class='dt-button buttons-create']"));
		Assert.assertFalse(value3);
		Log.info("User has only delete access rights");
	}

}
