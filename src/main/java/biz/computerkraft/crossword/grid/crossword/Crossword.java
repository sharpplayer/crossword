package biz.computerkraft.crossword.grid.crossword;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.renderer.CrosswordCellRenderer;

/**
 * 
 * Simple crossword puzzle.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "crossword")
public class Crossword extends AbstractCrossword {

	/** Fill action. */
	private static final String ACTION_FILL = "Fill";

	/** Unfill action. */
	private static final String ACTION_UNFILL = "Unfill";

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#cellMenuAction(biz.
	 * computerkraft.crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final boolean cellMenuAction(final Cell cell, final String action) {

		boolean dirtyReturn = false;
		if (action.equals(ACTION_FILL)) {
			for (Cell symmetric : cell.getSymmetrics()) {
				symmetric.setBlock(DIRECTION_E, true);
				symmetric.setBlock(DIRECTION_W, true);
				symmetric.setBlock(DIRECTION_N, true);
				symmetric.setBlock(DIRECTION_S, true);
			}
			setMarkers();
			dirtyReturn = true;
		} else if (action.equals(ACTION_UNFILL)) {
			for (Cell symmetric : cell.getSymmetrics()) {
				unfill(symmetric, DIRECTION_E);
				unfill(symmetric, DIRECTION_W);
				unfill(symmetric, DIRECTION_N);
				unfill(symmetric, DIRECTION_S);
			}
			setMarkers();
			dirtyReturn = true;
		}

		return dirtyReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.PuzzleProperties#populateCellMenu(biz.
	 * computerkraft.crossword.grid.Cell, java.util.List)
	 */
	@Override
	public final void populateCellMenu(final Cell cell, final List<String> actions) {

		if (isCellFilled(cell)) {
			for (Cell adjacent : cell.getAdjacents()) {
				if (!isCellFilled(adjacent)) {
					actions.add(ACTION_UNFILL);
					break;
				}
			}
		} else {
			boolean fillable = true;
			for (Cell symmetricCell : cell.getSymmetrics()) {
				if (!symmetricCell.getContents().isEmpty()) {
					fillable = false;
					break;
				}
			}
			if (fillable) {
				actions.add(ACTION_FILL);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getNewCellRenderer()
	 */
	@Override
	public final CellRenderer getNewCellRenderer() {
		return new CrosswordCellRenderer();
	}

}
