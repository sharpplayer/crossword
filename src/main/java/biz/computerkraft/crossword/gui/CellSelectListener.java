package biz.computerkraft.crossword.gui;

import java.awt.geom.Point2D;

import biz.computerkraft.crossword.grid.Cell;

/**
 * 
 * Callback interface to action a cell selection.
 * 
 * @author Raymond Francis
 *
 */
public interface CellSelectListener {

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
	 * Event raised when cell selected.
	 * 
	 * @param cell
	 *            selected cell.
	 * @param direction
	 *            direction of selection
	 */
	void selectCell(Cell cell, int direction);

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

}