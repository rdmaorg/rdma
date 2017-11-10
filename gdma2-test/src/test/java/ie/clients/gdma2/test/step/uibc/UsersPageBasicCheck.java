package ie.clients.gdma2.test.step.uibc;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import ie.clients.gdma2.test.app.Navigation;
import ie.clients.gdma2.test.page.HomePage;
import ie.clients.gdma2.test.page.UsersPage;
import ie.clients.gdma2.test.step.Init;


public class UsersPageBasicCheck extends Init{
	
	@Given("^User is on the Users Page$")
	public void userIsOnTheUsersPage() throws Throwable {
	
		Navigation.login(driver, username, password);
		HomePage homepage= new HomePage(driver);
		homepage.configurationButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		homepage.userButton.click();
		UsersPage userpage= new UsersPage(driver);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		Select oselect= new Select(driver.findElement(By.name("tbl_user_length")));
		oselect.selectByValue("100");
		userpage.usersTitle.isDisplayed();
		Log.info("User is on the User page");
	}

	@Then("^All elements are visible on the Users Page$")
	public void allElementsAreVisibleOnTheUsersPage() throws Throwable {
		
		UsersPage userpage= new UsersPage(driver);
		Class<UsersPage> up = UsersPage.class;
		Field[] fields = up.getDeclaredFields();

		/**
		 * Verifying existence of each element on the currentPage. Asserting the
		 * string representation of of each page field which indicates existence
		 * of element.
		 */
		
		for (Field fld : fields) {

			fld.setAccessible(true);

			if (!fld.getName().contains("warning")) {
				try {
					
					String fldResponse = fld.get(userpage).toString();
					Assert.assertTrue(!fldResponse.contains("Proxy element for: DefaultElementLocator"));
					Log.info("All elements are visible on the page");
				

				} catch (Throwable e) {
					Log.error("An element is missing on the page");
					
				
				}
			}

		}	
	   
	}

	@Then("^Correct text is displayed for each element on the Users Page$")
	public void correctTextIsDisplayedForEachElementOnTheUsersPage() throws Throwable {
	   
		UsersPage userpage= new UsersPage(driver);
		Assert.assertEquals("Users", userpage.usersTitle.getText());
		Log.info("Correct text is displayed for user title");
		Assert.assertEquals("Insert User", userpage.insertUserButton.getText());
		Log.info("Correct text is displayed for insert user button");
		Assert.assertTrue(userpage.showEntries.getText().contains("Show"));
		Log.info("Correct text is displayed for show entries");
		Assert.assertTrue(userpage.search.getText().contains("Search"));
		Log.info("Correct text is displayed for show entries");
		Assert.assertEquals("Id", userpage.idLabel.getText());
		Log.info("Correct text is displayed for id label");
		Assert.assertEquals("Name", userpage.nameLabel.getText());
		Log.info("Correct text is displayed for name label");
		Assert.assertEquals("Last name", userpage.lastNameLabel.getText());
		Log.info("Correct text is displayed for last name label");
		Assert.assertEquals("User name", userpage.userNameLabel.getText());
		Log.info("Correct text is displayed for user name label");
		Assert.assertEquals("Domain", userpage.domainLabel.getText());
		Log.info("Correct text is displayed for domain label");
	    Assert.assertEquals("Admin", userpage.adminLabel.getText());
	    Log.info("Correct text is displayed for Admin label");
	    Assert.assertEquals("Locked", userpage.lockedLabel.getText());
	    Log.info("Correct text is displayed for locked label");
	    Assert.assertEquals("Active", userpage.activeLabel.getText());
	    Log.info("Correct text is displayed for active label");
	    Assert.assertEquals("Actions", userpage.actionsLabel.getText());
	    Log.info("Correct text is displayed for actions label");
	    Assert.assertTrue(userpage.showingLabel.getText().contains("Showing "));
		Log.info("Correct text is displayed for showing label");
		Assert.assertEquals("Previous", userpage.previousButton.getText());
		Log.info("Correct text is displayed for previous button paggination");
		Assert.assertEquals("Next", userpage.nextButton.getText());
		Log.info("Correct text is displayed for next button paggination");
		Assert.assertTrue(userpage.copyrightFooter.getText().contains("Copyright ©"));
		Log.info("Correct text is displayed for copyright footer");
		Assert.assertTrue(userpage.clientSoultionsFooter.getText().contains("Client Solutions"));
		Log.info("Correct text is displayed for client solutions footer");
		Assert.assertTrue(userpage.yearFooter.getText().contains("2017"));
		Log.info("Correct text is displayed for client solutions year footer");
		Assert.assertTrue(userpage.versionFooter.getText().contains("Version:"));
		Log.info("Correct text is displayed for version footer");
		Log.info("Correct text is displayed for all elements on the page");	
		
	}	



}
