
// UI_main.java
// Author: Vivaan Mahtab

package user_interface;

import processing.core.PApplet;
import processing.core.PFont;

import word_processing.WordEvent;
import user_interface.UI_display_methods;
import settings.Settings;

import Serial_Communication.Communication_Interface;

/**
 * 
 * Main User Interface processing
 * Uses Processing 3.0
 * https://processing.org/
 * 
 * refer to https://processing.org/reference/ for processing methods
 * 
 * Draws a User interface window for user interaction
 * this allows for various options for users to display onto
 * the LED board
 * 
 * these options can be found in settings/Settings.OPTIONS
 * with documentation
 * 
 * Methods statically accessed from:
 * @see local/user_interface_UI_interaction_methods.java
 * @see local/user_interface_UI_display_methods.java
 * 
 * @see local/word_processing.WordEvent.java
 * @see local/Serial_Communication/Communication_Interface.java
 * 
 * @author Vivaan Mahtab
 *
 */
public class UI_main extends PApplet implements Settings{
	
	// If a custom message is inputed - String held here
	String current_text;
	
	// The WordEvent object 
	WordEvent wordEvent;
	
	// Arduino Serial Communication
	Communication_Interface comms;
	
	// These are used for initial setup to
	// select appropriate Serial comm port
	boolean setup_finished = false;
	int selectedPort = -1;
	
	// Button coordinates for settings.Settings.OPTIONS
	float[] buttonCoordinates;
	
	//none, sent_message, time, date, weather -
	//Current occurring action being executed
	String state;
	
	// scroll direction and speed (LEDs per a tick)
	// (left) -∞ <- 0 -> ∞ (right)
	int scroll;
	
	public static void main(String[] args) {
			   PApplet.main("user_interface.UI_main");
	}
	
	public void settings() {
		size(WIDTH, HEIGHT);
	}
	
	//generates font for UI, initializes appropriate variables
	public void setup() {

		background(255);
		
		this.state = "none";
		this.scroll = 0;
		fill(0);
		
		PFont font = createFont("HelveticaNeue", 16);
		textFont(font);
		
		// no current display
		this.current_text = "";
		this.wordEvent = new WordEvent("");
		
		this.buttonCoordinates = UI_interaction_methods.get_button_coordinates(OPTIONS); 
		
		
	}
	
