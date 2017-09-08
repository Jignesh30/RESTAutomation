package api_automation_rest.api;

import java.util.HashMap;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CreateAssessment extends RESTTestAutomation{
	protected static RESTTestAutomation restRequest = new RESTTestAutomation();
	protected static HashMap<String,String> map = new HashMap<String,String>();
	
	@Parameters({"method","ExpectedStatusCode","requiredUrl","Authorization"})
	@Test
	public void createAssessment(String method, String ExpectedStatusCode,String requiredUrl,String Authorization) {
		System.out.println("In test");
		String urlFinal = Configuration.baseURL + requiredUrl;
		map.put("URL", urlFinal);
		map.put("Method", method);
		map.put("ExpectedStatus", ExpectedStatusCode);
		map.put("Authorization", Authorization);
		map.put("Content_Type", "application/json");
		restRequest.executeREST_Service(map);
	} 
}