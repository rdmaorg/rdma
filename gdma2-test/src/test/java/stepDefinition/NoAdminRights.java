package stepDefinition;

import org.junit.Assert;
import org.openqa.selenium.By;

import pageObjects.LogIn_Page;
import utility.PropertyHandler;
import appModules.ElementPresence;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class NoAdminRights extends Init{

	@Given("^Log in as NoAdmin user$")
	public void log_in_as_NoAdmin_user() throws Throwable {
		   
		   String usernameNoAdmin= PropertyHandler.getPropretyFrom("src/test/resources/testData/NoAdminUser.properties", "username");
		   String passwordNoAdmin=PropertyHandler.getPropretyFrom("src/test/resources/testData/NoAdminUser.properties", "password");
		   driver.get(appURL);
		   LogIn_Page login=new LogIn_Page(driver);
		   login.usernameField.sendKeys(usernameNoAdmin);
		   login.passwordField.sendKeys(passwordNoAdmin);
		   login.loginButton.click();
		   Log.info("User logged in as No Admin user");
	}

	@Then("^No Admin user doesnt see configuration button$")
	public void no_Admina_user_doesnt_see_configuration_button() throws Throwable { 
		   
		  // Assert.assertFalse(driver.findElement(By.id("btn-admin")).isDisplayed());
		 
		   boolean value=ElementPresence.isElementPresence(driver, By.id("btn-admin"));
		   Assert.assertFalse("No Admin user cannot see configuration button", value);
		   Log.info("No Admin user cannot see configuration button");

	}
}
