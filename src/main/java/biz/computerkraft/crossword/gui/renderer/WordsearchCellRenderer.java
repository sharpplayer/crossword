package biz.computerkraft.crossword.gui.renderer;

import java.awt.Graphics2D;

import biz.computerkraft.crossword.gui.Selection;

/**
 * 
 * Basic crossword cell renderer.
 * 
 * @author Raymond Francis
 *
 */
public class WordsearchCellRenderer extends AbstractCellRenderer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellRenderer#renderCell(java.awt.
	 * Graphics2D, double, double, biz.computerkraft.crossword.gui.Selection)
	 */
	@Override
	public final void renderCell(final Graphics2D graphics, final double width, final double height,
			final Selection selection) {
		baseRenderCell(graphics, width, height, selection, false);
	}

}
