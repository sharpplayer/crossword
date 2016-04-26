package biz.computerkraft.crossword.gui.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

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

		if (fill) {
			graphics.setColor(Color.BLACK);
		} else if (selection == Selection.NONE) {
			graphics.setColor(Color.WHITE);
		} else if (selection == Selection.DIRECT) {
			graphics.setColor(Color.ORANGE);
		} else if (selection == Selection.INDIRECT) {
			graphics.setColor(Color.YELLOW);
		}

		cellShape = new Rectangle((int) (x * width), (int) (y * height), (int) (widthPc * width),
				(int) (heightPc * height));

		graphics.fill(cellShape);

		if (!fill) {
			graphics.setColor(Color.BLACK);
			graphics.draw(cellShape);
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
