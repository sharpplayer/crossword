package biz.computerkraft.crossword.gui.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.crossword.Crossword;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.Selection;

/**
 * 
 * Basic crossword cell renderer.
 * 
 * @author Raymond Francis
 *
 */
public class CrosswordCellRenderer implements CellRenderer {

	/** Cell to render. */
	private Cell cell;

	/** Shape rendered. */
	private Shape cellShape;

	/** Default font size. */
	private static final int DEFAULT_FONT_SIZE = 40;

	/** Marker font size. */
	private static final int MARKER_FONT_SIZE = 10;

	/** Default font size. */
	private static final int CELL_BORDER = 6;

	/** Font for crossword. */
	private static final Font DEFAULT_FONT = new Font("Arial", Font.BOLD, DEFAULT_FONT_SIZE);

	/** Marker font for crossword. */
	private static final Font MARKER_FONT = new Font("Arial", Font.BOLD, MARKER_FONT_SIZE);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellRenderer#setCell(biz.computerkraft.
	 * crossword.grid.Cell)
	 */
	@Override
	public final void setCell(final Cell cellToRender) {
		cell = cellToRender;
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
		boolean fill = false;
		if (cell.isBlocked(Crossword.DIRECTION_N) && cell.isBlocked(Crossword.DIRECTION_S)
				&& cell.isBlocked(Crossword.DIRECTION_E) && cell.isBlocked(Crossword.DIRECTION_W)) {
			fill = true;
		}

		double x = cell.getAnchorX();
		double y = cell.getAnchorY();
		double widthPc = 1 - x;
		double heightPc = 1 - y;
		if (cell.getAdjacent(Crossword.DIRECTION_E).isPresent()) {
			widthPc = cell.getAdjacent(Crossword.DIRECTION_E).get().getAnchorX() - x;
		}
		if (cell.getAdjacent(Crossword.DIRECTION_S).isPresent()) {
			heightPc = cell.getAdjacent(Crossword.DIRECTION_S).get().getAnchorY() - y;
		}

		cellShape = new Rectangle((int) Math.round(x * width), (int) Math.round(y * height),
				(int) Math.round(widthPc * width), (int) Math.round(heightPc * height));

		if (fill) {
			graphics.setColor(Color.BLACK);
		} else if (selection == Selection.NONE) {
			graphics.setColor(Color.WHITE);
		} else if (selection == Selection.DIRECT) {
			graphics.setColor(Color.ORANGE);
		} else if (selection == Selection.INDIRECT) {
			graphics.setColor(Color.YELLOW);
		}

		graphics.fill(cellShape);

		Shape borderShape = cellShape;
		if (!fill) {
			graphics.setStroke(new BasicStroke(1));
			graphics.setColor(Color.BLACK);
		} else {
			borderShape = new Rectangle((int) Math.round(x * width + CELL_BORDER / 2 + 1),
					(int) Math.round(y * height + CELL_BORDER / 2 + 1),
					(int) Math.round(widthPc * width) - 1 - CELL_BORDER,
					(int) Math.round(heightPc * height) - 1 - CELL_BORDER);
			graphics.setStroke(new BasicStroke(CELL_BORDER));
			if (selection == Selection.NONE) {
				graphics.setColor(Color.BLACK);
			} else if (selection == Selection.DIRECT) {
				graphics.setColor(Color.ORANGE);
			} else if (selection == Selection.INDIRECT) {
				graphics.setColor(Color.YELLOW);
			}
		}

		graphics.draw(borderShape);

		if (!cell.getContents().isEmpty()) {
			Rectangle2D bounds = cellShape.getBounds2D();
			graphics.setColor(Color.BLACK);
			graphics.setFont(DEFAULT_FONT);
			FontMetrics metrics = graphics.getFontMetrics(DEFAULT_FONT);
			double fontX = bounds.getX() + (bounds.getWidth() - metrics.stringWidth(cell.getContents())) / 2;
			double fontY = bounds.getY() + ((bounds.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
			graphics.drawString(cell.getContents(), (int) fontX, (int) fontY);
		}

		if (!cell.getMarker().isEmpty()) {
			Rectangle2D bounds = cellShape.getBounds2D();
			graphics.setColor(Color.BLACK);
			graphics.setFont(MARKER_FONT);
			FontMetrics metrics = graphics.getFontMetrics(MARKER_FONT);
			graphics.drawString(cell.getMarker(), (int) (bounds.getX() + 3),
					(int) (bounds.getY() + 3 + metrics.getAscent()));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellRenderer#getCellShape()
	 */
	@Override
	public final Shape getCellShape() {
		return cellShape;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellRenderer#getCell()
	 */
	@Override
	public final Cell getCell() {
		return cell;
	}

}
