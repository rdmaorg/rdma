package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Users_Page {
	
	public Users_Page (WebDriver driver)
	{
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy (xpath="html/body/div[1]/div[1]/section[1]/h1/span")
	public WebElement usersTitle;
	
	@FindBy(id="inserUser")
	public WebElement insertUserButton;
	
	@FindBy(id="tbl_user_length")
	public WebElement showEntries;
	
	@FindBy(id="tbl_user_filter")
	public WebElement search;
	
	@FindBy(xpath=".//*[@id='tbl_user']/thead/tr/th[1]")
	public WebElement idLabel;
	
	@FindBy(xpath=".//*[@id='tbl_user']/thead/tr/th[2]")
	public WebElement nameLabel;
	
	@FindBy(xpath=".//*[@id='tbl_user']/thead/tr/th[3]")
	public WebElement lastNameLabel;
	
	@FindBy(xpath=".//*[@id='tbl_user']/thead/tr/th[4]")
	public WebElement userNameLabel;
	
	@FindBy(xpath=".//*[@id='tbl_user']/thead/tr/th[5]")
	public WebElement domainLabel;
	
	@FindBy(xpath=".//*[@id='tbl_user']/thead/tr/th[6]")
	public WebElement adminLabel;
	
	@FindBy(xpath=".//*[@id='tbl_user']/thead/tr/th[7]")
	public WebElement lockedLabel;
	
	@FindBy(xpath=".//*[@id='tbl_user']/thead/tr/th[8]")
	public WebElement activeLabel;
	
	@FindBy(xpath=".//*[@id='tbl_user']/thead/tr/th[9]")
	public WebElement actionsLabel;
	
	@FindBy(id="tbl_user_info")
	public WebElement showingLabel;
	
	@FindBy(xpath=".//*[@id='tbl_user_previous']/a")
	public WebElement previousButton;
	
	@FindBy(xpath=".//*[@id='tbl_user_next']/a")
	public WebElement nextButton;
	
	@FindBy(xpath="html/body/div[1]/footer/strong")
	public WebElement copyrightFooter;
	
	@FindBy(xpath="html/body/div[1]/footer/span[1]")
	public WebElement clientSoultionsFooter;
	
	@FindBy(xpath="html/body/div[1]/footer/span[2]")
	public WebElement yearFooter;
	
	@FindBy(xpath="html/body/div[1]/footer/div")
	public WebElement versionFooter;
	
	@FindBy(xpath="html/body/div[1]/header/nav/ul[2]/li[1]/a")
	public WebElement username;
	
	@FindBy(id="btn-admin")
	public WebElement configurationButton;
	
	@FindBy(xpath="html/body/div[1]/header/nav/ul[1]/li[1]/a/span[1]/img")
	public WebElement logoGdma2;
	
	@FindBy(xpath="html/body/div[1]/header/nav/ul[1]/li[2]/a")
	public WebElement sidebarToggleButton;

}
