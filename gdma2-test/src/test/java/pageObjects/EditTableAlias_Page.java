package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditTableAlias_Page {
	
	public EditTableAlias_Page(WebDriver driver) {
		
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(id="alias")
	public WebElement aliasField;
	
	@FindBy(id="Save-table")
	public WebElement saveButton;
	
	@FindBy(className="btn-success")
	public WebElement confirmSave;	

}
