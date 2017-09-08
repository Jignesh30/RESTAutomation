package api_automation_rest.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

public class RESTVerification extends RESTTestAutomation{


	public static boolean verifyJsonObjectValues(String actualResponseBody,String expectedEntitiesToBeVerified) {
		log("6. Verify Response.");
		boolean isVerificationSuccessful = false;
		String valueToBeVerfiedKey ;
		String valueToBeVerfiedValue ;
		if(expectedEntitiesToBeVerified.contains("@")) {
		String valuesToBeVerified[] = expectedEntitiesToBeVerified.split("@");
		for(int outerCounter =0; outerCounter<valuesToBeVerified.length;outerCounter++) {
			System.out.println("Counter="+outerCounter);
			String valueToBeVerifiedArray[] = valuesToBeVerified[outerCounter].split("=");
			valueToBeVerfiedKey = valueToBeVerifiedArray[0];
			valueToBeVerfiedValue = valueToBeVerifiedArray[1];
			JSONObject jsonResponse = new JSONObject(actualResponseBody);
			try{
				String valueFromResponseBody = jsonResponse.getString(valueToBeVerfiedKey);
				System.out.println(valueFromResponseBody);
				if(valueFromResponseBody.equalsIgnoreCase(valueToBeVerfiedValue)){
					isVerificationSuccessful= true;
				}else{
					isVerificationSuccessful = false;
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				isVerificationSuccessful = false;
			}
		}

		} else {
			String valueToBeVerifiedArray[] = expectedEntitiesToBeVerified.split("=");
			valueToBeVerfiedKey = valueToBeVerifiedArray[0];
			valueToBeVerfiedValue = valueToBeVerifiedArray[1];
			JSONObject jsonResponse = new JSONObject(actualResponseBody);
			try{
				String valueFromResponseBody = jsonResponse.getString(valueToBeVerfiedKey);
				System.out.println(valueFromResponseBody);
				if(valueFromResponseBody.equalsIgnoreCase(valueToBeVerfiedValue)){
					isVerificationSuccessful= true;
				}else{
					isVerificationSuccessful = false;
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
				isVerificationSuccessful = false;
			}
		}

		return isVerificationSuccessful;

	}
	public boolean verifyResponseForDataStreamIdAndName(String responseBody,String id,String nameOfDataStream) {
		boolean isVerificationSuccessful = false;
		JSONObject jsonResponse = new JSONObject(responseBody);
		try{
			String idValueFromResponseBody = jsonResponse.getString("id");
			String namevalueFromResponseBody = jsonResponse.getString("name");
			System.out.println(idValueFromResponseBody);
			if(idValueFromResponseBody.equalsIgnoreCase(id) && namevalueFromResponseBody.equalsIgnoreCase(nameOfDataStream)){
				isVerificationSuccessful= true;
			}else{
				isVerificationSuccessful = false;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			isVerificationSuccessful = false;
		}
		return isVerificationSuccessful;
	}
}
