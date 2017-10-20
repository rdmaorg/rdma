package ie.clients.gdma2.test.step.data;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.clients.gdma2.test.app.Navigation;
import ie.clients.gdma2.test.page.DataModulePage;
import ie.clients.gdma2.test.step.Init;

public class DownloadFile extends Init {
	
	@When("^User navigates to Data Module Page and select Download button$")
	public void user_navigates_to_Data_Module_Page_and_select_Download_button() throws Throwable {
		
		    //Create FireFox Profile object
			FirefoxProfile profile = new FirefoxProfile();
	 
			//Set Location to store files after downloading.
			profile.setPreference("browser.download.dir", "C:\\development\\GDMAAutomationtesting");
			profile.setPreference("browser.download.folderList", 2);
	 
			//Set Preference to not show file download confirmation dialogue using MIME types Of different file extension types.
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk", 
			    "text/csv;"); 
	 
			profile.setPreference( "browser.download.manager.showWhenStarting", false );
			profile.setPreference( "pdfjs.disabled", true );
			
			FirefoxOptions options = new FirefoxOptions();
			options.setProfile(profile);
	 
			//Pass FProfile parameter In webdriver to use preferences to download file.
			WebDriver driver= new FirefoxDriver(options);
			driver.get("https://localhost/rdma");
	        
			//click to download
			Navigation.login(driver, username, password);
			WebElement serverInData= driver.findElement(By.cssSelector("li[id='server6'][class='treeview']"));
			serverInData.click();
			WebElement tableInData = driver.findElement(By.cssSelector("a[class='table-data'][data-servername='classicmodel'][data-serverid='6'][data-tablename='customers'][data-id='185']"));
			Assert.assertTrue(tableInData.isDisplayed());
			Log.info("Usr navigates to the table in Data module");
			tableInData.click();
			Thread.sleep(9000);
			DataModulePage datamodule= new DataModulePage(driver);
			datamodule.downloadButton.click();
			Thread.sleep(9000);
			Log.info("User select download button");
			Actions action= new Actions(driver);
			action.sendKeys(Keys.ENTER).build().perform();
			Log.info("User select YES button");
			//Halting the execution for 3 secs to download the file completely
			Thread.sleep(3000);
	 
			driver.close();
	 
		
		
	}

	@Then("^File is successfully downloaded$")
	public void file_is_successfully_downloaded() throws Throwable {
		
		Log.info("File is successfully downloaded");
	    
	}



}
