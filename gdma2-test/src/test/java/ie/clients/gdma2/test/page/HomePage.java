package ie.clients.gdma2.test.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
	
	public HomePage (WebDriver driver)
	{
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(xpath="//a[@class='dropdown-toggle']")
	public WebElement username;
	
	@FindBy(id="btn-admin")
	public WebElement configurationButton;
	
	@FindBy(xpath=".//*[@id='control-sidebar-settings-tab']/h3")
	public WebElement adminSettings;
	
	@FindBy(xpath=".//*[@id='control-sidebar-settings-tab']/div[1]/a")
	public WebElement serverButton;
	
	@FindBy(xpath=".//*[@id='control-sidebar-settings-tab']/div[2]/a")
	public WebElement userButton;
	
	@FindBy(xpath=".//*[@id='control-sidebar-settings-tab']/div[3]/a")
	public WebElement connectionsButton;
	
	@FindBy(xpath="html/body/div[1]/footer/strong")
	public WebElement copyrightFooter;
	
	@FindBy(xpath="html/body/div[1]/footer/span[1]")
	public WebElement clientSoultionsFooter;
	
	@FindBy(xpath="html/body/div[1]/footer/span[2]")
	public WebElement yearFooter;
	
	@FindBy(xpath="html/body/div[1]/footer/div")
	public WebElement versionFooter;
	
	
}
