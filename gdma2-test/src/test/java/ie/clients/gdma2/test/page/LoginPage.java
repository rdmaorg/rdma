package ie.clients.gdma2.test.page; 

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

	public LoginPage (WebDriver driver)
	{
		PageFactory.initElements(driver, this);	//static initElements method for PageFactory class for initializing Web Element
	}
	
	@FindBy(css="div.navbar-brand") // web element are identify by @FindBy annotation
	public WebElement CASHeaderTitle;
	
	@FindBy(xpath="//img[@src='auth_logo.png']")
	public WebElement CASHeaderImg;
	
	@FindBy(xpath="//h2[@class='panel-title']")
	public WebElement formTitle;
	
	@FindBy(css="div.col-md-8")
	public WebElement formText;

	@FindBy(xpath="html/body/div[1]/div[1]/div/div/div[2]/div/div[2]/div/a/img")
	public WebElement formImg;
	
	@FindBy(xpath=".//*[@id='fm1']/div[2]/div/label")
	public WebElement usernameLabel;
	
	@FindBy(id="username")
	public WebElement usernameField;
	
	@FindBy (xpath=".//*[@id='fm1']/div[2]/div/div/span")
	public WebElement usernameIcon;

	@FindBy(xpath=".//*[@id='fm1']/div[3]/div/label")
	public WebElement passwordLabel;
	
	@FindBy(id="password")
	public WebElement passwordField;
	
	@FindBy (xpath=".//*[@id='fm1']/div[3]/div/div/span")
	public WebElement passworIcon;
	
	@FindBy(xpath=".//*[@id='fm1']/div[4]/input[4]")
	public WebElement loginButton;
	
	@FindBy(xpath=".//*[@id='fm1']/div[4]/input[5]")
	public WebElement clearButton;
	
	@FindBy (xpath=".//*[@id='sidebar']/div/p")
	public WebElement notificationText;
	
	@FindBy(xpath=".//*[@id='copyright']/p[1]")
	public WebElement copyright;
	
	@FindBy (xpath=".//*[@id='copyright']/p[2]")
	public WebElement poweredBy;
	
	@FindBy(id="formError")
	public WebElement warningInvalidCredentials;
	
	@FindBy(id="username-error")
	public WebElement warningUsernameError;
	
	@FindBy(id="password-error")
	public WebElement warningPasswordError;
	
}
