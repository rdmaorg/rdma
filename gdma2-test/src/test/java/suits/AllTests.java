package suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import stepDefinition.InsertUser;
import stepDefinition.UIBCHomePage;
import stepDefinition.UIBCLoginPage;



@RunWith(Suite.class)
@SuiteClasses(
		
		{
			UIBCHomePage.class,UIBCLoginPage.class,InsertUser.class,
			
		}
		
		)


public class AllTests {
		

}
