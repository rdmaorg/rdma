package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DATAModule_Page {
	
	public DATAModule_Page(WebDriver driver)
	{
		PageFactory.initElements(driver, this); //static initElements method for PageFactory class for initializing Web Element
	}
	
	@FindBy(css="a[class='dt-button buttons-create']") // web element are identify by @FindBy annotation
	public WebElement newButton;
	
	@FindBy(css="a[class='dt-button buttons-selected buttons-edit disabled']")
	public WebElement editButton;
	
	@FindBy(css="a[class='dt-button buttons-selected buttons-remove disabled']")
	public WebElement deleteButton;
	
	@FindBy(css="a[class='dt-button buttons-select-all']")
	public WebElement selectAll;
	
	@FindBy(css="a[class='dt-button buttons-select-none disabled']")
	public WebElement deselectAll;
	
    @FindBy(xpath=".//*[@id='datatableButtonsDiv']/div/a[4]")
    public WebElement downloadButton;
    
    @FindBy(xpath=".//*[@id='datatableButtonsDiv']/div/a[7]")
    public WebElement uploadButton;
	
	@FindBy(id="table_data_length")
	public WebElement showEntries;
	
	@FindBy(id="datatableSearchFilter")
	public WebElement search;
	
	@FindBy(id="table_data_info")
	public WebElement showing;
	
	@FindBy(id="table_data_paginate")
	public WebElement pagination;
	
	@FindBy(id="table_data_previous")
	public WebElement previousButton;
	
	@FindBy(id="table_data_next")
	public WebElement nextButton;
	
	@FindBy(xpath="html/body/div[1]/footer/strong")
	public WebElement copyrightFooter;
	
	@FindBy(xpath="html/body/div[1]/footer/span[1]")
	public WebElement clientSoultionsFooter;
	
	@FindBy(xpath="html/body/div[1]/footer/span[2]")
	public WebElement yearFooter;
	
	@FindBy(xpath="html/body/div[1]/footer/div")
	public WebElement versionFooter;

}
