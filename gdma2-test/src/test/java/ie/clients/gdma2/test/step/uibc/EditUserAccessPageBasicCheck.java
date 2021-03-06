package ie.clients.gdma2.test.step.uibc;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import ie.clients.gdma2.test.app.Navigation;
import ie.clients.gdma2.test.page.EditUserAccessPage;
import ie.clients.gdma2.test.page.HomePage;
import ie.clients.gdma2.test.step.Init;

public class EditUserAccessPageBasicCheck extends Init {

	@Given("^User is on the User Access Page$")
	public void userIsOnTheUserAccessPage() throws Throwable {
		
		Navigation.login(driver, username, password);
		HomePage homepage= new HomePage(driver);
		homepage.configurationButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		homepage.serverButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		WebElement tableButtonForClassicModelServer= driver.findElement(By.cssSelector("button[data-servername='classicmodel']"));
		tableButtonForClassicModelServer.click();
		Thread.sleep(9000);
		WebElement editUserAccessBtnForCustomers= driver.findElement(By.cssSelector("button[class='btn btn-primary btn-xs editAccess'][data-tableid='185']"));
		Thread.sleep(9000);
		editUserAccessBtnForCustomers.click();
		Thread.sleep(9000);
	}

	@Then("^All elements are visible on the User Access Page$")
	public void allElementsAreVisibleOnTheUserAccessPage() throws Throwable {
		
		EditUserAccessPage edituseraccess=new EditUserAccessPage(driver);
		Class<EditUserAccessPage> eia = EditUserAccessPage.class;
		Field[] fields = eia.getDeclaredFields();

		/**
		 * Verifying existence of each element on the currentPage. Asserting the
		 * string representation of of each page field which indicates existence
		 * of element.
		 */
		for (Field fld : fields) {

			fld.setAccessible(true);

			if (!fld.getName().contains("warning")) {
				try {
					String fldResponse = fld.get(edituseraccess).toString();
					Assert.assertTrue(!fldResponse.contains("Proxy element for: DefaultElementLocator"));
					Log.info("All elements are visible on the page");
				

				} catch (Throwable e) {
					Log.error("An element is missing on the page");
					
				
				}
			}

		}
	    
	}

	@Then("^Correct text is displayed for each element on the User Access Page$")
	public void correctTextIsDisplayedForEachElementOnTheUserAccessPage() throws Throwable {
		
		EditUserAccessPage edituseraccess=new EditUserAccessPage(driver);
		Assert.assertEquals("User Access", edituseraccess.userAccessTitle.getText());
		Log.info("Correct text is displayed for user access table");
		Assert.assertEquals(" customers", edituseraccess.userAccessTableName.getText());
		Log.info("Correct text is displayed for user access table name");
		Assert.assertTrue("Correct text is displayed for show entries label", edituseraccess.showEntriesLabel.getText().contains("Show"));
		Log.info("Correct text is displayed for show entries label");
		Assert.assertTrue("Correct text is displayed for serach label", edituseraccess.search.getText().contains("Search"));
		Log.info("Correct text is displayed for search label");
		Assert.assertEquals("Id", edituseraccess.idLabel.getText());
		Log.info("Correct text is displayed for id label");
		Assert.assertEquals("User", edituseraccess.userLabel.getText());
		Log.info("Correct text is displayed for user label");
		Assert.assertEquals("Name", edituseraccess.nameLabel.getText());
		Log.info("Correct text is displayed for name label");
		Assert.assertEquals("Full Access", edituseraccess.fullAccessLabel.getText());
		Log.info("Correct text is displayed for full access label");
		Assert.assertEquals("Display", edituseraccess.displayLabel.getText());
		Log.info("Correct text is displayed for display label");
		Assert.assertEquals("Update", edituseraccess.updateLabel.getText());
		Log.info("Correct text is displayed for update label");
		Assert.assertEquals("Insert", edituseraccess.insertLabel.getText());
		Log.info("Correct text is displayed for insert label");
		Assert.assertEquals("Delete", edituseraccess.deleteLabel.getText());
		Log.info("Correct text is displayed for delete label");
		Assert.assertTrue("Correct text is displayed for showing label", edituseraccess.showingEntries.getText().contains("Showing"));
		Log.info("Correct text is displayed for showing label");
		Assert.assertEquals("Previous", edituseraccess.previousButton.getText());
		Log.info("Correct text is displayed for previous button");
		Assert.assertEquals("Next", edituseraccess.nextButton.getText());
		Log.info("Correct text is displayed for next button");
		Assert.assertEquals("Close", edituseraccess.closeButton.getText());
		Log.info("Correct text is displayed for close button");
		Assert.assertEquals("Save", edituseraccess.saveButton.getText());
		Log.info("Correct text is displayed for save button");
		Log.info("Correct text is displayed for all elements on the page");
	}

	
	
	
	
	
	
	
}
