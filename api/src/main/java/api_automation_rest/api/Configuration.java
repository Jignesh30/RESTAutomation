package api_automation_rest.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	
	public static String path;
	private static String configPath = "/config.properties";
	public static String baseURL = null;
	
	public static void initializasettings(){
		try{
			String line = null;
			String[] keyValue = null;
			path = new File(".").getCanonicalPath().replace("\\", "/");
			FileReader fr = new FileReader(new File(path+configPath).getAbsoluteFile());;
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
					keyValue = line.split("=");
					if(keyValue.length != 1) {
						keyValue[1] = keyValue[1].trim();
					} else {
						throw new Exception("The value for key='"+keyValue[0]+"' should not be blank.Please check config.properties file");
					}		
					switch(keyValue[0].toString()){
					case "baseUrl": 
						baseURL = keyValue[1];
						//System.out.println("baseURL:"+baseURL);
						break;
					}
			}}
		catch(Exception e) {}
	}
}
