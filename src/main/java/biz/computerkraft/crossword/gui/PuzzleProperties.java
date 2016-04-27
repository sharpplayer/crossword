package biz.computerkraft.crossword.gui;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;
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

	/**
	 * 
	 * Gets the indirectly selected cells.
	 * 
	 * @param cell
	 *            focus cell
	 * @param offset
	 *            offset to get direction of selection
	 * @return list of indirectly selected cells
	 */
	List<Cell> getIndirectSelection(Cell cell, Point2D offset);

	/**
	 * 
	 * Adds content to cell.
	 * 
	 * @param cell
	 *            cell to add content to
	 * @param content
	 *            content to add
	 */
	void addCellContent(Cell cell, String content);

	/**
	 * 
	 * Clear content from cell.
	 * 
	 * @param cell
	 *            cell to clear content of
	 */
	void clearCellContent(Cell cell);

	/**
	 * 
	 * Returns cell to left of determined cell.
	 * 
	 * @param cell
	 *            cell to base on
	 * @return cell to left
	 */
	Cell getCellLeft(Cell cell);

	/**
	 * 
	 * Returns cell to right of determined cell.
	 * 
	 * @param cell
	 *            cell to base on
	 * @return cell to right
	 */
	Cell getCellRight(Cell cell);

	/**
	 * 
	 * Returns cell above determined cell.
	 * 
	 * @param cell
	 *            cell to base on
	 * @return cell above
	 */
	Cell getCellUp(Cell cell);

	/**
	 * 
	 * Returns cell below determined cell.
	 * 
	 * @param cell
	 *            cell to base on
	 * @return cell below
	 */
	Cell getCellDown(Cell cell);
	
	/**
	 * 
	 * Performs an action on a cell.
	 * 
	 * @param cell
	 *            selected cell.
	 * @param action
	 *            action to perform
	 */
	void cellMenuAction(Cell cell, String action);

	/**
	 * 
	 * Retrieves an action list for a cell.
	 * 
	 * @param cell
	 *            selected cell.
	 * @param actions
	 *            actions to perform
	 */
	void populateCellMenu(Cell cell, List<String> actions);


}
