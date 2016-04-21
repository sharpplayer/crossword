package biz.computerkraft.crossword.gui;

import java.util.Map;

/**
 * 
 * Identifies a puzzle and it's required properties.
 * 
 * @author Raymond Francis
 *
 */
public interface PuzzleProperties {

	/**
	 * 
	 * Gets a list of properties and their types.
	 * 
	 * @return property map
	 */
	Map<String, Class<?>> getProperties();

	/**
	 * 
	 * Sets the puzzle properties.
	 * 
	 * @param properties
	 *            properties to set
	 */
	void setProperties(Map<String, Object> properties);

}
