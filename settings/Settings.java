package settings;

public interface Settings {
	
	// THESE MUST BE INPUTED FOR WEATHER FUNCTIONALITY TO WORK
	// GENERATE KEY AT: http://api.openweathermap.org/
	// ZIPCODE IS FOR US 5 DIGIT ZIPCODES
	
	public final static String openweathermap_api_key = "";
	public final static String openweathermap_api_zipcode = "";
	
	
	/**     USER INTERFACE SETTINGS     **/
	
	//MAX HEIGHT OF GRID
		public final static int BULB_GRID_HEIGHT = 10;
		//LENGTH OF PHYSICAL GRID
		public final static int BULB_GRID_LENGTH = 30;
	
	
	/**      USER INTERFACE SETTINGS     **/
		
	//WIDTH AND HEIGHT OF UI WINDOW
		final static int WIDTH = 1000;
		final static int HEIGHT = 500;
		
		
	//EXCESS BULB GRID FOR UI READABILITY
		final static int USER_INTERFACE_EXCESS_LENGTH = 10;
		
		
	//WIDTH AND HEIGHT OF BULB GRID ON UI WINDOW
		final static int GRID_WIDTH = 20 * (BULB_GRID_LENGTH + USER_INTERFACE_EXCESS_LENGTH) + 12;
		final static int GRID_HEIGHT = 20 * BULB_GRID_HEIGHT + 12;
		
		
	//WIDTH AND HEIGHT OF TEXTBOOK ON UI WINDOW
		final static int TEXTBOX_WIDTH = 2*WIDTH/3;
		final static int TEXTBOX_HEIGHT = 40;
		
		
	//WIDTH AND HEIGHT OF ONE BUTTON ON UI WINDOW
		final static int BUTTON_WIDTH = 100;
		final static int BUTTON_HEIGHT = 30;
		
		
	//VARIOUS BUTTON OPTIONS FOR UI SELECTION (ordered)
		
		// TIME: displays current time in 24H format
		// DATE: displays current date in mm/dd format
		// WEATHER: displays current temperature in Fahrenheit
		// PAUSE: toggles scroll feature of LED board
		final static String[] OPTIONS = {"TIME", "DATE", "WEATHER", "PAUSE"};
		
	
}
