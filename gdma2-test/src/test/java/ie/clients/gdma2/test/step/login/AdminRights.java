package ie.clients.gdma2.test.step.login;

import org.junit.Assert;
import org.openqa.selenium.By;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import ie.clients.gdma2.test.app.Navigation;
import ie.clients.gdma2.test.step.Init;


public class AdminRights extends Init{

  
	@Given("^Log in as admin user$")
	public void logInAsAdminUser() throws Throwable {
	    
		Navigation.login(driver, username, password);
		Log.info("User logged in as admin user");
	}

	@Then("^Admin user is able to see configuration button$")
	public void isConfigurationButtonDisplayed() throws Throwable {
		
		 Assert.assertTrue("Configuration button is displayed on the page", driver.findElement(By.id("btn-admin")).isDisplayed());
		 Log.info("Admin user can see configuration button");
	  
	}

}
