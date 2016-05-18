package biz.computerkraft.crossword.gui.renderer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import biz.computerkraft.crossword.gui.Selection;

/**
 * 
 * Basic crossword cell renderer.
 * 
 * @author Raymond Francis
 *
 */
public class SudokuCellRenderer extends RectangleGridCellRenderer {

	/** Number of markers to display in width. */
	private int markerWidth;

	/** Number of markers to display in height. */
	private int markerHeight;

	/** Contents of invalid cell. */
	private String invalidCell;

	/**
	 * 
	 * Constructs a Sudoku renderer with markers split over given width and
	 * height.
	 * 
	 * @param width
	 *            number of markers for width
	 * @param height
	 *            number of markers for for height
	 * @param invalidCellText
	 *            text of invalid cell
	 */
	public SudokuCellRenderer(final int width, final int height, final String invalidCellText) {
		markerWidth = width;
		markerHeight = height;
		invalidCell = invalidCellText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellRenderer#renderCell(java.awt.
	 * Graphics2D, double, double, biz.computerkraft.crossword.gui.Selection)
	 */
	@Override
	public final void renderCell(final Graphics2D graphics, final double width, final double height,
			final Selection selection) {
		Selection newSelection = selection;
		if (getCell().getContents().equals(invalidCell)) {
			newSelection = Selection.ERROR;
		}
		renderCell(graphics, width, height, newSelection, true, false);
		if (getCell().getContents().isEmpty()) {
			Rectangle2D bounds = getCellShape().getBounds2D();
			graphics.setColor(Color.BLACK);
			graphics.setFont(MARKER_FONT);
			FontMetrics metrics = graphics.getFontMetrics(MARKER_FONT);
			String markers = getCell().getMarker();
			int xStep = (int) (bounds.getWidth() - 2 * MARKER_BORDER - metrics.charWidth('W')) / (markerWidth - 1);
			int yStep = (int) (bounds.getHeight() - 2 * MARKER_BORDER - metrics.getAscent()) / (markerHeight - 1);

			for (int marker = 0; marker < markers.length(); marker++) {
				int x = marker % markerWidth;
				int y = marker / markerWidth;
				graphics.drawString(markers.substring(marker, marker + 1),
						(int) (bounds.getX() + MARKER_BORDER + x * xStep),
						(int) (bounds.getY() + MARKER_BORDER + y * yStep + metrics.getAscent()));
			}
		}

	}

}
