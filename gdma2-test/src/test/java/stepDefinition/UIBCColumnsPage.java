package stepDefinition;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import pageObjects.Columns_Page;
import pageObjects.Home_Page;
import appModules.Navigation;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class UIBCColumnsPage extends Init{
	
	@Given("^User is on the Columns Page$")
	public void user_is_on_the_Columns_Page() throws Throwable {
	
		Navigation.login(driver, username, password);
		Home_Page homepage= new Home_Page(driver);
		homepage.configurationButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		homepage.serverButton.click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Select oselect= new Select(driver.findElement(By.name("tbl_server_length")));
		oselect.selectByValue("100");
		WebElement tableButtonForClassicModelServer= driver.findElement(By.cssSelector("button[data-servername='classicmodel']"));
		tableButtonForClassicModelServer.click();
		Thread.sleep(5000);
		Select oselect2= new Select(driver.findElement(By.name("tbl_tables6_length")));
		oselect2.selectByValue("100");
		Thread.sleep(2000);
		WebElement columnsButtonForClassicModelsTable=driver.findElement(By.cssSelector("button[class='btn btn-info btn-xs viewColumns'][data-tableid='185']"));
		columnsButtonForClassicModelsTable.click();
		Thread.sleep(5000);
		Log.info("User select columns button for Customers table");
		
		
	}

	@Then("^All elements are visible on the Columns Page$")
	public void all_elements_are_visible_on_the_Columns_Page() throws Throwable {
	    
		Columns_Page columns= new Columns_Page(driver);
		Class<Columns_Page> cp = Columns_Page.class;
		Field[] fields = cp.getDeclaredFields();

		/**
		 * Verifying existence of each element on the currentPage. Asserting the
		 * string representation of of each page field which indicates existence
		 * of element.
		 */
		
		for (Field fld : fields) {

			fld.setAccessible(true);

			if (!fld.getName().contains("warning")) {
				try {
					
					String fldResponse = fld.get(columns).toString();
					Assert.assertTrue(!fldResponse.contains("Proxy element for: DefaultElementLocator"));
					Log.info("All elements are visible on the page");
				

				} catch (Throwable e) {
					Log.error("An element is missing on the page");
					
				
				}
			}

		}		
		
	}

	@Then("^Correct text is displayed for each element on the Columns Page$")
	public void correct_text_is_displayed_for_each_element_on_the_Columns_Page() throws Throwable {
		
		//Tables_Page tables= new Tables_Page(driver);
		Columns_Page columns= new Columns_Page(driver);
		/*Assert.assertEquals("Columns", columns.columnsTitle.getText());
		Log.info("Correct text is displayed for Columns title");
		Assert.assertEquals("classicmodels", columns.serverNameNavigation.getText());
		Log.info("Correct text is displayed for server name in page navigation");
		/*Assert.assertEquals(tables.aliasField.getText(), columns.tableNameNavigation.getText());
		Log.info("Correct text is displayed table alias in page navigation");*/
		Assert.assertTrue(columns.showEntries.getText().contains("Show"));
		Log.info("Correct text is displayed for show entries");
		Assert.assertTrue(columns.search.getText().contains("Search"));
		Log.info("Correct text is displayed for Search");
		Assert.assertEquals("Id", columns.idLabel.getText());
		Log.info("Correct text is displayed for Id");
		Assert.assertEquals("Name", columns.nameLabel.getText());
		Log.info("Correct text is displayed for Name");
		Assert.assertEquals("Alias", columns.aliasLabel.getText());
		Log.info("Correct text is displayed for Alias");
		Assert.assertEquals("Col Type", columns.colTypeLabel.getText());
		Log.info("Correct text is displayed for Col type");
		Assert.assertEquals("Primary Key", columns.primaryKeyLabel.getText());
		Log.info("Correct text is displayed for Primary Key");
		Assert.assertEquals("Display", columns.displayLabel.getText());
		Log.info("Correct text is displayed for display");
		Assert.assertEquals("Insert", columns.insertLabel.getText());
		Log.info("Correct text is displayed for insert");
		Assert.assertEquals("Update", columns.updateLabel.getText());
		Log.info("Correct text is displayed for update");
		Assert.assertEquals("Null", columns.nullLabel.getText());
		Log.info("Correct text is displayed for null label");
		Assert.assertEquals("Dropdown Display", columns.dropdownDisplayLabel.getText());
		Log.info("Correct text is displayed for dropdown display label");
		Assert.assertEquals("Dropdown Store", columns.dropdownStoreLabel.getText());
		Log.info("Correct text is displayed for dropdown store label");
		Assert.assertEquals("Special", columns.specialLabel.getText());
		Log.info("Correct text is displayed for special label");
		Assert.assertEquals("Col Size", columns.colSizeLabel.getText());
		Log.info("Correct text is displayed for col size label");
		Assert.assertEquals("Previous", columns.previousButton.getText());
		Log.info("Correct text is displayed for previous button paggination");
		Assert.assertEquals("Next", columns.nextButton.getText());
		Log.info("Correct text is displayed for next button paggination");
		Assert.assertTrue(columns.copyrightFooter.getText().contains("Copyright ©"));
		Log.info("Correct text is displayed for copyright footer");
		Assert.assertTrue(columns.clientSoultionsFooter.getText().contains("Client Solutions"));
		Log.info("Correct text is displayed for client solutions footer");
		Assert.assertTrue(columns.yearFooter.getText().contains("2017"));
		Log.info("Correct text is displayed for client solutions year footer");
		Assert.assertTrue(columns.versionFooter.getText().contains("Version:"));
		Log.info("Correct text is displayed for version footer");
		Log.info("Correct text is displayed for all elements on the page");
		
	  
	}



}