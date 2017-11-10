package ie.clients.gdma2.test.step.login;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.page.LoginPage;
import ie.clients.gdma2.test.step.Init;

public class LoginFail extends Init{

	//  Scenario: Login Fail - invalid credentials
	
	@When("^User enters invalid username and password$")
	public void userEntersInvalidUsernameAndPassword() throws Throwable {
		LoginPage login= new LoginPage(driver);
		login.usernameField.sendKeys("test1");
		login.passwordField.sendKeys("test2");
		Log.info("User enters invalid credentials");
		
	}


	@Then("^Login failed and correct warning message is displayed$")
	public void loginFailedAndCorrectWarningMessageIsDisplayed() throws Throwable {
		
		LoginPage login= new LoginPage(driver);
		login.warningInvalidCredentials.isDisplayed();
		login.warningInvalidCredentials.getText().contains("Invalid credentials.");
		Log.info("Login Failed and Correct warning message is displayed");
	    
	}

	//Scenario: Login Fail - without populating any field on the form
	
	@When("^User doesnt populate any field on the form$")
	public void userDoesntPopulateAnyFieldOnTheForm() throws Throwable {
	 
		LoginPage login= new LoginPage(driver);
		login.usernameField.clear();
		login.passwordField.clear();
		Log.info("User doesn't populate any field on the form");
		
	}

	@Then("^Login failed and correct warning message (\\d+) is displayed$")
	public void loginFailedAndCorrectWarningMessageIsDisplayed(int arg1) throws Throwable {
	    
		LoginPage login = new LoginPage(driver);
		login.warningUsernameError.isDisplayed();
		login.warningPasswordError.isDisplayed();
		login.warningUsernameError.getText().contains("Please enter the required information");
		login.warningPasswordError.getText().contains("Please enter the required information");
		Log.info("Login Failed and Correct warning message is displayed");
	}
	
	
	//Scenario: Clear function
	
	@And("^User press Clear button$")
	public void userPressClearButton() throws Throwable {
		LoginPage login = new LoginPage(driver);
		login.clearButton.click();
		Log.info("User press Clear button");
	
	}

	@Then("^User credentials are successfully deleted$")
	public void userCredentialsAreSuccessfullyDeleted() throws Throwable {
		
		LoginPage login = new LoginPage(driver);
		login.usernameField.getText().isEmpty();
		login.passwordField.getText().isEmpty();
		Log.info("Username and passsword are successfully removed");
	   
	}
	
	
	
	
}
