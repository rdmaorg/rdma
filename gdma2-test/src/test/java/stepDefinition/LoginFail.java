package stepDefinition;

import pageObjects.LogIn_Page;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginFail extends Init{

	//  Scenario: Login Fail - invalid credentials
	
	@When("^User enters invalid username and password$")
	public void user_enters_invalid_username_and_password() throws Throwable {
		LogIn_Page login= new LogIn_Page(driver);
		login.usernameField.sendKeys("test1");
		login.passwordField.sendKeys("test2");
		Log.info("User enters invalid credentials");
		
	}


	@Then("^Login failed and correct warning message is displayed$")
	public void login_failed_and_correct_warning_message_is_displayed() throws Throwable {
		
		LogIn_Page login= new LogIn_Page(driver);
		login.warningInvalidCredentials.isDisplayed();
		login.warningInvalidCredentials.getText().contains("Invalid credentials.");
		Log.info("Login Failed and Correct warning message is displayed");
	    
	}

	//Scenario: Login Fail - without populating any field on the form
	
	@When("^User doesnt populate any field on the form$")
	public void user_doesnt_populate_any_field_on_the_form() throws Throwable {
	 
		LogIn_Page login= new LogIn_Page(driver);
		login.usernameField.clear();
		login.passwordField.clear();
		Log.info("User doesn't populate any field on the form");
		
	}

	@Then("^Login failed and correct warning message (\\d+) is displayed$")
	public void login_failed_and_correct_warning_message_is_displayed(int arg1) throws Throwable {
	    
		LogIn_Page login = new LogIn_Page(driver);
		login.warningUsernameError.isDisplayed();
		login.warningPasswordError.isDisplayed();
		login.warningUsernameError.getText().contains("Please enter the required information");
		login.warningPasswordError.getText().contains("Please enter the required information");
		Log.info("Login Failed and Correct warning message is displayed");
	}
	
	
	//Scenario: Clear function
	
	@And("^User press Clear button$")
	public void user_press_Clear_button() throws Throwable {
		LogIn_Page login = new LogIn_Page(driver);
		login.clearButton.click();
		Log.info("User press Clear button");
	
	}

	@Then("^User credentials are successfully deleted$")
	public void user_credentials_are_successfully_deleted() throws Throwable {
		
		LogIn_Page login = new LogIn_Page(driver);
		login.usernameField.getText().isEmpty();
		login.passwordField.getText().isEmpty();
		Log.info("Username and passsword are successfully removed");
	   
	}
	
	
	
	
}
