package ie.clients.gdma2.test.step.table;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.page.EditTableAliasPage;
import ie.clients.gdma2.test.step.Init;


public class EditTableAlias extends Init {


	@When("^User press Edit Alias button$")
	public void user_press_Edit_Alias_button() throws Throwable {
	  
		WebElement editAliasButton=driver.findElement(By.cssSelector("button[class='btn btn-primary btn-xs editTable'][data-tableid='185']"));
		editAliasButton.click();
		Log.info("User select edit alias button for 'customers' table");
		
	}

	@And("^User edit alias field$")
	public void user_edit_alias_field() throws Throwable {

		WebElement elementName= driver.findElement(By.id("tableNameModal"));
		String name = elementName.getAttribute("value");
		Log.info(name);
		WebElement elementAlias= driver.findElement(By.id("alias"));
		String alias = elementAlias.getAttribute("value");
		Log.info(alias);
		if (name.equals(alias))
		{
			Log.info("Name and alias are the same");
			elementAlias.clear();
			elementAlias.sendKeys(tableAlias);
			Log.info("User changed the alias");
		}
		else
		{
			Log.error("Name and alias values are not the same");
			elementAlias.clear();
			elementAlias.sendKeys(name);
			EditTableAliasPage edittablealias= new EditTableAliasPage(driver);
			edittablealias.saveButton.click();
			edittablealias.confirmSave.click();
			Thread.sleep(2000);
			Log.info("Now name and alias values are the same");
			WebElement editAliasButton=driver.findElement(By.cssSelector("button[class='btn btn-primary btn-xs editTable'][data-tableid='185']"));
			editAliasButton.click();
			elementAlias.clear();
			elementAlias.sendKeys(tableAlias);
			Log.info("User changed the alias");
			
			
			
		}
	}
	
	@And("^Press submit button on the edit Alias page$")
	public void press_submit_button_on_the_edit_Alias_page() throws Throwable {
		
		EditTableAliasPage edittablealias= new EditTableAliasPage(driver);
		edittablealias.saveButton.click();
		Log.info("User select save button");
		 
	}

	@And("^Confirm save alias action by clicking on the Yes button$")
	public void confirm_save_alias_action_by_clicking_on_the_Yes_button() throws Throwable {
	   
		EditTableAliasPage edittablealias= new EditTableAliasPage(driver);
		edittablealias.confirmSave.click();
		Thread.sleep(2000);
		Log.info("User select confirm save button");
		
	}

	

	@Then("^Table alias is successfully edit$")
	public void table_alias_is_successfully_edit() throws Throwable {
		
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
				if (cellText.contains(tableAlias))
				{
					Log.info("Alis is successfully update");
					break;
				}
				else
		
				{
					if(column<=columns_count) continue;
					Log.error("Error:Alis is not updated successfully");
				}
				
			}
		
		}	
	}
	
	// test
	/*
	@When("^Find the value for customer alias$")
	public void find_press_Edit_Alias_button() throws Throwable {
	  
		WebElement element= driver.findElement(By.xpath(".//*[@id='tbl_tables']/tbody/tr[1]/td[3]"));
		String valelm=element.getText();
		Log.info(valelm);
		
	}
	
	*/

}
