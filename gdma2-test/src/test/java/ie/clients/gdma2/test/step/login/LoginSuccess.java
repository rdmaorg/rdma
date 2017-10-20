package ie.clients.gdma2.test.step.login;

import org.openqa.selenium.By;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.page.LoginPage;
import ie.clients.gdma2.test.step.Init;

public class LoginSuccess extends Init{
	
	
	@When("^User enters valid username and password$")
	public void user_enters_valid_username_and_password() throws Throwable {
	
		LoginPage login= new LoginPage(driver);
		login.usernameField.sendKeys(username);
		login.passwordField.sendKeys(password);
		Log.info("User enter valid credentials");
	}

	@And("^User press Login button$")
	public void user_press_Login_button() throws Throwable {
	   
		LoginPage login = new LoginPage(driver);
		login.loginButton.click();
		Log.info("User press login button");
	}

	@Then("^User is successfully logged in$")
	public void user_is_successfully_logged_in() throws Throwable {
		
		driver.findElement(By.cssSelector("a[class='dropdown-toggle'][data-toggle='dropdown']"));
		Log.info("User is successfully logged in");
		
	   
	}

}
