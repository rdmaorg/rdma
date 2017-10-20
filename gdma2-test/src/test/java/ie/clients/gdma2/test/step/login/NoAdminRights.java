package ie.clients.gdma2.test.step.login;

import org.junit.Assert;
import org.openqa.selenium.By;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import ie.clients.gdma2.test.app.ElementPresence;
import ie.clients.gdma2.test.page.LoginPage;
import ie.clients.gdma2.test.step.Init;
import ie.clients.gdma2.test.utility.PropertyHandler;

public class NoAdminRights extends Init{

	@Given("^Log in as NoAdmin user$")
	public void log_in_as_NoAdmin_user() throws Throwable {
		   
		   String usernameNoAdmin= PropertyHandler.getPropertyFrom("src/test/resources/testData/NoAdminUser.properties", "username");
		   String passwordNoAdmin=PropertyHandler.getPropertyFrom("src/test/resources/testData/NoAdminUser.properties", "password");
		   driver.get(appURL);
		   LoginPage login=new LoginPage(driver);
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
