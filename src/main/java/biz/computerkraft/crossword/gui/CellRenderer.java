package biz.computerkraft.crossword.gui;

import java.awt.Graphics2D;

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
	 * Render cell.
	 * 
	 * @param graphics
	 *            2d graphics context to render to.
	 * @param width
	 *            width of graphics context
	 * @param height
	 *            height of graphics context
	 */
	void renderCell(Graphics2D graphics, double width, double height);

}
