package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Servers_Page {

	public Servers_Page (WebDriver driver)
	{
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(xpath="html/body/div[1]/div[1]/section[1]/h1/span")
	public WebElement serversTitle;
	
	@FindBy(id="addServer")
	public WebElement insertServerButton;
	
	@FindBy(xpath=".//*[@id='tbl_server_length']/label")
	public WebElement showEntriesLabel;
	
	@FindBy(xpath=".//*[@id='tbl_server_length']/label/select")
	public WebElement showEntriesDropDown;
	
	@FindBy(xpath=".//*[@id='tbl_server_filter']/label")
	public WebElement searchLabel;
	
	@FindBy(xpath=".//*[@id='tbl_server_filter']/label/input")
	public WebElement searchField;
	
	@FindBy(xpath=".//*[@id='tbl_server']/thead/tr/th[1]")
	public WebElement idLabel;
	
	@FindBy(xpath=".//*[@id='tbl_server']/thead/tr/th[2]")
	public WebElement nameLabel;
	
	@FindBy(xpath=".//*[@id='tbl_server']/thead/tr/th[3]")
	public WebElement userNameLabel;
	
	@FindBy(xpath=".//*[@id='tbl_server']/thead/tr/th[4]")
	public WebElement connectionURLLabel;
	
	@FindBy(xpath=".//*[@id='tbl_server']/thead/tr/th[5]")
	public WebElement connectionTypeLabel;
	
	@FindBy(xpath=".//*[@id='tbl_server']/thead/tr/th[6]")
	public WebElement prefixLabel;
	
	@FindBy(xpath=".//*[@id='tbl_server']/thead/tr/th[7]")
	public WebElement activeLabel;
	
	@FindBy(xpath=".//*[@id='tbl_server']/thead/tr/th[8]")
	public WebElement actionsLabel;
	
	@FindBy(id="tbl_server_info")
	public WebElement showing;
	
	@FindBy(xpath=".//*[@id='tbl_server_previous']/a")
	public WebElement previousButton;
	
	@FindBy(xpath=".//*[@id='tbl_server_next']/a")
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
	
	@FindBy(css=".logo-mini")
	public WebElement logoGdma2;
	
	@FindBy(css=".glyphicon glyphicon-chevron-right menu-open")
	public WebElement sidebarToggleButton;
	

	
}
