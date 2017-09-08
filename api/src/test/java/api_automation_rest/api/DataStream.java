package api_automation_rest.api;

import java.util.HashMap;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import api_automation_rest_retrieveResponseData.DataStreamResponse;

public class DataStream extends RESTTestAutomation{

	protected static RESTTestAutomation restRequest = new RESTTestAutomation();
	protected static RESTVerification responsVerification = new RESTVerification();
	protected static HashMap<String,String> map = new HashMap<String,String>();
	
	
	@Parameters({"method","ExpectedStatusCode","requiredUrl","Authorization","verificationEntities"})
	@Test
	public void retrieveDataFromDatastream(String method, String ExpectedStatusCode,String requiredUrl,String Authorization,String verificationEntities) {
		System.out.println("In test");
		int numOfFailure = 0;
		String newURLToInvoke = null;
		if(requiredUrl.contains("#")) {
			newURLToInvoke=requiredUrl.replace("#","&");
			log("1. URL= "+newURLToInvoke);
			log("2. ExpectedStatusCode= "+ExpectedStatusCode);
			log("3. Authorization= "+Authorization);
			log("4. Content_Type= text/plain");
			System.out.println("New modified url="+newURLToInvoke);
		}
		String urlFinal = Configuration.baseURL + newURLToInvoke;
		map.put("URL", urlFinal);
		map.put("Method", method);
		map.put("ExpectedStatus", ExpectedStatusCode);
		map.put("Authorization", Authorization);
		map.put("Content_Type", "application/json");
		String responseBody = restRequest.executeREST_Service(map);
		//boolean isJsonVerifiedSuccessfully = responsVerification.verifyJsonObjectValues(responseBody.trim(), verificationEntities);
		//System.out.println("isJsonVerifiedSuccessfully= "+isJsonVerifiedSuccessfully);
		log("ResponseBody= "+responseBody);
		if (responsVerification.verifyJsonObjectValues(responseBody.trim(), verificationEntities)) {
			log("<Strong><font color=#008000>Pass</font></strong> - Id id matched");
		} else {
			log("Fail - Id is not matched");
			numOfFailure++;
		}
		if (numOfFailure > 0) {
			Assert.assertTrue(false);
		}
	} 
	
	@Parameters({"method","ExpectedStatusCode","requiredUrl","Authorization","verificationEntities"})
	@Test
	public void NoSuchDatastreamAvailable(String method, String ExpectedStatusCode,String requiredUrl,String Authorization,String verificationEntities) {
		System.out.println("In test");
		String newURLToInvoke = null;
		if(requiredUrl.contains("#")) {
			newURLToInvoke=requiredUrl.replace("#","&");
			System.out.println("New modified url="+newURLToInvoke);
		}
		String urlFinal = Configuration.baseURL + newURLToInvoke;
		map.put("URL", urlFinal);
		map.put("Method", method);
		map.put("ExpectedStatus", ExpectedStatusCode);
		map.put("Authorization", Authorization);
		map.put("Content_Type", "application/json");
		String responseBody = restRequest.executeREST_Service(map);
		log("ResponseBody= "+responseBody);
		boolean isJsonVerifiedSuccessfully = responsVerification.verifyJsonObjectValues(responseBody.trim(), verificationEntities);
		System.out.println("isJsonVerifiedSuccessfully= "+isJsonVerifiedSuccessfully);
	}
	
	@Parameters({"method","ExpectedStatusCode","requiredUrl","Authorization","requestBody"})
	@Test
	public void createDataStream(String method, String ExpectedStatusCode,String requiredUrl,String Authorization,String requestBody) {
		System.out.println("In test");
		String newURLToInvoke = null;
		if(requiredUrl.contains("#")) {
			newURLToInvoke=requiredUrl.replace("#","&");
			System.out.println("New modified url="+newURLToInvoke);
		} else {
			newURLToInvoke = requiredUrl;
		}
		String urlFinal = Configuration.baseURL + newURLToInvoke;
		log("URL Invoked = "+urlFinal);
		map.put("URL", urlFinal);
		map.put("Method", method);
		map.put("ExpectedStatus", ExpectedStatusCode);
		map.put("Authorization", Authorization);
		map.put("Content_Type", "application/json");
		map.put("RequestBody", requestBody.trim());
		String responseBody = restRequest.executeREST_Service(map);
		log("ResponseBody= "+responseBody);
		//boolean isJsonVerifiedSuccessfully = responsVerification.verifyJsonObjectValues(responseBody.trim(), verificationEntities);
		//System.out.println("isJsonVerifiedSuccessfully= "+isJsonVerifiedSuccessfully);
		
	}
	
