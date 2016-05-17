package biz.computerkraft.crossword.gui.renderer;

import java.awt.Graphics2D;

import biz.computerkraft.crossword.gui.Selection;

/**
 * 
 * Renders a classic crossword cell.
 * 
 * @author Raymond Francis
 *
 */
public class CrosswordCellRenderer extends RectangleGridCellRenderer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellRenderer#renderCell(java.awt.
	 * Graphics2D, double, double, biz.computerkraft.crossword.gui.Selection)
	 */
	@Override
	public final void renderCell(final Graphics2D graphics, final double width, final double height,
			final Selection selection) {
		renderCell(graphics, width, height, selection, false, true);
	}

}
