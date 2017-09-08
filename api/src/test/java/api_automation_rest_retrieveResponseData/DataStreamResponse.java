package api_automation_rest_retrieveResponseData;

import org.json.JSONObject;

import api_automation_rest.api.RESTTestAutomation;

public class DataStreamResponse extends RESTTestAutomation{
	public  String retriveDataStreamId(String createdDataStreamResponseBody,String valueToBeFetched) {
		log("6. Traverse created datastream.");
		String valueFromResponseBody = null;		
			JSONObject jsonResponse = new JSONObject(createdDataStreamResponseBody);
			try{
				valueFromResponseBody = jsonResponse.getString(valueToBeFetched);
				System.out.println(valueFromResponseBody);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		return valueFromResponseBody;
	}
	
}
