package biz.computerkraft.crossword.gui;

import java.awt.Graphics2D;
import java.awt.Shape;

import biz.computerkraft.crossword.grid.Cell;

/**
 * 
 * Renders a cell.
 * 
 * @author Raymond Francis
 *
 */
public interface CellRenderer {

	/**
	 * 
	 * Sets cell to render.
	 * 
	 * @param cellToRender
	 *            cell to render
	 */
	void setCell(Cell cellToRender);

	/**
	 * 
	 * Gets cell rendered.
	 * 
	 * @return cell rendered
	 */
	Cell getCell();

	/**
	 * Render cell.
	 * 
	 * @param graphics
	 *            2d graphics context to render to.
	 * @param width
	 *            width of graphics context
	 * @param height
	 *            height of graphics context
	 * @param selection
	 *            how to render selection
	 */
	void renderCell(Graphics2D graphics, double width, double height, Selection selection);

	/**
	 * 
	 * Get shape renderered.
	 * 
	 * @return shape rendered
	 */
	Shape getCellShape();
}
