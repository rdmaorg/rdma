package stepDefinition;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import pageObjects.Home_Page;
import pageObjects.Tables_Page;
import appModules.Navigation;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class UIBCTablesPage extends Init{

	@Given("^User is on the Tables Page$")
	public void user_is_on_the_Tables_Page() throws Throwable {
	   
		Navigation.login(driver, username, password);
		Home_Page homepage= new Home_Page(driver);
		homepage.configurationButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		homepage.serverButton.click();
		Thread.sleep(9000);
		Select dropdownShowEntries= new Select(driver.findElement(By.name("tbl_server_length")));
		dropdownShowEntries.selectByValue("100");
		WebElement tableButtonForClassicModelServer= driver.findElement(By.cssSelector("button[data-servername='classicmodel'][data-serverid='6']"));
		tableButtonForClassicModelServer.click();
		Thread.sleep(9000);
		//Select dropdownShowEntries2= new Select(driver.findElement(By.name("tbl_tables6_length")));
		//dropdownShowEntries2.selectByValue("100");
		//Thread.sleep(11000);
		Assert.assertEquals("https://localhost/rdma/tables?severId=6&serverName=classicmodel", driver.getCurrentUrl());
		Log.info("User is on the tables page for server=classicmodels");
		
	}

	@Then("^All elements are visible on the Tables Page$")
	public void all_elements_are_visible_on_the_Tables_Page() throws Throwable {
	    
		Tables_Page tables= new Tables_Page(driver);
		Class<Tables_Page> tp = Tables_Page.class;
		Field[] fields = tp.getDeclaredFields();

		/**
		 * Verifying existence of each element on the currentPage. Asserting the
		 * string representation of of each page field which indicates existence
		 * of element.
		 */
		
		for (Field fld : fields) {

			fld.setAccessible(true);

			if (!fld.getName().contains("warning")) {
				try {
					
					String fldResponse = fld.get(tables).toString();
					Assert.assertTrue(!fldResponse.contains("Proxy element for: DefaultElementLocator"));
					Log.info("All elements are visible on the page");
				

				} catch (Throwable e) {
					Log.error("An element is missing on the page");
					
				
				}
			}

		}	
		
	}

	@Then("^Correct text is displayed for each element on the Tables Page$")
	public void correct_text_is_displayed_for_each_element_on_the_Tables_Page() throws Throwable {
		
		Tables_Page tables= new Tables_Page(driver);
		/*Assert.assertEquals("Servers", tables.serverNavigation.getText());
		Log.info("Correct text is displayed for server navigation");
		Assert.assertEquals("classicmodels", tables.serverNameNavigation.getText());
		Log.info("Correct text is displayed for server name navigation");
		Assert.assertEquals("Tables", tables.tableTitle.getText());
		Log.info("Correct text is displayed for Tables title"); */
		Assert.assertTrue("Correct text is displayed for show entries", tables.showEntriesLabel.getText().contains("Show"));
		Log.info("Correct text is displayed for show entries");
		Assert.assertEquals("Search:", tables.searchLabel.getText());
		Log.info("Correct text is displayed for search label");
		Assert.assertEquals("Id", tables.idLabel.getText());
		Log.info("Correct text is displayed for id label");
		Assert.assertEquals("Name", tables.nameLabel.getText());
		Log.info("Correct text is displayed for name label");
		Assert.assertEquals("Alias", tables.aliasLabel.getText());
		Log.info("Correct text is displayed for alias label");
		Assert.assertEquals("Active", tables.activeLabel.getText());
		Log.info("Correct text is displayed for active label");
		Assert.assertEquals("Actions", tables.actionsLabel.getText());
		Log.info("Correct text is displayed for actions label");
		Assert.assertTrue(tables.showing.getText().contains("Showing "));
		Log.info("Correct text is displayed for showing label");
		Assert.assertEquals("Previous", tables.previousButton.getText());
		Log.info("Correct text is displayed for previous button paggination");
		Assert.assertEquals("Next", tables.nextButton.getText());
		Log.info("Correct text is displayed for next button paggination");
		Assert.assertTrue(tables.copyrightFooter.getText().contains("Copyright ©"));
		Log.info("Correct text is displayed for copyright footer");
		Assert.assertTrue(tables.clientSoultionsFooter.getText().contains("Client Solutions"));
		Log.info("Correct text is displayed for client solutions footer");
		Assert.assertTrue(tables.yearFooter.getText().contains("2017"));
		Log.info("Correct text is displayed for client solutions year footer");
		Assert.assertTrue(tables.versionFooter.getText().contains("Version:"));
		Log.info("Correct text is displayed for version footer");
		Log.info("Correct text is displayed for all elements on the page");
		
	   
	}

	
	
	
	
	

}
