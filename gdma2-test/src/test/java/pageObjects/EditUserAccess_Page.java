package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditUserAccess_Page {

	public EditUserAccess_Page (WebDriver driver)
	{
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(xpath=".//*[@id='editUserAccess']/div/div[1]/h4/span[1]")
	public WebElement userAccessTitle;
	
	@FindBy(id="selectedTableName")
	public WebElement userAccessTableName;
	
	@FindBy(id="tbl_userAccess_length")
	public WebElement showEntriesLabel;
	
	@FindBy(xpath=".//*[@id='tbl_userAccess_length']/label/select")
	public WebElement showEntriesDropDown;
	
	@FindBy(id="tbl_userAccess_filter")
	public WebElement search;
	
	@FindBy(xpath=".//*[@id='tbl_userAccess']/thead/tr/th[1]")
	public WebElement idLabel;
	
	@FindBy(xpath=".//*[@id='tbl_userAccess']/thead/tr/th[2]")
	public WebElement userLabel;
	
	@FindBy(xpath=".//*[@id='tbl_userAccess']/thead/tr/th[3]")
	public WebElement nameLabel;
	
	@FindBy(xpath=".//*[@id='tbl_userAccess']/thead/tr/th[4]")
	public WebElement fullAccessLabel;
	
	@FindBy(xpath=".//*[@id='tbl_userAccess']/thead/tr/th[5]")
	public WebElement displayLabel;
	
	@FindBy(xpath=".//*[@id='tbl_userAccess']/thead/tr/th[6]")
	public WebElement updateLabel;
	
	@FindBy(xpath=".//*[@id='tbl_userAccess']/thead/tr/th[7]")
	public WebElement insertLabel;
	
	@FindBy(xpath=".//*[@id='tbl_userAccess']/thead/tr/th[8]")
	public WebElement deleteLabel;
	
    @FindBy(id="tbl_userAccess_info")
    public WebElement showingEntries;
    
    @FindBy(xpath=".//*[@id='tbl_userAccess_previous']/a")
    public WebElement previousButton;
    
    @FindBy (xpath=".//*[@id='tbl_userAccess_next']/a")
    public WebElement nextButton;
    
    @FindBy (id="modal-close-access")
    public WebElement closeButton;
    
    @FindBy (id="save-userAccess")
    public WebElement saveButton;

}
