package biz.computerkraft.crossword.gui;

import java.awt.geom.Point2D;
import java.util.List;

import biz.computerkraft.crossword.grid.Cell;

/**
 * 
 * Interface to listen to cell updates.
 * 
 * @author Raymond Francis
 *
 */
public interface CellUpdateListener {

	/**
	 * 
	 * Event raised when cell selected.
	 * 
	 * @param cell
	 *            selected cell.
	 * @param point
	 *            point in cell of selection
	 */
	void selectCell(Cell cell, Point2D point);

	/**
	 * 
	 * Event raised when cell to left needs to be selected.
	 * 
	 * @param cell
	 *            currently selected cell.
	 */
	void selectCellLeft(Cell cell);

	/**
	 * 
	 * Event raised when cell up needs to be selected.
	 * 
	 * @param cell
	 *            currently selected cell.
	 */
	void selectCellUp(Cell cell);

	/**
	 * 
	 * Event raised when cell to right needs to be selected.
	 * 
	 * @param cell
	 *            currently selected cell.
	 */
	void selectCellRight(Cell cell);

	/**
	 * 
	 * Event raised when cell down needs to be selected.
	 * 
	 * @param cell
	 *            currently selected cell.
	 */
	void selectCellDown(Cell cell);

	/**
	 * 
	 * Event raised when cell content to be added to cell.
	 * 
	 * @param cell
	 *            selected cell.
	 * @param content
	 *            content to add to cell
	 */
	void addCellContent(Cell cell, String content);

	/**
	 * 
	 * Resets cell content.
	 * 
	 * @param cell
	 *            selected cell.
	 */
	void clearCellContent(Cell cell);

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
