package ie.clients.gdma2.test.step;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features="Feature"
		,glue={"stepDefinition"}
		,monochrome=true
		,plugin ={"pretty","html:target/html/", "json:target/json/output.json"}
		,tags={"@Run"}
		)


public class TestRunner {

}

