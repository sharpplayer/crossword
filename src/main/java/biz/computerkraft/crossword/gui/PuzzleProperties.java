package biz.computerkraft.crossword.gui;

import java.util.Collection;
import java.util.Map;

import biz.computerkraft.crossword.grid.Cell;

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
	Map<String, Object> getProperties();

	/**
	 * 
	 * Sets the puzzle properties.
	 * 
	 * @param properties
	 *            properties to set
	 */
	void setProperties(Map<String, Object> properties);

	/**
	 * Gets puzzle name.
	 * 
	 * @return puzzle name
	 */
	String getName();

	/**
	 * 
	 * Get list of cells.
	 * 
	 * @return list cells
	 */
	Collection<Cell> getCells();

	/**
	 * 
	 * Get width in cells.
	 * 
	 * @return cell width
	 */
	int getCellWidth();

	/**
	 * 
	 * Get height in cells.
	 * 
	 * @return cell width
	 */
	int getCellHeight();

	/**
	 * 
	 * Gets the cell renderer class.
	 * 
	 * @return cell renderer
	 */
	Class<? extends CellRenderer> getRendererClass();
}
