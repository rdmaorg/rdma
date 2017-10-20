package ie.clients.gdma2.test.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditTableAliasPage {
	
	public EditTableAliasPage(WebDriver driver) {
		
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(id="alias")
	public WebElement aliasField;
	
	@FindBy(id="Save-table")
	public WebElement saveButton;
	
	@FindBy(className="btn-success")
	public WebElement confirmSave;	

}
