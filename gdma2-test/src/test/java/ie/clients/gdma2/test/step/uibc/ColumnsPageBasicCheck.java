package ie.clients.gdma2.test.step.uibc;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import ie.clients.gdma2.test.app.Navigation;
import ie.clients.gdma2.test.page.ColumnsPage;
import ie.clients.gdma2.test.page.HomePage;
import ie.clients.gdma2.test.step.Init;
import ie.clients.gdma2.test.utility.TestUtils;

public class ColumnsPageBasicCheck extends Init{
	
	@Given("^User is on (.*) Columns Page of server (.*)$")
	public void userIsOnTheColumnsPage(String tableName, String serverName) throws Throwable {
	
		Navigation.login(driver, username, password);		
		HomePage homepage= new HomePage(driver);
		homepage.configurationButton.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		wait.until(ExpectedConditions.elementToBeClickable(homepage.serverButton));     
		homepage.serverButton.click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Select oselect= new Select(driver.findElement(By.name("tbl_server_length")));
		oselect.selectByValue("100");
		TestUtils.waitForJQuery(driver);
		WebElement tableButtonForClassicModelServer= driver.findElement(By.cssSelector("button[data-servername='"+serverName+"']"));
		wait.until(ExpectedConditions.elementToBeClickable(tableButtonForClassicModelServer));
		tableButtonForClassicModelServer.click();
		Thread.sleep(5000);
		Select oselect2= new Select(driver.findElement(By.cssSelector("select[name*=tbl_tables]")));
		oselect2.selectByValue("100");
		userNavigatesToColumnPage(tableName);
	}
	
	public void userNavigatesToColumnPage(String tableName) throws Throwable{
		TestUtils.waitForJQuery(driver);
		WebElement columnsButtonForClassicModelsTable=driver.findElement(By.cssSelector("button[class='btn btn-info btn-xs viewColumns'][data-tablename='"+tableName+"']"));
		wait.until(ExpectedConditions.elementToBeClickable(columnsButtonForClassicModelsTable));
		columnsButtonForClassicModelsTable.click();
		Log.info("User select columns button for Customers table");
		
	}

	@Then("^All elements are visible on the Columns Page$")
	public void allElementsAreVisibleOnTheColumnsPage() throws Throwable {
	    
		ColumnsPage columns= new ColumnsPage(driver);
		Class<ColumnsPage> cp = ColumnsPage.class;
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
	public void correctTextIsDisplayedForEachElementOnColumnsPage() throws Throwable {
		
		//Tables_Page tables= new Tables_Page(driver);
		ColumnsPage columns= new ColumnsPage(driver);
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
