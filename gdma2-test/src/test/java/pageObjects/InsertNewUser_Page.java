package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class InsertNewUser_Page {

	public InsertNewUser_Page (WebDriver driver)
	{
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[1]/h4")
	public WebElement UserTitle;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[2]/div/div/div[1]/label")
	public WebElement nameLabel;
	
	@FindBy(id="name")
	public WebElement nameField;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[2]/div/div/div[2]/label")
	public WebElement lastNameLabel;
	
	@FindBy(id="lastname")
	public WebElement lastNameField;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[2]/div/div/div[3]/label")
	public WebElement userNameLabel;
	
	@FindBy(id="username")
	public WebElement userNameField;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[2]/div/div/div[4]/label")
	public WebElement domainLabel;
	
	@FindBy(id="url")
	public WebElement domainField;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[2]/div/div/div[5]/label")
	public WebElement adminLabel;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[2]/div/div/div[5]/div")
	public WebElement adminButton;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[2]/div/div/div[6]/label")
	public WebElement lockedLabel;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[2]/div/div/div[6]/div")
	public WebElement lockedButton;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[2]/div/div/div[7]/label")
	public WebElement activeLabel;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[2]/div/div/div[7]/div")
	public WebElement activeButton;
	
	@FindBy(xpath=".//*[@id='newUser']/div/div[3]/button[1]")
	public WebElement cancelButton;
	
	@FindBy(id="Save-user" )
	public WebElement saveButton;
	
	@FindBy(id="name-error")
	public WebElement nameWarning;
	
	@FindBy(id="lastname-error")
	public WebElement lastNameWarning;
	
	@FindBy(id="username-error")
	public WebElement userNameWarning;
	
	@FindBy(id="url-error")
	public WebElement domainWarning;
}
