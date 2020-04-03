
// WordEvent.java
// Author: Vivaan Mahtab

package word_processing;
import word_processing.WordConstructor;
import java.util.ArrayList;

/**
 * <em>WordEvent Class</em>
 * requests a handle and translates them
 * into a series of <em>WordConstructor</em> objects and contains
 * various methods to manipulate them with
 * 
 * @author Vivaan Mahtab
 * 
 * @implNote WordEvents are alphanumeric strings with spaces
 * @see local/src/word_processing/WordConstructor
 *
 */
public class WordEvent implements settings.Settings{
	
	WordConstructor[] words;
	int position;
	int size;
	
	/**
	 * Creates a new WordEvent that is series of strings
	 * generated into <em>Word</em> objects
	 * 
	 * @param str - a series of alphanumeric characters to be
	 * 				converted to <em>Word</em> objects, can contain
	 * 				spaces and various special characters
	 * 
	 * @param position - an initial position that the board writes from
	 * 					 so that the board can display letters and characters
	 * 					 located within the <em>Word</em> list
	 */
	public WordEvent(String str, int position) {
		this.position = position;
		
		//if no characters are inputed as the wordEvent
		if(str.equals("")) {
			words = new WordConstructor[1];
			words[0] = new WordConstructor("");
			this.size = 0;
			return;
		}
		
		//Splits the string into its various components based on spaces
		String[] strings = str.trim().split("\\s+");

		
		//Generates WordConstructor objects from list of strings
		this.words = new WordConstructor[strings.length];
		for(int i = 0; i < strings.length; i++) {
			words[i] = new WordConstructor(strings[i]);
		}
			
		//Gets total length of Words with kerning in between
		this.size = 0;
		for(WordConstructor get_length_dump : this.words) {
			this.size += get_length_dump.getLength();
			this.size += 2;
		}
		size -= 2;

	}
	
	// default position of 0
	public WordEvent(String str) {
		this(str, 0);
	}
	
	// adds or subtracts from position by @param displacement
	public void move(int displacement) {
		this.position += displacement;
	}
	
	public WordConstructor[] getWords() {
		return this.words;
	}
	
	/**
	 * a getter that returns the list of <em>WordConstructor</em> objects
	 * into a series of TRUE and FALSE characters that represent if a
	 * corresponding LED light should be on or off in a given inputed
	 * scenario or light board display
	 * 
	 * Goes through each <em>WordConstructor</em> Object and adds corresponding TRUE or FALSE for various
	 * object - performed by going through first <em>WordConstructor</em> object and than repeating on
	 * first ROW of LED's with next <em>WordConstructor</em> object and adding extra FALSEs in between each
	 * WordConstructor objects for kerning readability
	 * 
	 * This assumes all <em>WordConstructor</em> objects are the same height
	 * of BULB_HEIGHT_GRID and are perfectly rectangular arrays
	 * 
	 * @see local/src/settings/Settings
	 */
	public String[] get_offset_board() {
		
		//The "height" of the LED grid, as in the bulbs that compose of it vertically
		String[] grid = new String[BULB_GRID_HEIGHT];
		for(int i = 0; i < grid.length; i++) {
			grid[i] = "";
		}
		
		//if no characters in this clause
		if(this.size==0)
			return grid;
	
		//main loop - reads each WordConstructor object
		for(WordConstructor current_word_object : this.words) {
			ArrayList<ArrayList<String>> OUTER_ARRAYLIST = current_word_object.getGrid();
			int i = 0;
			for(ArrayList<String> INNER_ARRAYLIST : OUTER_ARRAYLIST) {
				for(String led_seq : INNER_ARRAYLIST) {
					grid[i] += led_seq;
				}
				grid[i] += "000";
				i++;
			}
			
		}
		
		// checks initialPosition based on this.position
		// note: if position <= 0, goes to 0
		int startIndex = this.position > 0? this.position : 0;
		
		// Gets last index before excess FALSE trail
		int endIndex = grid[0].length()-2;
		
		// Breaks the corresponding generated grid into various
		// substrings that fit within the board
		if(endIndex > 0) {
			for(int i =0; i < grid.length; i++) {
				grid[i] = grid[i].substring(startIndex, endIndex);
			}
		}
		
		// if position < 0, than excess FALSEs are added BEFORE initial grid
		// to allow for excess space before start of first on LED
		if(this.position<0) {

			for(int grid_index  = 0; grid_index < grid.length; grid_index++) {
				for(int num_itr = 0; num_itr < -this.position; num_itr++) {
					grid[grid_index] = "0" + grid[grid_index];
				}
			}
		}
		

		return grid;
	}	
	
	public int getPosition() {
		return this.position;
	}
	
	public int getSize() {
		return this.size;
	}
	
	// PRINTS into console with ON LED being represented by an 'X'
	public void print() {
		String[] arr = this.get_offset_board();
		for(String str : arr) {
			for(char chr : str.toCharArray()) {
				if(chr == '1') {
					System.out.print("X ");
				}
				else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}
}
