package biz.computerkraft.crossword.gui;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;

/**
 * 
 * Presents clue information for model.
 * 
 * @author Raymond Francis
 *
 */
public class ClueItem {

	/** Clue text. */
	private Clue clue;

	/** Clue category. */
	private String category;

	/** Start cell for clue. */
	private Cell startCell;

	/** Direction of word for clue. */
	private int direction;

	/** Word for clue. */
	private String word;

	/**
	 * 
	 * Constructor for clue data.
	 * 
	 * @param newClue
	 *            new clue text
	 * @param newCategory
	 *            category of clue
	 * @param newCell
	 *            start cell of word for clue
	 * @param newDirection
	 *            direction of word for clue
	 * @param newWord
	 *            word for clue
	 */
	public ClueItem(final Clue newClue, final String newCategory, final Cell newCell, final int newDirection,
			final String newWord) {
		clue = newClue;
		category = newCategory;
		startCell = newCell;
		direction = newDirection;
		word = newWord;
	}

	/**
	 * 
	 * Gets the clue object.
	 * 
	 * @return the clue
	 */
	public final Clue getClue() {
		return clue;
	}

	/**
	 * 
	 * Gets the clue category.
	 * 
	 * @return the category
	 */
	public final String getCategory() {
		return category;
	}

	/**
	 * 
	 * Gets the start cell for clue word.
	 * 
	 * @return the startCell
	 */
	public final Cell getStartCell() {
		return startCell;
	}

	/**
	 * 
	 * Gets the direction for the clue word.
	 * 
	 * @return the direction
	 */
	public final int getDirection() {
		return direction;
	}

	/**
	 * 
	 * Gets the clue word.
	 * 
	 * @return the word
	 */
	public final String getWord() {
		return word;
	}

}
