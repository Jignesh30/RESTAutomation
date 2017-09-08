package api_automation_rest.api;

import java.util.HashMap;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import api_automation_rest_retrieveResponseData.DataStreamResponse;

public class CreateDataStreamAndAddDataToDataStream extends RESTTestAutomation{

	protected static RESTTestAutomation restRequest = new RESTTestAutomation();
	protected static RESTVerification responsVerification = new RESTVerification();
	protected static HashMap<String,String> map = new HashMap<String,String>();
	
	
	@Parameters({"method","ExpectedStatusCode","requiredUrl","Authorization","requestBody","additionalRequestBody"})
	@Test
	public void createDataStreamAndAddDatatoDatastream(String method, String ExpectedStatusCode,String requiredUrl,String Authorization,String requestBody,String additionalRequestBody) {
		DataStreamResponse dataStreamResponse = new DataStreamResponse();
		System.out.println("Execution Start.");
		String newURLToInvoke = null;
		String newRequestBody = null;
		String newNameForDataStream = null;
		if(requiredUrl.contains("#")) {
			newURLToInvoke=requiredUrl.replace("#","&");
			System.out.println("New modified url="+newURLToInvoke);
		} else {
			newURLToInvoke = requiredUrl;
		}
		if(requestBody.contains("$newDataStreamId$")) {
			Random rn = new Random();
			newNameForDataStream = "jigTest"+rn.nextInt(999);
			newRequestBody = requestBody.replace("$newDataStreamId$", newNameForDataStream);
		}
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
		
		retrieveDataFromDatastreamUsingId("GET","200","/datastream/$dataStreamId$?streaming=false#hasMoreData=false",Authorization,datastreamId);
		boolean isJsonVerifiedSuccessfully = responsVerification.verifyResponseForDataStreamIdAndName(createdDataStreamResponseBody, datastreamId,newNameForDataStream);
		 Assert.assertEquals(isJsonVerifiedSuccessfully, true,"Data is not verified after retriving datastream id.");
		//Add data to created data stream
		addDataToDataStreamWithId("POST","202","/datastream/$dataStreamId$",Authorization,additionalRequestBody,datastreamId);
		//Retrieve data again to check attached data
		retrieveDataFromDatastreamUsingId("GET","200","/datastream/$dataStreamId$?streaming=false#hasMoreData=false",Authorization,datastreamId);
		isJsonVerifiedSuccessfully = responsVerification.verifyResponseForDataStreamIdAndName(createdDataStreamResponseBody, datastreamId,newNameForDataStream);
		 Assert.assertEquals(isJsonVerifiedSuccessfully, true,"Data is not matched after adding data to datastream.");
		
	}
	
	public void retrieveDataFromDatastreamUsingId(String method, String ExpectedStatusCode,String requiredUrl,String Authorization,String dataStreamId) {
		int numOfFailure = 0;
		String newURLToInvoke = null;
		if(requiredUrl.contains("$dataStreamId$")) {
			newURLToInvoke=requiredUrl.replace("$dataStreamId$",dataStreamId);
			System.out.println("Url to retrive datastream= "+newURLToInvoke);
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
		String newURLToInvoke = null;
		if(requiredUrl.contains("$dataStreamId$")) {
			newURLToInvoke=requiredUrl.replace("$dataStreamId$",dataStreamId);
			System.out.println("Add datastream url="+newURLToInvoke);
		}
		String urlFinal = Configuration.baseURL + newURLToInvoke;
		log("URL to Add data to DataStream = "+urlFinal);
		map.put("URL", urlFinal);
		map.put("Method", method);
		map.put("ExpectedStatus", ExpectedStatusCode);
		map.put("Authorization", Authorization);
		map.put("Content_Type", "text/plain");
		map.put("RequestBody", requestBody.trim());
		String responseBody = restRequest.executeREST_Service(map);
		log("ResponseBody= "+responseBody);
	}
	
}
