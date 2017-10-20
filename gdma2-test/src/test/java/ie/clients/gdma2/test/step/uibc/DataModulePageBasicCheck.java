package ie.clients.gdma2.test.step.uibc;

import java.lang.reflect.Field;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import ie.clients.gdma2.test.app.Navigation;
import ie.clients.gdma2.test.page.DataModulePage;
import ie.clients.gdma2.test.step.Init;


public class DataModulePageBasicCheck extends Init{

	@Given("^User is on the Data module Page$")
	public void user_is_on_the_Data_module_Page() throws Throwable {
	    
		Navigation.login(driver, username, password);
		WebElement serverInData= driver.findElement(By.cssSelector("li[id='server6'][class='treeview']"));
		serverInData.click();
		WebElement tableInData = driver.findElement(By.cssSelector("a[class='table-data'][data-servername='classicmodel'][data-serverid='6'][data-tablename='customers'][data-id='185']"));
		Assert.assertTrue(tableInData.isDisplayed());
		Log.info("Table is visible in Data module");
		tableInData.click();
		Thread.sleep(4000);
		Log.info("User navigates to DATA module");
		
		
	}

	@Then("^All elements are visible on the Data module Page$")
	public void all_elements_are_visible_on_the_Data_module_Page() throws Throwable {

		DataModulePage datamodulepage= new DataModulePage(driver);
		Class<DataModulePage> dm = DataModulePage.class;
		Field[] fields = dm.getDeclaredFields();

		/**
		 * Verifying existence of each element on the currentPage. Asserting the
		 * string representation of of each page field which indicates existence
		 * of element.
		 */
		for (Field fld : fields) {

			fld.setAccessible(true);

			if (!fld.getName().contains("warning")) {
				try {
					String fldResponse = fld.get(datamodulepage).toString();
					Assert.assertTrue(!fldResponse.contains("Proxy element for: DefaultElementLocator"));
					Log.info("All elements are visible on the page");
				

				} catch (Throwable e) {
					Log.error("An element is missing on the page");
					
				
				}
			}

		}
		
	}

	@Then("^Correct text is displayed for each element on the Data module Page$")
	public void correct_text_is_displayed_for_each_element_on_the_Data_module_Page() throws Throwable {
	
		DataModulePage datamodulepage= new DataModulePage(driver);
		Assert.assertEquals("Select all", datamodulepage.selectAll.getText());
		Log.info("Correct text is displayed for select all button");
		Assert.assertEquals("Deselect all", datamodulepage.deselectAll.getText());
		Log.info("Correct text is displayed for deselect all button");
		//missing download and upload
		Assert.assertTrue(datamodulepage.showEntries.getText().contains("Show"));
		Log.info("Correct text is displayed for show entries");
		Assert.assertEquals("Search:", datamodulepage.search.getText());
		Log.info("Correct text is displayed for search");
		Assert.assertTrue(datamodulepage.showing.getText().contains("Showing"));
		Log.info("Correct text is displayed for showing label");
		Assert.assertEquals("Previous", datamodulepage.previousButton.getText());
		Log.info("Correct text is displayed for previous button");
		Assert.assertEquals("Next", datamodulepage.nextButton.getText());
		Log.info("Correct text is displayed for next button");
		Log.info("Correct text is displayed for all elements on the page");	
	}

}
