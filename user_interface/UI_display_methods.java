
// UI_display_methods.java
// Author: Vivaan Mahtab

package user_interface;

import Serial_Communication.Communication_Interface;
import processing.core.PApplet;
import settings.Settings;


/**
 * series of static methods to be implemented by <em>UI_main.java</em> in order
 * to display various elements on UI window
 * 
 * @author Vivaan Mahtab
 *
 * @see local/src/user_interface/UI_main
 */
public class UI_display_methods implements Settings{
	
	/**
	 * displays an empty board that represents the physical LEDs on a UI window
	 * This grid is GRID_WIDTH by GRID_HEIGHT pixels containing
	 * BULB_GRID_LENGTH by BULB_GRID_HEIGHT circles representing LEDs
	 * 
	 * @param p - a processing PApplet application
	 * @see local/src/settings/Settings
	 */
	public static void draw_empty_LED_board(PApplet p) {
		
		
		p.pushMatrix();
		p.translate((WIDTH/2)-(GRID_WIDTH/2), (HEIGHT- 5*GRID_HEIGHT/4));
		p.noStroke();
		p.fill(0);
		
		//black rectangular background
		p.rect(0, 0, GRID_WIDTH, GRID_HEIGHT);
		
		p.stroke(255);
		p.noFill();
		
		//draws circles on background to represent LEDs
		for(int row = 0; row < BULB_GRID_HEIGHT; row++) {
			for(int col =0; col < BULB_GRID_LENGTH + USER_INTERFACE_EXCESS_LENGTH; col++) {
					p.circle(20 * col + 12, 20 * row + 12, 12);
				}
		}
		p.popMatrix();
	}
	
	/**
	 * displays a filled board that represents the physical LEDs on a UI window
	 * 
	 * @param p - a processing PApplet application
	 * @param grid - the offset grid of LEDs represented by TRUE and FALSE indicators
	 * 				 the method to retrieve this is get_offset_grid() in <em>WordEvent</em>
	 * 
	 * @see local/src/word_processing/WordEvent
	 */
	public static void draw_filled_LED_board(PApplet p, String[] grid) {
		p.pushMatrix();
		p.translate((WIDTH/2)-(GRID_WIDTH/2), (HEIGHT- 5*GRID_HEIGHT/4));
		p.noStroke();
		p.fill(255, 0, 0);
		
		// final column to draw to - conditional if maximum length of UI or current grid is shorter
		int colMax = grid[0].length()<BULB_GRID_LENGTH + USER_INTERFACE_EXCESS_LENGTH? grid[0].length() 
														: BULB_GRID_LENGTH + USER_INTERFACE_EXCESS_LENGTH;
		
		// draws RED circles to represent an ON LED at corresponding locations of grid
		for(int row = 0; row < grid.length; row++) {
			for(int col =0; col < colMax; col++) {
					if(grid[row].charAt(col)=='1') {
						p.circle(20 * col + 12, 20 * row + 12, 12);
					}
				}
		}
		p.popMatrix();
	}
	
	/**
	 * draws an active textbox in the UI window
	 * 
	 * @param p - a processing PApplet application
	 * @param text - the text to be displayed in the textbox
	 * @param display - a boolean that (if true) displays a vertical line at the end 
	 * 					of the text, this flashes to show the location of current text
	 * 					(idk every text editor has it, it felt wrong not to)
	 */
	public static void draw_textbox(PApplet p, String text, boolean display) {
		float TEXT_WIDTH = p.textWidth(text);
		float TEXTBOX_WIDTH = 2*WIDTH/3;
		p.noFill();
		p.stroke(0);
		
		
		//draw textbook
		p.pushMatrix();
		p.translate(WIDTH/2 - TEXTBOX_WIDTH/2, 20);
		p.rect(0, 0, TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		
		//display line
		if(display) {
			p.stroke(0, 0, 255);
			p.line(11 + TEXT_WIDTH, 10, 11 + TEXT_WIDTH, 10 + p.textAscent() + p.textDescent());
		}
		
		p.noStroke();
		p.fill(0);
		
		//display string
		p.text(text, 10, 25);
		
		p.popMatrix();
		
	}

	/**
	 * draws button on UI window that are internally coded to have functionality based
	 * on description 
	 * 
	 * @param p - a processing PApplet application
	 * @param buttons - an array of strings where each string represents a button
	 * 
	 * @implNote - these current buttons are located in:
	 * @see local/src/settings/Settings.OPTIONS - functionality included there
	 */
	public static void draw_buttons(PApplet p, String[] buttons) {
		
		float initialPos = WIDTH/2 - TEXTBOX_WIDTH/2 + BUTTON_WIDTH/2;
		float dx = TEXTBOX_WIDTH/(buttons.length-1)-BUTTON_WIDTH/(buttons.length-1);
		
		for(int i = 0; i < buttons.length; i++) {
			
			float textWidth = p.textWidth(buttons[i]);
			
			float xCoor = initialPos - BUTTON_WIDTH/2 + i * (dx);
			
			p.pushMatrix();
			p.translate(xCoor, HEIGHT/8 + 10);
			p.stroke(0);
			p.fill(200);
			p.rect(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, 10);
			p.fill(0);
			p.text(buttons[i], (BUTTON_WIDTH-textWidth)/2, BUTTON_HEIGHT-p.textAscent()/2);
			p.popMatrix();
			
		}
	}
	
	/**
	 * displays current settings above UI LED grid
	 * 
	 * @param p - a processing PApplet application
	 * @param state - the current "state" of the board, this can be any of the options of buttons
	 * 				  or a sent_message which refers to a custom text
	 * 
	 * @param scroll - the scroll speed (positive represents right, negative represents left)
	 * 
	 * @see local/src/settings/Settings.OPTIONS - functionality included there
	 */
	public static void draw_settings(PApplet p, String state, int scroll) {
		
		float iniX = (WIDTH/2)-(GRID_WIDTH/2);
		float iniY = HEIGHT- 5*GRID_HEIGHT/4 - p.textDescent();
		
		p.text("state: " + state, iniX, iniY - p.textAscent());
		
		//displays scroll speed and direction
		
		//no scrolling
		if(scroll == 0)
			p.text("scroll: none", iniX, iniY);
		
		//left scrolling with scrolling negative X direction
		else if(scroll < 0)
			p.text("scroll: left @ " + -scroll, iniX, iniY);
		
		//right scrolling with scrolling positive X direction
		else
			p.text("scroll: righ @ " + -scroll, iniX, iniY);
	}
	
	/**
	 * setup for choosing Serial comm port, this displays the various
	 * comm port options to be selected from
	 * @param p - a processing PApplet application
	 * @param port - if a port is inputed within the range of the port list
	 * 				 that port will be highlighted in red instead of default black
	 * 
	 * @implSpec if port is outside range of port list, no text is red
	 * @implNote port received from <em>UI_main</em>.mouseMoved()
	 * 
	 * @see local/user_interface/UI_main.java
	 */
	public static void portSelection(PApplet p, int port) {
		
		p.fill(0);
		p.text("Select Serial Com Port:", 20, 20);
		
		//get available ports
		String[] com_ports = Communication_Interface.getPorts();
		for(int i = 0; i < com_ports.length; i++) {
			
			//color selection
			if(i == port)
				p.fill(255, 0, 0);
			else
				p.fill(0);
			
			//draw at appropriate height
			p.text(com_ports[i], 20, 30 + (p.textAscent() + p.textDescent())*(i+1));
		}
	}
	
}
