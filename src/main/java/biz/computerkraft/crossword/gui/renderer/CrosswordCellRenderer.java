package biz.computerkraft.crossword.gui.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.crossword.AbstractCrossword;
import biz.computerkraft.crossword.gui.Selection;

/**
 * 
 * Basic crossword cell renderer.
 * 
 * @author Raymond Francis
 *
 */
public class CrosswordCellRenderer extends AbstractCellRenderer {

	/** Marker font size. */
	private static final int MARKER_FONT_SIZE = 10;

	/** Default marker border. */
	private static final int MARKER_BORDER = 3;

	/** Marker font for crossword. */
	private static final Font MARKER_FONT = new Font("Arial", Font.BOLD, MARKER_FONT_SIZE);

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellRenderer#renderCell(java.awt.
	 * Graphics2D, double, double, biz.computerkraft.crossword.gui.Selection)
	 */
	@Override
	public final void renderCell(final Graphics2D graphics, final double width, final double height,
			final Selection selection) {
		boolean fill = false;
		Cell cell = getCell();
		if (cell.isBlocked(AbstractCrossword.DIRECTION_N) && cell.isBlocked(AbstractCrossword.DIRECTION_S)
				&& cell.isBlocked(AbstractCrossword.DIRECTION_E) && cell.isBlocked(AbstractCrossword.DIRECTION_W)) {
			fill = true;
		}

		baseRenderCell(graphics, width, height, selection, fill);

		if (!cell.getMarker().isEmpty()) {
			Rectangle2D bounds = getCellShape().getBounds2D();
			graphics.setColor(Color.BLACK);
			graphics.setFont(MARKER_FONT);
			FontMetrics metrics = graphics.getFontMetrics(MARKER_FONT);
			graphics.drawString(cell.getMarker(), (int) (bounds.getX() + MARKER_BORDER),
					(int) (bounds.getY() + MARKER_BORDER + metrics.getAscent()));
		}
	}

}
