package ie.clients.gdma2.test.suit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ie.clients.gdma2.test.step.uibc.HomePageBasicCheck;
import ie.clients.gdma2.test.step.uibc.LoginPageBasicCheck;
import ie.clients.gdma2.test.step.user.InsertUser;



@RunWith(Suite.class)
@SuiteClasses(
		
		{
			HomePageBasicCheck.class,LoginPageBasicCheck.class,InsertUser.class,
			
		}
		
		)


public class AllTests {
		

}
