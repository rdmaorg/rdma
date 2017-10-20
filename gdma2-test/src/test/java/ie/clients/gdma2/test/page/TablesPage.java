package ie.clients.gdma2.test.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TablesPage {
	
	public TablesPage (WebDriver driver)
	{
		PageFactory.initElements(driver, this);	
	}

	
	@FindBy(xpath="html/body/div[1]/div[1]/section[1]/h2/a")
	public WebElement serverNavigation;
	
	@FindBy(id="serverName")
	public WebElement serverNameNavigation;
	
	@FindBy(xpath="html/body/div[1]/div[1]/section[1]/h1/span")
	public WebElement tableTitle;
	
	@FindBy(xpath=".//*[@id='tbl_tables6_length']/label")
	public WebElement showEntriesLabel;
	
	@FindBy(xpath=".//*[@id='tbl_tables6_length']/label/select")
	public WebElement showEntriesDropDown;
	
	@FindBy(xpath=".//*[@id='tbl_tables6_filter']/label")
	public WebElement searchLabel;
	
	@FindBy(xpath=".//*[@id='tbl_tables6_filter']/label/input")
	public WebElement searchField;
	
	@FindBy(xpath=".//*[@id='tbl_tables6']/thead/tr/th[1]")
	public WebElement idLabel;
	
	@FindBy(xpath=".//*[@id='tbl_tables6']/thead/tr/th[2]")
	public WebElement nameLabel;
	
	@FindBy(xpath=".//*[@id='tbl_tables6']/thead/tr/th[3]")
	public WebElement aliasLabel;
	
	@FindBy(xpath=".//*[@id='tbl_tables6']/thead/tr/th[4]")
	public WebElement activeLabel;
	
	@FindBy(xpath=".//*[@id='tbl_tables6']/thead/tr/th[5]")
	public WebElement actionsLabel;
	
	@FindBy(id="tbl_tables6_info")
	public WebElement showing;
	
	@FindBy(xpath=".//*[@id='tbl_tables6_previous']/a")
	public WebElement previousButton;
	
	@FindBy(xpath=".//*[@id='tbl_tables6_next']/a")
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
