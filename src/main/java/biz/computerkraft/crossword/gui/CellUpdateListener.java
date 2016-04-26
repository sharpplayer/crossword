package biz.computerkraft.crossword.gui;

import java.awt.geom.Point2D;

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
}
