package ie.clients.gdma2.test.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ConnectionTypesPage {

	
	public ConnectionTypesPage (WebDriver driver)
	{
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(xpath="html/body/div[1]/div[1]/section[1]/h1")
	public WebElement connectionTypeTitle;
	
	@FindBy(id="addConnection")
	public WebElement insertConnectionButton;
	
	@FindBy(xpath=".//*[@id='tbl_connection_length']/label")
	public WebElement showEntries;
	
	@FindBy(xpath=".//*[@id='tbl_connection_length']/label/select")
	public WebElement showEntriesDropDown;
	
	@FindBy(xpath=".//*[@id='tbl_connection_filter']/label")
	public WebElement searchLabel;
	
	@FindBy(xpath=".//*[@id='tbl_connection']/thead/tr/th[1]")
	public WebElement idLabel;
	
	@FindBy(xpath=".//*[@id='tbl_connection']/thead/tr/th[2]")
	public WebElement nameLabel;
	
	@FindBy(xpath=".//*[@id='tbl_connection']/thead/tr/th[3]")
	public WebElement connectionClassLabel;
	
	@FindBy(xpath=".//*[@id='tbl_connection']/thead/tr/th[4]")
	public WebElement sqlToGetTablesLabel;

	@FindBy(xpath=".//*[@id='tbl_connection']/thead/tr/th[5]")
	public WebElement actionsLabel;
	
	@FindBy(id="tbl_connection_info")
	public WebElement showingLabel;
	
	@FindBy(xpath=".//*[@id='tbl_connection_previous']/a")
	public WebElement previousButton;
	
	@FindBy(xpath=".//*[@id='tbl_connection_next']/a")
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
	
	@FindBy(id="tbl_connection")
	public WebElement table;
	
	//insert connection page
	
	@FindBy(xpath=".//*[@id='newConnectionType']/div/div[1]/h4/span")
	public WebElement insertConnectionTitle;
	
	@FindBy(xpath=".//*[@id='newConnectionType']/div/div[1]/button")
	public WebElement insertConnectionXButton;
	
	@FindBy(xpath=".//*[@id='newConnectionType']/div/div[2]/div/div/div[1]/label/span[1]")
	public WebElement insertConnectionNameLabel;
	
	@FindBy(xpath=".//*[@id='newConnectionType']/div/div[2]/div/div/div[1]/label/span[2]")
	public WebElement insertConnectionNameMandatory;
	
	@FindBy(id="name")
	public WebElement insertConnectionNameField;
	
	@FindBy(xpath=".//*[@id='newConnectionType']/div/div[2]/div/div/div[2]/label/span[1]")
	public WebElement insertConnectionCCLabel;
	
	@FindBy(xpath=".//*[@id='newConnectionType']/div/div[2]/div/div/div[2]/label/span[2]")
	public WebElement insertConnectionCCMandatory;
	
	@FindBy(id="connectionClass")
	public WebElement insertConnectionCCField;
	
	@FindBy(xpath=".//*[@id='newConnectionType']/div/div[2]/div/div/div[3]/label/span[1]")
	public WebElement insertConnectionSQLToGetTableLabel;
	
	@FindBy(xpath=".//*[@id='newConnectionType']/div/div[2]/div/div/div[3]/label/span[2]")
	public WebElement insertConnectionSTGMandatory;
	
	@FindBy(id="sqlGetTables")
	public WebElement insertConnectionSTGField;
	
	@FindBy(xpath=".//*[@id='newConnectionType']/div/div[3]/button[1]")
	public WebElement cancelButton;
	
	@FindBy(id="Save-conection")
	public WebElement saveButton;
	
	@FindBy(id="name-error")
	public WebElement nameError;
	
	@FindBy(id="connectionClass-error")
	public WebElement connectionClassError;
	
	@FindBy(id="sqlGetTables-error")
	public WebElement sqlGetTablesError;
	
}
