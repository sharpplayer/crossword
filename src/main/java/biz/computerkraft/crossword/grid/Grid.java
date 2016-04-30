package biz.computerkraft.crossword.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.ClueModel;
import biz.computerkraft.crossword.gui.Puzzle;

/**
 * 
 * Manages basic grid functions.
 * 
 * @author Raymond Francis
 *
 */
public abstract class Grid implements Puzzle {

	/** Cells list. */
	private Collection<Cell> cells;

	/** Clue models. */
	private List<ClueModel> clueModels = new ArrayList<>();

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
		word.add(startCell);
		return word;
	}

	/**
	 * 
	 * Sets grid cells.
	 * 
	 * @param newCells
	 *            new cells
	 */
	@XmlElement(name = "cell")
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getClueModels()
	 */
	@Override
	@XmlTransient
	public final List<ClueModel> getClueModels() {
		return clueModels;
	}

	/**
	 * 
	 * Adds a clue model.
	 * 
	 * @param model
	 *            clue model to add
	 */
	protected final void addClueModel(final ClueModel model) {
		clueModels.add(model);
	}

	/**
	 * Updates clue lists.
	 */
	protected final void updateClues() {
		List<ClueItem> clues = getClues();
		for (ClueModel model : clueModels) {
			model.setClues(clues);
		}
	}

	/**
	 * 
	 * Get adjacent cell in given direction.
	 * 
	 * @param cell
	 *            cell to move from
	 * @param direction
	 *            direction to move
	 * @return new cell
	 */
	protected final Cell getCell(final Cell cell, final int direction) {
		Optional<Cell> optionalCell = cell.getAdjacent(direction);
		if (optionalCell.isPresent()) {
			return optionalCell.get();
		} else {
			return cell;
		}

	}

	/**
	 * 
	 * Gets list of clues.
	 * 
	 * @return list of clues
	 */
	public abstract List<ClueItem> getClues();

}
