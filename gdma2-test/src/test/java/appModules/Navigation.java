package appModules;

import org.openqa.selenium.WebDriver;

import pageObjects.LogIn_Page;

public class Navigation {
	
	public static void login(WebDriver driver, String username, String password) throws InterruptedException
	{
		LogIn_Page login=new LogIn_Page(driver);
		login.usernameField.sendKeys(username);
		login.passwordField.sendKeys(password);
		login.loginButton.click();
			
	}

}
