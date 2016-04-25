package biz.computerkraft.crossword.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import biz.computerkraft.crossword.gui.PuzzleProperties;

/**
 * 
 * Manages basic grid functions.
 * 
 * @author Raymond Francis
 *
 */
public abstract class Grid implements PuzzleProperties {

	/** Cells list. */
	private Collection<Cell> cells;

	/**
	 * 
	 * Gets word in straight line including cell.
	 * 
	 * @param cell
	 *            cell to read from
	 * @param backward
	 *            forward direction
	 * @param forward
	 *            reverse direction
	 * @return list of cells forming word
	 */
	public final List<Cell> getWordWithCell(final Cell cell, final int backward, final int forward) {
		List<Cell> word = new ArrayList<>();
		Cell startCell = cell;
		while (!startCell.isBlocked(backward)) {
			startCell = startCell.getAdjacent(backward).get();
		}
		while (!startCell.isBlocked(forward)) {
			word.add(startCell);
			startCell = startCell.getAdjacent(forward).get();
		}
		return word;
	}

	/**
	 * 
	 * Sets grid cells.
	 * 
	 * @param newCells
	 *            new cells
	 */
	protected final void setCells(final Collection<Cell> newCells) {
		cells = newCells;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getCells()
	 */
	@Override
	public final Collection<Cell> getCells() {
		return cells;
	}
}
