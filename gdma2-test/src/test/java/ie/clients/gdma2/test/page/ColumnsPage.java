package ie.clients.gdma2.test.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ColumnsPage {
	
	public ColumnsPage (WebDriver driver)
	{
		PageFactory.initElements(driver, this);	
	}
	
	
	@FindBy(xpath="html/body/div[1]/div[1]/section[1]/h2/a[1]")
	public WebElement serverTitleNavigation;
	
	@FindBy(id="serverName")
	public WebElement serverNameNavigation;
	
	@FindBy(id="tableName")
	public WebElement tableNameNavigation;
	
	@FindBy(xpath="html/body/div[1]/div[1]/section[1]/h1/span")
	public WebElement columnsTitle;
	
	@FindBy(css="div[id$='tbl_column_length']")
	public WebElement showEntries;
	
	@FindBy(css="div[id$='tbl_column_filter']")
	public WebElement search;
	
	@FindBy(css="table thead tr th:nth-child(1)")
	public WebElement idLabel;
	
	@FindBy(css="table thead tr th:nth-child(2)")
	public WebElement nameLabel;
	
	@FindBy(css="table thead tr th:nth-child(3)")
	public WebElement aliasLabel;
	
	@FindBy(css="table thead tr th:nth-child(4)")
	public WebElement colTypeLabel;
	
	@FindBy(css="table thead tr th:nth-child(5)")
	public WebElement primaryKeyLabel;
	
	@FindBy(css="table thead tr th:nth-child(6)")
	public WebElement displayLabel;
	
	@FindBy(css="table thead tr th:nth-child(7)")
	public WebElement insertLabel;
	
	@FindBy(css="table thead tr th:nth-child(8)")
	public WebElement updateLabel;
	
	@FindBy(css="table thead tr th:nth-child(9)")
	public WebElement nullLabel;
	
	@FindBy(css="table thead tr th:nth-child(10)")
	public WebElement dropdownDisplayLabel;
	
	@FindBy(css="table thead tr th:nth-child(11)")
	public WebElement dropdownStoreLabel;
	
	@FindBy(css="table thead tr th:nth-child(12)")
	public WebElement specialLabel;
	
	@FindBy(css="table thead tr th:nth-child(13)")
	public WebElement colSizeLabel;
	
	@FindBy(id="tbl_column_info")
	public WebElement showing;
	
	@FindBy(css="[id$='tbl_column_previous']")
	public WebElement previousButton;
	
	@FindBy(css="[id$='tbl_column_next']")
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