	@Parameters({"method","ExpectedStatusCode","requiredUrl","Authorization","requestBody","additionalRequestBody"})
	@Test
	public void endToEnd(String method, String ExpectedStatusCode,String requiredUrl,String Authorization,String requestBody,String additionalRequestBody) {
		DataStreamResponse dataStreamResponse = new DataStreamResponse();
		System.out.println("In test");
		String newURLToInvoke = null;
		String newRequestBody = null;
		if(requiredUrl.contains("#")) {
			newURLToInvoke=requiredUrl.replace("#","&");
			System.out.println("New modified url="+newURLToInvoke);
		} else {
			newURLToInvoke = requiredUrl;
		}
		if(requestBody.contains("$newDataStreamId$")) {
			Random rn = new Random();
			newRequestBody = requestBody.replace("$newDataStreamId$", "jig"+rn.nextInt(999));
		}
		System.out.println("New request body="+newRequestBody);
		String urlFinal = Configuration.baseURL + newURLToInvoke;
		log("URL Invoked = "+urlFinal);
		map.put("URL", urlFinal);
		map.put("Method", method);
		map.put("ExpectedStatus", ExpectedStatusCode);
		map.put("Authorization", Authorization);
		map.put("Content_Type", "application/json");
		map.put("RequestBody", newRequestBody.trim());
		String createdDataStreamResponseBody = restRequest.executeREST_Service(map);
		log("CreatedDataStreamResponseBody= "+createdDataStreamResponseBody);
		String datastreamId = dataStreamResponse.retriveDataStreamId(createdDataStreamResponseBody,"id");
		log("Data stream created with ID="+datastreamId);
		log("Details of the created stream fetched = ");
		retrieveDataFromDatastreamUsingId("GET","200","/datastream/$dataStreamId$?streaming=false#hasMoreData=false",Authorization,datastreamId);
		//boolean isJsonVerifiedSuccessfully = responsVerification.verifyJsonObjectValues(responseBody.trim(), verificationEntities);
		//System.out.println("isJsonVerifiedSuccessfully= "+isJsonVerifiedSuccessfully);
		//Add data to created data stream
		addDataToDataStreamWithId("POST","202","/datastream/$dataStreamId$",Authorization,additionalRequestBody,datastreamId);
		//Retrieve data again to check attached data
		retrieveDataFromDatastreamUsingId("GET","200","/datastream/$dataStreamId$?streaming=false#hasMoreData=false",Authorization,datastreamId);
	}
	
	public void retrieveDataFromDatastreamUsingId(String method, String ExpectedStatusCode,String requiredUrl,String Authorization,String dataStreamId) {
		System.out.println("In test");
		int numOfFailure = 0;
		String newURLToInvoke = null;
		if(requiredUrl.contains("$dataStreamId$")) {
			newURLToInvoke=requiredUrl.replace("$dataStreamId$",dataStreamId);
			log("1. URL= "+newURLToInvoke);
			log("2. ExpectedStatusCode= "+ExpectedStatusCode);
			log("3. Authorization= "+Authorization);
			log("4. Content_Type= text/plain");
			System.out.println("New modified url="+newURLToInvoke);
		}
		String urlFinal = Configuration.baseURL + newURLToInvoke;
		map.put("URL", urlFinal);
		map.put("Method", method);
		map.put("ExpectedStatus", ExpectedStatusCode);
		map.put("Authorization", Authorization);
		map.put("Content_Type", "application/json");
		String responseBody = restRequest.executeREST_Service(map);
		//boolean isJsonVerifiedSuccessfully = responsVerification.verifyJsonObjectValues(responseBody.trim(), verificationEntities);
		//System.out.println("isJsonVerifiedSuccessfully= "+isJsonVerifiedSuccessfully);
		log("ResponseBody= "+responseBody);
		/*if (responsVerification.verifyJsonObjectValues(responseBody.trim(), verificationEntities)) {
			log("<Strong><font color=#008000>Pass</font></strong> - Id id matched");
		} else {
			log("Fail - Id is not matched");
			numOfFailure++;
		}
		if (numOfFailure > 0) {
			Assert.assertTrue(false);
		}*/
	} 
	
	public void addDataToDataStreamWithId(String method, String ExpectedStatusCode,String requiredUrl,String Authorization,String requestBody,String dataStreamId) {
		System.out.println("In test");
		String newURLToInvoke = null;
		if(requiredUrl.contains("$dataStreamId$")) {
			newURLToInvoke=requiredUrl.replace("$dataStreamId$",dataStreamId);
			System.out.println("Add datastream url="+newURLToInvoke);
		}
		String urlFinal = Configuration.baseURL + newURLToInvoke;
		log("URL Invoked = "+urlFinal);
		map.put("URL", urlFinal);
		map.put("Method", method);
		map.put("ExpectedStatus", ExpectedStatusCode);
		map.put("Authorization", Authorization);
		map.put("Content_Type", "text/plain");
		map.put("RequestBody", requestBody.trim());
		String responseBody = restRequest.executeREST_Service(map);
		log("ResponseBody= "+responseBody);
	}
	
	
	@Parameters({"method","ExpectedStatusCode","requiredUrl","Authorization","requestBody"})
	@Test
	public void addDataToDataStream(String method, String ExpectedStatusCode,String requiredUrl,String Authorization,String requestBody) {
		System.out.println("In test");
		String newURLToInvoke = null;
		if(requiredUrl.contains("#")) {
			newURLToInvoke=requiredUrl.replace("#","&");
			System.out.println("New modified url="+newURLToInvoke);
		} else {
			newURLToInvoke = requiredUrl;
		}
		String urlFinal = Configuration.baseURL + newURLToInvoke;
		log("URL Invoked = "+urlFinal);
		map.put("URL", urlFinal);
		map.put("Method", method);
		map.put("ExpectedStatus", ExpectedStatusCode);
		map.put("Authorization", Authorization);
		map.put("Content_Type", "text/plain");
		map.put("RequestBody", requestBody.trim());
		String responseBody = restRequest.executeREST_Service(map);
		log("ResponseBody= "+responseBody);
		//boolean isJsonVerifiedSuccessfully = responsVerification.verifyJsonObjectValues(responseBody.trim(), verificationEntities);
		//System.out.println("isJsonVerifiedSuccessfully= "+isJsonVerifiedSuccessfully);
	}
	
}
