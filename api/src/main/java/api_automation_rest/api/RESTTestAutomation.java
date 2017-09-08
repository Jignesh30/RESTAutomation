package api_automation_rest.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import org.json.JSONArray;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class RESTTestAutomation {
	
	public static HashMap<String,String> map = new HashMap<String,String>();
	
	@BeforeMethod
	public static void initialization(){
		System.out.println("Initialize the settings");
		Configuration.initializasettings();
		String baseUrl = Configuration.baseURL;
	}
	
	@AfterMethod
	public static void afterclass() 
	{
		System.out.println("Execution Done.");
	}
	
	public static void log(String msg) {
		System.out.println(msg);
		Reporter.log("<br></br>" + msg);
	}
	
	public String executeREST_Service(HashMap map) {
		String actualResponse = null;
		String url,method,ExpectedStatusCode,REST_RequestBody;
		url = method = ExpectedStatusCode = REST_RequestBody = null;
		Map<String, String> headers = new HashMap<String, String>();
		Set set = map.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			String keyval = me.getKey().toString();
			switch(keyval)
			{
			//case "UrlForAuthentication": url= me.getValue().toString(); 							break;
			case "Authorization":   	 headers.put("Authorization", me.getValue().toString()); 	break;
			case "authToken":   	 	 headers.put("authToken", me.getValue().toString());		break;
			case "Content_Type":   		 headers.put("Content_Type", me.getValue().toString());		break;
			case "Method": 				 method=me.getValue().toString();							break;
			case "RequestBody": 		 REST_RequestBody=me.getValue().toString(); 				break;
			case "ExpectedStatus": 		 ExpectedStatusCode=me.getValue().toString();				break;
			case "URL": 				 url= me.getValue().toString();								break;
			}
		}
		try{
			actualResponse = callHttpService(url, method, headers,ExpectedStatusCode,REST_RequestBody);
		}catch(Exception e){
			e.printStackTrace();
		}
		return actualResponse;
	}
	/**
	 * callHttpService           	This method executes the http method
	 * @param url					URL which is to be executed
	 * @param method				Execution method - GET/POST/PUT/DELETE
	 * @param headers				It includes multiple headers for method execution like username/password/authentication/parameters
	 * @param REST_expectedResponse Expected response after execution
	 * @param REST_RequestBody		Request body is required for POST and PUT method
	 * @return String				Response body is returned as a String
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	protected String callHttpService(String url, String method, Map<String, String> headers,String REST_expectedResponse,String REST_RequestBody)throws Exception {
		HttpClient client = null;
		HttpMethodBase httpBaseMethod = null;
		int actualResponse = 0;
		try{
			client = new HttpClient();
		} catch(Exception e){
			System.out.println("**** Exception in creating the client*****");
			e.printStackTrace();
		}
		switch(method) 
		{
		case "GET" : 	
			httpBaseMethod = new GetMethod(url);
			GetMethod getMethod = (GetMethod) httpBaseMethod;
			getMethod.setRequestHeader("Content-Type", (String)headers.get("Content_Type"));
			getMethod.setRequestHeader("Authorization", (String)headers.get("Authorization"));
			break;
		case "POST" :
			httpBaseMethod = new PostMethod(url);
			PostMethod postMethod = (PostMethod) httpBaseMethod;
			postMethod.setRequestHeader("Content-Type", (String)headers.get("Content_Type"));
			postMethod.setRequestHeader("Authorization", (String)headers.get("Authorization"));
			if(REST_RequestBody != null){
				postMethod.setRequestBody(REST_RequestBody);
			}
			break;
		case "PUT" :
			httpBaseMethod = new PutMethod(url);
			PutMethod putMethod = (PutMethod) httpBaseMethod;
			putMethod.setRequestHeader("Content-Type", (String)headers.get("Content_Type"));
			if(REST_RequestBody != null){
				putMethod.setRequestBody(REST_RequestBody);
			}
			break;
		case "DELETE" :
			httpBaseMethod = new DeleteMethod(url);
			DeleteMethod deleteMethod = (DeleteMethod) httpBaseMethod;
			deleteMethod.setRequestHeader("Content-Type", (String)headers.get("Content_Type"));
			break;
		}
		try{
			actualResponse = client.executeMethod(httpBaseMethod);
		}catch(Exception e){
			e.printStackTrace();
		}
		/*if (actualResponse != HttpStatus.SC_OK) {
			throw new Exception("Method execution failed for method type=:: " + method + " and Response code generated :: " + actualResponse);
		} */
		String responsebody = httpBaseMethod.getResponseBodyAsString();
		System.out.println("ResponseBody = "+responsebody);
		Assert.assertEquals(actualResponse, Integer.parseInt(REST_expectedResponse));
		
		return responsebody;
	}
}
