package biz.computerkraft.crossword.gui.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.gui.Selection;

/**
 * 
 * Rectangle grid cell renderer.
 * 
 * @author Raymond Francis
 *
 */
public abstract class RectangleGridCellRenderer extends AbstractCellRenderer {

	/** Marker font size. */
	private static final int MARKER_FONT_SIZE = 10;

	/** Default marker border. */
	protected static final int MARKER_BORDER = 3;

	/** Marker font for crossword. */
	protected static final Font MARKER_FONT = new Font("Arial", Font.BOLD, MARKER_FONT_SIZE);

	/**
	 * 
	 * Renders cell.
	 * 
	 * @param graphics
	 *            graphics context
	 * @param width
	 *            grid width
	 * @param height
	 *            grid height
	 * @param selection
	 *            how cell is selected
	 * @param defaultMarkers
	 *            default marker rendering
	 * @param bars
	 *            render bars or whole cell
	 */
	protected final void renderCell(final Graphics2D graphics, final double width, final double height,
			final Selection selection, final boolean bars, final boolean defaultMarkers) {
		Cell cell = getCell();

		baseRenderCell(graphics, width, height, selection, cell.getFill(!bars));

		if (!cell.getMarker().isEmpty() && defaultMarkers) {
			Rectangle2D bounds = getCellShape().getBounds2D();
			graphics.setColor(Color.BLACK);
			graphics.setFont(MARKER_FONT);
			FontMetrics metrics = graphics.getFontMetrics(MARKER_FONT);

			graphics.drawString(cell.getMarker(), (int) (bounds.getX() + MARKER_BORDER),
					(int) (bounds.getY() + MARKER_BORDER + metrics.getAscent()));
		}
	}

}
