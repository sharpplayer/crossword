package biz.computerkraft.crossword.gui.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.crossword.Crossword;
import biz.computerkraft.crossword.gui.CellRenderer;

public class CrosswordCellRenderer implements CellRenderer {

	/** Cell to render. */
	private Cell cell;

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

	@Override
	public final void renderCell(final Graphics2D graphics, final double width, final double height) {
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

		graphics.setColor(Color.BLACK);
		if (fill) {
			graphics.fillRect((int) (x * width), (int) (y * height), (int) (widthPc * width),
					(int) (heightPc * height));
		} else {
			graphics.drawRect((int) (x * width), (int) (y * height), (int) (widthPc * width),
					(int) (heightPc * height));
		}

	}

}
