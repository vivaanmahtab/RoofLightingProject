
// UI_interaction_methods.java
// Author: Vivaan Mahtab

package user_interface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import settings.Settings;

/**
 * series of static methods to be implemented by <em>UI_main.java</em> in order
 * to process and handle functionality of UI window
 * 
 * @author Vivaan Mahtab
 *
 * @see local/src/user_interface/UI_main
 */
public class UI_interaction_methods implements Settings{
	
	/**
	 * gets coordinates of buttons based on initial X position of each button
	 * @param buttons - the buttons (used for # of buttons)
	 * @return -  an array of coordinates
	 */
	public static float[] get_button_coordinates(String[] buttons) {
		
		float[] coors = new float[buttons.length];
		float initialPos = WIDTH/2 - TEXTBOX_WIDTH/2 + BUTTON_WIDTH/2;
		float dx = (TEXTBOX_WIDTH-BUTTON_WIDTH)/(buttons.length-1);
		
		for(int i = 0; i < buttons.length; i++) {
			float xCoor = initialPos - BUTTON_WIDTH/2 + i *(dx);
			coors[i] = xCoor;
		}		
		return coors;
	}
	
	/**
	 * gets current time and returns it as "HH:MM" in 24H format
	 * @return a string representing the time
	 */
	public static String getTime() {
		SimpleDateFormat formatter= new SimpleDateFormat("HH:mm");
		Date time = new Date(System.currentTimeMillis());
		return formatter.format(time);
	}
	
	/**
	 * gets current date and returns it as "MM/DD"
	 * @return a string representing the date
	 */
	public static String getDate() {
		SimpleDateFormat formatter= new SimpleDateFormat("MM/dd");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}
	
	/**
	 * gets current temperature in Fahrenheit and returns it as a String 
	 * 
	 * This method uses an external public API from https://openweathermap.org/
	 * then parses the weather out of it
	 * 
	 * this weather is the ACUTAL weather, neglecting wind chill and external factors
	 * 
	 * @param zipCode - zip code to retrieve weather within United States
	 * 
	 * @param appID - personal authentication code for API (go to api.openweathermap.org to generate)
	 * @return - a string representing current temperature in Fahrenheit
	 * 
	 * @implNote only works for US
	 * @exception if method is unable to retrieve the weather it returns the String "ERROR"
	 */
	public static String getWeather(String zipCode, String appID) {
		try {
			
			//request URL of API from @param zipCode
			URL urlForGetRequest = new URL("http://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + ",us&appid=" + appID);
	
		    String readLine = null;
	
		    //does connection work?
		    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
		    
		    //set API request mode to GET
		    conection.setRequestMethod("GET");
		    
		    //response?
		    int responseCode = conection.getResponseCode();
		    
		    //given that response works:
		    if (responseCode == HttpURLConnection.HTTP_OK) {
		    	
		    	//Read in entirety of weather info from API
		    	BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	
		    	StringBuffer response = new StringBuffer();
		    	while ((readLine = in .readLine()) != null) {
		    		response.append(readLine);
		    	} in .close();
		    		
		    	//extracts weather information
		    	return grabTemp(response.toString());
		    }
		    
		} catch(IOException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
	/**
	 * a helper function that parses weather out of the total weather info from https://openweathermap.org/ API
	 * @param weatherInfo - the string of the weather info
	 * @return - the temperature in Fahrenheit
	 */
	private static String grabTemp(String weatherInfo) {

		//find index of temperature from weather info
		int tempIndex = weatherInfo.indexOf("\"temp\":");
		
		//clause if cannot find temperature
		if(tempIndex==-1)
			return "ERROR";
		
		//API temp settings is after String "temp": so 
		//7 characters allows for receiving float value
		int endIndex = tempIndex + 7;
		
		//gets values until end of float
		while(weatherInfo.charAt(endIndex)!=',') {
			endIndex++;
		}

		//try clause to catch if number is a float
		try { 
			float current_temperature = Float.parseFloat(weatherInfo.substring(tempIndex + 7, endIndex));
			
			//converts from Kelvin to Fahrenheit
			current_temperature -= 273.15;
			current_temperature *= 9d/5;
			current_temperature += 32;
			
			//removes decimals to limit display length then returns
			return String.format("%.0f", current_temperature) + "°F";
			
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return "ERROR";	
		}
	}
}


