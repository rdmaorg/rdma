package stepDefinition;

import org.openqa.selenium.By;
import pageObjects.LogIn_Page;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginSuccess extends Init{
	
	
	@When("^User enters valid username and password$")
	public void user_enters_valid_username_and_password() throws Throwable {
	
		LogIn_Page login= new LogIn_Page(driver);
		login.usernameField.sendKeys(username);
		login.passwordField.sendKeys(password);
		Log.info("User enter valid credentials");
	}

	@And("^User press Login button$")
	public void user_press_Login_button() throws Throwable {
	   
		LogIn_Page login = new LogIn_Page(driver);
		login.loginButton.click();
		Log.info("User press login button");
	}

	@Then("^User is successfully logged in$")
	public void user_is_successfully_logged_in() throws Throwable {
		
		driver.findElement(By.cssSelector("a[class='dropdown-toggle'][data-toggle='dropdown']"));
		Log.info("User is successfully logged in");
		
	   
	}

}
