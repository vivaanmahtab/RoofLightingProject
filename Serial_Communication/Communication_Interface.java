
// Communication_Interface.java
// Author: Vivaan Mahtab

package Serial_Communication;

import processing.core.PApplet;
import processing.serial.*;

/**
 * <em>Serial_Communcation Class</em>
 * 
 * backend interface to allow processing UI window to
 * communicate to arduino software for arduino processing
 * of current display to be forwarded to arduino controller
 * for display interface
 * 
 * @author Vivaan Mahtab
 * 
 * @see local/user_interface/Communication_Interface.java
 * @see https://www.arduino.cc/
 * @see https://processing.org/
 * 
 * @implSpec Serial Baud rate is 9600
 * 
 */
public class Communication_Interface {
	
	//PApplet application
	PApplet app;
	//Serial communication port
	Serial myPort;
	
	/**
	 * initializer function - finds current arduino port and
	 * 						  sets serial port accordingly
	 * 
	 * @param application - PApplet application to retrieve
	 * 						information from
	 * 
	 * @param port_index - signifies the port to select as the Serial
	 * 					   communication port, this is selected using
	 * 					   before UI interaction displays are met
	 * 	
	 * @see UI_main.setup_finished
	 * @see UI_main.selectedPort
	 * @see UI_display_methods.
	 * 								
	 * @exception if port cannot be found, serialPort set to null
	 */
	public Communication_Interface(PApplet application, int port_index) {
		
		this.app = application;
		
		
		//attempt to set Serial object port to port_index; defaults to null
		try { 
			String portName = Serial.list()[port_index];
			this.myPort = new Serial(this.app, portName, 9600);
		
		} catch(Exception e) {
			this.myPort = null;
		}
		
		
	}
	
	// Getter function to receive port options for Serial communication
	public static String[] getPorts() {
		return Serial.list();
	}

	/**
	 * function that writes current board state into Serial for Arduino to
	 * read in, this can be processed from arduino to process for LED board
	 * @param grid - any array of Strings be processed - this is received from
	 * 				<em>WordConstructor</em>.get_offset_grid()
	 * 
	 * @implNote assumes grid is rectangular and has height of Settings.BOARD_GRID_HEIGHT
	 * 
	 * @see local/word_processing/WordConstructor
	 * @see local/settings/Settings
	 */
	public void update(String[] grid) {
		
		// check if this.myPort has been initialized to a port
		if(myPort == null) {
			return;
		}
		
		// port available for communication?
		if ( myPort.available() > 0) 
		{ 
			try {
				
				// write in to Serial communication: format is each row split
				// by ", " spacing
				String cumulativeGrid = "";
				for(String command : grid) {
					cumulativeGrid += command;
					cumulativeGrid += ", ";
				}
				
				//remove final ", " and then write
				cumulativeGrid = cumulativeGrid.substring(0, cumulativeGrid.length()-1);
				myPort.write(cumulativeGrid);
			}

			catch (NullPointerException e) {
				e.printStackTrace();}
		}	  
	}
}
