package stepDefinition;

import java.lang.reflect.Field;
import org.junit.Assert;
import pageObjects.Home_Page;
import appModules.Navigation;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class UIBCHomePage extends Init{

	@Given("^User is on the Home Page$")
	public void user_is_on_the_Home_Page() throws Throwable {
		
		Navigation.login(driver, username, password);
		Log.info("Home page is displayed");

	}

	@Then("^All elements are visible on the Home Page$")
	public void all_elements_are_visible_on_the_Home_Page() throws Throwable {
		
		Home_Page homepage= new Home_Page(driver);
		Class<Home_Page> h = Home_Page.class;
		Field[] fields = h.getDeclaredFields();

		/**
		 * Verifying existence of each element on the currentPage. Asserting the
		 * string representation of of each page field which indicates existence
		 * of element.
		 */
		for (Field fld : fields) {

			fld.setAccessible(true);

			if (!fld.getName().contains("warning")) {
				try {
					String fldResponse = fld.get(homepage).toString();
					Assert.assertTrue(!fldResponse.contains("Proxy element for: DefaultElementLocator"));
					Log.info("Element is visible on the page");
				

				} catch (Throwable e) {
					Log.error("An element is missing on the page");
					
				
				}
			}

		}
	   
	}

	@Then("^Correct text is displayed for each element on the Home Page$")
	public void correct_text_is_displayed_for_each_element_on_the_Home_Page() throws Throwable {
	
		Home_Page homepage= new Home_Page(driver);
		Assert.assertTrue(homepage.copyrightFooter.getText().contains("Copyright ©"));
		Log.info("Correct text is displayed for copyright footer");
		Assert.assertTrue(homepage.clientSoultionsFooter.getText().contains("Client Solutions"));
		Log.info("Correct text is displayed for client solutions footer");
		Assert.assertTrue(homepage.yearFooter.getText().contains("2017"));
		Log.info("Correct text is displayed for client solutions year footer");
		Assert.assertTrue(homepage.versionFooter.getText().contains("Version:"));
		Log.info("Correct text is displayed for version footer");
		Assert.assertEquals("Configuration", homepage.configurationButton.getText());
		Log.info("Correct text is displayed for configuration button");
	}

}