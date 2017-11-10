package ie.clients.gdma2.test.step.server;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.app.ElementPresence;
import ie.clients.gdma2.test.step.Init;


public class DeactivateServer extends Init {

	@When("^User choose one active server and grant full access on one table within this server$")
	public void userChooseOneActiveServerFromTheListAndSelectEditButton() throws Throwable {
	    
		WebElement editButton = driver.findElement(By.cssSelector("button[class='btn btn-primary btn-xs editServer'][data-serverid='9']"));
		editButton.click();
		WebElement activeButton= driver.findElement(By.cssSelector("div[class='bootstrap-switch bootstrap-switch-wrapper bootstrap-switch-on bootstrap-switch-id-active bootstrap-switch-animate']"));
		//WebElement serverDataModule = driver.findElement(By.id("server6"));
		if (activeButton.isDisplayed() && driver.findElement(By.id("server9")).isDisplayed())
		{
			Log.info("User choose one active server where he has access rights");
			activeButton.click();
			Log.info("User deactivate the server");
		}
		else
		{
			Log.info("'ClassicModel' server is not active or user doesn't have access rights! Please check");
		}
	}

	@And("^User select edit button$")
	public void userSelectEditBbutton() throws Throwable {
	  //implemented in previous step
	}
	
	@And("^User select active button$")
	public void userSelectActiveButton() throws Throwable {
		//implemented in previous step
	}

	@Then("^Server is successfully deactivate and users who has access on this server cannot see them in data module$")
	public void serverIsSuccessfullyDeactivateAndUsersWhoHasAccessOnThisServerCannotSeeThemInDataModule() throws Throwable {

		
		boolean value2= ElementPresence.isElementPresence(driver, By.id("server9"));
		Assert.assertFalse("Inactive server is not visible", value2);
		Log.info("Server is successfully deactivated");
	    /*
		if (driver.findElement(By.id("server9")).isDisplayed())
		{
			Log.error("Error: Server is still displayed in data module");
		}
		else
		{
			Log.info("Server is successfully deactivated");
		}
		*/
		
		//set server with id=9 to be active again
		
		WebElement editButton = driver.findElement(By.cssSelector("button[class='btn btn-primary btn-xs editServer'][data-serverid='9']"));
		editButton.click();
		WebElement activeButton= driver.findElement(By.cssSelector("span[class='bootstrap-switch-label']"));
		activeButton.click();
		driver.findElement(By.id("Save-server")).click();
		driver.findElement(By.cssSelector("a[class='btn btn-sm btn-success'][data-apply='confirmation']")).click();
		Log.info("User set server ID=9 to be active again");
	}

}
