package biz.computerkraft.crossword.grid.crossword;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Symmetry;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.renderer.BarwordCellRenderer;

/**
 * 
 * Simple crossword puzzle.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "barword")
public class Barword extends AbstractCrossword {

	/** Block east action. */
	private static final String ACTION_BLOCK_E = "Block Right";

	/** Block west action. */
	private static final String ACTION_BLOCK_W = "Block Left";

	/** Block north action. */
	private static final String ACTION_BLOCK_N = "Block Up";

	/** Block south action. */
	private static final String ACTION_BLOCK_S = "Block Down";

	/** Unblock east action. */
	private static final String ACTION_UNBLOCK_E = "Unblock Right";

	/** Unblock west action. */
	private static final String ACTION_UNBLOCK_W = "Unblock Left";

	/** Unblock north action. */
	private static final String ACTION_UNBLOCK_N = "Unblock Up";

	/** Unblock south action. */
	private static final String ACTION_UNBLOCK_S = "Unblock Down";

	/** Blocking directions for menu actions. */
	private final Map<String, Integer> directions = new HashMap<>();

	/**
	 * Constructor for barword crossword.
	 */
	public Barword() {
		directions.put(ACTION_BLOCK_E, DIRECTION_E);
		directions.put(ACTION_BLOCK_N, DIRECTION_N);
		directions.put(ACTION_BLOCK_S, DIRECTION_S);
		directions.put(ACTION_BLOCK_W, DIRECTION_W);
		directions.put(ACTION_UNBLOCK_E, DIRECTION_E);
		directions.put(ACTION_UNBLOCK_N, DIRECTION_N);
		directions.put(ACTION_UNBLOCK_S, DIRECTION_S);
		directions.put(ACTION_UNBLOCK_W, DIRECTION_W);
		setSymmetry(Symmetry.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#cellMenuAction(biz.
	 * computerkraft.crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final boolean cellMenuAction(final Cell cell, final String action) {

		boolean dirtyReturn = false;

		int direction = directions.get(action);
		boolean block = action.startsWith("Block");

		for (Cell symmetric : cell.getSymmetrics()) {
			symmetric.setBlock(direction, block);
		}
		setAcrossDownMarkers();
		dirtyReturn = true;

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

		if (cell.getAnchorY() > 0) {
			if (cell.isBlocked(DIRECTION_N)) {
				actions.add(ACTION_UNBLOCK_N);
			} else {
				actions.add(ACTION_BLOCK_N);
			}
		}

		if (cell.getAnchorX() < 1 - 1.0 / getCellWidth()) {
			if (cell.isBlocked(DIRECTION_E)) {
				actions.add(ACTION_UNBLOCK_E);
			} else {
				actions.add(ACTION_BLOCK_E);
			}
		}

		if (cell.getAnchorY() < 1 - 1.0 / getCellHeight()) {
			if (cell.isBlocked(DIRECTION_S)) {
				actions.add(ACTION_UNBLOCK_S);
			} else {
				actions.add(ACTION_BLOCK_S);
			}
		}

		if (cell.getAnchorX() > 0) {
			if (cell.isBlocked(DIRECTION_W)) {
				actions.add(ACTION_UNBLOCK_W);
			} else {
				actions.add(ACTION_BLOCK_W);
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
		return new BarwordCellRenderer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.grid.crossword.AbstractCrossword#setMarkers()
	 */
	@Override
	protected final void setMarkers() {
		setAcrossDownMarkers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.AbstractCrossword#
	 * setClueModels()
	 */
	@Override
	protected final void setClueModels() {
		setAcrossDownClueModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.Grid#getClues()
	 */
	@Override
	public final List<ClueItem> getClues() {
		return getAcrossDownClues();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.Puzzle#addCellContent(biz.computerkraft.
	 * crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final void addCellContent(final Cell cell, final String content) {
		baseAddCellContent(cell, content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.Puzzle#clearCellContent(biz.computerkraft
	 * .crossword.grid.Cell)
	 */
	@Override
	public final void clearCellContent(final Cell cell) {
		baseClearCellContent(cell);
	}


}
