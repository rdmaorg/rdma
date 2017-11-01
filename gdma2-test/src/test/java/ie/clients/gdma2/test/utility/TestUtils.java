package ie.clients.gdma2.test.utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtils {

	public static void waitForJQuery(WebDriver driver) {
	    (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
	        public Boolean apply(WebDriver d) {
	            JavascriptExecutor js = (JavascriptExecutor) d;
	            return (Boolean) js.executeScript("return !!window.jQuery && window.jQuery.active == 0");
	        }
	    });
	}
}
