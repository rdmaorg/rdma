package ie.clients.gdma2.test.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class InsertNewServerPage {
	
	public InsertNewServerPage (WebDriver driver)
	{
		PageFactory.initElements(driver, this);	
	}

	
	@FindBy(xpath=".//*[@id='newServer']/div/div[1]/h4")
	public WebElement institle;
	
	@FindBy(xpath=".//*[@id='newServer']/div/div[1]/button")
	public WebElement insXbutton;
	
	@FindBy(xpath=".//*[@id='newServer']/div/div[2]/div/div/div[1]/label[1]")
	public WebElement insNamelabel;
	
	@FindBy(id="name")
	public WebElement insNameField;
	
	@FindBy(xpath=".//*[@id='newServer']/div/div[2]/div/div/div[2]/label")
	public WebElement insUserNameLabel;
	
	@FindBy(id="username")
	public WebElement insUserNameField;
	
	@FindBy(xpath=".//*[@id='newServer']/div/div[2]/div/div/div[3]/label")
	public WebElement insPasswordLabel;
	
	@FindBy(id="password" )
	public WebElement insPasswordField;
	
	@FindBy(xpath=".//*[@id='newServer']/div/div[2]/div/div/div[4]/label")
	public WebElement insConnectionURLLabel;
	
	@FindBy(id="url")
	public WebElement insConnectionURLField;
	
	@FindBy(xpath=".//*[@id='newServer']/div/div[2]/div/div/div[5]/label")
	public WebElement insConnectionTypeLabel;
	
	@FindBy(id="connectionType")
	public WebElement insConnectionTypeDropDown;
	
	@FindBy(xpath=".//*[@id='newServer']/div/div[2]/div/div/div[6]/label")
	public WebElement insPrefixLabel;
	
	@FindBy(id="prefix")
	public WebElement insPrefixField;
	
	@FindBy(xpath=".//*[@id='newServer']/div/div[2]/div/div/div[7]/label")
	public WebElement insActiveLabel;
	
	@FindBy(xpath=".//*[@id='newServer']/div/div[2]/div/div/div[7]/div")
	public WebElement insActiveField;
	
	@FindBy(xpath=".//*[@id='newServer']/div/div[3]/button[1]")
	public WebElement insCancelButton;
	
	@FindBy(id="Save-server")
	public WebElement insSaveButton;
	
	@FindBy(id="name-error")
	public WebElement insNameWarning;
	
	@FindBy(id="username-error")
	public WebElement insUsernameWarning;
	
	@FindBy(id="password-error")
	public WebElement insPasswordWarning;
	
	@FindBy(id="url-error")
	public WebElement insURLWarning;
}
