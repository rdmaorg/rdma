package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Columns_Page {
	
	public Columns_Page (WebDriver driver)
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
	
	@FindBy(id="tbl_column185_length")
	public WebElement showEntries;
	
	@FindBy(id="tbl_column185_filter")
	public WebElement search;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[1]")
	public WebElement idLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[2]")
	public WebElement nameLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[3]")
	public WebElement aliasLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[4]")
	public WebElement colTypeLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[5]")
	public WebElement primaryKeyLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[6]")
	public WebElement displayLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[7]")
	public WebElement insertLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[8]")
	public WebElement updateLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[9]")
	public WebElement nullLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[10]")
	public WebElement dropdownDisplayLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[11]")
	public WebElement dropdownStoreLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[12]")
	public WebElement specialLabel;
	
	@FindBy(xpath=".//*[@id='tbl_column185']/thead/tr/th[13]")
	public WebElement colSizeLabel;
	
	@FindBy(id="tbl_column185_info")
	public WebElement showing;
	
	@FindBy(id="tbl_column185_previous")
	public WebElement previousButton;
	
	@FindBy(id="tbl_column185_next")
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