	// processing draw() method
	public void draw() {
		
		clear();
		background(255);
		fill(0);
		
		//Initial setup display
		if(!this.setup_finished) {
			UI_display_methods.portSelection(this, this.selectedPort);
			return;
		}
		
		//draw static UI components
		UI_display_methods.draw_settings(this, this.state, this.scroll);
		UI_display_methods.draw_buttons(this, OPTIONS);
		UI_display_methods.draw_empty_LED_board(this);
		UI_display_methods.draw_filled_LED_board(this, wordEvent.get_offset_board());
		UI_display_methods.draw_textbox(this, current_text, frameCount%70>20);
		
		
		//CONSTANTLY UPDATE TIME IF TIME IS OPTION SELECTED AND NOT MOVED
		if(this.state.equals("TIME") && wordEvent.getPosition()<=0)
			wordEvent = new WordEvent(UI_interaction_methods.getTime());
		
		
		//flashing for vertical bar of textbox indicator
		if(frameCount%7==0) {
			this.wordEvent.move(-scroll);
			if(wordEvent.getSize() - wordEvent.getPosition() < 0) {
				this.display_changeRESET();
			}
		}
	}
	
	
	// processing method that checks if key pressed on computer keyboard
	public void keyPressed() {
		
		// only reads text if setup finished
		if(!this.setup_finished)
			return;
		
		// coded keys to avoid arrow keys and non ASCII
		if(key == CODED) {
			return;
		}
		
		// if enter/return send board as sent_message (custom entered text)
		else if (key==ENTER||key==RETURN) {
			
			this.display_change("sent_message", -1);
			
			this.wordEvent = new WordEvent(this.current_text, -BULB_GRID_LENGTH);
			this.current_text = "";
			  
		} 
		
		// backspace for deleting previous index of sent message
		else if(key==BACKSPACE) {
			int stringLength = current_text.length();
			if(stringLength > 0) {
				this.current_text = this.current_text.substring(0, stringLength - 1);
			}
		}
		
		// if none other conditions met, extend current_text in textbox
		else
			this.current_text = this.current_text + key;
		}
	
	
	// processing method that checks if a mouse button clicked
	public void mouseClicked() {
		
		//initial mouse click checking during setup
		if(!this.setup_finished) {
			
			// find current ports for indices
			String[] ports = Communication_Interface.getPorts();
			
			// if selected port (received from this.mouseMoved() ) matches index
			if(0 <= this.selectedPort && this.selectedPort < ports.length)
				
				// sets current Serial Comm port for Serial communication
				clear();
				background(255);
				this.comms = new Communication_Interface(this, this.selectedPort);
				this.setup_finished = true;
				return;
		}
		
		//within row of button options
		if(HEIGHT/8 + 10 <= mouseY && mouseY <= HEIGHT/8 + BUTTON_HEIGHT + 10) {
			
			//checks if and which button is selected
			for(int i = 0; i < buttonCoordinates.length; i++) {
				if(buttonCoordinates[i] <= mouseX && mouseX <= buttonCoordinates[i] + BUTTON_WIDTH) {
					
					//if button matches pause, toggles scrolling left
					if(OPTIONS[i].equals("PAUSE")) {
						if(this.scroll!=0)
							this.scroll = 0;
						else
							this.scroll = -1;
						return;
					
					}
					
					//if matches other option, sets this.state to option then breaks loop
						this.state = OPTIONS[i];
					break;
				}
			}
		}
		
		// if a state has been selected:
		
		//TIME OPTION
		if(!this.state.equals("none")) {
			switch(this.state) {
			case "TIME":
				this.state = "LOADING";
				wordEvent = new WordEvent(UI_interaction_methods.getTime());
				this.state = "TIME";
				this.scroll = 0;
				break;
			
		//DATE OPTION
			case "DATE":
				this.state = "LOADING";
				wordEvent = new WordEvent(UI_interaction_methods.getDate());
				this.state = "DATE";
				this.scroll = 0;
				break;
		
		//WEATHER OPTION
			case "WEATHER":
				this.state = "LOADING";
				wordEvent = new WordEvent(UI_interaction_methods.getWeather(openweathermap_api_zipcode, openweathermap_api_key));
				this.state = "WEATHER";
				this.scroll = 0;
				break;
				
			}
		}
	}
	
	//processing mouseMoved() method -> only used during setup
	public void mouseMoved() {
		if(!this.setup_finished) {
			
			float textHeight = textAscent() + textDescent();
			
			//if location ABOVE ports, selectedPort = -1
			if(mouseY < 30 + textDescent()) {
				this.selectedPort = -1;
				return;
			}

			int port_row = (int) ((mouseY - 30 - textDescent())/textHeight);
			
			String[] ports = Communication_Interface.getPorts();
			
			//matches to a row (same as index within Serial ports)
			if(port_row >= 0 && port_row < ports.length)
				
				//checks if on text and not to right
				if(mouseX >= 20 && mouseX <= textWidth(ports[port_row])) {
					this.selectedPort = port_row;
					return;
				}
			
			//if BELOW ports or RIGHT of text within row, selectedPort = -1
			this.selectedPort = -1;
		}
	}
				
	/**
	 * changes setting state of state and scroll (RESET brings back to default [state:none, scroll:0])
	 * 
	 * @param state - the state to be set to
	 * @param scroll - the scroll value to be set to
	 */
	public void display_change(String state, int scroll) {
		this.state = state;
		this.scroll = scroll;
	}
	
	/**
	 * resets board state to no current action
	 */
	public void display_changeRESET() {
		this.display_change("none", 0);
		wordEvent = new WordEvent("");
	}
}
