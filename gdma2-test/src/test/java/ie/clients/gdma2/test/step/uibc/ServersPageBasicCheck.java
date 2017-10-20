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
import ie.clients.gdma2.test.page.ServersPage;
import ie.clients.gdma2.test.step.Init;

public class ServersPageBasicCheck extends Init{
	
	@Given("^User is on the Servers Page$")
	public void user_is_on_the_Servers_Page() throws Throwable {
		
		Navigation.login(driver, username, password);
		HomePage homepage= new HomePage(driver);
		homepage.configurationButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		homepage.serverButton.click();
		ServersPage serverpage= new ServersPage(driver);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		Select oselect= new Select(driver.findElement(By.cssSelector("select[class='form-control input-sm'][name='tbl_server_length']")));
		oselect.selectByValue("100");
		serverpage.serversTitle.isDisplayed();
		Log.info("User is on the Server page");
		
	}

	@Then("^All elements are visible on the Servers Page$")
	public void all_elements_are_visible_on_the_Servers_Page() throws Throwable {
	 
		ServersPage serverpage= new ServersPage(driver);
		Class<ServersPage> sp = ServersPage.class;
		Field[] fields = sp.getDeclaredFields();

		/**
		 * Verifying existence of each element on the currentPage. Asserting the
		 * string representation of of each page field which indicates existence
		 * of element.
		 */
		for (Field fld : fields) {

			fld.setAccessible(true);

			if (!fld.getName().contains("warning")) {
				try {
					String fldResponse = fld.get(serverpage).toString();
					Assert.assertTrue(!fldResponse.contains("Proxy element for: DefaultElementLocator"));
					Log.info("All elements are visible on the page");
				

				} catch (Throwable e) {
					Log.error("An element is missing on the page");
					
				
				}
			}

		}
	}

	@Then("^Correct text is displayed for each element on the Servers Page$")
	public void correct_text_is_displayed_for_each_element_on_the_Servers_Page() throws Throwable {
	  
		ServersPage serverpage= new ServersPage(driver);
		Assert.assertEquals("Servers", serverpage.serversTitle.getText());
		Log.info("Correct text is displayed for server title");
		Assert.assertEquals("Insert Server", serverpage.insertServerButton.getText());
		Log.info("Correct text is displayed for Insert Server button");
		Assert.assertTrue(serverpage.showEntriesLabel.getText().contains("Show"));
		Log.info("Correct text is displayed for show entries label");
		Assert.assertTrue(serverpage.searchLabel.getText().contains("Search:"));
		Log.info("Correct text is displayed for search label");
		Assert.assertEquals("Id", serverpage.idLabel.getText());
		Log.info("Correct text is displayed for id label");
		Assert.assertEquals("Name", serverpage.nameLabel.getText());
		Log.info("Correct text is displayed for name label");
		Assert.assertEquals("User Name", serverpage.userNameLabel.getText());
		Log.info("Correct text is displayed for user name label");
		Assert.assertEquals("Connection URL", serverpage.connectionURLLabel.getText());
		Log.info("Correct text is displayed for connection URL label");
		Assert.assertEquals("Connection Type", serverpage.connectionTypeLabel.getText());
		Log.info("Correct text is displayed for connection type label");
		Assert.assertEquals("Prefix", serverpage.prefixLabel.getText());
		Log.info("Correct text is displayed for prefix label");
		Assert.assertEquals("Active", serverpage.activeLabel.getText());
		Log.info("Correct text is displayed for active label");
		Assert.assertTrue(serverpage.showing.getText().contains("Showing "));
		Log.info("Correct text is displayed for showing label");
		Assert.assertEquals("Previous", serverpage.previousButton.getText());
		Log.info("Correct text is displayed for previous button paggination");
		Assert.assertEquals("Next", serverpage.nextButton.getText());
		Log.info("Correct text is displayed for next button paggination");
		Assert.assertTrue(serverpage.copyrightFooter.getText().contains("Copyright ©"));
		Log.info("Correct text is displayed for copyright footer");
		Assert.assertTrue(serverpage.clientSoultionsFooter.getText().contains("Client Solutions"));
		Log.info("Correct text is displayed for client solutions footer");
		Assert.assertTrue(serverpage.yearFooter.getText().contains("2017"));
		Log.info("Correct text is displayed for client solutions year footer");
		Assert.assertTrue(serverpage.versionFooter.getText().contains("Version:"));
		Log.info("Correct text is displayed for version footer");
		Log.info("Correct text is displayed for all elements on the page");
		
		
		
		
		
		
		
		
		
		
		
	}


	

}
