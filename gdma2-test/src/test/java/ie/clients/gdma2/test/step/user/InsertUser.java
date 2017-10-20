package ie.clients.gdma2.test.step.user;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.page.InsertNewUserPage;
import ie.clients.gdma2.test.page.UsersPage;
import ie.clients.gdma2.test.step.Init;
import ie.clients.gdma2.test.utility.PropertyHandler;


public class InsertUser extends Init{
	
	String name= PropertyHandler.getPropertyFrom("src/test/resources/testData/User1.properties", "name");
	String lastName=PropertyHandler.getPropertyFrom("src/test/resources/testData/User1.properties", "lastName");
	String userName=PropertyHandler.getPropertyFrom("src/test/resources/testData/User1.properties", "userName");
	String domain=PropertyHandler.getPropertyFrom("src/test/resources/testData/User1.properties", "domain");
	
	String name2= PropertyHandler.getPropertyFrom("src/test/resources/testData/User2.properties", "name");
	String lastName2=PropertyHandler.getPropertyFrom("src/test/resources/testData/User2.properties", "lastName");
	String userName2=PropertyHandler.getPropertyFrom("src/test/resources/testData/User2.properties", "userName");
	String domain2=PropertyHandler.getPropertyFrom("src/test/resources/testData/User2.properties", "domain");
	
	@When("^User press Insert User button$")
	public void user_press_Insert_User_button() throws Throwable {
		
		UsersPage userpage= new UsersPage(driver);
		userpage.insertUserButton.click();
		Log.info("User select insert user button");
	    
	}

	@And("^Populate all fields on the Insert new User form$")
	public void populate_all_fields_on_the_Insert_new_User_form() throws Throwable {
		
		InsertNewUserPage insertNewUser= new InsertNewUserPage(driver);
		insertNewUser.nameField.sendKeys(name);
		insertNewUser.lastNameField.sendKeys(lastName);
		insertNewUser.userNameField.sendKeys(userName);
		insertNewUser.domainField.sendKeys(domain);
		Log.info("User populate all fields on the form");
	}
	
	@And("^User set admin button to Yes$")
	public void user_set_admin_button_to_Yes() throws Throwable {
		
		InsertNewUserPage insertNewUser= new InsertNewUserPage(driver);
		insertNewUser.adminButton.click();
		Log.info("User select admin button");
	    
	}

	@And("^Press Save button on the Insert New User form$")
	public void press_Save_button_on_the_Insert_New_User_form() throws Throwable {
		
		InsertNewUserPage insertNewUser= new InsertNewUserPage(driver);
		insertNewUser.saveButton.click();
		Log.info("User select save button on the form");  
	}

	@And("^Press Save User button to confirm the save$")
	public void press_Save_User_button_to_confirm_the_save() throws Throwable {
		
		WebElement confirmSave= driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success']"));
		confirmSave.click();
		Thread.sleep(5000);
		driver.navigate().refresh();
		Thread.sleep(2000);
		Log.info("User select confirm save button");
		
	  
	}

	@Then("^New user is created and it is displayed on the users list page$")
	public void new_user_is_created_and_it_is_displayed_on_the_users_list_page() throws Throwable {
		
		WebElement table= driver.findElement(By.cssSelector("table[id='tbl_user']")); //locate table
		List<WebElement>row_table=table.findElements(By.tagName("tr")); //locate rows
		int numberOfRows=row_table.size(); //calculate number of rows
		
		for (int i=0; i<numberOfRows; i++) //loop will execute till the last row of table
		{
			List<WebElement>columns_table=row_table.get(i).findElements(By.tagName("td")); //locate columns
			int columns_count=columns_table.size();
			
			for (int column = 0; column < columns_count; column++)
			{
				String cellText= columns_table.get(column).getText();
				if (cellText.contains(name) || cellText.contains(name2))
				{
					Log.info("New user is successfully created");
				}
				else
				{
					Log.error("Error:New user is not created successfully");
				}
				
			}
		}
		
		
	 
	}
	

	@And("^Populate all fields on the Insert new form for No Admin User$")
	public void populate_all_fields_on_the_Insert_new_User_form_for_No_Admin_User() throws Throwable {
		
		InsertNewUserPage insertNewUser= new InsertNewUserPage(driver);
		insertNewUser.nameField.sendKeys(name2);
		insertNewUser.lastNameField.sendKeys(lastName2);
		insertNewUser.userNameField.sendKeys(userName2);
		insertNewUser.domainField.sendKeys(domain2);
		Log.info("User populate all fields on the form");
	}
	
	// Scenario: Insert new user - fail
	
	@And("^User doesnt populate any field on the Insert new user form$")
	public void user_doesnt_populate_any_field_on_the_Insert_new_user_form() throws Throwable {
		
		InsertNewUserPage insertNewUser= new InsertNewUserPage(driver);
		insertNewUser.nameField.clear();
		insertNewUser.lastNameField.clear();
		insertNewUser.userNameField.clear();
		insertNewUser.domainField.clear();
		Log.info("User doesn't populate any field on the form");
		
	}

	@Then("^Create new user action faild and correct warning message is displayed$")
	public void create_new_user_action_faild_and_correct_warning_message_is_displayed() throws Throwable {
		
		InsertNewUserPage insertNewUser= new InsertNewUserPage(driver);
		insertNewUser.nameWarning.isDisplayed();
		insertNewUser.lastNameWarning.isDisplayed();
		insertNewUser.userNameWarning.isDisplayed();
		insertNewUser.domainWarning.isDisplayed();
		Log.info("Correct warning messages are displayed");
	
	}
	


}
